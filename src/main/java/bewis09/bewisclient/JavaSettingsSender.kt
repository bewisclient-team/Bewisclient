package bewis09.bewisclient

import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import kotlin.io.path.pathString

class JavaSettingsSender {
    companion object {
        var settings = SettingsLoader
        var isZoomed: Boolean = true
    }
}