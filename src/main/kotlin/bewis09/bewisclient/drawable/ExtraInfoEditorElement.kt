package bewis09.bewisclient.drawable

import bewis09.bewisclient.screen.MainOptionsScreen
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

class ExtraInfoEditorElement: MainOptionsElement("python","", Identifier("")) {
    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        TODO()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}