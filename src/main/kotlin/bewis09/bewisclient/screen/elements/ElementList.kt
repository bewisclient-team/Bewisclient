package bewis09.bewisclient.screen.elements

import bewis09.bewisclient.JavaSettingsSender.Companion.DesignSettings
import bewis09.bewisclient.drawable.*
import bewis09.bewisclient.screen.CosmeticsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.io.File

object ElementList {

    private val excludedProperties = arrayOf("posX","posY","partX","partY","enabled","effect","effect.enabled","effect.transparency","effect.size")

    val widgets: ()->ArrayList<MainOptionsElement> = { loadWidgetsFromDefault(DefaultSettings.getDefault("widgets")) }

    val macros: ()->ArrayList<MainOptionsElement> = {
        loadMacrosFromFile()
    }

    private val design: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                FloatOptionsElement("%options_menu.animation_time", "options_menu.animation_time", SettingsLoader.DesignSettings),
                FloatOptionsElement("%options_menu.scale", "options_menu.scale", SettingsLoader.DesignSettings),
                BooleanOptionsElement("%options_menu.all_click", "options_menu.all_click", SettingsLoader.DesignSettings),
        )
    }

    private val blockhit: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%blockhit.enabled", "blockhit.enabled", SettingsLoader.DesignSettings),
                ColorPickerElement("%blockhit.color", "blockhit.color", SettingsLoader.DesignSettings),
                FloatOptionsElement("%blockhit.alpha", "blockhit.alpha", SettingsLoader.DesignSettings)
        )
    }

    private val fullbright: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%fullbright.enabled", "fullbright.enabled", SettingsLoader.DesignSettings) {
                    if ((DesignSettings.getValue<Any>("fullbright") as SettingsLoader.Settings).getValue("enabled"))
                        MinecraftClient.getInstance().options.gamma.value = ((DesignSettings.getValue<Any>("fullbright") as SettingsLoader.Settings).getValue<Any>("value") as Float).toDouble()
                    else
                        MinecraftClient.getInstance().options.gamma.value = 1.0
                },
                FloatOptionsElement("%fullbright.value", "fullbright.value", SettingsLoader.DesignSettings) {
                    if ((DesignSettings.getValue<Any>("fullbright") as SettingsLoader.Settings).getValue("enabled"))
                        MinecraftClient.getInstance().options.gamma.value = ((DesignSettings.getValue<Any>("fullbright") as SettingsLoader.Settings).getValue<Any>("value") as Float).toDouble()
                },
                BooleanOptionsElement("%fullbright.night_vision", "fullbright.night_vision", SettingsLoader.DesignSettings)
        )
    }

    private val better_visibility: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%better_visibility.lava", "better_visibility.lava", SettingsLoader.DesignSettings),
                FloatOptionsElement("%better_visibility.lava_view", "better_visibility.lava_view", SettingsLoader.DesignSettings),
                BooleanOptionsElement("%better_visibility.nether", "better_visibility.nether", SettingsLoader.DesignSettings),
                BooleanOptionsElement("%better_visibility.water", "better_visibility.water", SettingsLoader.DesignSettings),
                BooleanOptionsElement("%better_visibility.powder_snow", "better_visibility.powder_snow", SettingsLoader.DesignSettings)
        )
    }

    private val contact: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                ContactElement("modrinth","https://modrinth.com/mod/bewisclient"),
                ContactElement("sources","https://github.com/Bewis09/bewisclient-2/"),
                ContactElement("issues","https://github.com/Bewis09/Bewisclient-2/issues"),
                ContactElement("discord","https://discord.com/invite/kuUyGUeEZS")
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
                BooleanOptionsElement("%shulker_box_tooltip", "shulker_box_tooltip", SettingsLoader.DesignSettings),
        )
    }

    val main = {
        arrayListOf(
                MainOptionsElement("gui.widgets", "gui.widgets.description", widgets(), Identifier("bewisclient", "textures/main_icons/widgets.png")),
                MainOptionsElement("gui.design", "gui.design.description", design(), Identifier("bewisclient", "textures/main_icons/design.png")),
                MainOptionsElement("gui.util", "gui.util.description", util(), Identifier("bewisclient", "textures/main_icons/util.png")),
                MainOptionsElement("gui.cosmetics", "gui.cosmetics.description", { CosmeticsScreen(it) }, Identifier("bewisclient", "textures/main_icons/cosmetics.png")),
                MainOptionsElement("gui.fullbright", "gui.fullbright.description", fullbright(), Identifier("bewisclient", "textures/main_icons/fullbright.png")),
                MainOptionsElement("gui.contact", "gui.contact.description", contact(), Identifier("bewisclient", "textures/main_icons/contact.png")),
                MainOptionsElement("gui.better_visibility", "gui.better_visibility.description", better_visibility(), Identifier("bewisclient", "textures/main_icons/better_visibility.png")),
                MainOptionsElement("gui.blockhit", "gui.blockhit.description", blockhit(), Identifier("bewisclient", "textures/main_icons/blockhit.png")),
                MainOptionsElement("gui.zoom", "gui.zoom.description", zoom(), Identifier("bewisclient", "textures/main_icons/zoom.png")),
                MainOptionsElement("gui.pumpkin", "gui.pumpkin.description", pumpkin(), Identifier("bewisclient", "textures/main_icons/pumpkin.png")),
                MainOptionsElement("gui.held_item_info", "gui.held_item_info.description", held_item_info(), Identifier("bewisclient", "textures/main_icons/held_item_info.png")),
                //MainOptionsElement("gui.macros","gui.macros.description", macros(), Identifier("bewisclient","textures/main_icons/macros.png"))
        )
    }

    fun loadWidgetsFromDefault(def: Array<Pair<String, Any>>): ArrayList<MainOptionsElement> {

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
            is String -> if(str.split("_")[0]=="info")
                            InfoElement("info.$str")
                        else
                            StringOptionsElement(str,value,SettingsLoader.WidgetSettings)
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