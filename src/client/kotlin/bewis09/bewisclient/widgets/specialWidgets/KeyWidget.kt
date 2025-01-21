package bewis09.bewisclient.widgets.specialWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.widgets.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text

/**
 * A [Widget] which displays the movement keys and mouse buttons that are currently pressed
 */
@Suppress("SameParameterValue")
class KeyWidget: Widget<SettingTypes.KeyWidgetSettingsObject>("keys") {
    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        drawContext.matrices.push()
        drawContext.matrices.scale(settings.size.get(),settings.size.get(),1F)
        var off = 0
        if(settings.show_movement_keys.get()) {
            renderKey(20, 19, x + 22, y + 0, MinecraftClient.getInstance().options.forwardKey, drawContext)
            renderKey(20, 19, x + 0, y + 21, MinecraftClient.getInstance().options.leftKey, drawContext)
            renderKey(20, 19, x + 22, y + 21, MinecraftClient.getInstance().options.backKey, drawContext)
            renderKey(20, 19, x + 44, y + 21, MinecraftClient.getInstance().options.rightKey, drawContext)

            off+=42
        }
        if(settings.show_space_bar.get()) {
            renderKey(64, x + 0, y + off, MinecraftClient.getInstance().options.jumpKey, drawContext)
            off+=17
        }

        if(settings.show_mouse_button.get()) {
            renderKey(31, x + 0, y + off, Text.of(if(settings.show_cps.get()) Bewisclient.lCount().toString()+" L" else "LMB"), MinecraftClient.getInstance().options.attackKey, drawContext)
            renderKey(31, x + 33, y + off, Text.of(if(settings.show_cps.get()) Bewisclient.rCount().toString()+" R" else "RMB"), MinecraftClient.getInstance().options.useKey, drawContext)
        }

        drawContext.matrices.pop()
    }

    override fun getOriginalWidth(): Int {
        return 64
    }

    override fun getOriginalHeight(): Int {
        return (if(settings.show_movement_keys.get()) 40 else -2)+(if(settings.show_space_bar.get()) 17 else 0)+(if(settings.show_mouse_button.get()) 17 else 0)
    }

    /**
     * Renders a key with the text of the [keyBinding] and a height of 15
     *
     * @param width The width of the key
     * @param x The relative x-coordinate of the key
     * @param y The relative y-coordinate of the key
     * @param keyBinding The [KeyBinding] that gets drawn
     * @param drawContext The [DrawContext] for drawing
     */
    private fun renderKey(width: Int, x:Int, y:Int, keyBinding: KeyBinding, drawContext: DrawContext) {
        renderKey(width, 15, x, y, keyBinding.boundKeyLocalizedText, keyBinding, drawContext)
    }

    /**
     * Renders a key with the text of the [keyBinding]
     *
     * @param width The width of the key
     * @param height The height of the key
     * @param x The relative x-coordinate of the key
     * @param y The relative y-coordinate of the key
     * @param keyBinding The [KeyBinding] that gets drawn
     * @param drawContext The [DrawContext] for drawing
     */
    private fun renderKey(width: Int, height: Int, x:Int, y:Int, keyBinding: KeyBinding, drawContext: DrawContext) {
        renderKey(width, height, x, y, keyBinding.boundKeyLocalizedText, keyBinding, drawContext)
    }

    /**
     * Renders a key with a specialized Text that gets highlighted if the [keyBinding] is pressed. The height is specified as 15
     *
     * @param width The width of the key
     * @param x The relative x-coordinate of the key
     * @param y The relative y-coordinate of the key
     * @param keyBinding The [KeyBinding] that gets drawn
     * @param drawContext The [DrawContext] for drawing
     */
    private fun renderKey(width: Int, x:Int, y:Int, text: Text, keyBinding: KeyBinding, drawContext: DrawContext) {
        renderKey(width, 15, x, y, text, keyBinding, drawContext)
    }

    /**
     * Renders a key with a specialized Text that gets highlighted if the [keyBinding] is pressed
     *
     * @param width The width of the key
     * @param height The height of the key
     * @param x The relative x-coordinate of the key
     * @param y The relative y-coordinate of the key
     * @param keyBinding The [KeyBinding] that gets drawn
     * @param drawContext The [DrawContext] for drawing
     */
    private fun renderKey(width: Int, height: Int, x:Int, y:Int, text: Text, keyBinding: KeyBinding, drawContext: DrawContext) {
        drawContext.fill(x,y,x+width,y+height,
            (getAlphaModifier() + if (keyBinding.isPressed) 0xFFFFFF else 0x000000).toInt()
        )
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,text,x+width/2,y+((height-9)/2+1),(0xFF000000L+settings.text_color.get().getColor()).toInt())
    }

    /**
     * @return A modifier that can be added to any RGB color to make a ARGB color out of it with the [bewis09.bewisclient.settingsLoader.Settings.TRANSPARENCY] as alpha
     */
    private fun getAlphaModifier(): Long {
        return (settings.transparency.get().times(255f)).toLong()*0x1000000
    }

    override fun getWidgetSettings(): SettingTypes.KeyWidgetSettingsObject {
        return SettingTypes.KeyWidgetSettingsObject(id,5.0f,-1f,17f,1f,.43f,1f)
    }
}