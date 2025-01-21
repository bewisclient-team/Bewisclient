package bewis09.bewisclient.screen

import bewis09.bewisclient.drawable.option_elements.*
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsDrawBigElement
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement
import bewis09.bewisclient.drawable.option_elements.screenshot.ScreenshotElement
import bewis09.bewisclient.drawable.option_elements.settings.*
import bewis09.bewisclient.drawable.option_elements.util.InfoElement
import bewis09.bewisclient.drawable.option_elements.util.TitleOptionElement
import bewis09.bewisclient.exception.WidgetToElementLoadingException
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.Util
import bewis09.bewisclient.widgets.Widget
import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.util.*
import kotlin.collections.ArrayList

/**
 * Collections of the elements for the [MainOptionsScreen]
 */
object ElementList: Settings() {

    /**
     * Properties that should not generate an [OptionElement] when in a widget
     */
    private val excludedProperties = arrayOf("posX","posY","partX","partY","effect","enabled")

    val widgets: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.widgets"),
            MultiplePagesOptionElement(
                loadWidgetsFromDefault().toArray(arrayOf()),100
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
            FloatOptionElement(options_menu.animation_time),
            FloatOptionElement(options_menu.scale),
            BooleanOptionElement(options_menu.show_game_menu),
            BooleanOptionElement(options_menu.show_title_menu),
        )
    }

    val scoreboard: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.scoreboard"),
            FloatOptionElement(Settings.scoreboard.scale),
            BooleanOptionElement(Settings.scoreboard.hide_numbers),
        )
    }

    val cosmetics: ()->ArrayList<OptionElement> = {
        Util.modFoundDependent("notenoughanimations","1.9.0",{ it <= 0 },{
            arrayListOf(
                TitleOptionElement("nea_incompatible"),
                InfoElement("cosmetics.nae_incompatible"),
                ContactElement("nea_link","https://modrinth.com/mod/not-enough-animations")
            )
        },{
            arrayListOf(
                TitleOptionElement("gui.cosmetics"),
                CosmeticsDrawBigElement(),
                CosmeticsDrawBigElement(true),
                CosmeticsElement("cape", CosmeticsElement.RenderType.REVERSED),
                CosmeticsElement("wing", CosmeticsElement.RenderType.REVERSED),
                CosmeticsElement("hat", CosmeticsElement.RenderType.FAST_CHANGING)
            )
        })

    }

    val experimental: ()->ArrayList<OptionElement> = {
        val a: ArrayList<OptionElement> = arrayListOf(
            TitleOptionElement("gui.experimental")
        )
        if(System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win"))
            a.add(BooleanOptionElement("%experimental.auto_update", EXPERIMENTAL,AUTO_UPDATE, GENERAL,true))
        a
    }

    val blockhit: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.blockhit"),
            BooleanOptionElement(Settings.blockhit),
            ColorPickerElement(Settings.blockhit.color),
            FloatOptionElement(Settings.blockhit.alpha),
            TitleOptionElement("gui.hit_overlay"),
            BooleanOptionElement(hit_overlay),
            ColorPickerElement(hit_overlay.color),
            FloatOptionElement(hit_overlay.alpha)
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
                FloatOptionElement("%fullbright.value", FULLBRIGHT,FULLBRIGHT_VALUE, DESIGN, {
                    if (SettingsLoader.get(DESIGN, FULLBRIGHT, ENABLED))
                        MinecraftClient.getInstance().options.gamma.value = SettingsLoader.get(
                            DESIGN,
                            FULLBRIGHT,
                            FULLBRIGHT_VALUE
                        ).toDouble()
                },true),
                BooleanOptionElement("%fullbright.night_vision", FULLBRIGHT,NIGHT_VISION, DESIGN)
        )
    }

    val better_visibility: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.better_visibility"),
            MultipleBooleanOptionElement(Settings.better_visibility.multiple),
            FloatOptionElement(Settings.better_visibility.lava_view)
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
                TitleWidgetEnablerOptionElement(Settings.zoom.zoom_enabled, "gui.zoom"),
                BooleanOptionElement(Settings.zoom.instant_zoom),
                BooleanOptionElement(Settings.zoom.hard_zoom)
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
                TitleWidgetEnablerOptionElement(DESIGN, HELD_ITEM_INFO, HELD_ITEM_INFO_ENABLED, "gui.held_item_info"),
                FloatOptionElement("%held_item_info.maxinfolength", HELD_ITEM_INFO,MAX_INFO_LENGTH, DESIGN),
        )
    }

    val util: ()->ArrayList<OptionElement> = {
        arrayListOf(
                TitleOptionElement("gui.util"),
                BooleanOptionElement("%extend_status_effect_info", arrayOf(),EXTEND_STATUS_EFFECT_INFO, DESIGN, true),
                FloatOptionElement("%fire_height", arrayOf(),FIRE_HEIGHT, DESIGN),
                BooleanOptionElement("%screenshot_folder_open", arrayOf(),SCREENSHOT_OPEN_FOLDER, GENERAL, true)
        )
    }

    val cleaner_debug_menu: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleWidgetEnablerOptionElement(DESIGN,arrayOf(),CLEANER_DEBUG_MENU, "gui.cleaner_debug_menu"),
            ContactElement("missing_option","c")
        )
    }

    val donation: ()->ArrayList<OptionElement> = {
        arrayListOf(
            DonateElement()
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

    val screenshot: ()->ArrayList<OptionElement> = {
        arrayListOf(
            TitleOptionElement("gui.screenshot"),
            ScreenshotElement()
        )
    }

    val main = {
        arrayListOf(
            MainOptionElement("gui.widgets", "gui.widgets.description", widgets(), Identifier.of("bewisclient", "textures/main_icons/widgets.png")),
            MainOptionElement("gui.design", "gui.design.description", design(), Identifier.of("bewisclient", "textures/main_icons/design.png")),
            MainOptionElement("gui.util", "gui.util.description", util(), Identifier.of("bewisclient", "textures/main_icons/util.png")),
            MainOptionElement("gui.cosmetics", "gui.cosmetics.description", cosmetics(), Identifier.of("bewisclient", "textures/main_icons/cosmetics.png")),
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
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.screenshot",
                    screenshot(),
                    Identifier.of("bewisclient", "textures/main_icons/screenshot.png")
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.donation",
                    donation(),
                    Identifier.of("bewisclient", "textures/main_icons/donation.png")
                )
            ),70),
        ).addNewElements()
    }

    /**
     * Loads the elements for the widget config from the default settings of it
     */
    fun loadWidgetsFromDefault(): ArrayList<MultiplePagesOptionElement.MultiplePagesElement> {
        val map: ArrayList<MultiplePagesOptionElement.MultiplePagesElement> = arrayListOf()

        WidgetRenderer.widgets.forEach { v: Widget<*> ->
            val m: ArrayList<OptionElement> = loadWidgetsSingleFromDefault(v)

            if (!excludedProperties.contains(v.id))
                map.add(MultiplePagesOptionElement.MultiplePagesElement("settings.widgets."+v.id, m,"description.settings.widgets."+v.id, v.settings.enabled))
        }

        return map
    }

    /**
     * Loads the options from one widget. Used in the [bewis09.bewisclient.screen.widget.WidgetConfigScreen]
     *
     * @param widget The [Widget] for which the elements should be loaded
     */
    fun loadWidgetsSingleFromDefault(widget: Widget<*>): ArrayList<OptionElement> {
        val map: ArrayList<OptionElement> = arrayListOf(
            TitleWidgetEnablerOptionElement(widget.settings.enabled,"gui.widgets","settings.widgets."+widget.id)
        )

        widget.getWidgetSettings().getSettingList().forEach {
            if (!excludedProperties.contains(widget.id) && !excludedProperties.contains(it.id.id)) {
                it.createOptionElement()?.let { it1 -> map.add(it1) }
            }
        }

        if (MinecraftClient.getInstance()!!.world != null)
            map.add(WidgetPreviewOptionElement(widget))

        return map
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
