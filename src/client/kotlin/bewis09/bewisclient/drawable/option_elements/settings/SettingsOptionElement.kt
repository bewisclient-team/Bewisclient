package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.settingsLoader.settings.Setting

/**
 * An [OptionElement] which changes a setting
 */
abstract class SettingsOptionElement<K, L: Setting<K, *>>(val setting: L): OptionElement("setting."+setting.settings+"."+setting.id  ,"description.setting."+setting.settings+"."+setting.id)