package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.ArrayOptionElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.element_options.ElementOptions
import com.google.gson.JsonPrimitive

class ArraySetting : Setting<Int, ElementOptions> {
    val entries: Array<String>
    val descriptionEnabled: Boolean
    val enableFunction: (()->Boolean)?

    constructor(settings: String, path: Array<String>, id: String, entries: Array<String>) : this(settings, path, id, entries, false)

    constructor(settings: String, path: Array<String>, id: String, entries: Array<String>, descriptionEnabled: Boolean) : this(settings, path, id, entries, descriptionEnabled, null)

    constructor(settings: String, path: Array<String>, id: String, entries: Array<String>, descriptionEnabled: Boolean, enableFunction: (()->Boolean)?) : super(settings, path, id, 0, ElementOptions()) {
        this.entries = entries
        this.descriptionEnabled = descriptionEnabled ?: true
        this.enableFunction = enableFunction
    }

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