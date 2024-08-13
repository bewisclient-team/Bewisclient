package bewis09.bewisclient.widgets.lineWidgets

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class CoordinatesWidget: LineWidget("coordinates",100,false) {
    override fun getText(): ArrayList<String> {
        return if(getProperty(SHOW_BIOME)) {
            arrayListOf(
                    "X: "+MinecraftClient.getInstance().player?.blockX,
                    "Y: "+MinecraftClient.getInstance().player?.blockY,
                    "Z: "+MinecraftClient.getInstance().player?.blockZ,
                    BiomeWidget.textGetter(getProperty(COLORCODE_BIOME))
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
        return if (getProperty(SHOW_BIOME)) 130 else 100
    }

    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        super.render(drawContext,x,y)
        if(getProperty(SHOW_DIRECTION)) {
            drawContext.matrices.push()
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
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x + getOriginalWidth() - 6 - MinecraftClient.getInstance().textRenderer.getWidth(text), y + 4, (0xFF000000L+getProperty(TEXT_COLOR).getColor()).toInt())
            drawContext.matrices.pop()
        }
    }

    override fun getWidgetSettings(): JsonObject {
        val list = super.getWidgetSettings(.7f,5f,1f,5f,-1f)
        list.add("show_biome", JsonPrimitive(false))
        list.add("show_direction", JsonPrimitive(false))
        list.add("colorcode_biome", JsonPrimitive(true))
        return list
    }
}