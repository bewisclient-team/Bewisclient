package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.ElementList.dependentDisabler
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.NumberFormatter
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import kotlin.math.pow
import kotlin.math.round

/**
 * A [SettingsOptionElement] which sets a float and displays a fader
 */
class FloatOptionElement : SettingsOptionElement<Float> {

    var descriptionEnabled: Boolean

    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Float>, settings: String, vararg description: Boolean) : super(
        title,
        settings,
        path,
        id
    ) {
        this.valueChanged = { a ->
            run {
                set((round((a*(range!!.end-range.start)+range.start)* 10.0.pow(range.decimalPoints.toDouble()))/ 10.0.pow(range.decimalPoints.toDouble())).toFloat())
            }
        }
        this.range = DefaultSettings.sliders[toPointNotation(path,id)]
                ?: DefaultSettings.sliders[".$id"]
        this.descriptionEnabled = description.getOrElse(0) { false }
    }

    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Float>, settings: String, valueChanger: (Double) -> Unit, description: Boolean) : super(
        title,
        settings,
        path,
        id
    ) {
        this.valueChanged = { a ->
            run {
                set((round((a*(range!!.end-range.start)+range.start)* 10.0.pow(range.decimalPoints.toDouble()))/ 10.0.pow(range.decimalPoints.toDouble())).toFloat())
                valueChanger(a)
            }
        }
        this.range = DefaultSettings.sliders[toPointNotation(path,id)]
                ?: DefaultSettings.sliders[".$id"]
        this.descriptionEnabled = description
    }

    /**
     * Gets executed when the value gets changed
     */
    val valueChanged: (Double) -> Unit

    /**
     * The information about the range and the step size of the fader
     */
    private val range: DefaultSettings.SliderInfo?

    /**
     * Indicates if the [widget] is being clicked
     */
    var clicked = false

    var isSelected: Boolean = false

    var value = 1f

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return -8

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 13 + if(descriptionEnabled) descriptionLines.size*10 + 4 else 0

        isSelected = x+width-80 < mouseX && y < mouseY && x+width > mouseX && y+13 > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        value = (SettingsLoader.get(settings, id, *path)-range!!.start)/(range.end-range.start)

        if(descriptionEnabled)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        if(clicked) {
            value = MathHelper.clamp((mouseX - x - width + 75) / 70f, 0f, 1f)

            SettingsLoader.disableAutoSave()
            SettingsLoader.set(settings, value*(range.end-range.start)+range.start, id, *path)
        }

        value = (((Math.round((value*(range.end-range.start)+range.start)* 10.0.pow(range.decimalPoints.toDouble())))
                / 10.0.pow(range.decimalPoints.toDouble())-range.start)/(range.end-range.start)).toFloat()

        val str = NumberFormatter.withAfterPointZero((value*(range.end-range.start)+range.start).toDouble(),range.decimalPoints)

        context.drawTextWithShadow(client.textRenderer, str,width+x-84-client.textRenderer.getWidth(str),y+3,-1)

        context.fill(x+width-80,y,x+width,y+13, 0xFF000000.toInt())
        context.drawBorder(x+width-80,y,80,13, (alphaModifier+ 0xFFFFFF).toInt())

        context.fill((x+width-77+value*67).toInt(),y+3, (x+width-70+value*67).toInt(),y+10, (alphaModifier+0xFFFFFF).toInt())

        if(isSelected) {
            context.drawBorder((x+width-78+value*67).toInt(),y+2, 9,9, (alphaModifier+0xAAAAFF).toInt())
        }

        return height
    }

    override fun getTypeParameter(): String = "float"

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return

        clicked = isSelected
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        if(clicked) {
            set(value*(range!!.end-range.start)+range.start)
        }

        clicked = false
    }
}