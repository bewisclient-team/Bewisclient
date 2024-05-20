package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.util.ColorSaver

open class Settings {
    val SCALE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("size")
    val OPTIONS_SCALE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("scale")
    val POSX: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("posX")
    val POSY: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("posY")
    val PARTX: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("partX")
    val TRANSPARENCY: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("transparency")
    val ENABLED: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("enabled")
    val PARTY: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("partY")
    val CLOCK24: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("24Clock")
    val ANIMATION_TIME: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("animation_time")
    val FIRST_LINE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("first_line")
    val SECOND_LINE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("second_line")
    val THIRD_LINE: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("third_line")
    val SHOW_BIOME: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("show_biome")
    val SHOW_DIRECTION: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("show_direction")
    val COLORCODE_BIOME: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("colorcode_biome")
    val CPS_ELEMENTS: SettingsLoader.TypedSettingID<Float> = SettingsLoader.TypedSettingID("cps_elements")
    val TEXT_COLOR: SettingsLoader.TypedSettingID<ColorSaver> = SettingsLoader.TypedSettingID("text_color")
    val TOP_COLOR: SettingsLoader.TypedSettingID<ColorSaver> = SettingsLoader.TypedSettingID("top_color")
    val BOTTOM_COLOR: SettingsLoader.TypedSettingID<ColorSaver> = SettingsLoader.TypedSettingID("bottom_color")
    val SHOW_BLOCK_ICON: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("show_block_icon")
    val SHOW_HEALTH_INFORMATION: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("show_health_information")
    val SHOW_PROGRESS_BAR: SettingsLoader.TypedSettingID<Boolean> = SettingsLoader.TypedSettingID("show_progress_bar")

    companion object {
        val Settings: Settings = Settings()
    }
}