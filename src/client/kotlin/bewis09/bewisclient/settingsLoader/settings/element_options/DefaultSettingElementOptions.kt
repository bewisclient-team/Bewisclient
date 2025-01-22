package bewis09.bewisclient.settingsLoader.settings.element_options

open class DefaultSettingElementOptions: ElementOptions() {
    var description = false
    var enableFunction: (()->Boolean)? = null
    var onChange: (()->Unit)? = null
}

fun <T: DefaultSettingElementOptions> T.withDescription(): T {
    this.description = true
    return this
}

fun <T: DefaultSettingElementOptions> T.withEnableFunction(enableFunction: ()->Boolean): T {
    this.enableFunction = enableFunction
    return this
}

fun <T: DefaultSettingElementOptions> T.withValueChanger(onChange: ()->Unit): T {
    this.onChange = onChange
    return this
}