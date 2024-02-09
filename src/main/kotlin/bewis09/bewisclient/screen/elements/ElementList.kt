package bewis09.bewisclient.screen.elements

import bewis09.bewisclient.drawable.*
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.util.Identifier

object ElementList {

    private val excludedProperties = arrayOf("posX","posY","partX","partY","enabled")

    val widgets: ArrayList<MainOptionsElement> = loadWidgetsFromDefault()

    val macros: ArrayList<MainOptionsElement> = arrayListOf(
            MacroGroupElement("macro1", arrayListOf(
                    MacroElement(),
                    MacroElement()
            ))
    )

    private val design: ArrayList<MainOptionsElement> = arrayListOf(
        FloatOptionsElement("%options_menu.animation_time","options_menu.animation_time",SettingsLoader.DesignSettings)
    )

    private val zoom: ArrayList<MainOptionsElement> = arrayListOf(
        BooleanOptionsElement("%gui.zoom_enabled","zoom_enabled",SettingsLoader.GeneralSettings),
            BooleanOptionsElement("%gui.instant_zoom","instant_zoom",SettingsLoader.GeneralSettings),
            BooleanOptionsElement("%gui.hard_zoom","hard_zoom",SettingsLoader.GeneralSettings)
    )

    val util: ArrayList<MainOptionsElement> = arrayListOf(
            BooleanOptionsElement("%held_item_info.held_item_info","held_item_info",SettingsLoader.DesignSettings),
            FloatOptionsElement("%held_item_info.maxinfolength","maxinfolength",SettingsLoader.DesignSettings),
            BooleanOptionsElement("%pumpkin_overlay.disable_pumpkin_overlay","disable_pumpkin_overlay",SettingsLoader.DesignSettings),
            BooleanOptionsElement("%pumpkin_overlay.show_pumpkin_icon","show_pumpkin_icon",SettingsLoader.DesignSettings),
            BooleanOptionsElement("%extend_status_effect_info","extend_status_effect_info",SettingsLoader.DesignSettings),
            FloatOptionsElement("%fire_height","fire_height",SettingsLoader.DesignSettings)
    )

    val main = arrayListOf(
        MainOptionsElement("gui.widgets","gui.widgets.description", widgets, Identifier("bewisclient","textures/main_icons/widgets.png")),
        MainOptionsElement("gui.design","gui.design.description", design, Identifier("bewisclient","textures/main_icons/design.png")),
        MainOptionsElement("gui.zoom","gui.zoom.description", zoom, Identifier("bewisclient","textures/main_icons/zoom.png")),
        MainOptionsElement("gui.util","gui.util.description", util, Identifier("bewisclient","textures/main_icons/util.png")),
        //MainOptionsElement("gui.macros","gui.macros.description", macros, Identifier("bewisclient","textures/main_icons/macros.png"))
    )

    private fun loadWidgetsFromDefault(): ArrayList<MainOptionsElement> {
        val def = DefaultSettings.getDefault("widgets")

        val map: ArrayList<MainOptionsElement> = arrayListOf()

        def.forEach {
            val split = it.first.split(".")

            var m: ArrayList<MainOptionsElement> = map

            for ((index, s) in split.iterator().withIndex()) {
                if(index+1!=split.size) {
                    for (pl in ArrayList(m)) {
                        if ((pl as WidgetOptionsElement).originalTitle == s) {
                            m = pl.elements!!
                            break
                        }
                    }
                } else {
                    if(!excludedProperties.contains(s))
                        m.add(loadWidget(s,it.second,it.first))
                }
            }
        }

        return map
    }

    private fun loadWidget(str: String,any: Any,value: String): MainOptionsElement {
        return when (any) {
            is Boolean -> BooleanOptionsElement(str,value,SettingsLoader.WidgetSettings)
            is Float -> {
                if((DefaultSettings.arrays[value]
                                ?: DefaultSettings.arrays["." + value.split(".")[value.split(".").size - 1]]) == null)
                    FloatOptionsElement(str, value, SettingsLoader.WidgetSettings)
                else
                    ArrayOptionsElement(str,value,SettingsLoader.WidgetSettings)
            }
            is String -> StringOptionsElement(str,value,SettingsLoader.WidgetSettings)
            else -> WidgetOptionsElement(str, arrayListOf())
        }
    }
}