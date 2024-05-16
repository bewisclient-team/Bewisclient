package bewis09.bewisclient

import bewis09.bewisclient.cape.Capes
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.SnakeScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.WidgetRenderer
import bewis09.bewisclient.wings.WingFeatureRenderer
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.event.ActionEvent
import java.lang.Float.min
import javax.swing.Timer
import kotlin.math.max

object Bewisclient : ClientModInitializer {
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

	override fun onInitializeClient() {
		SettingsLoader.loadSettings()

		HudRenderCallback.EVENT.register(WidgetRenderer())

		val keyBinding1 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.gamma", GLFW.GLFW_KEY_G, "bewisclient.category.bewisclient"))
		val keyBinding2 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.gamma_up", GLFW.GLFW_KEY_UP, "bewisclient.category.bewisclient"))
		val keyBinding3 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.gamma_down", GLFW.GLFW_KEY_DOWN, "bewisclient.category.bewisclient"))
		val keyBinding4 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.night_vision", GLFW.GLFW_KEY_H, "bewisclient.category.bewisclient"))

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

				speed = if(SettingsLoader.WidgetSettings.getValue<SettingsLoader.Settings>("speed").getValue("vertical_speed"))
					posNew.subtract(posOld).length()
				else
					posNew.subtract(posOld).horizontalLength()
			}

			while (openOptionScreenKeyBindimg?.wasPressed() == true) {
				it.setScreen(MainOptionsScreen())
			}

			while (keyBinding1.wasPressed()) {
				val fullBright = SettingsLoader.DesignSettings.getValue<SettingsLoader.Settings>("fullbright")
				fullBright.setValue("enabled",true)
				fullBright.setValue("value",if(fullBright.getValue<Float>("value")<=1) 10f else 1f)
				MinecraftClient.getInstance().options.gamma.value = fullBright.getValue<Float>("value").toDouble()
				printGammaMessage(fullBright.getValue<Float>("value")/10)
			}
			while (keyBinding2.wasPressed()) {
				val fullBright = SettingsLoader.DesignSettings.getValue<SettingsLoader.Settings>("fullbright")
				fullBright.setValue("enabled",true)
				fullBright.setValue("value",min(10f,fullBright.getValue<Float>("value")+0.25f))
				MinecraftClient.getInstance().options.gamma.value = fullBright.getValue<Float>("value").toDouble()
				printGammaMessage(fullBright.getValue<Float>("value")/10)
			}
			while (keyBinding3.wasPressed()) {
				val fullBright = SettingsLoader.DesignSettings.getValue<SettingsLoader.Settings>("fullbright")
				fullBright.setValue("enabled",true)
				fullBright.setValue("value",max(0f,fullBright.getValue<Float>("value")-0.25f))
				MinecraftClient.getInstance().options.gamma.value = fullBright.getValue<Float>("value").toDouble()
				printGammaMessage(fullBright.getValue<Float>("value")/10)
			}
			while (keyBinding4.wasPressed()) {
				val fullBright = SettingsLoader.DesignSettings.getValue<SettingsLoader.Settings>("fullbright")
				fullBright.setValue("night_vision",!fullBright.getValue<Boolean>("night_vision"))
				assert(MinecraftClient.getInstance().player != null)
				MinecraftClient.getInstance().player!!.sendMessage(Text.translatable("bewisclient.night_vision." + (if (fullBright.getValue("night_vision")) "enabled" else "disabled")).setStyle(
						Style.EMPTY.withColor(if (fullBright.getValue("night_vision")) 0xFFFF00 else 0xFF0000)
				), true)
			}
			if(SettingsLoader.GeneralSettings.getValue("zoom_enabled")) {
				if (zoomBinding?.isPressed == true) {
					JavaSettingsSender.isZoomed = true
					if (pt == null)
						pt = MinecraftClient.getInstance().options.smoothCameraEnabled
					if(!SettingsLoader.GeneralSettings.getValue<Boolean>("hard_zoom"))
						MinecraftClient.getInstance().options.smoothCameraEnabled = true
				} else {
					JavaSettingsSender.isZoomed = false
					if (pt != null && !SettingsLoader.GeneralSettings.getValue<Boolean>("hard_zoom"))
						MinecraftClient.getInstance().options.smoothCameraEnabled = pt!!
					pt = null
				}
			}
		}

		ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, _: CommandRegistryAccess? ->
			dispatcher.register(ClientCommandManager.literal("snake")
					.executes { context: CommandContext<FabricClientCommandSource> ->
						context.source.client.send {
							context.source.client.setScreen(SnakeScreen())
						}
						1
					})
		})

		wing()
		Capes.register()
	}

	fun printGammaMessage(gamma: Float) {
		assert(MinecraftClient.getInstance().player != null)
		MinecraftClient.getInstance().player!!.sendMessage(Text.translatable("options.gamma")
				.setStyle(Style.EMPTY.withColor((0xFF00 + (((gamma * 0xFF).toInt()))) shl 8))
				.append(": ").append((gamma * 1000f).toString() + "%"), true)
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

	fun wing() {
		Timer(50) {
			WingFeatureRenderer.wing_animation_duration = (WingFeatureRenderer.wing_animation_duration + 1) % 60
		}.start()
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