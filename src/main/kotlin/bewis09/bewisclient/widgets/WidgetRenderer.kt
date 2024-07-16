package bewis09.bewisclient.widgets

import bewis09.bewisclient.screen.WidgetConfigScreen
import bewis09.bewisclient.widgets.lineWidgets.*
import bewis09.bewisclient.widgets.specialWidgets.KeyWidget
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter

class WidgetRenderer: HudRenderCallback {

    companion object {
        val effectWidget = EffectWidget()

        val widgets = arrayListOf(
                BiomeWidget(),
                SpeedWidget(),
                CPSWidget(),
                FPSWidget(),
                TiwylaWidget(),
                PingWidget(),
                CoordinatesWidget(),
                effectWidget,
                DayWidget(),
                DaytimeWidget(),
                KeyWidget(),
                InventoryWidget()
        )

        fun getOptionsObject(): JsonObject {
            val array = JsonObject()

            widgets.forEach {
                array.add(it.id,it.getWidgetSettings())
            }

            return array
        }
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