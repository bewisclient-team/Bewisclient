package bewis09.bewisclient.pop_up

import bewis09.bewisclient.screen.MainOptionsScreen
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

class ColorPickerPopup(screen: MainOptionsScreen): PopUp(screen) {
    override fun getWidth() = 200

    override fun getHeight() = 100

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float, x: Int, y: Int, a: Float) {
        super.render(context, mouseX, mouseY, delta, x, y, a)

        RenderSystem.enableBlend()
        context.setShaderColor(1f,1f,1f,a)
        context.drawTexture(Identifier.of("bewisclient","textures/color_space.png"),x+getWidth()-69,y+11,58,58,0f,0f,58,58,58,58)
        context.setShaderColor(1f,1f,1f,1f)
        RenderSystem.disableBlend()

        context.drawBorder(x+getWidth()-70,y+10,60,60, (a*0xFF).toInt()*0x1000000+0xFFFFFF)
    }
}