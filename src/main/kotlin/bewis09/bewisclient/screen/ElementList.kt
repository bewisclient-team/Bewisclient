package bewis09.bewisclient.screen

import bewis09.bewisclient.drawable.option_elements.*
import bewis09.bewisclient.drawable.option_elements.settings.*
import bewis09.bewisclient.exception.WidgetToElementLoadingException
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.util.*

// TODO Document
object ElementList: Settings() {

    private val excludedProperties = arrayOf("posX","posY","partX","partY","effect")

    val dependentDisabler = hashMapOf(Pair("biome.text_color") {
        !SettingsLoader.get(
            "widgets",
            COLORCODE_BIOME,
            "biome"
        )
    },Pair("coordinates.colorcode_biome") {
        SettingsLoader.get(
            "widgets",
            SHOW_BIOME,
            "coordinates",
            *SELECT_PARTS
        )
    })

    val widgets: ()->ArrayList<OptionsElement> = {
        arrayListOf(
            TitleOptionsElement("gui.widgets"),
            MultiplePagesOptionsElement(
                loadWidgetsFromDefault(DefaultSettings.getDefault("widgets")).toArray(arrayOf()),100,true
            )
        )
    }

    val newMainOptionsElements: ArrayList<()->OptionsElement> = arrayListOf()

