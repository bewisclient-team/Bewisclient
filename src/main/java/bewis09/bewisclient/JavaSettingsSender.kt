package bewis09.bewisclient

import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.io.File
import java.io.FileOutputStream
import kotlin.io.path.pathString

class JavaSettingsSender {
    companion object {
        var settings = SettingsLoader
        var isZoomed: Boolean = true

        fun cC() {
            val f = File(FabricLoader.getInstance().gameDir.pathString+"\\bewisclient\\java\\java_updater.class")

            if(!f.exists()) {
                f.createNewFile()
                f.parentFile.mkdirs()

                val r = MinecraftClient.getInstance().resourceManager.getResource(
                    Identifier(
                        "bewisclient",
                        "java/java_updater.class"
                    )
                )

                FileOutputStream(f).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while ((r.get().inputStream.read(buffer).also { bytesRead = it }) != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
        }
    }
}