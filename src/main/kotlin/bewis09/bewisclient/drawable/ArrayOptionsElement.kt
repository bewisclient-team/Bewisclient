package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class ArrayOptionsElement(title: String, private val value: String, private val settings: SettingsLoader.Settings) : WidgetOptionsElement(title, arrayListOf()) {

    private var v = getValue<Float>(settings,value)!!.toInt()

    private val widget = ButtonWidget.builder(Text.empty()) {
        v += 1
        v %= de.size
        setValue(settings,value,v.toFloat())
    }.dimensions(0,0,100,20).build()

    private val de = (DefaultSettings.arrays[value] ?: DefaultSettings.arrays["."+value.split(".")[value.split(".").size-1]])!!

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-122)

        val height = 24+10*descriptionLines.size

        widget.message = Bewisclient.getTranslationText("tiwyla."+de[v])

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
        widget.mouseClicked(mouseX, mouseY, button)
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double, button: Int) {
        if(widget.isHovered)
            widget.mouseDragged(mouseX,mouseY,button,deltaX,deltaY)
    }
}