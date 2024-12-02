package bewis09.bewisclient.drawable

import bewis09.bewisclient.util.drawTexture
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.widget.TexturedButtonWidget

/**
 * Just like a [TexturedButtonWidget] but working, when the textures are saved sprites
 *
 * @param x The x-position of the [TexturedButtonWidget]
 * @param y The y-position of the [TexturedButtonWidget]
 * @param width The width of the [TexturedButtonWidget]
 * @param height The height of the [TexturedButtonWidget]
 * @param textures The textures of the [TexturedButtonWidget]
 * @param pressAction The function that gets executed when clicking the button
 */
class UsableTexturedButtonWidget(x: Int, y: Int, width: Int, height: Int, textures: ButtonTextures?, pressAction: PressAction?) : TexturedButtonWidget(x, y, width, height, textures, pressAction) {
    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val identifier = textures[this.isNarratable, this.isSelected]
        RenderSystem.enableBlend()
        context.drawTexture(identifier,this.x, this.y, width, height)
        RenderSystem.setShaderColor(1f,1f,1f,alpha)
        context.fill(0,0,0,0,0)
        RenderSystem.setShaderColor(1f,1f,1f,1f)
        RenderSystem.disableBlend()
    }
}