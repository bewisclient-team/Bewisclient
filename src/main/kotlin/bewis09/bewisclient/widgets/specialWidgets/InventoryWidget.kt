package bewis09.bewisclient.widgets.specialWidgets

import bewis09.bewisclient.widgets.Widget
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

/**
 * A [Widget] which displays the current content of the own inventory
 */
class InventoryWidget: Widget("inventory") {

    /**
     * The texture that renders in the background of the widget
     */
    val identifier: Identifier = Identifier.of("bewisclient","textures/inventory_widget.png")

    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        RenderSystem.setShaderColor(1f,1f,1f,getProperty(TRANSPARENCY))
        drawContext.matrices.push()
        drawContext.matrices.scale(getScale(),getScale(),1F)
        RenderSystem.enableBlend()
        drawContext.drawTexture(identifier,x,y,getOriginalWidth(),getOriginalHeight(),0f,0f,getOriginalWidth(),getOriginalHeight(),180,60)
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1f,1f,1f,1f)

        for (i in 0 .. 8) {
            for (j in 0 .. 2) {
                drawContext.drawItem(MinecraftClient.getInstance().player?.inventory?.getStack(j*9+i+9),x+i*20+2,y+j*20+2)
                drawContext.drawItemInSlot(MinecraftClient.getInstance().textRenderer,MinecraftClient.getInstance().player?.inventory?.getStack(j*9+i+9),x+i*20+2,y+j*20+2)
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

    override fun getWidgetSettings(): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.add(ENABLED.id, JsonPrimitive(true))
        jsonObject.add(TRANSPARENCY.id, JsonPrimitive(1))
        jsonObject.add(SIZE.id, JsonPrimitive(1))
        jsonObject.add(POSX.id, JsonPrimitive(5))
        jsonObject.add(PARTX.id, JsonPrimitive(1))
        jsonObject.add(POSY.id, JsonPrimitive(5))
        jsonObject.add(PARTY.id, JsonPrimitive(1))

        return jsonObject
    }
}