package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.JavaSettingsSender.Companion.DesignSettings
import bewis09.bewisclient.screen.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import kotlin.math.max

class EffectWidget: Widget("effect") {
    override fun render(drawContext: DrawContext) {
        if(MinecraftClient.getInstance().currentScreen is WidgetConfigScreen) {
            drawContext.fill(getScreenWidth()-5, getPosY(), getScreenWidth() , getPosY() + getHeight(), 0xAA000000.toInt())
        }
    }

    override fun getOriginalWidth(): Int {
        return 5
    }

    override fun getOriginalHeight(): Int {
        return if(DesignSettings.getValue<Boolean>("extend_status_effect_info") == true) 72 else 50
    }

    override fun getOriginalPosX(): Int {
        return getScreenWidth()-getWidth()
    }

    override fun getScale(): Float {
        return 1f
    }

    override fun setPropertyPosX(value: Float, allV: Int, wV: Int,sneak:Boolean): Float {
        return 0F
    }

    override fun setPropertyPosY(value: Float, allV: Int, wV: Int,sneak:Boolean): Float? {
        val pos: Float
        val part: Float
        if(allV/2>value) {
            pos = value
            part = -1F
        } else {
            pos = allV-value-wV
            part = 1F
        }
        SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValueWithoutSave(Settings.PARTY, part)
        return SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValueWithoutSave(Settings.POSY, max(pos, 0f))
    }
}