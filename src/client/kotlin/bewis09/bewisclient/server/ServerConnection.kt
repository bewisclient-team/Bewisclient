package bewis09.bewisclient.server

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.cosmetics.AnimatedCape
import bewis09.bewisclient.cosmetics.Cosmetic
import bewis09.bewisclient.cosmetics.Cosmetics
import bewis09.bewisclient.exception.TooLowAPILevelException
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


object ServerConnection {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    var specials: Array<Cosmetic> = arrayOf()
    var cosmetic_data: Array<DefaultCosmetic> = arrayOf()
    var user_data: Array<String> = arrayOf()
    var current: Selection? = null
    var base_url: String = ""

    var loadStatus = Status.NONE

    @OptIn(ExperimentalEncodingApi::class)
    fun load() {
        Util.getIoWorkerExecutor().execute {
            Bewisclient.info("Loading Bewisclient data from server")

            try {
                val url: URL = URI("https://bewisclient.deno.dev/api/on_launch").toURL()
                val con: URLConnection = url.openConnection()
                val http: HttpURLConnection = con as HttpURLConnection
                http.setRequestMethod("POST")
                http.setDoOutput(true)

                val out: ByteArray = "{\"uuid\":\"${
                    MinecraftClient.getInstance().gameProfile.id
                }\"}".toByteArray(StandardCharsets.UTF_8)
                val length = out.size

                http.setFixedLengthStreamingMode(length)
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                http.connect()
                http.outputStream.use { os ->
                    os.write(out)
                }

                val result = String(http.inputStream.readAllBytes())

                val data = gson.fromJson(result, ReturnData::class.java)

                specials = data.specials
                cosmetic_data = data.cosmetics
                user_data = data.user_data
                current = data.current
                base_url = data.base_url

                if (data.min_api_level > Bewisclient.API_LEVEL) throw TooLowAPILevelException(data.min_api_level)

                val file = File("${FabricLoader.getInstance().gameDir}/bewisclient/server/data.json")

                file.parentFile.mkdirs()
                file.createNewFile()

                file.writeText(gson.toJson(JsonObject().apply {
                    add("specials", gson.toJsonTree(specials))
                    add("cosmetics", gson.toJsonTree(cosmetic_data))
                    add("base_url", JsonPrimitive(base_url))
                }))

                loadStatus = Status.ONLINE

                Bewisclient.info("Successfully loaded Bewisclient data from server")
            } catch (e: Exception) {
                Bewisclient.warn("Failed to load Bewisclient data from server, using data from previous runs")

                val file = File("${FabricLoader.getInstance().gameDir}/bewisclient/server/data.json")

                if (file.exists()) {
                    val data = gson.fromJson(file.readText(), JsonObject::class.java)

                    specials = gson.fromJson(data.getAsJsonArray("specials"), Array<Cosmetic>::class.java)
                    cosmetic_data = gson.fromJson(data.getAsJsonArray("cosmetics"), Array<DefaultCosmetic>::class.java)
                    base_url = gson.fromJson(data.getAsJsonPrimitive("base_url"), String::class.java)

                    loadStatus = Status.OFFLINE
                }
            }

            for (cosmetic in cosmetic_data) {
                val texture = File("${FabricLoader.getInstance().gameDir}/bewisclient/server/${cosmetic.type}/${cosmetic.id}"+(if(cosmetic.frames > 1) ".gif" else ".png"))

                try {
                    if (base_url != "") {
                        val address = base_url.replace("%s", cosmetic.type + "/" + cosmetic.id + (if(cosmetic.frames > 1) ".gif" else ".png"))

                        if (!texture.exists()) {
                            bewis09.bewisclient.util.Util.downloadToFile(address, texture)
                        } else {
                            val digest = MessageDigest.getInstance("SHA-256")
                            val hash = Base64.encode(digest.digest(texture.readBytes()))

                            if (hash != cosmetic.hash) {
                                Bewisclient.warn("The texture of " + cosmetic.type + "/" + cosmetic.id + " was either changed on the local machine or updated on the server. Downloading the correct one...")

                                bewis09.bewisclient.util.Util.downloadToFile(address, texture)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Bewisclient.warn("Error downloading cape texture: " + e.localizedMessage)
                }
            }
        }
    }

    enum class Status {
        NONE,
        ONLINE,
        OFFLINE
    }

    data class ReturnData(val specials: Array<Cosmetic>, val user_data: Array<String>, val cosmetics: Array<DefaultCosmetic>, val min_api_level: Int, val current: Selection, val base_url: String)

    class DefaultCosmetic(type: String, id: String, val frames: Int, val hash: String?, val default: Boolean, val old_id: Int?): Cosmetic(type, id)

    open class Cosmetic(val type: String, val id: String) {
        override fun equals(other: Any?): Boolean {
            return other is Cosmetic && other.id == id && other.type == type
        }

        override fun hashCode(): Int {
            return 31 * type.hashCode() + id.hashCode()
        }

        override fun toString(): String {
            return javaClass.simpleName+"/"+type+"/"+id
        }
    }

    data class Selection(val wing: String, val hat: String, val cape: String)

    var loadedCosmetics = false

    fun registerCosmetics() {
        if(loadedCosmetics) return

        loadedCosmetics = true

        for (cosmetic in cosmetic_data) {
            val texture = File("${FabricLoader.getInstance().gameDir}/bewisclient/server/${cosmetic.type}/${cosmetic.id}" + (if (cosmetic.frames > 1) ".gif" else ".png"))

            try {
                if (texture.exists()) {
                    if (cosmetic.frames > 1) {
                        val gif = bewis09.bewisclient.util.Util.getFrames(texture)

                        gif.forEachIndexed { i, image ->
                            val baos = ByteArrayOutputStream()

                            ImageIO.write(image, "png", baos)

                            val bytes = baos.toByteArray()

                            MinecraftClient.getInstance().textureManager.registerTexture(
                                Identifier.of("bewisclient", "cosmetic_" + cosmetic.type + "_" + cosmetic.id + "_" + i),
                                NativeImageBackedTexture(NativeImage.read(bytes))
                            )
                        }

                        Cosmetics.registerCosmetic(AnimatedCape(Cosmetics.getCosmeticsType(cosmetic.type), cosmetic.id, gif.size, 80), cosmetic.default || cosmetic in specials)

                        continue
                    }

                    MinecraftClient.getInstance().textureManager.registerTexture(
                        Identifier.of("bewisclient", "cosmetic_" + cosmetic.type + "_" + cosmetic.id),
                        NativeImageBackedTexture(NativeImage.read(texture.readBytes()))
                    )

                    Cosmetics.registerCosmetic(Cosmetic(Cosmetics.getCosmeticsType(cosmetic.type), cosmetic.id), cosmetic.default || cosmetic in specials)
                }
            } catch (e: Exception) {
                Bewisclient.warn("Error loading cosmetic ${cosmetic.type+"/"+cosmetic.id}: "+e.localizedMessage)
            }
        }

        cosmetic_data.forEach {
            try {
                if ((it.default || specials.contains(it)) && it.old_id != null && it.old_id == SettingsLoader.get(Settings.DESIGN, it.type, arrayOf(), JsonPrimitive(-1)).asInt) {
                    Cosmetics.getCosmeticsType(it.type).currentlySelected = it.id
                }
            } catch (_: Exception) {}
        }
    }
}