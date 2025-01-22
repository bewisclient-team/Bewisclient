package bewis09.bewisclient.screen

import bewis09.bewisclient.drawable.option_elements.*
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsDrawBigElement
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement
import bewis09.bewisclient.drawable.option_elements.screenshot.ScreenshotElement
import bewis09.bewisclient.drawable.option_elements.settings.BooleanOptionElement
import bewis09.bewisclient.drawable.option_elements.settings.TitleWidgetEnablerOptionElement
import bewis09.bewisclient.drawable.option_elements.util.InfoElement
import bewis09.bewisclient.drawable.option_elements.util.TitleOptionElement
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.util.Util
import bewis09.bewisclient.widgets.Widget
import bewis09.bewisclient.widgets.WidgetRenderer
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

    val widgets = {
        arrayOf(
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

    val design = { options_menu.getElements() }

    val scoreboard = { Settings.scoreboard.getElements() }

    val cosmetics = {
        Util.modFoundDependent("notenoughanimations","1.9.0",{ it <= 0 },{
            arrayOf(
                TitleOptionElement("nea_incompatible"),
                InfoElement("cosmetics.nae_incompatible"),
                ContactElement("nea_link","https://modrinth.com/mod/not-enough-animations")
            )
        },{
            arrayOf(
                TitleOptionElement("gui.cosmetics"),
                CosmeticsDrawBigElement(),
                CosmeticsDrawBigElement(true),
                CosmeticsElement("cape", CosmeticsElement.RenderType.REVERSED),
                CosmeticsElement("wing", CosmeticsElement.RenderType.REVERSED),
                CosmeticsElement("hat", CosmeticsElement.RenderType.FAST_CHANGING)
            )
        })

    }

    val experimental = {
        val a: ArrayList<OptionElement> = arrayListOf(
            TitleOptionElement("gui.experimental")
        )
        if(System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win"))
            a.add(BooleanOptionElement(Settings.experimental.auto_update))
        a.toTypedArray()
    }

    val blockhit = { Settings.blockhit.getElements() }

    val fullbright = { Settings.fullbright.getElements() }

    val better_visibility = { Settings.better_visibility.getElements() }

    val contact = {
        arrayOf(
                TitleOptionElement("gui.contact"),
                ContactElement("modrinth","https://modrinth.com/mod/bewisclient"),
                ContactElement("sources","https://github.com/Bewis09/bewisclient-2/"),
                ContactElement("issues","https://github.com/Bewis09/Bewisclient-2/issues"),
                ContactElement("discord","https://discord.com/invite/kuUyGUeEZS")
        )
    }

    val zoom = { Settings.zoom.getElements() }

    val pumpkin = { Settings.pumpkin.getElements() }

    val held_item_info = { Settings.held_item_info.getElements() }

    val util = { utilities.getElements() }

    val cleaner_debug_menu = { cleanerDebugMenu.getElements() }

    val donation: () -> Array<OptionElement> = {
        arrayOf(
            DonateElement()
        )
    }

    val perspective = { Settings.perspective.getElements() }

    val shulker_box_tooltip = { shulkerBoxTooltip.getElements() }

    val tnt_timer = { tntTimer.getElements() }

    val screenshot = {
        arrayOf(
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
                    Settings.fullbright
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
                    Settings.blockhit
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.zoom",
                    zoom(),
                    Identifier.of("bewisclient", "textures/main_icons/zoom.png"),
                    Settings.zoom
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
                    Settings.held_item_info
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.cleaner_debug_menu",
                    cleaner_debug_menu(),
                    Identifier.of("bewisclient", "textures/main_icons/cleaner_debug_menu.png"),
                    cleanerDebugMenu
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.shulker_box_tooltip",
                    shulker_box_tooltip(),
                    Identifier.of("bewisclient", "textures/main_icons/shulker_box_tooltip.png"),
                    shulkerBoxTooltip
                ),
                MultiplePagesOptionElement.MultiplePagesElement(
                    "gui.tnt_timer",
                    tnt_timer(),
                    Identifier.of("bewisclient", "textures/main_icons/tnt_timer.png"),
                    tntTimer
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
                    Settings.perspective
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
            val m: Array<OptionElement> = loadWidgetsSingleFromDefault(v)

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
    fun loadWidgetsSingleFromDefault(widget: Widget<*>): Array<OptionElement> {
        val map: ArrayList<OptionElement> = arrayListOf(
            TitleWidgetEnablerOptionElement(widget.settings.enabled,"gui.widgets","settings.widgets."+widget.id)
        )

        widget.getWidgetSettings().getSettingList().forEach {
            if (!excludedProperties.contains(widget.id) && !excludedProperties.contains(it.id)) {
                it.createOptionElement()?.let { it1 -> map.add(it1) }
            }
        }

        if (MinecraftClient.getInstance()!!.world != null)
            map.add(WidgetPreviewOptionElement(widget))

        return map.toTypedArray()
    }
}

/**
 * Adds the elements from [ElementList.newMainOptionsElements] to a given [ArrayList]
 *
 * @return The modified [ArrayList]
 */
fun ArrayList<OptionElement>.addNewElements(): Array<OptionElement> {
    ElementList.newMainOptionsElements.forEach {
        this.add(it())
    }

    return this.toTypedArray()
}
