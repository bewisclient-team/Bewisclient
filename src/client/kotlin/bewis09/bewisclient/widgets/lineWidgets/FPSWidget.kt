package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.SettingTypes
import net.minecraft.client.MinecraftClient

/**
 * A [LineWidget] which displays the current FPS
 */
class FPSWidget: LineWidget<SettingTypes.TextWidgetSettingsObject>("fps",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(MinecraftClient.getInstance().currentFps.toString()+" FPS")
    }

    override fun getWidgetSettings(): SettingTypes.TextWidgetSettingsObject {
        return SettingTypes.TextWidgetSettingsObject(id,5f,1f,44f,-1f, 0.43f,0.7f)
    }
}