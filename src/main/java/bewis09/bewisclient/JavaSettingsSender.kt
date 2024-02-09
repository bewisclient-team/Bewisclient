package bewis09.bewisclient

import bewis09.bewisclient.settingsLoader.SettingsLoader

class JavaSettingsSender {
    companion object {
        var WidgetSettings = SettingsLoader.WidgetSettings
        var GeneralSettings = SettingsLoader.GeneralSettings
        var DesignSettings = SettingsLoader.DesignSettings
        var isZoomed: Boolean = true
    }
}