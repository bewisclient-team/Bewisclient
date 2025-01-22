package bewis09.bewisclient.settingsLoader.settings.element_options

import bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement

class FloatSettingsElementOptions: DefaultSettingElementOptions() {
    var sliderInfo: FloatOptionElement.SliderInfo? = null
}

fun FloatSettingsElementOptions.withSliderInfo(sliderInfo: FloatOptionElement.SliderInfo): FloatSettingsElementOptions {
    this.sliderInfo = sliderInfo
    return this
}