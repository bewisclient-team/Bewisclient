package bewis09.bewisclient

import bewis09.bewisclient.mixin.ZoomMixin
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.WidgetRenderer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object Bewisclient : ModInitializer {
    private val LOGGER: Logger = LoggerFactory.getLogger("bewisclient")

	var openOptionScreenKeyBindimg: KeyBinding? = null
	var zoomBinding: KeyBinding? = null

	var pt: Boolean? = false

	override fun onInitialize() {
		SettingsLoader.loadSettings()

		HudRenderCallback.EVENT.register(WidgetRenderer())

		openOptionScreenKeyBindimg = KeyBindingHelper.registerKeyBinding(KeyBinding(
				"bewisclient.key.open_screen",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				"bewisclient.category.bewisclient"
		))

		zoomBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
				"bewisclient.key.zoom",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_C,
				"bewisclient.category.bewisclient"
		))

		ClientTickEvents.END_CLIENT_TICK.register {
			while (openOptionScreenKeyBindimg?.wasPressed() == true) {
				it.setScreen(MainOptionsScreen())
			}
			if(SettingsLoader.GeneralSettings.getValue<Boolean>("zoom_enabled") == true) {
				if (zoomBinding?.isPressed == true) {
					JavaSettingsSender.isZoomed = true
					if (pt == null)
						pt = MinecraftClient.getInstance().options.smoothCameraEnabled
					if(SettingsLoader.GeneralSettings.getValue<Boolean>("hard_zoom") != true)
						MinecraftClient.getInstance().options.smoothCameraEnabled = true
				} else {
					JavaSettingsSender.isZoomed = false
					if (pt != null && SettingsLoader.GeneralSettings.getValue<Boolean>("hard_zoom") != true)
						MinecraftClient.getInstance().options.smoothCameraEnabled = pt!!
					pt = null
				}
			}
		}
	}

	fun log(string: Any?) {
		LOGGER.info(string.toString())
	}

	fun getTranslationText(key: String): Text {
		return Text.translatable("bewisclient.$key")
	}

	fun getTranslatedString(key: String): String {
		return getTranslationText(key).string
	}
}