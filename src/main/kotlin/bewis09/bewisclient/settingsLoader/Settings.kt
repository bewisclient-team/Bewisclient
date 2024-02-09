package bewis09.bewisclient.settingsLoader

object Settings {
    val SCALE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("size")
    val POSX: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("posX")
    val POSY: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("posY")
    val PARTX: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("partX")
    val TRANSPARENCY: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("transparency")
    val ENABLED: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("enabled")
    val PARTY: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("partY")
    val CLOCK24: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("24Clock")
    val ANIMATION_TIME: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("animation_time")
    val OPTIONS_MENU: SettingsLoader.TypedSettingID<SettingsLoader.Settings> = SettingsLoader.TypedSettingID("options_menu")
    val FIRST_LINE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("first_line")
    val SECOND_LINE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("second_line")
    val THIRD_LINE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("third_line")
}