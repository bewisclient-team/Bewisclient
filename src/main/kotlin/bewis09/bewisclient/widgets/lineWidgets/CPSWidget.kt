package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.SettingsLoader

class CPSWidget: LineWidget("cps",80,true) {
    override fun getText(): ArrayList<String> {
        when (getProperty(CPS_ELEMENTS)) {
            0f -> return arrayListOf("${Bewisclient.lCount()} | ${Bewisclient.rCount()} CPS")
            1f -> return arrayListOf("${Bewisclient.lCount()} CPS")
            2f -> return arrayListOf("${Bewisclient.rCount()} CPS")
        }
        return arrayListOf("${Bewisclient.lCount()} | ${Bewisclient.rCount()} CPS")
    }
}