package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.ElementList.dependentDisabler
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.DefaultSettings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * A [SettingsOptionElement] which displays a button where you can cycle through an array
 *
 * @param title The title of the element and gets converted to the description string
 * @param settings The category of settings the setting
 * @param path The path to the setting
 * @param id The id of the setting
 */
class ArrayOptionElement(
    title: String,
    path: Array<String>,
    id: SettingsLoader.TypedSettingID<Float>,
    settings: String,
    val descriptionEnabled: Boolean
) : SettingsOptionElement<Float>(title, settings, path, id) {

    /**
     * The index of the current value
     */
    private var v = get().toInt()

    /**
     * The array of the possible options
     */
    private val array = (DefaultSettings.arrays[toPointNotation(path,id)] ?: DefaultSettings.arrays[".${id}"])!!

    var isSelected: Boolean = false

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(dependentDisabler.contains(toPointNotation(path,id)) && !dependentDisabler[toPointNotation(path,id)]!!()) return -8

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 13 + if(descriptionEnabled) descriptionLines.size*10 + 4 else 0

        isSelected = x+width-80 < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        if(descriptionEnabled)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        context.fill(x+width-80,y,x+width,y+13, alphaModifier.toInt())
        context.drawBorder(x+width-80,y,80,13, (alphaModifier+ (if(isSelected) 0xAAAAFF else 0xFFFFFF)).toInt())

        context.drawCenteredTextWithShadow(client.textRenderer,Bewisclient.getTranslationText(array[v]),x+width-40,y+3,-1)

        return height
    }

    override fun getTypeParameter(): String = "float"

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(isSelected) {
            v += 1
            v %= array.size
            set(v.toFloat())
        }
    }
}