package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.text.Text
import kotlin.math.pow
import kotlin.math.round

class UsableSliderWidget(x: Int, y: Int, width: Int, height: Int, text: Text, value: Double, val maxValue: Float, val minValue: Float, val decimalPlaces: Int, val valueApply: (Double) -> Unit) : SliderWidget(x, y, width, height, text, value) {

    init {
        message = Text.of(Bewisclient.getTranslatedString("gui.value")+": "+withDecimalPlaces(value*(maxValue-minValue)+minValue,decimalPlaces))
    }

    override fun updateMessage() {
        message = Text.of(Bewisclient.getTranslatedString("gui.value")+": "+withDecimalPlaces(value*(maxValue-minValue)+minValue,decimalPlaces))
    }

    override fun applyValue() {
        valueApply(value)
    }

    interface ValueApplier {
         operator fun invoke(value: Double)
    }

    private fun withDecimalPlaces(value:Double,decimalPlaces: Int): String {
        if(decimalPlaces>0) {
            var p = (round(value* 10.0.pow(decimalPlaces.toDouble()))/ 10.0.pow(decimalPlaces.toDouble())).toString()
            while(p.split(".")[1].length<decimalPlaces) {
                p+="0"
            }
            return p
        } else {
            return value.toInt().toString()
        }
    }
}