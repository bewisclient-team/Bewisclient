package bewis09.bewisclient.screen.elements

import bewis09.bewisclient.drawable.*
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import java.io.File

object ElementList {

    private val excludedProperties = arrayOf("posX","posY","partX","partY","enabled","effect","effect.enabled","effect.transparency","effect.size")

    val widgets: ()->ArrayList<MainOptionsElement> = { loadWidgetsFromDefault() }

    val macros: ()->ArrayList<MainOptionsElement> = {
        loadMacrosFromFile()
    }

    private val design: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                FloatOptionsElement("%options_menu.animation_time", "options_menu.animation_time", SettingsLoader.DesignSettings),
                FloatOptionsElement("%options_menu.scale", "options_menu.scale", SettingsLoader.DesignSettings)
        )
    }

    private val zoom: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%gui.zoom_enabled", "zoom_enabled", SettingsLoader.GeneralSettings),
                BooleanOptionsElement("%gui.instant_zoom", "instant_zoom", SettingsLoader.GeneralSettings),
                BooleanOptionsElement("%gui.hard_zoom", "hard_zoom", SettingsLoader.GeneralSettings)
        )
    }

    private val pumpkin: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%pumpkin_overlay.disable_pumpkin_overlay", "disable_pumpkin_overlay", SettingsLoader.DesignSettings),
                BooleanOptionsElement("%pumpkin_overlay.show_pumpkin_icon", "show_pumpkin_icon", SettingsLoader.DesignSettings)
        )
    }

    private val held_item_info: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%held_item_info.held_item_info", "held_item_info", SettingsLoader.DesignSettings),
                FloatOptionsElement("%held_item_info.maxinfolength", "maxinfolength", SettingsLoader.DesignSettings)
        )
    }

    val util: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%extend_status_effect_info", "extend_status_effect_info", SettingsLoader.DesignSettings),
                FloatOptionsElement("%fire_height", "fire_height", SettingsLoader.DesignSettings),
                BooleanOptionsElement("%cleaner_debug_menu", "cleaner_debug_menu", SettingsLoader.DesignSettings),
        )
    }

    val main = {
        arrayListOf(
                MainOptionsElement("gui.widgets", "gui.widgets.description", widgets(), Identifier("bewisclient", "textures/main_icons/widgets.png")),
                MainOptionsElement("gui.design", "gui.design.description", design(), Identifier("bewisclient", "textures/main_icons/design.png")),
                MainOptionsElement("gui.util", "gui.util.description", util(), Identifier("bewisclient", "textures/main_icons/util.png")),
                MainOptionsElement("gui.zoom", "gui.zoom.description", zoom(), Identifier("bewisclient", "textures/main_icons/zoom.png")),
                MainOptionsElement("gui.pumpkin", "gui.pumpkin.description", pumpkin(), Identifier("bewisclient", "textures/main_icons/pumpkin.png")),
                MainOptionsElement("gui.held_item_info", "gui.held_item_info.description", held_item_info(), Identifier("bewisclient", "textures/main_icons/held_item_info.png")),
                //MainOptionsElement("gui.macros","gui.macros.description", macros(), Identifier("bewisclient","textures/main_icons/macros.png"))
        )
    }

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
                    if(!excludedProperties.contains(s) && !excludedProperties.contains(it.first))
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
            is ColorSaver -> ColorPickerElement(str,value,SettingsLoader.WidgetSettings)
            else -> WidgetOptionsElement(str, arrayListOf())
        }
    }

    private fun loadMacrosFromFile(): ArrayList<MainOptionsElement> {
        val file = File((FabricLoader.getInstance().gameDir.toString()+"\\macros"))
        file.mkdirs()

        val list = arrayListOf<MainOptionsElement>()

        file.listFiles()?.forEach {
            list.add(
                 MacroGroupElement(it.name.split(".")[0], it.name)
            )
        }

        return list
    }
}