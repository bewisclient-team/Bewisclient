package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement
import bewis09.bewisclient.settingsLoader.settings.*
import bewis09.bewisclient.settingsLoader.settings.element_options.*
import bewis09.bewisclient.util.ColorSaver
import bewis09.bewisclient.widgets.WidgetRenderer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object SettingTypes {
    const val WIDGETS = "widgets"

    abstract class SettingsObject {
        open fun getSettingList(): SettingList {
            return SettingList()
        }
    }

    interface ColorTextSettingsObject {
        fun getTextColor(): ColorSaver
    }

    open class DefaultWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) : SettingsObject() {
        val posX = FloatSetting(WIDGETS, arrayOf(id), "posX", posX, null)
        val partX = FloatSetting(WIDGETS, arrayOf(id), "partX", partX, null)
        val posY = FloatSetting(WIDGETS, arrayOf(id), "posY", posY, null)
        val partY = FloatSetting(WIDGETS, arrayOf(id), "partY", partY, null)
        val transparency = FloatSetting(WIDGETS, arrayOf(id), "transparency", transparency, FloatSettingsElementOptions().withSliderInfo(FloatOptionElement.SliderInfo(0f, 1f, 2)))
        val size = FloatSetting(WIDGETS, arrayOf(id), "size", size, FloatSettingsElementOptions().withSliderInfo(FloatOptionElement.SliderInfo(0.2f, 2f, 2)))
        val enabled = BooleanSetting(WIDGETS, arrayOf(id), "enabled", true, BooleanSettingsElementOptions().addPathToTitle())

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(transparency, size, enabled)
        }
    }

    open class TextWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        DefaultWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size), ColorTextSettingsObject {
        val textColor = ColorSaverSetting(WIDGETS, arrayOf(id), "text_color", ColorSaver.of(0xFFFFFF), null)

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(textColor)
        }

        override fun getTextColor(): ColorSaver {
            return textColor.get()
        }
    }

    open class SpeedWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        TextWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size) {
        val verticalSpeed = BooleanSetting(WIDGETS, arrayOf(id), "vertical_speed", false, BooleanSettingsElementOptions().withDescription())

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(verticalSpeed)
        }
    }

    open class ColorcodeBiomeWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        DefaultWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size), ColorTextSettingsObject {
        val colorcodeBiome = BooleanSetting(WIDGETS, arrayOf(id), "colorcode_biome", false, null)
        val textColor = ColorSaverSetting(WIDGETS, arrayOf(id), "text_color", ColorSaver.of(0xFFFFFF), DefaultSettingElementOptions().withEnableFunction {
            !WidgetRenderer.biomeWidget.settings.colorcodeBiome.get()
        })

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(colorcodeBiome, textColor)
        }

        override fun getTextColor(): ColorSaver {
            return textColor.get()
        }
    }

    open class DaytimeWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        TextWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size) {
        val clock24 = BooleanSetting(WIDGETS, arrayOf(id), "24clock", !((DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault())) as SimpleDateFormat).toPattern().contains("a"), null)

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(clock24)
        }
    }

    open class CPSWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        TextWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size) {
        val cpsElements = ArraySetting(WIDGETS, arrayOf(id), "cps_elements", arrayOf("cps.both", "cps.left", "cps.right"), 0, DefaultSettingElementOptions())

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(cpsElements)
        }
    }

    open class CoordinatesWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        TextWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size) {
        val showBiome = BooleanSetting(WIDGETS, arrayOf(id, "select_parts"), "show_biome", true, null)
        val showDirection = BooleanSetting(WIDGETS, arrayOf(id, "select_parts"), "show_direction", true, null)
        val selectParts = MultipleBooleanSetting(WIDGETS, arrayOf(id, "select_parts"), arrayOf(showBiome, showDirection))
        val colorcodeBiome = BooleanSetting(WIDGETS, arrayOf(id), "colorcode_biome", true, BooleanSettingsElementOptions().withEnableFunction {
            showBiome.get()
        })

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(selectParts, colorcodeBiome)
        }
    }

    open class KeyWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        TextWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size) {
        val showMovementKeys = BooleanSetting(WIDGETS, arrayOf(id, "select_parts"), "show_movement_keys", true, null)
        val showSpaceBar = BooleanSetting(WIDGETS, arrayOf(id, "select_parts"), "show_space_bar", true, null)
        val showMouseButton = BooleanSetting(WIDGETS, arrayOf(id, "select_parts"), "show_mouse_button", true, null)
        val showCps = BooleanSetting(WIDGETS, arrayOf(id), "show_cps", false, null)
        val selectParts = MultipleBooleanSetting(WIDGETS, arrayOf(id, "select_parts"), arrayOf(showMovementKeys, showSpaceBar, showMouseButton))

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(selectParts, showCps)
        }
    }

    open class TiwylaWidgetSettingsObject(id: String, posX: Float, partX: Float, posY: Float, partY: Float, transparency: Float, size: Float) :
        DaytimeWidgetSettingsObject(id, posX, partX, posY, partY, transparency, size) {
        private val tiwylaArray = arrayOf(
            "tiwyla.tool",
            "tiwyla.level",
            "tiwyla.time",
            "tiwyla.tool.progress",
            "tiwyla.level.progress",
            "tiwyla.time.progress",
            "tiwyla.tool.extra",
            "tiwyla.level.extra",
            "tiwyla.time.extra"
        )

        val topColor = ColorSaverSetting(WIDGETS, arrayOf(id), "top_color", ColorSaver.of("0xFFFFFF"), null)
        val bottomColor = ColorSaverSetting(WIDGETS, arrayOf(id), "bottom_color", ColorSaver.of("0xFFFFFF"), null)

        val firstLine = ArraySetting(WIDGETS, arrayOf(id), "first_line", tiwylaArray, 6, null)
        val secondLine = ArraySetting(WIDGETS, arrayOf(id), "second_line", tiwylaArray, 1, null)
        val thirdLine = ArraySetting(WIDGETS, arrayOf(id), "third_line", tiwylaArray, 5, null)

        val showBlockIcon = BooleanSetting(WIDGETS, arrayOf(id), "show_block_icon", true, null)
        val showHealthInformation = BooleanSetting(WIDGETS, arrayOf(id), "show_health_information", true, null)
        val showProgressBar = BooleanSetting(WIDGETS, arrayOf(id), "show_progress_bar", true, null)

        val multiple = MultipleBooleanSetting(WIDGETS, arrayOf(id, "select_parts"), arrayOf(showProgressBar, showHealthInformation, showBlockIcon))

        override fun getSettingList(): SettingList {
            return super.getSettingList().append(topColor, bottomColor, firstLine, secondLine, thirdLine, multiple)
        }
    }

    class SettingList : ArrayList<Setting<*, *>>() {
        fun append(vararg settings: Setting<*, *>): SettingList {
            this.addAll(settings)

            return this
        }
    }
}