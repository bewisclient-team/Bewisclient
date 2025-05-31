package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.SettingTypes

/**
 * A [LineWidget] which displays the current CPS
 */
class CPSWidget: LineWidget<SettingTypes.CPSWidgetSettingsObject>("cps",80,true) {
    override fun getText(): ArrayList<String> {
        when (settings.cps_elements.get()) {
            0 -> return arrayListOf("${Bewisclient.lCount()} | ${Bewisclient.rCount()} CPS")
            1 -> return arrayListOf("${Bewisclient.lCount()} CPS")
            2 -> return arrayListOf("${Bewisclient.rCount()} CPS")
        }
        return arrayListOf("${Bewisclient.lCount()} | ${Bewisclient.rCount()} CPS")
    }

    override fun getWidgetSettings(): SettingTypes.CPSWidgetSettingsObject {
        return SettingTypes.CPSWidgetSettingsObject(id,5f,1f,56f,-1f,.43f,.7f)
    }
}