package bewis09.bewisclient.server

import com.google.gson.Gson
import net.minecraft.client.MinecraftClient
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets

// work in progress
@Suppress("unused")
class ServerConnection {
    val gson: Gson = Gson()

    var specials: Array<Cosmetic> = arrayOf()
    var cosmetic_data: Array<Cosmetic> = arrayOf()
    var user_data: Array<UserData> = arrayOf()

    init {
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


    }

    data class ReturnData(val specials: Array<Cosmetic>, val user_data: UserData, val cosmetics: Array<Cosmetic>, val api_level: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ReturnData

            if (api_level != other.api_level) return false
            if (!specials.contentEquals(other.specials)) return false
            if (user_data != other.user_data) return false
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

    data class Cosmetic(val type: String, val name: String)

    data class UserData(val id: String, val wing: String, val hat: String, val cape: String)
}