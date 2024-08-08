package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.UsableSliderWidget
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import kotlin.math.pow
import kotlin.math.round

class FloatOptionsElement : SettingsOptionsElement {

    private val value: String
    private val settings: String

    constructor(title: String, path: String, value: String, settings: String) : super(title, path, arrayListOf()) {
        this.value = value
        this.settings = settings
        this.valueChanged = { a ->
            run {
                SettingsLoader.set(settings, path, (round((a*(de!!.end-de.start)+de.start)* 10.0.pow(de.decimalPoints.toDouble()))/ 10.0.pow(de.decimalPoints.toDouble())))
            }
        }
        this.de = DefaultSettings.sliders[path]
                ?: DefaultSettings.sliders["."+value.split(".")[value.split(".").size - 1]]
        this.widget = UsableSliderWidget(0, 0, 100, 20, Text.empty(), ((SettingsLoader.getFloat(settings, path) - de?.start!!) / (de.end - de.start)).toDouble(), de.end, de.start, de.decimalPoints, valueChanged)
    }

    constructor(title: String, path: String, value: String, settings: String, valueChanger: (Double)->Unit) : super(title, path, arrayListOf()) {
        this.value = value
        this.settings = settings
        this.valueChanged = { a ->
            run {
                SettingsLoader.set(settings, path, (round((a*(de!!.end-de.start)+de.start)* 10.0.pow(de.decimalPoints.toDouble()))/ 10.0.pow(de.decimalPoints.toDouble())))
                valueChanger(a)
            }
        }
        this.de = DefaultSettings.sliders[path]
                ?: DefaultSettings.sliders["." + value.split(".")[value.split(".").size - 1]]
        this.widget = UsableSliderWidget(0, 0, 100, 20, Text.empty(), ((SettingsLoader.getFloat(settings, path) - de?.start!!) / (de.end - de.start)).toDouble(), de.end, de.start, de.decimalPoints, valueChanged)
    }

    val valueChanged: (Double) -> Unit

    private val de: DefaultSettings.SliderInfo?

    val widget: UsableSliderWidget

    var clicked = false

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
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

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
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