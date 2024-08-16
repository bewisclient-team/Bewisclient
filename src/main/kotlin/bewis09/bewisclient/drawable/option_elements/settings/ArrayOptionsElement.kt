package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.ScalableButtonWidget
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * A [SettingsOptionsElement] which displays a button where you can cycle through an array
 *
 * @param title The title of the element and gets converted to the description string
 * @param settings The category of settings the setting
 * @param path The path to the setting
 * @param id The id of the setting
 */
class ArrayOptionsElement(
    title: String,
    path: Array<String>,
    id: SettingsLoader.TypedSettingID<Float>,
    settings: String
) : SettingsOptionsElement<Float>(title, settings, path, id) {

    /**
     * The index of the current value
     */
    private var v = get().toInt()

    /**
     * The [ScalableButtonWidget] that gets displayed
     */
    private val widget = ScalableButtonWidget.builder(Text.empty()) {
        v += 1
        v %= array.size
        set(v.toFloat())
    }.dimensions(0,0,100,20).build()

    /**
     * The array of the possible options
     */
    private val array = (DefaultSettings.arrays[toPointNotation(path,id)] ?: DefaultSettings.arrays[".${id}"])!!

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-122)

        val height = 24+10*descriptionLines.size

        widget.message = Bewisclient.getTranslationText(array[v])

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
        widget.mouseClicked(mouseX, mouseY, button)
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double, button: Int) {
        if(widget.isHovered)
            widget.mouseDragged(mouseX,mouseY,button,deltaX,deltaY)
    }
}