package bewis09.bewisclient.screen

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import bewis09.bewisclient.widgets.WidgetRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import kotlin.math.cos

@Suppress("CAST_NEVER_SUCCEEDS")
class WidgetConfigScreen(val parent: MainOptionsScreen): Screen(Text.empty()) {

    private var selected: Widget? = null

    var alphaStart = -1L;
    var alphaDirection = 0;

    var _sel_element: Widget? = null
    var _sel_xOffset: Int = 0
    var _sel_yOffset: Int = 0

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {

        if(alphaStart==-1L) {
            alphaStart= System.currentTimeMillis();
        }

        val animationSpeed = MathHelper.clamp(SettingsLoader.DesignSettings.getValue(Settings.Settings.OPTIONS_MENU)!!.getValue(Settings.Settings.ANIMATION_TIME)!!.toInt(),1,500).toFloat()

        if(alphaDirection ==1 && (System.currentTimeMillis() - alphaStart)/animationSpeed>1) {
            MinecraftClient.getInstance().setScreen(parent)
            parent.animationStart = System.currentTimeMillis()
            parent.animatedScreen = null
            parent.animationState = MainOptionsScreen.AnimationState.TO_MAIN_SCREEN
            return
        }

        var sdfji = MathHelper.clamp((System.currentTimeMillis() - alphaStart)/animationSpeed,0f,1f)
        sdfji = Math.abs(alphaDirection-sdfji)
        sdfji = (1-cos(Math.PI/2 * sdfji)).toFloat()

        val sdfjij = ((sdfji*0xAA).toLong()*0x1000000)

        context?.drawHorizontalLine(0,width,height/2, (0xAAAAAAL+sdfjij).toInt())
        context?.drawVerticalLine(width/3,0,height, (0xAAAAAAL+sdfjij).toInt())
        context?.drawVerticalLine(width/3*2,0,height, (0xAAAAAAL+sdfjij).toInt())
        context?.drawVerticalLine(width/2,0,height, (0xFF8888L+sdfjij).toInt())

        selected = null

        WidgetRenderer.widgets.forEach {
            if (context != null && it.isEnabled()) {
                it.render(context)
            }

            if(it.getOriginalPosX()<mouseX && it.getOriginalPosY()<mouseY && it.getOriginalPosX()+it.getWidth()>mouseX && it.getOriginalPosY()+it.getHeight()>mouseY) {
                selected = it
            }
        }

        if(selected!=null)
           context?.drawTooltip(textRenderer,Bewisclient.getTranslationText("widgets."+selected?.id),mouseX,mouseY)

        if(_sel_element!=null) {
            _sel_element!!.setPropertyPosX((mouseX-_sel_xOffset).toFloat(), width, _sel_element!!.getWidth(), hasShiftDown())
            _sel_element!!.setPropertyPosY((mouseY-_sel_yOffset).toFloat(), height, _sel_element!!.getHeight(), hasShiftDown())
            _sel_element!!.render(context!!)
        }
    }

    override fun close() {
        alphaStart = System.currentTimeMillis()
        alphaDirection = 1
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(selected!=null) {
            _sel_element = selected!!
            _sel_xOffset = (mouseX - selected!!.getOriginalPosX()).toInt()
            _sel_yOffset = (mouseY - selected!!.getOriginalPosY()).toInt()
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        _sel_element = null
        SettingsLoader.saveAllSettings()
        return super.mouseReleased(mouseX, mouseY, button)
    }
}