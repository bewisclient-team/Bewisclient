package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.drawable.option_elements.MainOptionsElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.util.Identifier

open class SettingsOptionsElement<K>(originalTitle: String, val path: Array<String>, val id: SettingsLoader.TypedSettingID<K>, elements: ArrayList<MainOptionsElement>): MainOptionsElement(if (originalTitle.toCharArray()[0] =='%') originalTitle.drop(1) else "widgets.$originalTitle", if (originalTitle.toCharArray()[0] =='%') "description."+originalTitle.drop(1) else "widgets.description.$originalTitle", elements, Identifier.of(""))