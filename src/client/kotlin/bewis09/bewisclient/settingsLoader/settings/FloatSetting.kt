package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.element_options.FloatSettingsElementOptions
import com.google.gson.JsonPrimitive

class FloatSetting(settings: String, path: Array<String>, id: String, defaultValue: Float, elementOptions: FloatSettingsElementOptions) : Setting<Float, FloatSettingsElementOptions>(settings, path, id, defaultValue, elementOptions) {

    override fun get(): Float {
        return try {
            SettingsLoader.get(settings, id, path, JsonPrimitive(defaultValue)).asFloat
        } catch (e: Exception) {
            defaultValue
        }
    }

    override fun set(value: Float) {
        SettingsLoader.set(settings,JsonPrimitive(value),path,id)
    }

    override fun createOptionElement(): OptionElement {
        return FloatOptionElement(this)
    }
}