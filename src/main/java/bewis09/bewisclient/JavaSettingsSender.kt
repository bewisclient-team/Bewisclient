package bewis09.bewisclient

import bewis09.bewisclient.settingsLoader.SettingsLoader

class JavaSettingsSender {
    companion object {
        var settings = SettingsLoader
        var isZoomed: Boolean = true
    }
}