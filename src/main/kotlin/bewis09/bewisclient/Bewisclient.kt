package bewis09.bewisclient

import bewis09.bewisclient.autoUpdate.UpdateChecker
import bewis09.bewisclient.autoUpdate.Updater
import bewis09.bewisclient.cape.Capes
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.SnakeScreen
import bewis09.bewisclient.server.ServerConnection
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.WidgetRenderer
import bewis09.bewisclient.wings.WingFeatureRenderer
import com.google.gson.JsonObject
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
			var update: JsonObject? = null
		}
	}

	override fun onInitializeClient() {
		Companion.update = UpdateChecker.checkForUpdates()
		if(Companion.update!=null)
			Updater.downloadVersion(Companion.update!!)

		SettingsLoader.loadSettings()

		//ServerConnection()

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

				speed = if(SettingsLoader.getBoolean("widgets","speed.vertical_speed"))
					posNew.subtract(posOld).length()
				else
					posNew.subtract(posOld).horizontalLength()
			}

			while (openOptionScreenKeyBindimg?.wasPressed() == true) {
				it.setScreen(MainOptionsScreen())
			}

			while (keyBinding1.wasPressed()) {
				SettingsLoader.set("design","fullbright.enabled",true)
				SettingsLoader.set("design","fullbright.value",if(SettingsLoader.getFloat("design","fullbright.value")<=1) 10f else 1f)
				MinecraftClient.getInstance().options.gamma.value = SettingsLoader.getFloat("design","fullbright.value").toDouble()
				printGammaMessage(SettingsLoader.getFloat("design","fullbright.value")/10)
			}
			while (keyBinding2.wasPressed()) {
				SettingsLoader.set("design","fullbright.enabled",true)
				SettingsLoader.set("design","fullbright.value",min(10f,SettingsLoader.getFloat("design","fullbright.value")+0.25f))
				MinecraftClient.getInstance().options.gamma.value = SettingsLoader.getFloat("design","fullbright.value").toDouble()
				printGammaMessage(SettingsLoader.getFloat("design","fullbright.value")/10)
			}
			while (keyBinding3.wasPressed()) {
				SettingsLoader.set("design","fullbright.enabled",true)
				SettingsLoader.set("design","fullbright.value",max(0f,SettingsLoader.getFloat("design","fullbright.value")-0.25f))
				MinecraftClient.getInstance().options.gamma.value = SettingsLoader.getFloat("design","fullbright.value").toDouble()
				printGammaMessage(SettingsLoader.getFloat("design","fullbright.value")/10)
			}
			while (keyBinding4.wasPressed()) {
				SettingsLoader.set("design","fullbright.night_vision",!SettingsLoader.getBoolean("design","fullbright.night_vision"))
				assert(MinecraftClient.getInstance().player != null)
				MinecraftClient.getInstance().player!!.sendMessage(Text.translatable("bewisclient.night_vision." + (if (SettingsLoader.getBoolean("design","fullbright.night_vision")) "enabled" else "disabled")).setStyle(
						Style.EMPTY.withColor(if (SettingsLoader.getBoolean("design","fullbright.night_vision")) 0xFFFF00 else 0xFF0000)
				), true)
			}
			if(SettingsLoader.getBoolean("general","zoom_enabled")) {
				if (zoomBinding?.isPressed == true) {
					JavaSettingsSender.isZoomed = true
					if (pt == null)
						pt = MinecraftClient.getInstance().options.smoothCameraEnabled
					if(!SettingsLoader.getBoolean("general","hard_zoom"))
						MinecraftClient.getInstance().options.smoothCameraEnabled = true
				} else {
					JavaSettingsSender.isZoomed = false
					if (pt != null && !SettingsLoader.getBoolean("general","hard_zoom"))
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