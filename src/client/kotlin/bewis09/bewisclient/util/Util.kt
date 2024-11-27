package bewis09.bewisclient.util

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier

object Util {
    fun isIn(mouseX: Number, mouseY: Number, x1: Int, y1: Int, x2: Int, y2: Int) = mouseX.toDouble()>=x1&&mouseX.toDouble()<=x2&&mouseY.toDouble()>=y1&&mouseY.toDouble()<=y2
}

fun DrawContext.drawTexture(
    sprite: Identifier?,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    this.drawTexture(
        { texture: Identifier? ->
            RenderLayer.getGuiTexturedOverlay( texture )
        },
        sprite,
        x,
        y,
        0f,
        0f,
        width,
        height,
        width,
        height,
        width,
        height
    )
}