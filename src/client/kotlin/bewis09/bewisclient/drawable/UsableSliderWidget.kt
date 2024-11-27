package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.util.NumberFormatter.withAfterPointZero
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.text.Text

/**
 * An implementation of a [SliderWidget] which can be easily used in the [bewis09.bewisclient.screen.MainOptionsScreen]
 */
class UsableSliderWidget : SliderWidget {

    /**
     * The maximum value of the slider
     */
    val maxValue: Float

    /**
     * The minimum value of the slider
     */
    val minValue: Float

    /**
     * The number of digits after the decimal point that should be rounded to
     */
    val decimalPlaces: Int

    /**
     * A function which gets executed when the value was changed
     */
    val valueApply: (Double) -> Unit

    /**
     * A function which gets executed when the value was changed
     * and returns the text of the widget according to the value
     */
    var messageApplier: ((Double) -> Text)? = null

    /**
     * @param x The x-position of the widget
     * @param y The y-position of the widget
     * @param width The width of the widget
     * @param height The height of the widget
     * @param text The [Text] that displays on the widget
     * @param value The default value of the widget
     * @param minValue The minimum value of the slider
     * @param maxValue The maximum value of the slider
     * @param decimalPlaces The number of digits after the decimal point that should be rounded to
     * @param valueApply A function which gets executed when the value was changed
     */
    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        text: Text,
        value: Double,
        minValue: Float,
        maxValue: Float,
        decimalPlaces: Int,
        valueApply: (Double) -> Unit
    ) : super(x, y, width, height, text, value) {
        this.maxValue = maxValue
        this.minValue = minValue
        this.decimalPlaces = decimalPlaces
        this.valueApply = valueApply
        updateMessage()
    }

    /**
     * @param x The x-position of the widget
     * @param y The y-position of the widget
     * @param width The width of the widget
     * @param height The height of the widget
     * @param text The [Text] that displays on the widget
     * @param value The default value of the widget
     * @param minValue The minimum value of the slider
     * @param maxValue The maximum value of the slider
     * @param decimalPlaces The number of digits after the decimal point that should be rounded to
     * @param valueApply A function which gets executed when the value was changed
     * @param messageApplier A function which gets executed when the value was changed and returns the text of the widget according to the value
     */
    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        text: Text,
        value: Double,
        minValue: Float,
        maxValue: Float,
        decimalPlaces: Int,
        valueApply: (Double) -> Unit,
        messageApplier: (Double) -> Text
    ) : super(x, y, width, height, text, value) {
        this.maxValue = maxValue
        this.minValue = minValue
        this.decimalPlaces = decimalPlaces
        this.valueApply = valueApply
        this.messageApplier = messageApplier
        updateMessage()
    }

    override fun updateMessage() {
        message = if(messageApplier==null)
            Text.of(Bewisclient.getTranslatedString("gui.value")+": "+ withAfterPointZero(value*(maxValue-minValue)+minValue,decimalPlaces))
        else
            messageApplier!!(value)
    }

    override fun applyValue() {
        valueApply(value)
    }

    /**
     * @return The current value
     */
    fun getValue(): Double {
        return value
    }
}