package bewis09.bewisclient.widgets.lineWidgets

class CPSWidget: LineWidget("cps",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf("0 | 0 CPS")
    }
}