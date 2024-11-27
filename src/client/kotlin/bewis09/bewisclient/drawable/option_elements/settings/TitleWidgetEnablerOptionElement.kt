package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.TitleOptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which displays a title and allows you to enable/disable the widget
 */
class TitleWidgetEnablerOptionElement : TitleOptionElement {
    val setting: String
    val path: Array<String>
    val settingID: SettingsLoader.TypedSettingID<Boolean>
    val valueApplier: (()->Unit)?

    constructor(
        setting: String,
        path: Array<String>,
        settingID: SettingsLoader.TypedSettingID<Boolean>,
        vararg titles: String
    ) : super(*titles) {
        this.setting = setting
        this.path = path
        this.settingID = settingID
        this.valueApplier = null
    }

    constructor(
        setting: String,
        path: Array<String>,
        settingID: SettingsLoader.TypedSettingID<Boolean>,
        vararg titles: String,
        valueApplier: ()->Unit
    ) : super(*titles) {
        this.setting = setting
        this.path = path
        this.settingID = settingID
        this.valueApplier = valueApplier
    }

    /**
     * Indicates if the widget enabler is hovered
     */
    var isWidgetHovered = false

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val a = super.render(context, x, y, width, mouseX, mouseY, alphaModifier)

        isWidgetHovered = mouseX>x+width-80&&mouseX<x+width&&mouseY>y+3&&mouseY<y+17

        val enabled = (SettingsLoader.get(setting, settingID, *path))

        if(!isWidgetHovered) {
            context.fill(
                x + width - 80,
                y + 3,
                x + width,
                y + 17,
                (alphaModifier + (if (enabled) 0x44BB44 else 0xFF0000)).toInt()
            )
        } else {
            context.fill(
                x + width - 80,
                y + 3,
                x + width,
                y + 17,
                (alphaModifier + (if (enabled) 0x226022 else 0x800000)).toInt()
            )
        }
        context.drawBorder(
            x+width-80,
            y + 3,
            80,
            14,
            (alphaModifier + (if(isWidgetHovered) 0xAAAAFF else 0xFFFFFF)).toInt()
        )

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            if (enabled) Bewisclient.getTranslatedString("enabled") else Bewisclient.getTranslatedString("disabled"),
            x + width - 40,
            y+6,
            (alphaModifier + 0xFFFFFF).toInt()
        )

        return a
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(isWidgetHovered) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            val enabled = (SettingsLoader.get(setting, settingID, *path))
            SettingsLoader.set(setting, !enabled, settingID, *path)
            valueApplier?.invoke()
        }
    }
}