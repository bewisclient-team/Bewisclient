package bewis09.bewisclient

import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.WidgetRenderer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object Bewisclient : ModInitializer {
    private val LOGGER: Logger = LoggerFactory.getLogger("bewisclient")

	var openOptionScreenKeyBindimg: KeyBinding? = null
	var zoomBinding: KeyBinding? = null

	var posOld = Vec3d.ZERO
	var posNew = Vec3d.ZERO

	var speed = 0.0

	var pt: Boolean? = false

	class Companion {
		companion object {
			var rightList: ArrayList<Long> = ArrayList()
			val leftList: ArrayList<Long> = ArrayList()
		}
	}

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

			if(it.player!=null && it.isPaused.not()) {
				posOld = posNew
				posNew = it.player!!.pos

				speed = if(SettingsLoader.WidgetSettings.getValue<SettingsLoader.Settings>("speed")?.getValue<Boolean>("vertical_speed")==true)
					posNew.subtract(posOld).length()
				else
					posNew.subtract(posOld).horizontalLength()
			}

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

	fun getTranslationText(key: String): MutableText {
		return Text.translatable("bewisclient.$key")
	}

	fun getTranslatedString(key: String): String {
		return getTranslationText(key).string
	}

	fun lCount(): Int {
		for (l in java.util.ArrayList(Companion.leftList)) {
			if (System.currentTimeMillis() - l > 1000) Companion.leftList.remove(l)
		}
		return Companion.leftList.size
	}

	fun rCount(): Int {
		for (l in java.util.ArrayList(Companion.rightList)) {
			if (System.currentTimeMillis() - l > 1000) Companion.rightList.remove(l)
		}
		return Companion.rightList.size
	}
}