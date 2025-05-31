package bewis09.bewisclient.settingsLoader.settings.element_options

open class ElementOptions {
    var pathedTitle = false
}

fun <T: ElementOptions> T.addPathToTitle(): T {
    pathedTitle = true
    return this
}