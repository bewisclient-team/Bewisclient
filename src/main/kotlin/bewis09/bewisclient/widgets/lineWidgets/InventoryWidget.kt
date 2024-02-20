package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

class InventoryWidget: Widget("inventory") {

    val Identifier = Identifier("bewisclient","textures/inventory_widget.png")

    override fun render(drawContext: DrawContext) {
        RenderSystem.setShaderColor(1f,1f,1f,getProperty(Settings.TRANSPARENCY))
        drawContext.matrices.scale(getScale(),getScale(),1F)
        RenderSystem.enableBlend()
        drawContext.drawTexture(Identifier,getPosX(),getPosY(),getOriginalWidth(),getOriginalHeight(),0f,0f,getOriginalWidth(),getOriginalHeight(),180,60)
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1f,1f,1f,1f)

        for (i in 0 .. 8) {
            for (j in 0 .. 2) {
                drawContext.drawItem(MinecraftClient.getInstance().player?.inventory?.getStack(j*9+i+9),getPosX()+i*20+2,getPosY()+j*20+2)
                drawContext.drawItemInSlot(MinecraftClient.getInstance().textRenderer,MinecraftClient.getInstance().player?.inventory?.getStack(j*9+i+9),getPosX()+i*20+2,getPosY()+j*20+2)
            }
        }

        drawContext.matrices.scale(1/getScale(),1/getScale(),1F)
    }

    override fun getOriginalWidth(): Int {
        return 180
    }

    override fun getOriginalHeight(): Int {
        return 60
    }

    override fun getWidgetSettings(): ArrayList<Pair<String, Any>> = arrayListOf(
            Pair(id, SettingsLoader.Settings()),
            Pair("$id.enabled",true),
            Pair("$id.transparency",1F),
            Pair("$id.size",1f),
            Pair("$id.posX",5f),
            Pair("$id.partX",1f),
            Pair("$id.posY",5f),
            Pair("$id.partY",1f)
    )
}