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
            TitleOptionElement("setting.widgets"),
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
                TitleOptionElement("setting.cosmetics"),
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
            TitleOptionElement("setting.experimental")
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
                TitleOptionElement("setting.contact"),
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
            TitleOptionElement("setting.screenshot"),
            ScreenshotElement()
        )
    }

    val main = {
        arrayListOf(
            MainOptionElement("setting.widgets", "description.setting.widgets", widgets(), Identifier.of("bewisclient", "textures/main_icons/widgets.png")),
            MainOptionElement("setting.option_menu", "description.setting.option_menu", design(), Identifier.of("bewisclient", "textures/main_icons/design.png")),
            MainOptionElement("setting.util", "description.setting.util", util(), Identifier.of("bewisclient", "textures/main_icons/util.png")),
            MainOptionElement("setting.cosmetics", "description.setting.cosmetics", cosmetics(), Identifier.of("bewisclient", "textures/main_icons/cosmetics.png")),
            MultiplePagesOptionElement(arrayOf(
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    fullbright(),
                    Settings.fullbright
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    contact(),
                    "contact"
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    better_visibility(),
                    "better_visibility"
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    blockhit(),
                    "blockhit_and_hit_overlay"
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    zoom(),
                    Settings.zoom
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    pumpkin(),
                    Settings.pumpkin
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    held_item_info(),
                    Settings.held_item_info
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    cleaner_debug_menu(),
                    cleanerDebugMenu
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    shulker_box_tooltip(),
                    shulkerBoxTooltip
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    tnt_timer(),
                    tntTimer
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    scoreboard(),
                    "scoreboard"
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    experimental(),
                    "experimental"
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    perspective(),
                    Settings.perspective
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    screenshot(),
                    "screenshot"
                ),
                MultiplePagesOptionElement.ImagedMultiplePagesElement(
                    donation(),
                    "donation"
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
                map.add(MultiplePagesOptionElement.DescriptionedMultiplePagesElement( m,v.settings.enabled))
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
