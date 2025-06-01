package bewis09.bewisclient.autoUpdate

import bewis09.bewisclient.autoUpdate.UpdateChecker.getVersionNumber
import bewis09.bewisclient.util.NetTools
import bewis09.bewisclient.util.Result
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.SharedConstants
import kotlin.jvm.optionals.getOrElse

/**
 * Checks for updates
 */
object UpdateChecker {

    /**
     * Checks for updates
     */
    fun checkForUpdates(): Result<JsonObject?, Throwable> {
        return NetTools.loadHttpJsonData("https://api.modrinth.com/v2/project/bewisclient/version", JsonArray::class.java).map { jsonResponse ->
            var new: JsonObject? = null
            var vListed = false
            var versionNumber = 0

            for (r in jsonResponse) {
                if (r.isJsonObject) {
                    val l = r.asJsonObject

                    if (l.get("version_number").asString == getCurrentVersion()) {
                        vListed = true
                        break
                    }

                    if (l.get("game_versions").asJsonArray.contains(JsonPrimitive(SharedConstants.getGameVersion().name))) {
                        if (getVersionNumber(l.get("version_type").asString) >= getCurrentVersionNumber() && (new == null || getVersionNumber(l.get("version_type").asString) > versionNumber)) {
                            new = l
                            versionNumber = getVersionNumber(l.get("version_type").asString)
                        }
                    }
                }
            }

            if (vListed)
                return@map new

            return@map null
        }
    }

    /**
     * @return The version that is currently installed
     */
    fun getCurrentVersion(): String {
        return FabricLoader.getInstance().getModContainer("bewisclient").map { it.metadata.version.friendlyString }.getOrElse { "" }
    }

    /**
     * @return The version number given by [getVersionNumber] for the version that is currently installed
     */
    fun getCurrentVersionNumber(): Int {
        val v = getCurrentVersion()

        if (v.split("-").size < 2)
            return 2

        return getVersionNumber(v.split("-")[1].split(".")[0])
    }

    /**
     * @param s The version type as [String]
     *
     * @return 0, 1 or 2 if [s] is alpha, beta or anything else
     */
    fun getVersionNumber(s: String): Int {
        return when (s) {
            "beta" -> 1
            "alpha" -> 0
            else -> 2
        }
    }
}