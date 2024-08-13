package bewis09.bewisclient.autoUpdate

import bewis09.bewisclient.Bewisclient.Companion.Companion.update
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import org.apache.commons.io.FileUtils
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URI
import java.util.*
import kotlin.io.path.pathString


object Updater {
    fun downloadVersion(version: JsonObject) {
        if (!System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
            return
        }

        if(!SettingsLoader.get("general", Settings.Settings.EXPERIMENTAL, Settings.Settings.AUTO_UPDATE)) return

        val file = File(FabricLoader.getInstance().gameDir.pathString+"\\bewisclient\\download\\"+ update!!["name"].asString.lowercase(
            Locale.getDefault()
        ).replace(" ", "-")+".jar")

        file.parentFile.mkdirs()
        if(!file.exists()) {
            var fileURL: String? = null

            for (i in version.get("files").asJsonArray) {
                if (i.asJsonObject.get("primary").asBoolean) {
                    fileURL = i.asJsonObject.get("url").asString
                }
            }

            if(fileURL!=null) {
                val stream = URI(fileURL).toURL().openStream()

                stream.copyTo(file.outputStream())
            }
        }

        val f = File(FabricLoader.getInstance().gameDir.pathString+"\\bewisclient\\java\\JavaUpdater.class")

        if(!f.exists()) {
            f.parentFile.mkdirs()
            f.createNewFile()

            FileUtils.copyInputStreamToFile(ByteArrayInputStream(UpdateClass.CLASS_ARRAY), f)
        }
    }
}