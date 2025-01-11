package bewis09.bewisclient.server

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.cosmetics.AnimatedCape
import bewis09.bewisclient.cosmetics.Cosmetic
import bewis09.bewisclient.cosmetics.Cosmetics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets

object ServerConnection {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    var specials: Array<Cosmetic> = arrayOf()
    var cosmetic_data: Array<Cosmetic> = arrayOf()
    var user_data: Array<String> = arrayOf()
    var current: Selection? = null

    var hasLoadedOnline = false

    fun load() {
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

            val file = File("${FabricLoader.getInstance().gameDir}/bewisclient/server/data.json")

            file.parentFile.mkdirs()
            file.createNewFile()

            file.writeText(gson.toJson(JsonObject().apply {
                add("specials", gson.toJsonTree(specials))
                add("cosmetics", gson.toJsonTree(cosmetic_data))
            }))

            hasLoadedOnline = true

            Bewisclient.info("Successfully loaded Bewisclient data from server")
        } catch (e: Exception) {
            Bewisclient.warn("Failed to load Bewisclient data from server, using data from previous runs: ${e.message}")

            val file = File("${FabricLoader.getInstance().gameDir}/bewisclient/server/data.json")

            if (file.exists()) {
                val data = gson.fromJson(file.readText(), JsonObject::class.java)

                specials = gson.fromJson(data.getAsJsonArray("specials"), Array<Cosmetic>::class.java)
                cosmetic_data = gson.fromJson(data.getAsJsonArray("cosmetics"), Array<Cosmetic>::class.java)
            }
        }

        for (cosmetic in cosmetic_data) {
            if(cosmetic.frames > 1)
                Cosmetics.registerCosmetic(AnimatedCape(Cosmetics.getCosmeticsType(cosmetic.type), cosmetic.id, cosmetic.frames, 80))
            else
                Cosmetics.registerCosmetic(Cosmetic(Cosmetics.getCosmeticsType(cosmetic.type), cosmetic.id))
        }
    }

    data class ReturnData(val specials: Array<Cosmetic>, val user_data: Array<String>, val cosmetics: Array<Cosmetic>, val api_level: Int, val current: Selection) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ReturnData

            if (api_level != other.api_level) return false
            if (!specials.contentEquals(other.specials)) return false
            if (!user_data.contentEquals(other.user_data)) return false
            if (!cosmetics.contentEquals(other.cosmetics)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = api_level
            result = 31 * result + specials.contentHashCode()
            result = 31 * result + user_data.hashCode()
            result = 31 * result + cosmetics.contentHashCode()
            return result
        }
    }

    data class Cosmetic(val type: String, val id: String, val frames: Int)

    data class Selection(val wing: String, val hat: String, val cape: String)
}