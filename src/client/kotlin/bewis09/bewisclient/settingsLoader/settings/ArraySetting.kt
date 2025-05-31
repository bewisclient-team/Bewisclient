package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.ArrayOptionElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.element_options.DefaultSettingElementOptions
import com.google.gson.JsonPrimitive

class ArraySetting(settings: String, path: Array<String>, id: String, val entries: Array<String>, defaultValue: Int, elementOptions: DefaultSettingElementOptions?) : Setting<Int, DefaultSettingElementOptions>(settings, path, id, defaultValue, elementOptions ?: DefaultSettingElementOptions()) {
    override fun get(): Int {
        return SettingsLoader.get(settings,id,path,JsonPrimitive(defaultValue)).asInt
    }

    override fun set(value: Int) {
        SettingsLoader.set(settings, JsonPrimitive(value),path,id)
    }

    override fun createOptionElement(): OptionElement {
        return ArrayOptionElement(this)
    }
}