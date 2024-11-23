package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.screen.MainOptionsScreen.Companion.scale
import bewis09.bewisclient.widgets.Widget
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

/**
 * An [OptionElement] for displaying a preview of a [Widget]
 *
 * @param widget The [Widget] that should be rendered
 */
class WidgetPreviewOptionElement(val widget: Widget?): OptionElement("","") {
    override fun getElementByKeywordLamba(): (String) -> OptionElement? {
        return { null }
    }

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        if(widget==null) return 0

        context.matrices.scale(scale, scale, scale)

        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(1F,1F,1F, (alphaModifier.toFloat()/0xFFFFFFFF))

        context.drawTexture({ texture: Identifier? ->
            RenderLayer.getGuiTexturedOverlay( texture )
        },Identifier.of("textures/block/light_blue_concrete.png"),
            ((x+width/2)/scale-widget.getOriginalWidth()/2).roundToInt()-2, ((y/scale)).roundToInt()+1,0f,0f,
            (widget.getOriginalWidth())+4, ((widget.getOriginalHeight()))+4,(widget.getOriginalWidth())+4,((widget.getOriginalHeight()))+4,32,32)

        context.drawBorder(
            ((x+width/2)/scale-widget.getOriginalWidth()/2).roundToInt()-2, (y/scale).roundToInt()+1,
            (widget.getOriginalWidth()+4f).roundToInt(),
            (widget.getOriginalHeight()+4),0xFF000000.toInt())

        context.drawBorder(
            ((x+width/2)/scale-widget.getOriginalWidth()/2).roundToInt()-3, (y/scale).roundToInt(),
            (widget.getOriginalWidth()+6f).roundToInt(),
            (widget.getOriginalHeight()+6),0xFFFFFFFF.toInt())

        context.matrices.scale(1/widget.getScale(), 1/widget.getScale(), 1/widget.getScale())

        widget.render(context,(((x+width/2)/scale-widget.getOriginalWidth()/2)).roundToInt(), ((y/scale+3f)).roundToInt())

        RenderSystem.setShaderColor(1F,1F,1F, 1F)
        RenderSystem.disableBlend()

        context.matrices.scale(1/scale, 1/scale, 1/scale)

        return (widget.getOriginalHeight()*scale+4).roundToInt()
    }
}