package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.drawable.option_elements.OptionsElement
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver

abstract class SettingsOptionsElement<K>(
    originalTitle: String,
    val settings: String,
    val path: Array<String>,
    val id: SettingsLoader.TypedSettingID<K>
): OptionsElement(if (originalTitle.toCharArray()[0] =='%') originalTitle.drop(1) else "widgets.$originalTitle", if (originalTitle.toCharArray()[0] =='%') "description."+originalTitle.drop(1) else "widgets.description.$originalTitle") {

    /**
     * Sets the value of the setting
     *
     * @param value The new value
     */
    @Suppress("UNCHECKED_CAST")
    fun set(value: K) {
        when (value) {
            is Number -> SettingsLoader.set(settings,value,path,id as SettingsLoader.TypedSettingID<out Number>)
            is Boolean -> SettingsLoader.set(settings,value,path,id as SettingsLoader.TypedSettingID<Boolean>)
            is ColorSaver -> SettingsLoader.set(settings,value,path,id as SettingsLoader.TypedSettingID<ColorSaver>)
            is String -> SettingsLoader.set(settings,value,path,id as SettingsLoader.TypedSettingID<String>)
            else -> {}
        }
    }

    /**
     * @return The current value of the setting
     */
    @Suppress("unchecked_cast")
    fun get(): K {
        when (true) {
            (getTypeParameter()=="float") -> return SettingsLoader.get(settings,path,id as SettingsLoader.TypedSettingID<Float>) as K
            (getTypeParameter()=="boolean") -> return SettingsLoader.get(settings,path,id as SettingsLoader.TypedSettingID<Boolean>) as K
            (getTypeParameter()=="colorsaver") -> return SettingsLoader.get(settings,path,id as SettingsLoader.TypedSettingID<ColorSaver>) as K
            (getTypeParameter()=="string") -> return SettingsLoader.get(settings,path,id as SettingsLoader.TypedSettingID<String>) as K
            else -> {}
        }
        throw ClassCastException()
    }

    abstract fun getTypeParameter(): String
}