package bewis09.bewisclient.pop_up

import bewis09.bewisclient.screen.MainOptionsScreen
import net.minecraft.client.gui.DrawContext

abstract class PopUp(val screen: MainOptionsScreen) {
    abstract fun getWidth(): Int
    abstract fun getHeight(): Int

    open fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float, x: Int, y: Int, a: Float){
        context.fill(0,0, (screen.width*MainOptionsScreen.scale).toInt()+10, (screen.height*MainOptionsScreen.scale).toInt()+10, (a*0xAA).toInt()*0x1000000)

        context.fill(x,y,x+getWidth(),y+getHeight(), (a*0xFF).toInt()*0x1000000)
        context.drawBorder(x,y,getWidth(),getHeight(), (a*0xFF).toInt()*0x1000000+0xFFFFFF)
    }

    open fun mouseReleased(mouseX: Int, mouseY: Int, x: Int, y: Int){}
    open fun mouseDragged(mouseX: Int, mouseY: Int, deltaX: Double, deltaY: Double, x: Int, y: Int){}
    fun close() = screen.setPopUp(null,true)

    open fun mouseClicked(mouseX: Int, mouseY: Int, x: Int, y: Int){
        if(mouseX<x||mouseX>x+getWidth()||mouseY<y||mouseY>y+getHeight()) {
            close()
        }
    }
}