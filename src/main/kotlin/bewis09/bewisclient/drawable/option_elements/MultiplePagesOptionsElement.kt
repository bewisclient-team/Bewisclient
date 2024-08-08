package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.Search
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.OrderedText
import net.minecraft.util.Identifier
import java.util.Arrays
import kotlin.math.ceil

class MultiplePagesOptionsElement(val elementList: Array<MultiplePagesElement>): MainOptionsElement("","",Identifier.of("")) {
    var hoveredElement = -1

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val elementsPerRow = (width+4)/70
        val elementWidth: Int = (width+4)/elementsPerRow-4
        val elementWidthFloat: Float = ((width+4).toFloat())/elementsPerRow

        hoveredElement = -1

        elementList.forEachIndexed{ i: Int, multiplePagesElement: MultiplePagesElement ->
            val inline = i%elementsPerRow
            val line = i/elementsPerRow

            val isHovered = mouseX>x+(elementWidthFloat*inline)&&mouseX<x+(elementWidthFloat*inline)+elementWidth&&mouseY>y+line*84&&mouseY<y+line*84+80

            if(isHovered) hoveredElement = i

            context.matrices.push()

            context.matrices.translate((elementWidthFloat*inline),0f,0f)

            context.setShaderColor(1f,1f,1f, ((alphaModifier.toFloat()/0xFFFFFFFF)))

            if(isHovered) {
                context.matrices.translate(x.toFloat(),y.toFloat()+line*84,0f)
                context.matrices.scale((1+2f/elementWidth),
                    (1+2f/elementWidth),0f)
                context.matrices.translate(-x.toFloat()-1,-y.toFloat()-line*84-1,0f)
            }

            context.fill(x,y+line*84,x+elementWidth,y+line*84+80, 0xFF000000.toInt())
            context.drawBorder(x,y+line*84,elementWidth,80, if(isHovered) (alphaModifier + 0xAAAAFF).toInt() else 0xFFFFFFFF.toInt())

            RenderSystem.enableBlend()
            context.drawTexture(multiplePagesElement.image,x+elementWidth/2-16,y+line*84+10,32,32,0f,0f,32,31,32,32)
            RenderSystem.disableBlend()

            val l = MinecraftClient.getInstance().textRenderer.wrapLines(Bewisclient.getTranslationText(multiplePagesElement.title),elementWidth-8)
            l.forEachIndexed{ i1: Int, orderedText: OrderedText ->
                context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText,x+elementWidth/2,y+line*84+50+i1*9+9-(l.size*4.5).toInt(),-1)
            }

            context.setShaderColor(1f,1f,1f,1f)

            context.matrices.pop()
        }

        return ceil(elementList.size/(elementsPerRow.toDouble())).toInt()*84-4
    }

    class MultiplePagesElement(val title: String, val elements: ArrayList<MainOptionsElement>, val image: Identifier): Search.SearchableElement<MultiplePagesElement> {
        override fun getSearchKeywords(): ArrayList<String> {
            return arrayListOf(Bewisclient.getTranslatedString(title))
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(hoveredElement!=-1) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.openNewSlice(elementList[hoveredElement].elements)
        }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }

    override fun getAdditionalElementsWithKeyword(): (String) -> ArrayList<MainOptionsElement>? {
        val collection = Search.collect((elementList.toList()))

        return {
            arrayListOf(MultiplePagesOptionsElement(Search.search(collection,it).toArray(arrayOf())))
        }
    }

    override fun getChildElementsForSearch(): ArrayList<MainOptionsElement> {
        val l = arrayListOf<MainOptionsElement>()

        elementList.forEach {
            l.addAll(it.elements)
        }

        return l
    }
}