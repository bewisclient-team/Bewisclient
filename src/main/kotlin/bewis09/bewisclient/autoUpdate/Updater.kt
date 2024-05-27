package bewis09.bewisclient.autoUpdate

import bewis09.bewisclient.Bewisclient.Companion.Companion.update
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.util.*
import kotlin.io.path.pathString


object Updater {
    fun downloadVersion(version: JsonObject) {
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
    }
}