package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.SettingTypes
import net.minecraft.client.MinecraftClient

/**
 * A [LineWidget] which displays the number of in-game-days the world has existed for
 */
class DayWidget: LineWidget<SettingTypes.TextWidgetSettingsObject>("days",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(Bewisclient.getTranslatedString("widgets.day")+" "+ (MinecraftClient.getInstance().world?.timeOfDay?.div(24000L) ?: 0))
    }

    override fun getWidgetSettings(): SettingTypes.TextWidgetSettingsObject {
        return SettingTypes.TextWidgetSettingsObject(id,5f,1f,68f,-1f,0.43f, .7f)
    }
}