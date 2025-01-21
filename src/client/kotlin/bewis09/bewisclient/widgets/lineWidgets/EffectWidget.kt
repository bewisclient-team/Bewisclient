package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.widgets.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * A [Widget] which only exists for the settings, but doesn't display anything. The real rendering happens in the [net.minecraft.client.gui.hud.InGameHud] and is changed in [bewis09.bewisclient.mixin.InGameHudMixin]. The setting that can be by this is the y-coordinate of the status effect overlay
 */
class EffectWidget: Widget<SettingTypes.DefaultWidgetSettingsObject>("effect") {
    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        if(MinecraftClient.getInstance().currentScreen is WidgetConfigScreen) {
            drawContext.fill(getScreenWidth()-5, y, getScreenWidth() , y + getHeight(), 0xAA000000.toInt())
        }
    }

    override fun getOriginalWidth(): Int {
        return 5
    }

    override fun getOriginalHeight(): Int {
        return if(SettingsLoader.get(DESIGN,EXTEND_STATUS_EFFECT_INFO)) 75 else 52
    }

    override fun getOriginalPosX(): Int {
        return getScreenWidth()-getWidth()
    }

    override fun isScalable(): Boolean = false

    override fun setPropertyPosX(value: Float, allV: Int, wV: Int, sneak:Boolean) {

    }

    override fun setPropertyPosY(value: Float, allV: Int, wV: Int,sneak:Boolean) {
        return super.setPropertyPosY(value, allV, wV, true)
    }

    override fun getWidgetSettings(): SettingTypes.DefaultWidgetSettingsObject {
        return SettingTypes.DefaultWidgetSettingsObject(id, 0f, 0f, 2f, -2f, 1f, 1f)
    }
}