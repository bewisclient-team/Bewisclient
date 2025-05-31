package bewis09.bewisclient.settingsLoader.settings

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.settingsLoader.settings.element_options.ElementOptions

abstract class Setting<K, L: ElementOptions>(val settings: String, val path: Array<String>, val id: String, val defaultValue: K, val elementOptions: L) {
    abstract fun get(): K?

    abstract fun set(value: K)

    open fun createOptionElement(): OptionElement? {
        return null
    }
}