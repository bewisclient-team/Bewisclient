package bewis09.bewisclient.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper

fun DrawContext.drawTexture(
    sprite: Identifier?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    red: Float = 1f,
    green: Float = 1f,
    blue: Float = 1f,
    alpha: Float = 1f
) {
    this.drawTexture(
        { texture: Identifier? ->
            RenderLayer.getGuiTexturedOverlay(texture)
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
        height,
        ColorHelper.fromFloats(alpha, red, green, blue)
    )
}

fun DrawContext.fillWithBorder(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    color: Number,
    borderColor: Number
) {
    this.fill(x, y, x + width, y + height, color.toInt())
    this.drawBorder(x, y, width, height, borderColor.toInt())
}

fun DrawContext.fillHoverable(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    mouseX: Number,
    mouseY: Number,
    color: Number,
    hoverColor: Number
): Boolean {
    val hovered = mouseX.toFloat() >= x && mouseY.toFloat() >= y && mouseX.toFloat() < x + width && mouseY.toFloat() < y + height

    this.fill(
        x, y, x + width, y + height,
        if (hovered) {
            hoverColor.toInt()
        } else {
            color.toInt()
        }
    )

    return hovered
}

fun DrawContext.fillHoverableWithBorder(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    mouseX: Number,
    mouseY: Number,
    color: Number,
    hoverColor: Number,
    borderColor: Number,
    borderHoverColor: Number = borderColor
): Boolean {
    val hovered = mouseX.toFloat() >= x && mouseY.toFloat() >= y && mouseX.toFloat() < x + width && mouseY.toFloat() < y + height

    this.fillHoverable(x, y, width, height, mouseX, mouseY, color, hoverColor)
    this.drawBorder(
        x, y, width, height,
        if (hovered) {
            borderHoverColor.toInt()
        } else {
            borderColor.toInt()
        }
    )

    return hovered
}

/**
 * @return The height of the drawn text
 */
fun DrawContext.drawCenteredTextWithShadow(
    width: Int,
    text: String,
    centerX: Int,
    y: Int,
    color: Int = 0xFFFFFFFF.toInt(),
    spacing: Int = 11
): Int {
    return this.drawCenteredTextLinesWithShadow(
        width,
        Text.literal(text),
        centerX,
        y,
        color,
        spacing
    )
}

/**
 * @return The height of the drawn text
 */
fun DrawContext.drawCenteredTextLinesWithShadow(
    width: Int,
    text: Text,
    centerX: Int,
    y: Int,
    color: Int = 0xFFFFFFFF.toInt(),
    spacing: Int = 11
): Int {
    val lines = MinecraftClient.getInstance().textRenderer.wrapLines(text, width - 4)
    lines.forEachIndexed { index, line ->
        this.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            line,
            centerX,
            y + index * spacing,
            color
        )
    }

    return lines.size * spacing
}

/**
 * @return The height of the drawn text
 */
fun DrawContext.drawTextLinesWithShadow(
    width: Int,
    text: String,
    x: Int,
    y: Int,
    color: Int = 0xFFFFFFFF.toInt(),
    spacing: Int = 11
): Int {
    return this.drawTextLinesWithShadow(
        width,
        Text.literal(text),
        x,
        y,
        color,
        spacing
    )
}

/**
 * @return The height of the drawn text
 */
fun DrawContext.drawTextLinesWithShadow(
    width: Int,
    text: Text,
    x: Int,
    y: Int,
    color: Int = 0xFFFFFFFF.toInt(),
    spacing: Int = 11
): Int {
    val lines = MinecraftClient.getInstance().textRenderer.wrapLines(text, width - 4)
    lines.forEachIndexed { index, line ->
        this.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            line,
            x,
            y + index * spacing,
            color
        )
    }

    return lines.size * spacing
}