package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.elements.ElementList
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.util.Identifier
import java.io.File
import java.io.PrintWriter
import java.util.Scanner

class MacroGroupElement(title: String, private val file: String): MainOptionsElement(title,"", arrayListOf(), Identifier("")) {

    private var deleteConfirm = false

    private val list = getTextFromFile(file)

    private val deleteButton = ButtonWidget.builder(Bewisclient.getTranslationText("delete")) {
        val file = File(FabricLoader.getInstance().gameDir.toString()+"\\macros\\"+file)

        val currentScreen = MinecraftClient.getInstance().currentScreen

        if(currentScreen is MainOptionsScreen && deleteConfirm) {

            file.delete()

            currentScreen.allElements.removeLast()
            currentScreen.allElements.add(ElementList.macros())
        } else {
            deleteConfirm = true

            it.message = Bewisclient.getTranslationText("confirm")
        }

    }.build()
    private val shortcutButton = ButtonWidget.builder(Bewisclient.getTranslationText("shortcutButton")) {}.build()

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        deleteButton.x = x+4
        deleteButton.height = 20
        deleteButton.width = (width-22)/2-5
        deleteButton.y = y+18

        shortcutButton.x = x+width-21-(width-22)/2
        shortcutButton.height = 20
        shortcutButton.width = (width-22)/2-5
        shortcutButton.y = y+18

        val height = 42

        val isSelected = x+width-20 < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(x,y,x+width-22,y+height, alphaModifier.toInt())
        context.drawBorder(x,y,width-22,height, (alphaModifier+0xFFFFFF).toInt())

        context.drawTextWithShadow(client.textRenderer, title,x+6,y+6,(alphaModifier+0xFFFFFF).toInt())

        if(!isSelected) {
            context.fill(x+width-20,y,x+width,y+height, (alphaModifier).toInt())
            context.drawBorder(x+width-20,y,20,height, (alphaModifier+ 0xFFFFFF).toInt())
        } else {
            context.fill(x+width-20-1,y-1,x+width+1,y+height+1, (alphaModifier).toInt())
            context.drawBorder(x+width-20-1,y-1,22,height+2, (alphaModifier+ 0xAAAAFF).toInt())
        }

        deleteButton.render(context,mouseX,mouseY,0f)

        if(deleteConfirm)
            context.fill(deleteButton.x,deleteButton.y,deleteButton.x+deleteButton.width,deleteButton.y+deleteButton.height, 0x66AA0000)

        shortcutButton.render(context,mouseX,mouseY,0f)

        RenderSystem.enableBlend()

        context.setShaderColor(1F,1F,1F,(alphaModifier.toFloat()/0xFFFFFFFF))

        if(!isSelected) {
            context.drawTexture(select,x+width-27,y+height/2-16,32,32,0F,0F,32,32,32,32)
        } else {
            context.drawTexture(selectHovered,x+width-29,y+height/2-18,36,36,0F,0F,36,36,36,36)
        }

        context.setShaderColor(1F,1F,1F, 1F)

        RenderSystem.disableBlend()

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        val d = deleteConfirm
        deleteButton.mouseClicked(mouseX, mouseY, button)
        shortcutButton.mouseClicked(mouseX, mouseY, button)
        if(d) {
            deleteConfirm = false
            deleteButton.message=Bewisclient.getTranslationText("delete")
        }
        val isSelected = pos[2] - 20 < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY
        if(isSelected) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.openNewSlice(getElementsFromFile(file,this))
        }
    }

    fun saveFile() {
        val f = File(FabricLoader.getInstance().gameDir.toString()+"\\macros\\"+ file)
        val p = PrintWriter(f)
        list.forEach {
            p.println(it)
        }
        p.flush()
        p.close()
    }

    fun changeFile(i: Int, lastText: String) {
        list[i] = lastText

        if(i+1==list.size) {
            (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).allElements.last().add(MacroElement("",i+1,this))
            list.add("")
        }

        saveFile()
    }

    fun deleteEntry(i: Int) {
        if(i+1<list.size) {
            list.removeAt(i)

            saveFile()

            val currentScreen = MinecraftClient.getInstance().currentScreen as MainOptionsScreen
            currentScreen.allElements.removeLast()
            currentScreen.allElements.add(getElementsFromFile(file,this))
        }
    }

    private fun getElementsFromFile(file: String, element: MacroGroupElement): ArrayList<MainOptionsElement> {
        val f = File(FabricLoader.getInstance().gameDir.toString()+"\\macros\\"+ file)
        val a: ArrayList<MainOptionsElement> = arrayListOf()
        try {
            val s = Scanner(f)

            var i = -1
            while(s.hasNextLine()) {
                val t = s.nextLine()
                if(t!="")
                    a.add(MacroElement(t,++i,element))
            }
            a.add(MacroElement("",++i,element))
        } catch (_: Exception){}
        return a
    }

    private fun getTextFromFile(file: String): ArrayList<String> {
        val f = File(FabricLoader.getInstance().gameDir.toString()+"\\macros\\"+ file)
        val a: ArrayList<String> = arrayListOf()
        try {
            val s = Scanner(f)

            while(s.hasNextLine()) {
                val t = s.nextLine()
                if(t!="")
                    a.add(t)
            }
            a.add("")
        } catch (_: Exception){}
        return a
    }
}