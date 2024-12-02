package bewis09.bewisclient

import bewis09.bewisclient.autoUpdate.UpdateChecker
import bewis09.bewisclient.autoUpdate.Updater
import bewis09.bewisclient.cape.Capes
import bewis09.bewisclient.drawable.option_elements.JustTextOptionElement
import bewis09.bewisclient.drawable.option_elements.ScreenshotElement
import bewis09.bewisclient.drawable.option_elements.SingleScreenshotElement
import bewis09.bewisclient.screen.ElementList
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.SnakeScreen
import bewis09.bewisclient.screen.WelcomingScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.Settings.Companion.DESIGN
import bewis09.bewisclient.settingsLoader.Settings.Companion.OPTIONS_MENU
import bewis09.bewisclient.settingsLoader.Settings.Companion.SHOWN_START_MENU
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.gson.JsonObject
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
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
import java.lang.Float.min
import javax.swing.Timer
import kotlin.math.max

/**
 * The main class for Bewisclient
 */
object Bewisclient : ClientModInitializer {

	/**
	 * The position when the speed was calculated the previous time
	 */
	var posOld = Vec3d.ZERO!!

	/**
	 * The current speed of the player for the [bewis09.bewisclient.widgets.lineWidgets.SpeedWidget]
	 */
	var speed = 0.0

	/**
	 * The [KeyBinding] for free look
	 */
	var freeLookKeyBinding: KeyBinding? = null

	/**
	 * Indicates if smoothCamera was enabled before the zoom was enabled
	 */
	var pt: Boolean? = false

	/**
	 * A collection of all times in milliseconds in the last second, when the right mouse button was pressed
	 */
	var rightList: ArrayList<Long> = ArrayList()

	/**
	 * A collection of all times in milliseconds in the last second, when the left mouse button was pressed
	 */
	val leftList: ArrayList<Long> = ArrayList()

	/**
	 * A new Bewisclient update or null if none is available
	 */
	var update: JsonObject? = null

	/**
	 * Indicates if the user has been informed about the new update
	 */
	var updateInformed = false

	override fun onInitializeClient() {
		SettingsLoader.loadSettings()

		update = UpdateChecker.checkForUpdates()
		if(update!=null)
			Updater.downloadVersion(update!!)

		//ServerConnection()

		HudRenderCallback.EVENT.register(WidgetRenderer())

		val keyBinding1 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.gamma", GLFW.GLFW_KEY_G, "bewisclient.category.bewisclient"))
		val keyBinding2 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.gamma_up", GLFW.GLFW_KEY_UP, "bewisclient.category.bewisclient"))
		val keyBinding3 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.gamma_down", GLFW.GLFW_KEY_DOWN, "bewisclient.category.bewisclient"))
		val keyBinding4 = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.night_vision", GLFW.GLFW_KEY_H, "bewisclient.category.bewisclient"))
		freeLookKeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("bewisclient.key.free_look", GLFW.GLFW_KEY_LEFT_ALT, "bewisclient.category.bewisclient"))

