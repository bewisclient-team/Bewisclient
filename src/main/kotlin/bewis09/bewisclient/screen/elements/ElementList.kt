package bewis09.bewisclient.screen.elements

import bewis09.bewisclient.drawable.*
import bewis09.bewisclient.screen.CosmeticsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

object ElementList {

    private val excludedProperties = arrayOf("posX","posY","partX","partY","enabled","effect")

    val widgets: ()->ArrayList<MainOptionsElement> = { loadWidgetsFromDefault(DefaultSettings.getDefault("widgets")) }

    val newMainOptionsElements: ArrayList<()->MainOptionsElement> = arrayListOf()

    private val design: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                FloatOptionsElement("%options_menu.animation_time", "options_menu.animation_time", "options_menu.animation_time", "design"),
                FloatOptionsElement("%options_menu.scale", "options_menu.scale", "options_menu.scale", "design"),
                BooleanOptionsElement("%options_menu.all_click", "options_menu.all_click", "options_menu.all_click", "design"),
        )
    }

    private val blockhit: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%blockhit.enabled", "blockhit.enabled","blockhit.enabled", "design"),
                ColorPickerElement("%blockhit.color", "blockhit.color", "blockhit.color", "design"),
                FloatOptionsElement("%blockhit.alpha", "blockhit.alpha", "blockhit.alpha", "design")
        )
    }

    private val fullbright: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%fullbright.enabled", "fullbright.enabled", "fullbright.enabled", "design") {
                    if (SettingsLoader.getBoolean("design","fullbright.enabled"))
                        MinecraftClient.getInstance().options.gamma.value =
                            SettingsLoader.getFloat("design","fullbright.value").toDouble()
                    else
                        MinecraftClient.getInstance().options.gamma.value = 1.0
                },
                FloatOptionsElement("%fullbright.value", "fullbright.value", "fullbright.value", "design") {
                    if (SettingsLoader.getBoolean("design","fullbright.enabled"))
                        MinecraftClient.getInstance().options.gamma.value = SettingsLoader.getFloat("design","fullbright.value").toDouble()
                },
                BooleanOptionsElement("%fullbright.night_vision", "fullbright.night_vision", "fullbright.night_vision", "design")
        )
    }

    private val better_visibility: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%better_visibility.lava", "better_visibility.lava", "better_visibility.lava", "design"),
                FloatOptionsElement("%better_visibility.lava_view", "better_visibility.lava_view", "better_visibility.lava_view", "design"),
                BooleanOptionsElement("%better_visibility.nether", "better_visibility.nether", "better_visibility.nether", "design"),
                BooleanOptionsElement("%better_visibility.water", "better_visibility.water", "better_visibility.water", "design"),
                BooleanOptionsElement("%better_visibility.powder_snow", "better_visibility.powder_snow", "better_visibility.powder_snow", "design")
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
                BooleanOptionsElement("%gui.zoom_enabled", "zoom_enabled", "zoom_enabled", "general"),
                BooleanOptionsElement("%gui.instant_zoom", "instant_zoom", "instant_zoom", "general"),
                BooleanOptionsElement("%gui.hard_zoom", "hard_zoom", "hard_zoom", "general")
        )
    }

    private val pumpkin: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%pumpkin_overlay.disable_pumpkin_overlay", "disable_pumpkin_overlay", "disable_pumpkin_overlay", "design"),
                BooleanOptionsElement("%pumpkin_overlay.show_pumpkin_icon", "show_pumpkin_icon", "show_pumpkin_icon", "design")
        )
    }

    private val held_item_info: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%held_item_info.held_item_info", "held_item_info.held_item_info", "held_item_info", "design"),
                FloatOptionsElement("%held_item_info.maxinfolength", "held_item_info.maxinfolength", "maxinfolength", "design")
        )
    }

    val util: ()->ArrayList<MainOptionsElement> = {
        arrayListOf(
                BooleanOptionsElement("%extend_status_effect_info", "extend_status_effect_info","extend_status_effect_info", "design"),
                FloatOptionsElement("%fire_height", "fire_height", "fire_height", "design"),
                BooleanOptionsElement("%cleaner_debug_menu", "cleaner_debug_menu", "cleaner_debug_menu", "design"),
                BooleanOptionsElement("%shulker_box_tooltip", "shulker_box_tooltip", "shulker_box_tooltip", "design"),
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
                MainOptionsElement("gui.held_item_info", "gui.held_item_info.description", held_item_info(), Identifier("bewisclient", "textures/main_icons/held_item_info.png"))
        ).addNewElements()
    }

    fun loadWidgetsFromDefault(def: JsonObject): ArrayList<MainOptionsElement> {
        val map: ArrayList<MainOptionsElement> = arrayListOf()

        def.entrySet().forEach { v ->
            val m: ArrayList<MainOptionsElement> = arrayListOf()

            v.value.asJsonObject.entrySet().forEach {
                if (!excludedProperties.contains(it.key))
                    m.add(loadWidget(v.key + "." + it.key, it.key, it.value))
            }

            if (!excludedProperties.contains(v.key))
                map.add(WidgetOptionsElement(v.key, v.key, m))
        }

        return map
    }

    fun loadWidgetsSingleFromDefault(def: JsonObject, vkey: String): ArrayList<MainOptionsElement> {
        val map: ArrayList<MainOptionsElement> = arrayListOf()

        def.entrySet().forEach {
            if (!excludedProperties.contains(vkey) && !excludedProperties.contains(it.key)) {
                map.add(loadWidget(vkey + "." + it.key, it.key, it.value))
            }
        }

        return map
    }

    private fun loadWidget(str: String, key:String,value: JsonElement): MainOptionsElement {
        return when (true) {
            value.asJsonPrimitive.isBoolean -> BooleanOptionsElement(key,str,key,"widgets")
            (value.asJsonPrimitive.isString && value.asString.startsWith("0x")) -> ColorPickerElement(key,str,key,"widgets")
            value.asJsonPrimitive.isNumber -> {
                if((DefaultSettings.arrays[key]
                                ?: DefaultSettings.arrays[".$key"]) == null)
                    FloatOptionsElement(key,str, key, "widgets")
                else
                    ArrayOptionsElement(key,str,key,"widgets")
            }
            value.asJsonPrimitive.isString -> if(str.split("_")[0]=="info")
                            InfoElement("info.$str")
                        else
                            StringOptionsElement(key,str,key,"widgets")
            else -> WidgetOptionsElement(key,str, arrayListOf())
        }
    }
}

private fun ArrayList<MainOptionsElement>.addNewElements(): ArrayList<MainOptionsElement> {
    ElementList.newMainOptionsElements.forEach {
        this.add(it())
    }

    return this
}
