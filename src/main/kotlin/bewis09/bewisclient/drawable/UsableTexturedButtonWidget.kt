package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.widget.TexturedButtonWidget

class UsableTexturedButtonWidget(x: Int, y: Int, width: Int, height: Int, textures: ButtonTextures?, pressAction: PressAction?) : TexturedButtonWidget(x, y, width, height, textures, pressAction) {
    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val identifier = textures[this.isNarratable, this.isSelected]
        context.drawTexture(identifier, this.x, this.y, width, height, 0F, 0F, 20, 20, 20, 20)
    }
}