package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.settingsLoader.settings.FloatSetting
import bewis09.bewisclient.util.NumberFormatter
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import kotlin.math.pow
import kotlin.math.round

/**
 * A [SettingsOptionElement] which sets a float and displays a fader
 */
class FloatOptionElement(setting: FloatSetting) : SettingsOptionElement<Float, FloatSetting>(setting) {

    /**
     * The information for a slider in a [bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement]
     *
     * @param start The minimum value
     * @param end The maximum value
     * @param decimalPoints The number of digits after the decimal point
     */
    class SliderInfo(val start: Float, val end: Float, val decimalPoints: Int)

    var descriptionEnabled: Boolean

    /**
     * Gets executed when the value gets changed
     */
    val valueChanged: (Double) -> Unit

    /**
     * The information about the range and the step size of the fader
     */
    private val range: SliderInfo

    /**
     * Indicates if the widget is being clicked
     */
    var clicked = false

    var isSelected: Boolean = false

    var value = 1f

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(setting.elementOptions.enableFunction?.invoke() == false) return -8

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 13 + if(descriptionEnabled) descriptionLines.size*10 + 4 else 0

        isSelected = x+width-80 < mouseX && y < mouseY && x+width > mouseX && y+13 > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        value = (setting.get()-range.start)/(range.end-range.start)

        if(descriptionEnabled)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        if(clicked) {
            value = MathHelper.clamp((mouseX - x - width + 75) / 70f, 0f, 1f)

            SettingsLoader.disableAutoSave()
            setting.set(value*(range.end-range.start)+range.start)
        }

        value = (((Math.round((value*(range.end-range.start)+range.start)* 10.0.pow(range.decimalPoints.toDouble())))
                / 10.0.pow(range.decimalPoints.toDouble())-range.start)/(range.end-range.start)).toFloat()

        val str = NumberFormatter.withAfterPointZero((value*(range.end-range.start)+range.start).toDouble(),range.decimalPoints)

        context.drawTextWithShadow(client.textRenderer, str,width+x-84-client.textRenderer.getWidth(str),y+3,(alphaModifier+0xFFFFFF).toInt())

        context.fill(x+width-80,y,x+width,y+13, (alphaModifier).toInt())
        context.drawBorder(x+width-80,y,80,13, (alphaModifier+ 0xFFFFFF).toInt())

        context.fill((x+width-77+value*67).toInt(),y+3, (x+width-70+value*67).toInt(),y+10, (alphaModifier+0xFFFFFF).toInt())

        if(isSelected) {
            context.drawBorder((x+width-78+value*67).toInt(),y+2, 9,9, (alphaModifier+0xAAAAFF).toInt())
        }

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(setting.elementOptions.enableFunction?.invoke() == false) return

        clicked = isSelected
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        if(clicked) {
            setting.set(value*(range.end-range.start)+range.start)
        }

        clicked = false
    }

    init {
        this.valueChanged = { a ->
            run {
                setting.set((round((a*(range.end-range.start)+range.start)* 10.0.pow(range.decimalPoints.toDouble()))/ 10.0.pow(range.decimalPoints.toDouble())).toFloat())
                setting.elementOptions.onChange?.invoke()
            }
        }
        this.range = setting.elementOptions.sliderInfo!!
        this.descriptionEnabled = setting.elementOptions.description
    }
}