package bewis09.bewisclient.screen

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.Settings.Companion.options_menu
import bewis09.bewisclient.util.Util
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import kotlin.math.roundToInt

class WelcomingScreen: Screen(Text.empty()) {
    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        context!!

        if (client!!.world == null) {
            this.renderPanoramaBackground(context, delta)
        } else {
            context.fill(0, 0, width, height, (0xAA000000).toInt())
        }

        val startX = width/6
        val startY = height/6
        val endX = (width/6f*5).roundToInt()
        val endY = (height/6f*5).roundToInt()
        val sizeX = (width/3f*2).roundToInt()
        val sizeY = (height/3f*2).roundToInt()

        context.fill(startX,startY,endX,endY, 0xFF000000.toInt())

        context.fill(endX-82,endY-19,endX-4,endY-4,0xAA555555.toInt())
        context.drawBorder(endX-82,endY-19,78,15,0xAAFFFFFF.toInt())
        context.drawCenteredTextWithShadow(textRenderer,Bewisclient.getTranslationText("skip"),endX-43,endY-15,-1)

        context.drawBorder(startX,startY,sizeX,sizeY, -1)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val startX = width/6
        val startY = height/6
        val endX = (width/6f*5).roundToInt()
        val endY = (height/6f*5).roundToInt()
        val sizeX = (width/3f*2).roundToInt()
        val sizeY = (height/3f*2).roundToInt()

        if(Util.isIn(mouseX,mouseY,endX-82,endY-19,endX-4,endY-4)) {
            MinecraftClient.getInstance().setScreen(MainOptionsScreen())
            options_menu.shown_start_menu.set(true)
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }
}