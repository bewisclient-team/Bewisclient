package bewis09.bewisclient.dialog

import bewis09.bewisclient.settingsLoader.Settings
import net.minecraft.client.gui.DrawContext

abstract class Dialog(val time: Int): Settings() {
    var startTime = System.currentTimeMillis()

    companion object {
        var pauseTime = -1L
        private val dialogs = arrayListOf<Dialog>()

        fun render(context: DrawContext, width: Int, mouseX: Int, mouseY: Int) {
            if(pauseTime!=-1L) return

            var y = 4

            dialogs.toMutableList().forEach {
                if(it.startTime+it.time<System.currentTimeMillis()) {
                    dialogs.remove(it)
                }
            }

            dialogs.forEach { y+=it.render(context,width,y,mouseX,mouseY) }
        }

        fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
            dialogs.toMutableList().forEach { it.mouseClicked(mouseX, mouseY, button) }
        }

        fun addDialog(d: Dialog) = dialogs.add(d)

        fun pause() {
            pauseTime = System.currentTimeMillis()
        }

        fun proceed() {
            if(pauseTime == -1L) return

            dialogs.forEach {
                it.startTime += System.currentTimeMillis()-pauseTime
            }
            pauseTime = -1L
        }
    }

    open fun getWidth(width: Int): Int {
        return width/5
    }

    open fun getBarColor(): Int {
        return 0xFF0000
    }

    fun render(context: DrawContext, width: Int, y: Int, mouseX: Int, mouseY: Int): Int {
        context.matrices.push()
        context.matrices.translate(0f,0f,10f)

        val height = renderText(context,width,y,mouseX,mouseY)

        context.matrices.pop()

        context.fill(width-getWidth(width),y,width+2,y+height+1, 0xAA000000.toInt())
        context.fill(width-getWidth(width),y+height, (width-getWidth(width)+(System.currentTimeMillis()-startTime)/time.toFloat()*getWidth(width)).toInt()+2,y+height+1,
            (0xFF000000+getBarColor()).toInt()
        )

        return height + 5
    }

    open fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {

    }

    fun kill() {
        dialogs.remove(this)
    }

    abstract fun renderText(context: DrawContext, width: Int, y: Int, mouseX: Int, mouseY: Int): Int
}