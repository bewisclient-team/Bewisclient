package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import kotlin.math.roundToInt

class CoordinatesWidget: LineWidget("coordinates",100,false) {
    override fun getText(): ArrayList<String> {
        return if(getProperty(SHOW_BIOME) == true) {
            arrayListOf(
                    "X: "+MinecraftClient.getInstance().player?.blockX,
                    "Y: "+MinecraftClient.getInstance().player?.blockY,
                    "Z: "+MinecraftClient.getInstance().player?.blockZ,
                    BiomeWidget.textGetter(getProperty(COLORCODE_BIOME) == true)
            )
        } else {
            arrayListOf(
                    "X: "+MinecraftClient.getInstance().player?.blockX,
                    "Y: "+MinecraftClient.getInstance().player?.blockY,
                    "Z: "+MinecraftClient.getInstance().player?.blockZ
            )
        }
    }

    override fun getOriginalWidth(): Int {
        return if (getProperty(SHOW_BIOME) == true) 130 else 100
    }

    override fun render(drawContext: DrawContext) {
        super.render(drawContext)
        if(getProperty(SHOW_DIRECTION) == true) {
            drawContext.matrices.scale(getScale(), getScale(), 1F)
            var direction = ""
            val yaw = MinecraftClient.getInstance().player!!.yaw/45+8000000.5
            when ((yaw.toInt())%8) {
                0 -> direction="S"
                1 -> direction="SW"
                2 -> direction="W"
                3 -> direction="NW"
                4 -> direction="N"
                5 -> direction="NE"
                6 -> direction="E"
                7 -> direction="SE"
            }
            val text = "- $direction -"
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, getPosX() + getOriginalWidth() - 6 - MinecraftClient.getInstance().textRenderer.getWidth(text), getPosY() + 4, (0xFF000000L+getProperty(TEXT_COLOR)!!.getColor()).toInt())
            drawContext.matrices.scale(1 / getScale(), 1 / getScale(), 1F)
        }
    }
}