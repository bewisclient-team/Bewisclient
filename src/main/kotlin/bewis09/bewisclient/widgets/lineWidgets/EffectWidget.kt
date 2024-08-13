package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

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
        return if(SettingsLoader.getBoolean("design","extend_status_effect_info")) 75 else 52
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
        SettingsLoader.set("widgets","$id.partY", part)
        SettingsLoader.disableAutoSave()
        SettingsLoader.set("widgets","$id.posY", 0f.coerceAtLeast(pos))
    }

    override fun getWidgetSettings(): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.add("enabled", JsonPrimitive(true) )
        jsonObject.add("transparency", JsonPrimitive(1) )
        jsonObject.add("size", JsonPrimitive(1) )
        jsonObject.add("posX", JsonPrimitive(0) )
        jsonObject.add("partX", JsonPrimitive(0) )
        jsonObject.add("posY", JsonPrimitive(2) )
        jsonObject.add("partY", JsonPrimitive(-1) )

        return jsonObject
    }
}