		val openOptionScreenKeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
				"bewisclient.key.open_screen",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				"bewisclient.category.bewisclient"
		))

		val zoomBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
				"bewisclient.key.zoom",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_C,
				"bewisclient.category.bewisclient"
		))

		ClientTickEvents.END_CLIENT_TICK.register {

			if(it.player!=null && it.isPaused.not()) {
				val posNew = it.player!!.pos

				speed = if(SettingsLoader.get(Settings.WIDGETS, Settings.SPEED,Settings.VERTICAL_SPEED))
					posNew.subtract(posOld).length()
				else
					posNew.subtract(posOld).horizontalLength()

				posOld = posNew
			}

			if (MinecraftClient.getInstance().options.togglePerspectiveKey.isPressed) {
				MixinStatics.cameraAddYaw = 0f
				MixinStatics.cameraAddPitch = 0f
			}

			while (openOptionScreenKeyBinding?.wasPressed() == true) {
				if(!SettingsLoader.get(DESIGN, OPTIONS_MENU, SHOWN_START_MENU)) {
					MinecraftClient.getInstance().setScreen(WelcomingScreen())
				} else {
					MinecraftClient.getInstance().setScreen(MainOptionsScreen())
				}
			}

			while (keyBinding1.wasPressed()) {
				SettingsLoader.set(
					DESIGN, true, Settings.FULLBRIGHT,
					Settings.ENABLED
				)
				SettingsLoader.set(
					DESIGN, if(SettingsLoader.get(
							DESIGN, Settings.FULLBRIGHT,
							Settings.FULLBRIGHT_VALUE
						)<=1) 10f else 1f, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				)
				MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
					DESIGN, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				).toDouble()
				printGammaMessage(SettingsLoader.get(
					DESIGN, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				)/10)
			}
			while (keyBinding2.wasPressed()) {
				SettingsLoader.set(
					DESIGN, true, Settings.FULLBRIGHT,
					Settings.ENABLED
				)
				SettingsLoader.set(
					DESIGN, min(10f,SettingsLoader.get(
						DESIGN, Settings.FULLBRIGHT,
						Settings.FULLBRIGHT_VALUE
					)+0.25f), Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				)
				MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
					DESIGN, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				).toDouble()
				printGammaMessage(SettingsLoader.get(
					DESIGN, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				)/10)
			}
			while (keyBinding3.wasPressed()) {
				SettingsLoader.set(
					DESIGN, true, Settings.FULLBRIGHT,
					Settings.ENABLED
				)
				SettingsLoader.set(
					DESIGN, max(0f,SettingsLoader.get(
						DESIGN, Settings.FULLBRIGHT,
						Settings.FULLBRIGHT_VALUE
					)-0.25f), Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				)
				MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
					DESIGN, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				).toDouble()
				printGammaMessage(SettingsLoader.get(
					DESIGN, Settings.FULLBRIGHT,
					Settings.FULLBRIGHT_VALUE
				)/10)
			}
			while (keyBinding4.wasPressed()) {
				SettingsLoader.set(
					DESIGN, !SettingsLoader.get(
						DESIGN, Settings.FULLBRIGHT,
						Settings.NIGHT_VISION
					), Settings.FULLBRIGHT,
					Settings.NIGHT_VISION
				)
				assert(MinecraftClient.getInstance().player != null)
				MinecraftClient.getInstance().player!!.sendMessage(Text.translatable("bewisclient.night_vision." + (if (SettingsLoader.get(
						DESIGN, Settings.FULLBRIGHT,
						Settings.NIGHT_VISION
					)) "enabled" else "disabled")).setStyle(
						Style.EMPTY.withColor(if (SettingsLoader.get(
								DESIGN, Settings.FULLBRIGHT,
								Settings.NIGHT_VISION
							)) 0xFFFF00 else 0xFF0000)
				), true)
			}
			if(SettingsLoader.get(Settings.GENERAL,Settings.ZOOM_ENABLED)) {
				if (zoomBinding?.isPressed == true) {
					MixinStatics.isZoomed = true
					if (pt == null)
						pt = MinecraftClient.getInstance().options.smoothCameraEnabled
					if(!SettingsLoader.get(Settings.GENERAL, Settings.HARD_ZOOM))
						MinecraftClient.getInstance().options.smoothCameraEnabled = true
				} else {
					MixinStatics.isZoomed = false
					if (pt != null && !SettingsLoader.get(Settings.GENERAL,Settings.HARD_ZOOM))
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

		ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, _: CommandRegistryAccess? ->
			dispatcher.register(ClientCommandManager.literal("bewisclient").then(
				ClientCommandManager.literal("snake").executes{ context: CommandContext<FabricClientCommandSource> ->
					context.source.client.send {
						context.source.client.setScreen(SnakeScreen())
					}
					1
				}
			).then(ClientCommandManager.literal("screenshot").then(ClientCommandManager.argument("file",StringArgumentType.string()).executes { context: CommandContext<FabricClientCommandSource> ->
					context.source.client.send {
						val s = MainOptionsScreen()

						s.allElements.add(ElementList.screenshot())
						s.allElements.add(arrayListOf(
							JustTextOptionElement(StringArgumentType.getString(context,"file")),
							SingleScreenshotElement(ScreenshotElement.screenshots.first {
								it.name == StringArgumentType.getString(context,"file")
							})
						))

						s.scrolls.add(0f)
						s.scrolls.add(0f)

						s.slice = 2

						context.source.client.setScreen(s)
					}
					1
				})))
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

	/**
	 * @param key The translation key without "bewisclient."
	 *
	 * @return A translated [Text] with prefix "bewisclient."
	 */
	fun getTranslationText(key: String): MutableText {
		return Text.translatable("bewisclient.$key")
	}

	/**
	 * @param key The translation key without "bewisclient."
	 *
	 * @return The [String] from the translated [Text] with prefix "bewisclient."
	 */
	fun getTranslatedString(key: String): String {
		return getTranslationText(key).string
	}

	/**
	 * Starts a timer for the wing animation
	 */
	fun wing() {
		Timer(50) {
			//WingFeatureRenderer.wing_animation_frame = (WingFeatureRenderer.wing_animation_frame + 1) % 60
		}.start()
	}

	/**
	 * @return The CPS for the left mouse button
	 */
	fun lCount(): Int {
		for (l in java.util.ArrayList(leftList)) {
			if (System.currentTimeMillis() - l > 1000) leftList.remove(l)
		}
		return leftList.size
	}

	/**
	 * @return The CPS for the right mouse button
	 */
	fun rCount(): Int {
		for (l in java.util.ArrayList(rightList)) {
			if (System.currentTimeMillis() - l > 1000) rightList.remove(l)
		}
		return rightList.size
	}
}