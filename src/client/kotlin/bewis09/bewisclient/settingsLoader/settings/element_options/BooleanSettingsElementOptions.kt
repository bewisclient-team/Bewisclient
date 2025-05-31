package bewis09.bewisclient.settingsLoader.settings.element_options

class BooleanSettingsElementOptions: DefaultSettingElementOptions() {
    var title = false
}

fun BooleanSettingsElementOptions.asTitle(): BooleanSettingsElementOptions {
    this.title = true
    return this
}