package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.MultipleBooleanOptionElement
import bewis09.bewisclient.settingsLoader.settings.element_options.ElementOptions
import bewis09.bewisclient.util.reduce
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class MultipleBooleanSetting(settings: String, path: Array<String>, val children: Array<BooleanSetting>): Setting<JsonObject, ElementOptions>(settings, path.copyOfRange(0,path.size-1), path.last(), toDefaultJsonObject(children), ElementOptions()) {
    override fun get(): JsonObject {
        throw InvalidSettingsGetterException()
    }

    override fun set(value: JsonObject) {
        throw InvalidSettingsSetterException()
    }

    override fun createOptionElement(): OptionElement {
        return MultipleBooleanOptionElement(this)
    }

    class InvalidSettingsGetterException: Exception("Tried to get an JsonObject setting, which is not allowed")
    class InvalidSettingsSetterException: Exception("Tried to set an JsonObject setting, which is not allowed")
}

fun toDefaultJsonObject(children: Array<BooleanSetting>): JsonObject {
    return children.reduce({ acc, booleanSetting -> acc.apply { acc.add(booleanSetting.id, JsonPrimitive(booleanSetting.id)) }  }, JsonObject())
}
