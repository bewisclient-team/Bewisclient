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
            WIDGETS,
            COLORCODE_BIOME,
            "biome"
        )
    },Pair("coordinates.colorcode_biome") {
        SettingsLoader.get(
            WIDGETS,
            SHOW_BIOME,
            "coordinates",
            *SELECT_PARTS
        )
    },Pair("better_visibility.lava_view") {
        SettingsLoader.get(
            DESIGN,
            LAVA,
            *BETTER_VISIBILITY
        )
    })

    val widgets: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.widgets"),
            MultiplePagesOptionElement(
                loadWidgetsFromDefault(DefaultSettings.getDefault(WIDGETS)).toArray(arrayOf()),100
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
            FloatOptionElement("%options_menu.animation_time", OPTIONS_MENU,ANIMATION_TIME, DESIGN),
            FloatOptionElement("%options_menu.scale", OPTIONS_MENU, SCALE, DESIGN),
            BooleanOptionElement("%options_menu.all_click", OPTIONS_MENU,ALL_CLICK, DESIGN),
            BooleanOptionElement("%options_menu.show_game_menu", OPTIONS_MENU,SHOW_GAME_MENU, DESIGN),
            BooleanOptionElement("%options_menu.show_title_menu", OPTIONS_MENU,SHOW_TITLE_MENU, DESIGN),
        )
    }

    val scoreboard: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.scoreboard"),
            FloatOptionElement("%scoreboard.scale", SCOREBOARD,SCALE, DESIGN),
            BooleanOptionElement("%scoreboard.hide_numbers", SCOREBOARD,HIDE_NUMBERS, DESIGN),
        )
    }

    val experimental: ()->ArrayList<OptionElement> = {
        val a: ArrayList<OptionElement> = arrayListOf(
            TitleOptionElement("gui.experimental")
        )
        if(System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win"))
            a.add(BooleanOptionElement("%experimental.auto_update", EXPERIMENTAL,AUTO_UPDATE, GENERAL))
        a
    }

    val blockhit: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.blockhit"),
                BooleanOptionElement("%blockhit.enabled", BLOCKHIT,ENABLED, DESIGN),
                ColorPickerElement("%blockhit.color",  BLOCKHIT,COLOR, DESIGN),
                FloatOptionElement("%blockhit.alpha",  BLOCKHIT,ALPHA, DESIGN),
                TitleOptionElement("gui.hit_overlay"),
                BooleanOptionElement("%blockhit.hit_overlay.enabled",  HIT_OVERLAY,ENABLED, DESIGN),
                ColorPickerElement("%blockhit.hit_overlay.color",  HIT_OVERLAY,COLOR, DESIGN),
                FloatOptionElement("%blockhit.hit_overlay.alpha",  HIT_OVERLAY,ALPHA, DESIGN)
        )
    }

    val fullbright: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleWidgetEnablerOptionElement(DESIGN,FULLBRIGHT,ENABLED, "gui.fullbright"){
                    if (SettingsLoader.get(DESIGN, FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value =
                            SettingsLoader.get(DESIGN, FULLBRIGHT, FULLBRIGHT_VALUE).toDouble()
                    else
                        MinecraftClient.getInstance().options.gamma.value = 1.0
                },
                FloatOptionElement("%fullbright.value", FULLBRIGHT,FULLBRIGHT_VALUE, DESIGN) {
                    if (SettingsLoader.get(DESIGN, FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
                            DESIGN,
                            FULLBRIGHT,
                            FULLBRIGHT_VALUE
                        ).toDouble()
                },
                BooleanOptionElement("%fullbright.night_vision", FULLBRIGHT,NIGHT_VISION, DESIGN)
        )
    }

    val better_visibility: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.better_visibility"),
            MultipleBooleanOptionElement("better_visibility.multiple", DESIGN, BETTER_VISIBILITY,
                LAVA.id,
                NETHER.id,
                WATER.id,
                POWDER_SNOW.id
            ),
            FloatOptionElement("%better_visibility.lava_view", BETTER_VISIBILITY,LAVA_VIEW, DESIGN)
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
                TitleWidgetEnablerOptionElement(GENERAL, arrayOf(), ZOOM_ENABLED, "gui.zoom"),
                BooleanOptionElement("%gui.instant_zoom", arrayOf(), INSTANT_ZOOM, GENERAL),
                BooleanOptionElement("%gui.hard_zoom", arrayOf(), HARD_ZOOM, GENERAL)
        )
    }

    val pumpkin: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.pumpkin"),
                BooleanOptionElement("%pumpkin_overlay.disable_pumpkin_overlay", arrayOf(),DISABLE_PUMPKIN_OVERLAY, DESIGN),
                BooleanOptionElement("%pumpkin_overlay.show_pumpkin_icon", arrayOf(),SHOW_PUMPKIN_ICON, DESIGN)
        )
    }

    val held_item_info: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.held_item_info"),
                BooleanOptionElement("%held_item_info.held_item_info", HELD_ITEM_INFO,HELD_ITEM_INFO_ENABLED, DESIGN),
                FloatOptionElement("%held_item_info.maxinfolength", HELD_ITEM_INFO,MAX_INFO_LENGTH, DESIGN),
        )
    }

    val util: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.util"),
                BooleanOptionElement("%extend_status_effect_info", arrayOf(),EXTEND_STATUS_EFFECT_INFO, DESIGN),
                FloatOptionElement("%fire_height", arrayOf(),FIRE_HEIGHT, DESIGN),
                BooleanOptionElement("%screenshot_folder_open", arrayOf(),SCREENSHOT_OPEN_FOLDER, GENERAL)
        )
    }

    val cleaner_debug_menu: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleWidgetEnablerOptionElement(DESIGN,arrayOf(),CLEANER_DEBUG_MENU, "gui.cleaner_debug_menu"),
            ContactElement("missing_option","https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Option&assignee=Bewis09&title=New%20Option:%20")
        )
    }

    val perspective: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleWidgetEnablerOptionElement(GENERAL,arrayOf(),PERSPECTIVE, "gui.perspective"),
            ContactElement("missing_option","https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Option&assignee=Bewis09&title=New%20Option:%20")
        )
    }

    val shulker_box_tooltip: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleWidgetEnablerOptionElement(DESIGN,arrayOf(), SHULKER_BOX_TOOLTIP, "gui.shulker_box_tooltip"),
            ContactElement("missing_option","https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Option&assignee=Bewis09&title=New%20Option:%20")
        )
    }

    val tnt_timer: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleWidgetEnablerOptionElement(GENERAL, arrayOf(),TNT_TIMER, "gui.tnt_timer"),
            ContactElement("missing_option","https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Option&assignee=Bewis09&title=New%20Option:%20")
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
                    Identifier.of("bewisclient", "textures/main_icons/fullbright.png"),
                    DESIGN, FULLBRIGHT, ENABLED
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
                    Identifier.of("bewisclient", "textures/main_icons/blockhit.png"),
                    DESIGN, BLOCKHIT, ENABLED
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.zoom",
                    zoom(),
                    Identifier.of("bewisclient", "textures/main_icons/zoom.png"),
                    GENERAL, arrayOf(), ZOOM_ENABLED
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.pumpkin",
                    pumpkin(),
                    Identifier.of("bewisclient", "textures/main_icons/pumpkin.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.held_item_info",
                    held_item_info(),
                    Identifier.of("bewisclient", "textures/main_icons/held_item_info.png"),
                    DESIGN, HELD_ITEM_INFO, HELD_ITEM_INFO_ENABLED
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.cleaner_debug_menu",
                    cleaner_debug_menu(),
                    Identifier.of("bewisclient", "textures/main_icons/cleaner_debug_menu.png"),
                    DESIGN, arrayOf(), CLEANER_DEBUG_MENU
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.shulker_box_tooltip",
                    shulker_box_tooltip(),
                    Identifier.of("bewisclient", "textures/main_icons/shulker_box_tooltip.png"),
                    DESIGN, arrayOf(), SHULKER_BOX_TOOLTIP
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.tnt_timer",
                    tnt_timer(),
                    Identifier.of("bewisclient", "textures/main_icons/tnt_timer.png"),
                    GENERAL, arrayOf(), TNT_TIMER
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
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.perspective",
                    perspective(),
                    Identifier.of("bewisclient", "textures/main_icons/perspective.png"),
                    GENERAL, arrayOf(), PERSPECTIVE
                )
            ),70),
        ).addNewElements()
    }

    /**
     * Loads the elements for the widget config from the default settings of it
     */
    fun loadWidgetsFromDefault(def: JsonObject): ArrayList<MultiplePagesOptionElement.MultiplePagesElement> {
        val map: ArrayList<MultiplePagesOptionElement.MultiplePagesElement> = arrayListOf()

        def.entrySet().forEach { v ->
            val m: ArrayList<OptionElement> = arrayListOf(
                TitleWidgetEnablerOptionElement(WIDGETS, arrayOf(v.key), ENABLED,"gui.widgets","widgets."+v.key)
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
                map.add(MultiplePagesOptionElement.MultiplePagesElement("widgets."+v.key, m,"widgets.description."+v.key, WIDGETS, arrayOf(v.key), ENABLED))
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
            TitleWidgetEnablerOptionElement(WIDGETS, arrayOf(vkey), ENABLED,"gui.widgets", "widgets.$vkey")
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
            value.isJsonObject -> MultipleBooleanOptionElement(key,WIDGETS, arrayOf(str,key),*value.asJsonObject.asMap().keys.toTypedArray())
            value.asJsonPrimitive.isBoolean -> BooleanOptionElement(key, arrayOf(str),
                SettingsLoader.TypedSettingID(key), WIDGETS)
            (value.asJsonPrimitive.isString && value.asString.startsWith("0x")) -> ColorPickerElement(key,arrayOf(str),SettingsLoader.TypedSettingID(key),WIDGETS)
            value.asJsonPrimitive.isNumber -> {
                if((DefaultSettings.arrays[key]
                                ?: DefaultSettings.arrays[".$key"]) == null)
                    FloatOptionElement(key, arrayOf(str),SettingsLoader.TypedSettingID(key), WIDGETS)
                else
                    ArrayOptionElement(key,arrayOf(str),SettingsLoader.TypedSettingID(key),WIDGETS)
            }
            value.asJsonPrimitive.isString -> if(str.split("_")[0]=="info")
                            InfoElement("info.$key")
                        else InfoElement("info.$key")
                            // String Element no longer exists: StringOptionsElement(key,str,key,WIDGETS)
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
