package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.ElementList.dependentDisabler
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.Animation
import bewis09.bewisclient.util.EaseMode
import bewis09.bewisclient.util.ScreenAnimation
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper

/**
 * A [SettingsOptionElement] which changes a true-false setting
 */
class BooleanOptionElement : SettingsOptionElement<Boolean> {

    /**
     * Gets executed when the value gets changed
     */
    val valueChanger: (Boolean)->Unit

    var descriptionEnabled: Boolean

    /**
     * @param title The title of the element and gets converted to the description string
     * @param settings The category of settings the setting
     * @param path The path to the setting
     * @param id The id of the setting
     */
    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Boolean>, settings: String, vararg description: Boolean) : super(
        title,
        settings,
        path,
        id
    ) {
        this.valueChanger = {}
        this.descriptionEnabled = description.getOrElse(0) { false }
    }

    /**
     * @param title The title of the element and gets converted to the description string
     * @param settings The category of settings the setting
     * @param path The path to the setting
     * @param id The id of the setting
     */
    constructor(title: String, path: Array<String>, id: SettingsLoader.TypedSettingID<Boolean>, settings: String, valueChanger: (Boolean) -> Unit, vararg description: Boolean) : super(
        title,
        settings,
        path,
        id
    ) {
        this.valueChanger = valueChanger
        this.descriptionEnabled = description.getOrElse(0) { false }
    }

    /**
     * The time when the animation started in ms in unix time
     *
     * @see [System.currentTimeMillis]
     */
    var animation = Animation(0,0, EaseMode.CONST)

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return -8

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 13 + if(descriptionEnabled) descriptionLines.size*10 + 4 else 0

        val isSelected = x+width-30 < mouseX && y < mouseY && x+width > mouseX && y+13 > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        if(descriptionEnabled)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        val enabled = SettingsLoader.get(settings, path, id)

        var progress = animation.getProgress()

        if(enabled) {
            progress = 1-progress
        }

        val enableColor: Int = ColorHelper.Argb.getArgb((alphaModifier/0x1000000).toInt(),
                (0xAA * progress + 0x55 * (1-progress)).toInt(),
                (0x55 * progress + 0xAA * (1-progress)).toInt(),
                0x55
        )

        context.fill(x+width-30,y,x+width,y+13, (enableColor))
        context.drawBorder(x+width-30,y,30,13, (alphaModifier+ 0xFFFFFF).toInt())

        context.fill((x+width-10-progress*(17)).toInt(),y+3, (x+width-3-progress*(17)).toInt(),y+10, (alphaModifier+0xFFFFFF).toInt())

        if(isSelected) {
            context.drawBorder((x+width-11-progress*(17)).toInt(),y+2, 9,9, (alphaModifier+0xAAAAFF).toInt())
        }

        return height
    }

    override fun getTypeParameter(): String = "boolean"

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return

        if ( pos[2] - 30 < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            animation = ScreenAnimation()
            val b = !get()
            set(b)

            valueChanger(b)
        }
    }
}