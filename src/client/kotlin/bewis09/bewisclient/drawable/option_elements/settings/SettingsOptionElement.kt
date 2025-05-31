package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.settingsLoader.settings.Setting

/**
 * An [OptionElement] which changes a setting
 */
abstract class SettingsOptionElement<K, L: Setting<K, *>>(val setting: L): OptionElement(getTitleBySetting(setting), getDescriptionBySetting(setting))

fun getTitleBySetting(setting: Setting<*, *>): String {
    if(setting.elementOptions.pathedTitle) return "setting."+setting.settings+"."+setting.path.reduce { acc, s -> "$acc.$s" }+"."+setting.id
    return "setting."+setting.settings+"."+setting.id
}

fun getDescriptionBySetting(setting: Setting<*, *>): String {
    if(setting.elementOptions.pathedTitle) return "description.setting."+setting.settings+"."+setting.path.reduce { acc, s -> "$acc.$s" }+"."+setting.id
    return "description.setting."+setting.settings+"."+setting.id
}