package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.element_options.ElementOptions
import com.google.gson.JsonPrimitive

class StringSetting(settings: String, path: Array<String>, id: String, defaultValue: String): Setting<String, ElementOptions>(settings, path, id, defaultValue, ElementOptions()) {
    override fun get(): String {
        return try {
            SettingsLoader.get(settings, id, path, JsonPrimitive(defaultValue)).asString
        } catch (e: Exception) {
            defaultValue
        }
    }

    override fun set(value: String) {
        SettingsLoader.set(settings, JsonPrimitive(value), path, id)
    }
}