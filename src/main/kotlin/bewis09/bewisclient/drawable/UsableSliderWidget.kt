package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.text.Text
import kotlin.math.pow
import kotlin.math.round

class UsableSliderWidget : SliderWidget {

    val maxValue: Float
    val minValue: Float
    val decimalPlaces: Int
    val valueApply: (Double) -> Unit
    var messageApplier: ((Double) -> Text)? = null

    constructor(x: Int, y: Int, width: Int, height: Int, text: Text, value: Double, maxValue: Float, minValue: Float, decimalPlaces: Int, valueApply: (Double) -> Unit) : super(x, y, width, height, text, value) {
        this.maxValue = maxValue
        this.minValue = minValue
        this.decimalPlaces = decimalPlaces
        this.valueApply = valueApply
        updateMessage()
    }

    constructor(x: Int, y: Int, width: Int, height: Int, text: Text, value: Double, maxValue: Float, minValue: Float, decimalPlaces: Int, valueApply: (Double) -> Unit, messageApplier: (Double) -> Text) : super(x, y, width, height, text, value) {
        this.maxValue = maxValue
        this.minValue = minValue
        this.decimalPlaces = decimalPlaces
        this.valueApply = valueApply
        this.messageApplier = messageApplier
        updateMessage()
    }

    override fun updateMessage() {
        message = if(messageApplier==null)
            Text.of(Bewisclient.getTranslatedString("gui.value")+": "+ withDecimalPlaces(value*(maxValue-minValue)+minValue,decimalPlaces))
        else
            messageApplier!!(value)
    }

    override fun applyValue() {
        valueApply(value)
    }

    fun getValue(): Double {
        return value
    }

    companion object {
        fun withDecimalPlaces(value:Double, decimalPlaces: Int): String {
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
}