package bewis09.bewisclient.screen.widget

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.elements.ElementList
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.widgets.Widget
import bewis09.bewisclient.widgets.WidgetRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.round

class WidgetConfigScreen(var parent: MainOptionsScreen): Screen(Text.empty()) {

    private var selected: Widget? = null

    var alphaStart = -1L
    var alphaDirection = 0

    var _sel_element: Widget? = null
    var _sel_xOffset: Int = 0
    var _sel_yOffset: Int = 0

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {

        if(alphaStart==-1L) {
            alphaStart= System.currentTimeMillis()
        }

        val animationSpeed = MathHelper.clamp(SettingsLoader.getFloat("design","options_menu.animation_time").toInt(),1,500).toFloat()

        if(alphaDirection ==1 && (System.currentTimeMillis() - alphaStart)/animationSpeed>1) {
            MinecraftClient.getInstance().setScreen(parent)
            parent.animationStart = System.currentTimeMillis()
            parent.animatedScreen = null
            parent.animationState = MainOptionsScreen.AnimationState.TO_MAIN_SCREEN
            return
        }

        var sdfji = MathHelper.clamp((System.currentTimeMillis() - alphaStart)/animationSpeed,0f,1f)
        sdfji = abs(alphaDirection-sdfji)
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

        if(selected!=null) {
            if(selected!!.id=="effect") {
                context?.drawTooltip(textRenderer, arrayListOf(
                        Bewisclient.getTranslationText("widgets." + selected?.id),
                ) as List<Text>?, mouseX, mouseY)
            } else {
                context?.drawTooltip(textRenderer, arrayListOf(
                        Bewisclient.getTranslationText("widgets." + selected?.id),
                        Bewisclient.getTranslationText("screen.info.shift").formatted(Formatting.GRAY),
                        Bewisclient.getTranslationText("screen.info.right").formatted(Formatting.GRAY),
                        Bewisclient.getTranslationText("screen.info.scroll").formatted(Formatting.GRAY).append(" (${selected!!.getProperty(Settings.Settings.SCALE)})")
                ) as List<Text>?, mouseX, mouseY)
            }
        }

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
        if (selected!=null) {
            if (button == 0) {
                _sel_element = selected!!
                _sel_xOffset = (mouseX - selected!!.getOriginalPosX()).toInt()
                _sel_yOffset = (mouseY - selected!!.getOriginalPosY()).toInt()
            } else if(button == 1  && selected?.id!="effect") {
                alphaStart = System.currentTimeMillis()
                alphaDirection = 1

                parent = MainOptionsScreen()
                parent.allElements.add(ElementList.widgets())
                parent.allElements.add(ElementList.loadWidgetsSingleFromDefault(selected!!.getWidgetSettings(),selected!!.id))
                parent.scrolls.add(0f)
                parent.scrolls.add(0f)
                parent.slice = 2
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        _sel_element = null
        SettingsLoader.saveAllSettings()
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if(selected!=null) {
            selected!!.setProperty(Settings.Settings.SCALE, MathHelper.clamp((round((selected!!.getProperty(Settings.Settings.SCALE)+verticalAmount.toFloat()/10f)*100)/100f),0.2f,2f))
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
}