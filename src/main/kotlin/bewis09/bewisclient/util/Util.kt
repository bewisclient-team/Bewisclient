package bewis09.bewisclient.util

object Util {
    fun isIn(mouseX: Number, mouseY: Number, x1: Int, y1: Int, x2: Int, y2: Int) = mouseX.toDouble()>=x1&&mouseX.toDouble()<=x2&&mouseY.toDouble()>=y1&&mouseY.toDouble()<=y2
}