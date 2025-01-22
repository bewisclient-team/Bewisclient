package bewis09.bewisclient.widgets

import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.widgets.lineWidgets.*
import bewis09.bewisclient.widgets.specialWidgets.InventoryWidget
import bewis09.bewisclient.widgets.specialWidgets.KeyWidget
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter

/**
 * A [HudRenderCallback] that renders the Widgets
 */
class WidgetRenderer: HudRenderCallback {

    companion object {
        val effectWidget = EffectWidget()
        val biomeWidget = BiomeWidget()
        val speedWidget = SpeedWidget()
        val cpsWidget = CPSWidget()
        val fpsWidget = FPSWidget()
        val tiwylaWidget = TiwylaWidget()
        val pingWidget = PingWidget()
        val coordinatesWidget = CoordinatesWidget()
        val dayWidget = DayWidget()
        val daytimeWidget = DaytimeWidget()
        val keyWidget = KeyWidget()
        val inventoryWidget = InventoryWidget()

        val widgets = arrayListOf(
            biomeWidget,
            speedWidget,
            cpsWidget,
            fpsWidget,
            tiwylaWidget,
            pingWidget,
            coordinatesWidget,
            effectWidget,
            dayWidget,
            daytimeWidget,
            keyWidget,
            inventoryWidget
        )
    }

    override fun onHudRender(drawContext: DrawContext?, tickCounter: RenderTickCounter) {
        if(MinecraftClient.getInstance().currentScreen !is WidgetConfigScreen && !MinecraftClient.getInstance().options.hudHidden) {
            widgets.forEach {
                if (drawContext != null && it.isEnabled()) {
                    it.render(drawContext)
                }
            }
        }
    }
}