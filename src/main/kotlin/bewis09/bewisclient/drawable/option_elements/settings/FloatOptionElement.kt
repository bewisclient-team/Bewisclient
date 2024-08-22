package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.UsableSliderWidget
import bewis09.bewisclient.screen.ElementList.dependentDisabler
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import kotlin.math.pow
import kotlin.math.round

/**
 * A [SettingsOptionElement] which sets a float and displays a fader
 */
class FloatOptionElement : SettingsOptionElement<Float> {

    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Float>, settings: String) : super(
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
        this.widget = UsableSliderWidget(
            0,
            0,
            100,
            20,
            Text.empty(),
            ((SettingsLoader.get(settings, path, id) - range?.start!!) / (range.end - range.start)).toDouble(),
            range.start,
            range.end,
            range.decimalPoints,
            valueChanged
        )
    }

    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Float>, settings: String, valueChanger: (Double) -> Unit) : super(
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
        this.widget = UsableSliderWidget(
            0,
            0,
            100,
            20,
            Text.empty(),
            ((get() - range?.start!!) / (range.end - range.start)).toDouble(),
            range.start,
            range.end,
            range.decimalPoints,
            valueChanged
        )
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
     * The [UsableSliderWidget] that gets displayed
     */
    val widget: UsableSliderWidget

    /**
     * Indicates if the [widget] is being clicked
     */
    var clicked = false

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return -4

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-122)

        val height = 24+10*descriptionLines.size

        widget.x = x+width-107
        widget.y = y+height/2-10

        widget.setAlpha((alphaModifier/0xFF0000).toFloat()/256)

        widget.render(context,mouseX,mouseY,0f)

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(x,y,x+width-108,y+height, alphaModifier.toInt())
        context.fill(x+width-109,y,x+width,y+height/2-12, alphaModifier.toInt())
        context.fill(x+width-109,y+height/2+12,x+width,y+height, alphaModifier.toInt())
        context.fill(x+width-5,y+height/2-12,x+width,y+height/2+12, alphaModifier.toInt())
        context.drawBorder(x+width-109,y+height/2-12,104,24, (alphaModifier+0xFFFFFF).toInt())
        context.drawBorder(x,y,width,height, (alphaModifier+0xFFFFFF).toInt())

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        return height
    }

    override fun getTypeParameter(): String = "float"

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return

        if(widget.isHovered)
            clicked = true
        widget.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        clicked = false
        super.mouseReleased(mouseX, mouseY, button)
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double, button: Int) {
        if(widget.isHovered && clicked)
            widget.mouseDragged(mouseX,mouseY,button,deltaX,deltaY)
    }
}