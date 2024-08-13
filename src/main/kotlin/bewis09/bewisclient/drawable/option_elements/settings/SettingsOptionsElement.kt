package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.drawable.option_elements.OptionsElement
import bewis09.bewisclient.settingsLoader.SettingsLoader

abstract class SettingsOptionsElement<K>(
    originalTitle: String,
    val path: Array<String>,
    val id: SettingsLoader.TypedSettingID<K>
): OptionsElement(if (originalTitle.toCharArray()[0] =='%') originalTitle.drop(1) else "widgets.$originalTitle", if (originalTitle.toCharArray()[0] =='%') "description."+originalTitle.drop(1) else "widgets.description.$originalTitle")