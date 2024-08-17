package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.ElementList.dependentDisabler
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import kotlin.math.cos

/**
 * A [SettingsOptionElement] which changes a true-false setting
 */
class BooleanOptionElement : SettingsOptionElement<Boolean> {

    /**
     * Gets executed when the value gets changed
     */
    val valueChanger: (Boolean)->Unit

    /**
     * @param title The title of the element and gets converted to the description string
     * @param settings The category of settings the setting
     * @param path The path to the setting
     * @param id The id of the setting
     */
    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Boolean>, settings: String) : super(
        title,
        settings,
        path,
        id
    ) {
        this.valueChanger = {}
    }

    /**
     * @param title The title of the element and gets converted to the description string
     * @param settings The category of settings the setting
     * @param path The path to the setting
     * @param id The id of the setting
     */
    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Boolean>, settings: String, valueChanger: (Boolean) -> Unit) : super(
        title,
        settings,
        path,
        id
    ) {
        this.valueChanger = valueChanger
    }

    /**
     * The time when the animation started in ms in unix time
     *
     * @see [System.currentTimeMillis]
     */
    var animationStart = 0L

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return -4

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 24+10*descriptionLines.size

        val isSelected = x+width-20 < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(x,y,x+width-22,y+height, alphaModifier.toInt())
        context.drawBorder(x,y,width-22,height, (alphaModifier+0xFFFFFF).toInt())

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        val middleX = x+width-10

        val enabled = SettingsLoader.get(settings, path, id)

        var progress = MathHelper.clamp((System.currentTimeMillis() - animationStart)/SettingsLoader.get(
            "design",
            OPTIONS_MENU,
            ANIMATION_TIME
        ),0F,1F)

        if(enabled) {
            progress = 1-progress
        }

        progress = ((1- cos(Math.PI*progress))/2).toFloat()

        val enableColor: Int = ColorHelper.Argb.getArgb((alphaModifier/0x1000000).toInt(),
                (0xAA * progress + 0x55 * (1-progress)).toInt(),
                (0x55 * progress + 0xAA * (1-progress)).toInt(),
                0x55
        )

        if(!isSelected) {
            context.fill(x+width-20,y,x+width,y+height, (enableColor))
            context.drawBorder(x+width-20,y,20,height, (alphaModifier+ 0xFFFFFF).toInt())

            context.fill(middleX-7, (y + ((height-12)*(progress)+3*(1-progress))).toInt(),middleX+7,(y + ((height-3)*(progress)+12*(1-progress))).toInt(), (alphaModifier+0xFFFFFF).toInt())
        } else {
            context.fill(x+width-20-1,y-1,x+width+1,y+height+1, (enableColor))
            context.drawBorder(x+width-20-1,y-1,22,height+2, (alphaModifier+ 0xAAAAFF).toInt())

            context.fill(middleX-8,(y + ((height-12)*(progress)+2*(1-progress))).toInt(),middleX+8,(y + ((height-2)*(progress)+12*(1-progress))).toInt(),(alphaModifier+0xFFFFFF).toInt())
            context.drawBorder(middleX-8,(y + ((height-13)*(progress)+2*(1-progress))).toInt(),16,11,(alphaModifier+0xAAAAFF).toInt())
        }

        return height
    }

    override fun getTypeParameter(): String = "boolean"

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return

        if ( pos[2] - 20 < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            animationStart = System.currentTimeMillis()
            val b = !get()
            set(b)

            valueChanger(b)
        }
    }
}