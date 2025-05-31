package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.SettingTypes
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * A [LineWidget] which displays the current coordinates and if enabled the orientation and biome
 */
class CoordinatesWidget: LineWidget<SettingTypes.CoordinatesWidgetSettingsObject>("coordinates",100,false) {
    override fun getText(): ArrayList<String> {
        return if(settings.show_biome.get()) {
            arrayListOf(
                    "X: "+MinecraftClient.getInstance().player?.blockX,
                    "Y: "+MinecraftClient.getInstance().player?.blockY,
                    "Z: "+MinecraftClient.getInstance().player?.blockZ,
                    BiomeWidget.getText(settings.colorcode_biome.get())
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
        return if (settings.show_biome.get()) 130 else 100
    }

    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        super.render(drawContext,x,y)
        if(settings.show_direction.get()) {
            drawContext.matrices.push()
            drawContext.matrices.scale(settings.size.get(), settings.size.get(), 1F)
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
            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x + getOriginalWidth() - 6 - MinecraftClient.getInstance().textRenderer.getWidth(text), y + 4, (0xFF000000L+settings.text_color.get().getColor()).toInt())
            drawContext.matrices.pop()
        }
    }

    override fun getWidgetSettings(): SettingTypes.CoordinatesWidgetSettingsObject {
        return SettingTypes.CoordinatesWidgetSettingsObject(id,5f,1f,5f,-1f, 0.43f, .7f)
    }
}