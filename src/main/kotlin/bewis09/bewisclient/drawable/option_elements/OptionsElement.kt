package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.util.Search
import net.minecraft.client.gui.DrawContext

abstract class OptionsElement(val title: String, val description: String) : Settings(), Search.SearchableElement<OptionsElement> {
    var pos = arrayOf(0, 0, 0, 0)

    abstract fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int

    open fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {

    }

    open fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {

    }

    open fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double, button: Int) {

    }

    open fun keyPressed(keyCode:Int, scanCode:Int, modifiers:Int) {

    }

    open fun charTyped(chr: Char, modifiers: Int) {

    }

    override fun getChildElementsForSearch(): ArrayList<OptionsElement>? {
        return null
    }

    override fun getSearchKeywords(): ArrayList<String> {
        if(Bewisclient.getTranslatedString(title)== "bewisclient.$title") return arrayListOf()
        return arrayListOf(Bewisclient.getTranslatedString(title))
    }
}