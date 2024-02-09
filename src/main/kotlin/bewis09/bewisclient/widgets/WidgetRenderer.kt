package bewis09.bewisclient.widgets

import bewis09.bewisclient.screen.WidgetConfigScreen
import bewis09.bewisclient.widgets.lineWidgets.*
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class WidgetRenderer: HudRenderCallback {

    companion object {
        val widgets = arrayListOf(
                BiomeWidget(),
                SpeedWidget(),
                CPSWidget(),
                FPSWidget(),
                TiwylaWidget(),
                PingWidget(),
                CoordinatesWidget(),
                DayWidget(),
                DaytimeWidget(),
                KeyWidget()
        )
    }

    override fun onHudRender(drawContext: DrawContext?, tickDelta: Float) {
        if(MinecraftClient.getInstance().currentScreen !is WidgetConfigScreen) {
            widgets.forEach {
                if (drawContext != null && it.isEnabled()) {
                    it.render(drawContext)
                }
            }
        }
    }
}