    val design: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.design"),
                FloatOptionsElement("%options_menu.animation_time", OPTIONS_MENU,ANIMATION_TIME, "design"),
                FloatOptionsElement("%options_menu.scale", OPTIONS_MENU, SCALE, "design"),
                BooleanOptionsElement("%options_menu.all_click", OPTIONS_MENU,ALL_CLICK, "design"),
        )
    }

    val scoreboard: ()->ArrayList<OptionsElement> = {
        arrayListOf(
            TitleOptionsElement("gui.scoreboard"),
            FloatOptionsElement("%scoreboard.scale", SCOREBOARD,SCALE, "design"),
            BooleanOptionsElement("%scoreboard.hide_numbers", SCOREBOARD,HIDE_NUMBERS, "design"),
        )
    }

    val experimental: ()->ArrayList<OptionsElement> = {
        val a: ArrayList<OptionsElement> = arrayListOf(
            TitleOptionsElement("gui.experimental")
        )
        if(System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win"))
            a.add(BooleanOptionsElement("%experimental.auto_update", EXPERIMENTAL,AUTO_UPDATE, "general"))
        a
    }

    val blockhit: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.blockhit"),
                BooleanOptionsElement("%blockhit.enabled", BLOCKHIT,ENABLED, "design"),
                ColorPickerElement("%blockhit.color",  BLOCKHIT,COLOR, "design"),
                FloatOptionsElement("%blockhit.alpha",  BLOCKHIT,ALPHA, "design"),
                TitleOptionsElement("gui.hit_overlay"),
                BooleanOptionsElement("%blockhit.hit_overlay.enabled",  HIT_OVERLAY,ENABLED, "design"),
                ColorPickerElement("%blockhit.hit_overlay.color",  HIT_OVERLAY,COLOR, "design"),
                FloatOptionsElement("%blockhit.hit_overlay.alpha",  HIT_OVERLAY,ALPHA, "design")
        )
    }

    val fullbright: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.fullbright"),
                BooleanOptionsElement("%fullbright.enabled", FULLBRIGHT,ENABLED, "design") {
                    if (SettingsLoader.get("design", FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value =
                            SettingsLoader.get("design", FULLBRIGHT, FULLBRIGHT_VALUE).toDouble()
                    else
                        MinecraftClient.getInstance().options.gamma.value = 1.0
                },
                FloatOptionsElement("%fullbright.value", FULLBRIGHT,FULLBRIGHT_VALUE, "design") {
                    if (SettingsLoader.get("design", FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
                            "design",
                            FULLBRIGHT,
                            FULLBRIGHT_VALUE
                        ).toDouble()
                },
                BooleanOptionsElement("%fullbright.night_vision", FULLBRIGHT,NIGHT_VISION, "design")
        )
    }

    val better_visibility: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.better_visibility"),
                BooleanOptionsElement("%better_visibility.lava", BETTER_VISIBILITY,LAVA, "design"),
                FloatOptionsElement("%better_visibility.lava_view", BETTER_VISIBILITY,LAVA_VIEW, "design"),
                BooleanOptionsElement("%better_visibility.nether", BETTER_VISIBILITY,NETHER, "design"),
                BooleanOptionsElement("%better_visibility.water", BETTER_VISIBILITY,WATER, "design"),
                BooleanOptionsElement("%better_visibility.powder_snow", BETTER_VISIBILITY,POWDER_SNOW, "design")
        )
    }

    val contact: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.contact"),
                ContactElement("modrinth","https://modrinth.com/mod/bewisclient"),
                ContactElement("sources","https://github.com/Bewis09/bewisclient-2/"),
                ContactElement("issues","https://github.com/Bewis09/Bewisclient-2/issues"),
                ContactElement("discord","https://discord.com/invite/kuUyGUeEZS")
        )
    }

    val zoom: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.zoom"),
                BooleanOptionsElement("%gui.zoom_enabled", arrayOf(), ZOOM_ENABLED, "general"),
                BooleanOptionsElement("%gui.instant_zoom", arrayOf(), INSTANT_ZOOM, "general"),
                BooleanOptionsElement("%gui.hard_zoom", arrayOf(), HARD_ZOOM, "general")
        )
    }

    val pumpkin: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.pumpkin"),
                BooleanOptionsElement("%pumpkin_overlay.disable_pumpkin_overlay", arrayOf(),DISABLE_PUMPKIN_OVERLAY, "design"),
                BooleanOptionsElement("%pumpkin_overlay.show_pumpkin_icon", arrayOf(),SHOW_PUMPKIN_ICON, "design")
        )
    }

    val held_item_info: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.held_item_info"),
                BooleanOptionsElement("%held_item_info.held_item_info", HELD_ITEM_INFO,HELD_ITEM_INFO_ENABLED, "design"),
                FloatOptionsElement("%held_item_info.maxinfolength", HELD_ITEM_INFO,MAX_INFO_LENGTH, "design"),
        )
    }

    val util: ()->ArrayList<OptionsElement> = {
        arrayListOf(
                TitleOptionsElement("gui.util"),
                BooleanOptionsElement("%extend_status_effect_info", arrayOf(),EXTEND_STATUS_EFFECT_INFO, "design"),
                FloatOptionsElement("%fire_height", arrayOf(),FIRE_HEIGHT, "design"),
                BooleanOptionsElement("%screenshot_folder_open", arrayOf(),SCREENSHOT_OPEN_FOLDER, "general")
        )
    }

    val cleaner_debug_menu: ()->ArrayList<OptionsElement> = {
        arrayListOf(
            TitleOptionsElement("gui.cleaner_debug_menu"),
            BooleanOptionsElement("%cleaner_debug_menu", arrayOf(),CLEANER_DEBUG_MENU, "design")
        )
    }

    val shulker_box_tooltip: ()->ArrayList<OptionsElement> = {
        arrayListOf(
            TitleOptionsElement("gui.shulker_box_tooltip"),
            BooleanOptionsElement("%shulker_box_tooltip", arrayOf(), SHULKER_BOX_TOOLTIP, "design"),
        )
    }

    val tnt_timer: ()->ArrayList<OptionsElement> = {
        arrayListOf(
            TitleOptionsElement("gui.tnt_timer"),
            BooleanOptionsElement("%tnt_timer", arrayOf(),TNT_TIMER, "general"),
        )
    }

    val main = {
        arrayListOf(
            MainOptionsElement("gui.widgets", "gui.widgets.description", widgets(), Identifier.of("bewisclient", "textures/main_icons/widgets.png")),
            MainOptionsElement("gui.design", "gui.design.description", design(), Identifier.of("bewisclient", "textures/main_icons/design.png")),
            MainOptionsElement("gui.util", "gui.util.description", util(), Identifier.of("bewisclient", "textures/main_icons/util.png")),
            MainOptionsElement("gui.cosmetics", "gui.cosmetics.description", { CosmeticsScreen(it) }, Identifier.of("bewisclient", "textures/main_icons/cosmetics.png")),
            MultiplePagesOptionsElement(arrayOf(
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.fullbright",
                    fullbright(),
                    Identifier.of("bewisclient", "textures/main_icons/fullbright.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.contact",
                    contact(),
                    Identifier.of("bewisclient", "textures/main_icons/contact.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.better_visibility",
                    better_visibility(),
                    Identifier.of("bewisclient", "textures/main_icons/better_visibility.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.blockhit_hit_overlay",
                    blockhit(),
                    Identifier.of("bewisclient", "textures/main_icons/blockhit.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.zoom",
                    zoom(),
                    Identifier.of("bewisclient", "textures/main_icons/zoom.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.pumpkin",
                    pumpkin(),
                    Identifier.of("bewisclient", "textures/main_icons/pumpkin.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.held_item_info",
                    held_item_info(),
                    Identifier.of("bewisclient", "textures/main_icons/held_item_info.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.cleaner_debug_menu",
                    cleaner_debug_menu(),
                    Identifier.of("bewisclient", "textures/main_icons/cleaner_debug_menu.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.shulker_box_tooltip",
                    shulker_box_tooltip(),
                    Identifier.of("bewisclient", "textures/main_icons/shulker_box_tooltip.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.tnt_timer",
                    tnt_timer(),
                    Identifier.of("bewisclient", "textures/main_icons/tnt_timer.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.scoreboard",
                    scoreboard(),
                    Identifier.of("bewisclient", "textures/main_icons/scoreboard.png")
                ),
                MultiplePagesOptionsElement.MultiplePagesElement(
                    "gui.experimental",
                    experimental(),
                    Identifier.of("bewisclient", "textures/main_icons/experimental.png")
                )
            ),70,false),
        ).addNewElements()
    }

    fun loadWidgetsFromDefault(def: JsonObject): ArrayList<MultiplePagesOptionsElement.MultiplePagesElement> {
        val map: ArrayList<MultiplePagesOptionsElement.MultiplePagesElement> = arrayListOf()

        def.entrySet().forEach { v ->
            val m: ArrayList<OptionsElement> = arrayListOf(
                TitleOptionsElement("gui.widgets","widgets."+v.key)
            )

            v.value.asJsonObject.entrySet().forEach {
                if (!excludedProperties.contains(it.key))
                    m.add(loadWidget(v.key, it.key, it.value))
            }

            var a: Widget? = null
            WidgetRenderer.widgets.forEach {
                if (it.id == v.key) a = it
            }
            m.add(WidgetPreviewOptionsElement(a))

            if (!excludedProperties.contains(v.key))
                map.add(MultiplePagesOptionsElement.MultiplePagesElement("widgets."+v.key, m,"widgets.description."+v.key))
        }

        return map
    }

    fun loadWidgetsSingleFromDefault(widget: Widget,def: JsonObject, vkey: String): ArrayList<OptionsElement> {
        val map: ArrayList<OptionsElement> = arrayListOf(
            TitleOptionsElement("gui.widgets", "widgets.$vkey")
        )

        def.entrySet().forEach {
            if (!excludedProperties.contains(vkey) && !excludedProperties.contains(it.key)) {
                map.add(loadWidget(vkey, it.key, it.value))
            }
        }

        map.add(WidgetPreviewOptionsElement(widget))

        return map
    }

    private fun loadWidget(str: String, key:String,value: JsonElement): OptionsElement {
        return when (true) {
            value.isJsonObject -> MultipleBooleanOptionsElement(key,"widgets", arrayOf(str,key),*value.asJsonObject.asMap().keys.toTypedArray())
            value.asJsonPrimitive.isBoolean -> BooleanOptionsElement(key, arrayOf(str),
                SettingsLoader.TypedSettingID(key), "widgets")
            (value.asJsonPrimitive.isString && value.asString.startsWith("0x")) -> ColorPickerElement(key,arrayOf(str),SettingsLoader.TypedSettingID(key),"widgets")
            value.asJsonPrimitive.isNumber -> {
                if((DefaultSettings.arrays[key]
                                ?: DefaultSettings.arrays[".$key"]) == null)
                    FloatOptionsElement(key, arrayOf(str),SettingsLoader.TypedSettingID(key), "widgets")
                else
                    ArrayOptionsElement(key,arrayOf(str),SettingsLoader.TypedSettingID(key),"widgets")
            }
            value.asJsonPrimitive.isString -> if(str.split("_")[0]=="info")
                            InfoElement("info.$key")
                        else InfoElement("info.$key")
                            // String Element no longer exists: StringOptionsElement(key,str,key,"widgets")
            else -> throw WidgetToElementLoadingException(key,value)
        }
    }
}

fun ArrayList<OptionsElement>.addNewElements(): ArrayList<OptionsElement> {
    ElementList.newMainOptionsElements.forEach {
        this.add(it())
    }

    return this
}
