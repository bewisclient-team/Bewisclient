package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.Search
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

/**
 * An option element that gets displayed in the [MainOptionsScreen]
 *
 * @param title The title of the element
 * @param description The description of the element
 */
abstract class OptionElement(val title: String, val description: String) : Settings(), Search.SearchableElement<OptionElement> {

    /**
     * The [Identifier] for the texture of the button to go to the next page
     */
    val select: Identifier = Identifier.of("bewisclient", "textures/sprites/select.png")

    /**
     * The [Identifier] for the texture of the button to go to the next page when hovered
     */
    val selectHovered: Identifier = Identifier.of("bewisclient", "textures/sprites/select_highlighted.png")

    /**
     * The current position of the widget (x, y, width, height)
     */
    var pos = arrayOf(0, 0, 0, 0)

    /**
     * Indicates if the [bewis09.bewisclient.settingsLoader.Settings.ALL_CLICK] setting is turned on, where you can click at an entire [MainOptionElement] to get to the corresponding screen
     */
    val allClicked: Boolean
        get() = SettingsLoader.get(DESIGN, OPTIONS_MENU, ALL_CLICK)

    /**
     * Renders the element on the screen
     *
     * @param context The [DrawContext] used to
     * @param x The x coordinate where the element should start to be drawn
     * @param y The y coordinate where the element should start to be drawn
     * @param width The width of the middle part of the option screen and the optimal width of the element
     * @param mouseX The x coordinate of the mouse
     * @param mouseY The y coordinate of the mouse
     * @param alphaModifier A number between 0x1000000 and 0xFF000000 that should be added to any RGB color to support the blend-in and blend-out animation
     *
     * @return The height of the element
     */
    abstract fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int

    /**
     * Gets called when a mouse button is clicked
     *
     * @param mouseX The x coordinate of the mouse
     * @param mouseY The y coordinate of the mouse
     * @param button The mouse button that got pressed
     * @param screen The current screen the element is shown on
     */
    open fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {

    }

    /**
     * Gets called when a mouse button is released
     *
     * @param mouseX The x coordinate of the mouse
     * @param mouseY The y coordinate of the mouse
     * @param button The mouse button that got released
     */
    open fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {

    }

    /**
     * Gets called when the mouse is clicked and then dragged
     *
     * @param mouseX The x coordinate of the mouse
     * @param mouseY The y coordinate of the mouse
     * @param deltaX Indicates how far the mouse has moved on the x-axis since starting to drag
     * @param deltaY Indicates how far the mouse has moved on the y-axis since starting to drag
     * @param button The mouse button that got released
     */
    open fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double, button: Int) {

    }

    /**
     * Gets called when a key is pressed
     *
     * @param keyCode The key code of the pressed key
     * @param scanCode The scan code of the pressed key
     * @param modifiers The modifiers that are active during the key press
     */
    open fun keyPressed(keyCode:Int, scanCode:Int, modifiers:Int) {

    }

    /**
     * Gets called when a char gets typed
     *
     * Triggers multiple times if the key is being hold
     *
     * @param chr The [Char] that will be typed
     * @param modifiers The modifiers that are active during the key press
     */
    open fun charTyped(chr: Char, modifiers: Int) {

    }

    override fun getChildElementsForSearch(): ArrayList<OptionElement>? {
        return null
    }

    override fun getSearchKeywords(): ArrayList<String> {
        if(Bewisclient.getTranslatedString(title)== "bewisclient.$title") return arrayListOf()
        return arrayListOf(Bewisclient.getTranslatedString(title))
    }
}