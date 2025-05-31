package bewis09.bewisclient.widgets.specialWidgets

import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.util.drawTexture
import bewis09.bewisclient.widgets.Widget
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

/**
 * A [Widget] which displays the current content of the own inventory
 */
class InventoryWidget: Widget<SettingTypes.DefaultWidgetSettingsObject>("inventory") {

    /**
     * The texture that renders in the background of the widget
     */
    val identifier: Identifier = Identifier.of("bewisclient","textures/inventory_widget.png")

    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        RenderSystem.setShaderColor(1f,1f,1f,settings.transparency.get())
        drawContext.matrices.push()
        drawContext.matrices.scale(settings.size.get(),settings.size.get(),1F)
        RenderSystem.enableBlend()
        drawContext.drawTexture(identifier,x,y,getOriginalWidth(),getOriginalHeight())
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1f,1f,1f,1f)

        for (i in 0 .. 8) {
            for (j in 0 .. 2) {
                drawContext.drawItem(MinecraftClient.getInstance().player?.inventory?.getStack(j*9+i+9),x+i*20+2,y+j*20+2)
                drawContext.drawStackOverlay(MinecraftClient.getInstance().textRenderer,MinecraftClient.getInstance().player?.inventory?.getStack(j*9+i+9),x+i*20+2,y+j*20+2)
            }
        }

        drawContext.matrices.pop()
    }

    override fun getOriginalWidth(): Int {
        return 180
    }

    override fun getOriginalHeight(): Int {
        return 60
    }

    override fun getWidgetSettings(): SettingTypes.DefaultWidgetSettingsObject {
        return SettingTypes.DefaultWidgetSettingsObject(id,5f,1f,5f,1f,1f,1f)
    }
}