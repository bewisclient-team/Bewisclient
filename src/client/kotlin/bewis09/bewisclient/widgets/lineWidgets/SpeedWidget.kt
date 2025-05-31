package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.util.NumberFormatter

/**
 * A [LineWidget] which displays the current speed
 */
class SpeedWidget: LineWidget<SettingTypes.SpeedWidgetSettingsObject>("speed",80,true) {

    override fun getText(): ArrayList<String> {
        return arrayListOf(NumberFormatter.withAfterPointZero(Bewisclient.speed*20,2)+" m/s")
    }

    override fun getWidgetSettings(): SettingTypes.SpeedWidgetSettingsObject {
        return SettingTypes.SpeedWidgetSettingsObject(id,5f,1f,80f,-1f, .43f, 0.7f)
    }
}