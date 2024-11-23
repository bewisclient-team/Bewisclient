package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.ScalableButtonWidget
import bewis09.bewisclient.drawable.UsableSliderWidget
import bewis09.bewisclient.screen.ElementList.dependentDisabler
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import bewis09.bewisclient.util.NumberFormatter
import bewis09.bewisclient.util.drawTexture
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import java.awt.Color
import kotlin.math.roundToInt

/**
 * A [SettingsOptionElement] which changes a color
 *
 * @param title The title of the element and gets converted to the description string
 * @param settings The category of settings the setting
 * @param path The path to the setting
 * @param id The id of the setting
 */
class LegacyColorPickerElement(
    title: String,
    path: Array<String>,
    id: SettingsLoader.TypedSettingID<ColorSaver>,
    settings: String
): SettingsOptionElement<ColorSaver>(title, settings, path, id) {

    /**
     * The x-position of the current color on the grid between 0 and 58
     */
    var posX: Int = 0

    /**
     * The y-position of the current color on the grid between 0 and 58
     */
    var posY: Int = 0

    /**
     * Indicates if the speed fader is clicked
     */
    var clicked = false

    /**
     * Indicates if the brightness fader is clicked
     */
    var hClicked = false

    /**
     * Indicates if the color is currently in the changing state
     */
    var changing = get().getColor()<0

    /**
     * The [ScalableButtonWidget] for the static option
     */
    val static = ScalableButtonWidget.builder(Bewisclient.getTranslationText("color.static")) {
        changing = false

        set(ColorSaver.of(get().getColor() + 0x1000000))
    }.build()

    /**
     * The [ScalableButtonWidget] for the change option
     */
    val change = ScalableButtonWidget.builder(Bewisclient.getTranslationText("color.change")) {
        changing = true

        set(ColorSaver.of((-(widget.getValue() * (19000) + 1000)).toInt()))
    }.build()

    /**
     * The [UsableSliderWidget] for the speed when changing
     */
    val widget = UsableSliderWidget(0, 0, 100, 20, Text.empty(), if(get().getColor()<0)
        (((get().getOriginalColor()*-1).toDouble())-1000)/19000 else 9/19.0, 1000F, 20000F, -2, {
        set(ColorSaver.of((-(it*(19000F)+1000F)).roundToInt()))
    }, {
        Text.of(Bewisclient.getTranslatedString("gui.speed")+": "+NumberFormatter.withAfterPointZero((it)*19+1,2)+" ${Bewisclient.getTranslatedString("seconds")}")
    })

    /**
     * The [UsableSliderWidget] for the brightness when static
     */
    val widget2 = UsableSliderWidget(0, 0, 100, 20, Text.empty(),
        if(get().getColor()>0) convertRGBtoHSB(get())[2].toDouble() else 1.0, 0F, 1F, 2, {
    set(ColorSaver.of(convertHSBtoRGB(posX/60f,posY/60f,it.toFloat())+0x1000000))
}, {
    Text.of(Bewisclient.getTranslatedString("gui.brightness")+": "+NumberFormatter.withAfterPointZero(it,2))
})

    init {
        val color = convertRGBtoHSB(get())
        posX = MathHelper.clamp((color[0]*60).toInt(),0,58)
        posY = MathHelper.clamp((color[1]*60).toInt(),0,58)
    }

    override fun getTypeParameter(): String = "colorsaver"

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(dependentDisabler.contains(toPointNotation(path, id)) && !dependentDisabler[toPointNotation(path, id)]!!()) return -8

        if(changing) {
            val color = convertRGBtoHSB(get())
            posX = MathHelper.clamp((color[0] * 60).toInt(), 0, 58)
            posY = MathHelper.clamp((color[1] * 60).toInt(), 0, 58)
        }

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-76)

        pos = arrayOf(x,y,x+width,y+60)

        context.fill(x+width-60,y,x+width,y+60, alphaModifier.toInt())

        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(if(get().getOriginalColor()<0f) 0.5f else widget2.getValue().toFloat(),if(get().getOriginalColor()<0f) 0.5f else widget2.getValue().toFloat(),if(SettingsLoader.get(settings, path, id).getOriginalColor()<0f) 0.5f else widget2.getValue().toFloat(),alphaModifier/(0xFFFFFFFF.toFloat()))
        context.drawTexture(Identifier.of("bewisclient","textures/color_space.png"),x+width-59,y+1,58,58)
        RenderSystem.setShaderColor(1f,1f,1f,1f)
        RenderSystem.disableBlend()

        context.drawBorder(x+width-61,y-1,62,62, (alphaModifier+ get().getColor()).toInt())
        context.drawBorder(x+width-60,y,60,60, (alphaModifier+ (get().getColor())).toInt())

        context.drawBorder(x+width-60+posX-1,y+posY-1,3,3, alphaModifier.toInt())

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        static.setAlpha(alphaModifier.toFloat()/0xFFFFFFFF)
        change.setAlpha(alphaModifier.toFloat()/0xFFFFFFFF)
        widget.setAlpha(alphaModifier.toFloat()/0xFFFFFFFF)
        widget2.setAlpha(alphaModifier.toFloat()/0xFFFFFFFF)

        static.x = x+3
        static.y = y+37
        static.height = 20
        static.width = (width-66)/4-2
        static.render(context,mouseX,mouseY,0f)

        change.x = x+(width-66)/4+2
        change.y = y+37
        change.height = 20
        change.width = (width-66)/4-2
        change.render(context,mouseX,mouseY,0f)

        change.active=true
        static.active=true

        widget.x = x+(width-66)/2
        widget.y = y+37
        widget.width = (width-66)/2-1


        widget2.x = x+(width-66)/2
        widget2.y = y+37
        widget2.width = (width-66)/2-1

        widget.active=true

        if(!changing) {
            static.active=false
            widget.active=false
        } else
            change.active=false

        if(changing)
            widget.render(context,mouseX,mouseY,0f)
        else
            widget2.render(context,mouseX,mouseY,0f)

        return 60
    }

    /**
     * Converts HSB to RGB with a static brightness of 1
     *
     * @param hue The hue (0-1)
     * @param saturation The saturation (0-1)
     *
     * @return The color as an RGB-Integer
     */
    fun convertHSBtoRGB(hue: Float, saturation: Float): Int {
        val rgb: Int = Color.HSBtoRGB(hue, saturation, 1f)
        return Color(rgb).rgb
    }

    /**
     * Converts HSB to RGB with a static brightness of 1
     *
     * @param hue The hue (0-1)
     * @param saturation The saturation (0-1)
     * @param brightness The brightness (0-1)
     *
     * @return The color as an RGB-Integer
     */
    fun convertHSBtoRGB(hue: Float, saturation: Float, brightness: Float): Int {
        val rgb: Int = Color.HSBtoRGB(hue, saturation, brightness)
        return Color(rgb).rgb
    }

    /**
     * Converts a [ColorSaver] to an HSB [FloatArray]
     *
     * @param color The [ColorSaver] that gets converted
     *
     * @return The HSB value as a FloatArray
     */
    fun convertRGBtoHSB(color: ColorSaver): FloatArray {
        val c = Color(color.getColor())
        val rgb: FloatArray = Color.RGBtoHSB(c.red,c.green,c.blue,null)
        return rgb
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double, button: Int) {
        if(dependentDisabler.contains(toPointNotation(path, id)) && !dependentDisabler[toPointNotation(path, id)]!!()) return

        if (clicked && !widget.active) {
            posX = MathHelper.clamp((mouseX-pos[2]+60).toInt(),1,58)
            posY = MathHelper.clamp((mouseY-pos[1]).toInt(),1,58)

            set(
                ColorSaver.of(convertHSBtoRGB((posX-1)/58f,(posY-1)/58f,widget2.getValue().toFloat())+0x1000000),
            )
        }
        if(widget.isHovered && hClicked && widget.active)
            widget.mouseDragged(mouseX,mouseY,button,deltaX,deltaY)
        if(widget2.isHovered && hClicked && !widget.active)
            widget2.mouseDragged(mouseX,mouseY,button,deltaX,deltaY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(dependentDisabler.contains(toPointNotation(path, id)) && dependentDisabler[toPointNotation(path, id)]!!()) return

        if(widget.isHovered)
            hClicked = true
        if(widget2.isHovered)
            hClicked = true
        if(widget.active)
            widget.mouseClicked(mouseX, mouseY, button)
        else
            widget2.mouseClicked(mouseX, mouseY, button)
        if (pos[2]-59 < mouseX && pos[1]+1 < mouseY && pos[2]-1 > mouseX && pos[3]-1 > mouseY  && !widget.active) {
            clicked = true

            posX = (mouseX-pos[2]+60).toInt()
            posY = (mouseY-pos[1]).toInt()

            set(ColorSaver.of(convertHSBtoRGB((posX-1)/58f,(posY-1)/58f)+0x1000000))
        }
        static.mouseClicked(mouseX, mouseY,button)
        change.mouseClicked(mouseX, mouseY,button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        clicked = false
        hClicked = false
    }
}