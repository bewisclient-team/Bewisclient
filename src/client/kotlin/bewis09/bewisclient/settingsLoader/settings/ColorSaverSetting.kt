package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.ColorPickerElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.element_options.DefaultSettingElementOptions
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.JsonPrimitive

class ColorSaverSetting(settings: String, path: Array<String>, id: String, defaultValue: ColorSaver, elementOptions: DefaultSettingElementOptions) : Setting<ColorSaver, DefaultSettingElementOptions>(settings, path, id, defaultValue, elementOptions) {
    override fun get(): ColorSaver {
        return try {
            ColorSaver.of(SettingsLoader.get(settings, id, path, JsonPrimitive(defaultValue.toString())).asString)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override fun set(value: ColorSaver) {
        SettingsLoader.set(settings, JsonPrimitive(value.toString()), path, id)
    }

    override fun createOptionElement(): OptionElement {
        return ColorPickerElement(this)
    }
}