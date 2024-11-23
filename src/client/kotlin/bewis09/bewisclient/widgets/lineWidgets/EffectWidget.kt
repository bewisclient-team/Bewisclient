package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * A [Widget] which only exists for the settings, but doesn't display anything. The real rendering happens in the [net.minecraft.client.gui.hud.InGameHud] and is changed in [bewis09.bewisclient.mixin.InGameHudMixin]. The setting that can be by this is the y-coordinate of the status effect overlay
 */
class EffectWidget: Widget("effect") {
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

    override fun getScale(): Float {
        return 1f
    }

    override fun setPropertyPosX(value: Float, allV: Int, wV: Int,sneak:Boolean) {

    }

    override fun setPropertyPosY(value: Float, allV: Int, wV: Int,sneak:Boolean) {
        val pos: Float
        val part: Float
        if(allV/2>value) {
            pos = value
            part = -1F
        } else {
            pos = allV-value-wV
            part = 1F
        }
        SettingsLoader.disableAutoSave()
        setProperty(PARTY,part)
        SettingsLoader.disableAutoSave()
        setProperty(POSY,0f.coerceAtLeast(pos))
    }

    override fun getWidgetSettings(): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.add(ENABLED.id, JsonPrimitive(true) )
        jsonObject.add(TRANSPARENCY.id, JsonPrimitive(1) )
        jsonObject.add(SIZE.id, JsonPrimitive(1) )
        jsonObject.add(POSX.id, JsonPrimitive(0) )
        jsonObject.add(PARTX.id, JsonPrimitive(0) )
        jsonObject.add(POSY.id, JsonPrimitive(2) )
        jsonObject.add(PARTY.id, JsonPrimitive(-1) )

        return jsonObject
    }
}