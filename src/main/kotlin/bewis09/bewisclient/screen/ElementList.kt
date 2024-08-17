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

/**
 * Collections of the elements for the [MainOptionsScreen]
 */
object ElementList: Settings() {

    /**
     * Properties that should not generate an [OptionElement] when in a widget
     */
    private val excludedProperties = arrayOf("posX","posY","partX","partY","effect","enabled")

    /**
     * Sets that some elements should be hidden at some point
     */
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

    val widgets: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.widgets"),
            MultiplePagesOptionElement(
                loadWidgetsFromDefault(DefaultSettings.getDefault("widgets")).toArray(arrayOf()),100,true
            )
        )
    }

    /**
     * A collection of every [MainOptionElement] that is added using the API
     *
     * @see [bewis09.bewisclient.api.APIEntryPoint]
     */
    val newMainOptionsElements: ArrayList<()->OptionElement> = arrayListOf()

    val design: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.design"),
                FloatOptionElement("%options_menu.animation_time", OPTIONS_MENU,ANIMATION_TIME, "design"),
                FloatOptionElement("%options_menu.scale", OPTIONS_MENU, SCALE, "design"),
                BooleanOptionElement("%options_menu.all_click", OPTIONS_MENU,ALL_CLICK, "design"),
        )
    }

    val scoreboard: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.scoreboard"),
            FloatOptionElement("%scoreboard.scale", SCOREBOARD,SCALE, "design"),
            BooleanOptionElement("%scoreboard.hide_numbers", SCOREBOARD,HIDE_NUMBERS, "design"),
        )
    }

    val experimental: ()->ArrayList<OptionElement> = {
        val a: ArrayList<OptionElement> = arrayListOf(
            TitleOptionElement("gui.experimental")
        )
        if(System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win"))
            a.add(BooleanOptionElement("%experimental.auto_update", EXPERIMENTAL,AUTO_UPDATE, "general"))
        a
    }

    val blockhit: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.blockhit"),
                BooleanOptionElement("%blockhit.enabled", BLOCKHIT,ENABLED, "design"),
                ColorPickerElement("%blockhit.color",  BLOCKHIT,COLOR, "design"),
                FloatOptionElement("%blockhit.alpha",  BLOCKHIT,ALPHA, "design"),
                TitleOptionElement("gui.hit_overlay"),
                BooleanOptionElement("%blockhit.hit_overlay.enabled",  HIT_OVERLAY,ENABLED, "design"),
                ColorPickerElement("%blockhit.hit_overlay.color",  HIT_OVERLAY,COLOR, "design"),
                FloatOptionElement("%blockhit.hit_overlay.alpha",  HIT_OVERLAY,ALPHA, "design")
        )
    }

    val fullbright: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.fullbright"),
                BooleanOptionElement("%fullbright.enabled", FULLBRIGHT,ENABLED, "design") {
                    if (SettingsLoader.get("design", FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value =
                            SettingsLoader.get("design", FULLBRIGHT, FULLBRIGHT_VALUE).toDouble()
                    else
                        MinecraftClient.getInstance().options.gamma.value = 1.0
                },
                FloatOptionElement("%fullbright.value", FULLBRIGHT,FULLBRIGHT_VALUE, "design") {
                    if (SettingsLoader.get("design", FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
                            "design",
                            FULLBRIGHT,
                            FULLBRIGHT_VALUE
                        ).toDouble()
                },
                BooleanOptionElement("%fullbright.night_vision", FULLBRIGHT,NIGHT_VISION, "design")
        )
    }

    val better_visibility: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.better_visibility"),
                BooleanOptionElement("%better_visibility.lava", BETTER_VISIBILITY,LAVA, "design"),
                FloatOptionElement("%better_visibility.lava_view", BETTER_VISIBILITY,LAVA_VIEW, "design"),
                BooleanOptionElement("%better_visibility.nether", BETTER_VISIBILITY,NETHER, "design"),
                BooleanOptionElement("%better_visibility.water", BETTER_VISIBILITY,WATER, "design"),
                BooleanOptionElement("%better_visibility.powder_snow", BETTER_VISIBILITY,POWDER_SNOW, "design")
        )
    }

    val contact: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.contact"),
                ContactElement("modrinth","https://modrinth.com/mod/bewisclient"),
                ContactElement("sources","https://github.com/Bewis09/bewisclient-2/"),
                ContactElement("issues","https://github.com/Bewis09/Bewisclient-2/issues"),
                ContactElement("discord","https://discord.com/invite/kuUyGUeEZS")
        )
    }

    val zoom: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.zoom"),
                BooleanOptionElement("%gui.zoom_enabled", arrayOf(), ZOOM_ENABLED, "general"),
                BooleanOptionElement("%gui.instant_zoom", arrayOf(), INSTANT_ZOOM, "general"),
                BooleanOptionElement("%gui.hard_zoom", arrayOf(), HARD_ZOOM, "general")
        )
    }

    val pumpkin: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.pumpkin"),
                BooleanOptionElement("%pumpkin_overlay.disable_pumpkin_overlay", arrayOf(),DISABLE_PUMPKIN_OVERLAY, "design"),
                BooleanOptionElement("%pumpkin_overlay.show_pumpkin_icon", arrayOf(),SHOW_PUMPKIN_ICON, "design")
        )
    }

    val held_item_info: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.held_item_info"),
                BooleanOptionElement("%held_item_info.held_item_info", HELD_ITEM_INFO,HELD_ITEM_INFO_ENABLED, "design"),
                FloatOptionElement("%held_item_info.maxinfolength", HELD_ITEM_INFO,MAX_INFO_LENGTH, "design"),
        )
    }

    val util: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.util"),
                BooleanOptionElement("%extend_status_effect_info", arrayOf(),EXTEND_STATUS_EFFECT_INFO, "design"),
                FloatOptionElement("%fire_height", arrayOf(),FIRE_HEIGHT, "design"),
                BooleanOptionElement("%screenshot_folder_open", arrayOf(),SCREENSHOT_OPEN_FOLDER, "general")
        )
    }

    val cleaner_debug_menu: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.cleaner_debug_menu"),
            BooleanOptionElement("%cleaner_debug_menu", arrayOf(),CLEANER_DEBUG_MENU, "design")
        )
    }

    val shulker_box_tooltip: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.shulker_box_tooltip"),
            BooleanOptionElement("%shulker_box_tooltip", arrayOf(), SHULKER_BOX_TOOLTIP, "design"),
        )
    }

    val tnt_timer: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.tnt_timer"),
            BooleanOptionElement("%tnt_timer", arrayOf(),TNT_TIMER, "general"),
        )
    }

    val main = {
        arrayListOf(
            MainOptionElement("gui.widgets", "gui.widgets.description", widgets(), Identifier.of("bewisclient", "textures/main_icons/widgets.png")),
            MainOptionElement("gui.design", "gui.design.description", design(), Identifier.of("bewisclient", "textures/main_icons/design.png")),
            MainOptionElement("gui.util", "gui.util.description", util(), Identifier.of("bewisclient", "textures/main_icons/util.png")),
            MainOptionElement("gui.cosmetics", "gui.cosmetics.description", { CosmeticsScreen(it) }, Identifier.of("bewisclient", "textures/main_icons/cosmetics.png"),true),
            MultiplePagesOptionElement(arrayOf(
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.fullbright",
                    fullbright(),
                    Identifier.of("bewisclient", "textures/main_icons/fullbright.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.contact",
                    contact(),
                    Identifier.of("bewisclient", "textures/main_icons/contact.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.better_visibility",
                    better_visibility(),
                    Identifier.of("bewisclient", "textures/main_icons/better_visibility.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.blockhit_hit_overlay",
                    blockhit(),
                    Identifier.of("bewisclient", "textures/main_icons/blockhit.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.zoom",
                    zoom(),
                    Identifier.of("bewisclient", "textures/main_icons/zoom.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.pumpkin",
                    pumpkin(),
                    Identifier.of("bewisclient", "textures/main_icons/pumpkin.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.held_item_info",
                    held_item_info(),
                    Identifier.of("bewisclient", "textures/main_icons/held_item_info.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.cleaner_debug_menu",
                    cleaner_debug_menu(),
                    Identifier.of("bewisclient", "textures/main_icons/cleaner_debug_menu.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.shulker_box_tooltip",
                    shulker_box_tooltip(),
                    Identifier.of("bewisclient", "textures/main_icons/shulker_box_tooltip.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.tnt_timer",
                    tnt_timer(),
                    Identifier.of("bewisclient", "textures/main_icons/tnt_timer.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.scoreboard",
                    scoreboard(),
                    Identifier.of("bewisclient", "textures/main_icons/scoreboard.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.experimental",
                    experimental(),
                    Identifier.of("bewisclient", "textures/main_icons/experimental.png")
                )
            ),70,false),
        ).addNewElements()
    }

    /**
     * Loads the elements for the widget config from the default settings of it
     */
    fun loadWidgetsFromDefault(def: JsonObject): ArrayList<MultiplePagesOptionElement.MultiplePagesElement> {
        val map: ArrayList<MultiplePagesOptionElement.MultiplePagesElement> = arrayListOf()

        def.entrySet().forEach { v ->
            val m: ArrayList<OptionElement> = arrayListOf(
                TitleWidgetEnablerOptionElement(v.key,"gui.widgets","widgets."+v.key)
            )

            v.value.asJsonObject.entrySet().forEach {
                if (!excludedProperties.contains(it.key))
                    m.add(loadElement(v.key, it.key, it.value))
            }

            var a: Widget? = null
            WidgetRenderer.widgets.forEach {
                if (it.id == v.key) a = it
            }

            if (MinecraftClient.getInstance()!!.world != null)
                m.add(WidgetPreviewOptionElement(a))

            if (!excludedProperties.contains(v.key))
                map.add(MultiplePagesOptionElement.MultiplePagesElement("widgets."+v.key, m,"widgets.description."+v.key))
        }

        return map
    }

    /**
     * Loads the options from one widget. Used in the [bewis09.bewisclient.screen.widget.WidgetConfigScreen]
     *
     * @param widget The [Widget] for which the elements should be loaded
     * @param def The [JsonObject] of the default settings
     * @param vkey The id of the [widget]
     */
    fun loadWidgetsSingleFromDefault(widget: Widget,def: JsonObject, vkey: String): ArrayList<OptionElement> {
        val map: ArrayList<OptionElement> = arrayListOf(
            TitleWidgetEnablerOptionElement("gui.widgets", "widgets.$vkey")
        )

        def.entrySet().forEach {
            if (!excludedProperties.contains(vkey) && !excludedProperties.contains(it.key)) {
                map.add(loadElement(vkey, it.key, it.value))
            }
        }

        if (MinecraftClient.getInstance()!!.world != null)
            map.add(WidgetPreviewOptionElement(widget))

        return map
    }

    /**
     * Load a single element from the default options
     *
     * @param str The id of the widget
     * @param key The id of the setting
     * @param value The default value of the setting
     *
     * @return The generated [OptionElement]
     */
    private fun loadElement(str: String, key:String, value: JsonElement): OptionElement {
        return when (true) {
            value.isJsonObject -> MultipleBooleanOptionElement(key,"widgets", arrayOf(str,key),*value.asJsonObject.asMap().keys.toTypedArray())
            value.asJsonPrimitive.isBoolean -> BooleanOptionElement(key, arrayOf(str),
                SettingsLoader.TypedSettingID(key), "widgets")
            (value.asJsonPrimitive.isString && value.asString.startsWith("0x")) -> ColorPickerElement(key,arrayOf(str),SettingsLoader.TypedSettingID(key),"widgets")
            value.asJsonPrimitive.isNumber -> {
                if((DefaultSettings.arrays[key]
                                ?: DefaultSettings.arrays[".$key"]) == null)
                    FloatOptionElement(key, arrayOf(str),SettingsLoader.TypedSettingID(key), "widgets")
                else
                    ArrayOptionElement(key,arrayOf(str),SettingsLoader.TypedSettingID(key),"widgets")
            }
            value.asJsonPrimitive.isString -> if(str.split("_")[0]=="info")
                            InfoElement("info.$key")
                        else InfoElement("info.$key")
                            // String Element no longer exists: StringOptionsElement(key,str,key,"widgets")
            else -> throw WidgetToElementLoadingException(key,value)
        }
    }
}

/**
 * Adds the elements from [ElementList.newMainOptionsElements] to a given [ArrayList]
 *
 * @return The modified [ArrayList]
 */
fun ArrayList<OptionElement>.addNewElements(): ArrayList<OptionElement> {
    ElementList.newMainOptionsElements.forEach {
        this.add(it())
    }

    return this
}
