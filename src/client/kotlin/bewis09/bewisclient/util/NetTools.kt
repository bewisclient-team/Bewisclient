package bewis09.bewisclient.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.net.URI

object NetTools {
    val GSON = Gson()

    fun loadHttpData(url: String): Result<String, Throwable> {
        return Result.catch {
            return@catch URI(url).toURL().readText()
        }
    }

    fun loadHttpJsonData(url: String): Result<JsonObject, Throwable> {
        return Result.catch {
            return@catch GSON.fromJson(loadHttpData(url).unwrap(), JsonObject::class.java)
        }
    }

    fun <T> loadHttpJsonData(url: String, jsonClass: Class<T>): Result<T, Throwable> {
        return Result.catch {
            return@catch GSON.fromJson(loadHttpData(url).unwrap(), jsonClass)
        }
    }
}