package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.BooleanOptionElement
import bewis09.bewisclient.drawable.option_elements.settings.TitleWidgetEnablerOptionElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.element_options.BooleanSettingsElementOptions
import com.google.gson.JsonPrimitive

open class BooleanSetting(settings: String, path: Array<String>, id: String, defaultValue: Boolean, elementOptions: BooleanSettingsElementOptions?) : Setting<Boolean, BooleanSettingsElementOptions>(settings, path, id, defaultValue, elementOptions ?: BooleanSettingsElementOptions()) {
    override fun get(): Boolean {
        return SettingsLoader.get(settings,id,path,JsonPrimitive(defaultValue)).asBoolean
    }

    override fun set(value: Boolean) {
        SettingsLoader.set(settings,JsonPrimitive(value),path,id)
    }

    override fun createOptionElement(): OptionElement {
        if(elementOptions.title)
            return TitleWidgetEnablerOptionElement(this, "setting.$settings.$id")
        return BooleanOptionElement(this)
    }
}