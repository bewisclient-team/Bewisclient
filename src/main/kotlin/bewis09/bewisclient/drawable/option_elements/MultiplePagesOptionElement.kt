package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.MultiplePagesOptionElement.MultiplePagesElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.Search
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.OrderedText
import net.minecraft.util.Identifier
import kotlin.math.ceil

/**
 * An [OptionElement] for displaying multiple elements in a grid
 *
 * @param elementList An [Array] of the [MultiplePagesElement] that should be displayed
 * @param minElementWidth The minimum width of a single [MultiplePagesElement]
 * @param widgetEnableSetter Indicates if a button to change the [bewis09.bewisclient.settingsLoader.Settings.ENABLED] state of a widget should be displayed
 */
class MultiplePagesOptionElement(val elementList: Array<MultiplePagesElement>, val minElementWidth: Int, val widgetEnableSetter: Boolean): OptionElement("","") {

    /**
     * The index of the [MultiplePagesElement] that is currently hovered over
     */
    var hoveredElement = -1

    /**
     * The index of the [MultiplePagesElement] on which the button to enable/disable the widget is hovered
     */
    var widgetHoveredElement = -1

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val elementsPerRow = (width+4)/minElementWidth
        val elementWidth: Int = (width+4)/elementsPerRow-4
        val elementWidthFloat: Float = ((width+4).toFloat())/elementsPerRow

        hoveredElement = -1
        widgetHoveredElement = -1

        val height = if(widgetEnableSetter) 100 else 84

        elementList.forEachIndexed{ i: Int, multiplePagesElement: MultiplePagesElement ->
            val hasImage = multiplePagesElement.image!=null

            val inline = i%elementsPerRow
            val line = i/elementsPerRow

            val isHovered = mouseX>x+(elementWidthFloat*inline)&&mouseX<x+(elementWidthFloat*inline)+elementWidth&&mouseY>y+line*height&&mouseY<y+line*height+height-4-(if(widgetEnableSetter) 14 else 0)

            if(isHovered) hoveredElement = i

            context.matrices.push()

            context.matrices.translate((elementWidthFloat*inline),0f,0f)

            context.setShaderColor(1f,1f,1f, ((alphaModifier.toFloat()/0xFFFFFFFF)))

            if(isHovered) {
                context.matrices.translate(x.toFloat(),y.toFloat()+line*height,0f)
                context.matrices.scale((1+2f/elementWidth),
                    (1+2f/elementWidth),0f)
                context.matrices.translate(-x.toFloat()-1,-y.toFloat()-line*height-1,0f)
            }

            context.fill(x,y+line*height,x+elementWidth,y+line*height+height-4, 0xFF000000.toInt())
            context.drawBorder(x,y+line*height,elementWidth,height-4, if(isHovered) (alphaModifier + 0xAAAAFF).toInt() else 0xFFFFFFFF.toInt())

            if(hasImage) {
                RenderSystem.enableBlend()
                context.drawTexture(
                    multiplePagesElement.image,
                    x + elementWidth / 2 - 16,
                    y + line * height + 10,
                    32,
                    32,
                    0f,
                    0f,
                    32,
                    31,
                    32,
                    32
                )
                RenderSystem.disableBlend()
            }

            val l = MinecraftClient.getInstance().textRenderer.wrapLines(Bewisclient.getTranslationText(multiplePagesElement.title),elementWidth-8)
            l.forEachIndexed{ i1: Int, orderedText: OrderedText ->
                context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText,x+elementWidth/2,y+line*height+(if(hasImage) 50 else 6)+i1*9+9-(l.size*4.5).toInt(),-1)
            }

            if(!hasImage) {
                val d = MinecraftClient.getInstance().textRenderer.wrapLines(Bewisclient.getTranslationText(multiplePagesElement.description!!),elementWidth-8)
                d.forEachIndexed{ i1: Int, orderedText: OrderedText ->
                    context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText,x+elementWidth/2,y+line*height+(40)+i1*9+9-(d.size*4.5).toInt(),0xFFAAAAAA.toInt())
                }
            }

            if(widgetEnableSetter) {
                val isWidgetHovered = mouseX>x+(elementWidthFloat*inline)&&mouseX<x+(elementWidthFloat*inline)+elementWidth&&mouseY>y+line*height+height-19&&mouseY<y+line*height+height-4

                val enabled = (SettingsLoader.get("widgets", ENABLED, multiplePagesElement.title.replace("widgets.","")))

                if(!isWidgetHovered) {
                    context.fill(
                        x,
                        y + line * height + 80 + 2,
                        x + elementWidth,
                        y + line * height + 80 + 16,
                        (alphaModifier + (if (enabled) 0x44BB44 else 0xFF0000)).toInt()
                    )
                } else {
                    widgetHoveredElement = i

                    context.fill(
                        x,
                        y + line * height + 80 + 2,
                        x + elementWidth,
                        y + line * height + 80 + 16,
                        (alphaModifier + (if (enabled) 0x226022 else 0x800000)).toInt()
                    )
                }
                context.drawBorder(
                    x,
                    y+line*height+80 + 2,
                    elementWidth,
                    14,
                    (alphaModifier + (if(isWidgetHovered) 0xAAAAFF else 0xFFFFFF)).toInt()
                )

                context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    if (enabled) Bewisclient.getTranslatedString("enabled") else Bewisclient.getTranslatedString("disabled"),
                    x + elementWidth/2,
                    y+line*height+80 + 5,
                    (alphaModifier + 0xFFFFFF).toInt()
                )
            }

            context.setShaderColor(1f,1f,1f,1f)

            context.matrices.pop()
        }

        return ceil(elementList.size/(elementsPerRow.toDouble())).toInt()*height-4
    }

    /**
     * The class of the individual elements
     */
    class MultiplePagesElement : Search.SearchableElement<MultiplePagesElement> {

        /**
         * The title of the [MultiplePagesElement]
         */
        val title: String

        /**
         * The elements that should be shown when clicking on the [MultiplePagesElement]
         */
        val elements: ArrayList<OptionElement>

        /**
         * The image that should be rendered in the [MultiplePagesElement]
         */
        val image: Identifier?

        /**
         * The description of the [MultiplePagesElement]
         */
        val description: String?

        /**
         * You can either add an image or a description
         *
         * @param title The title of the [MultiplePagesElement]
         * @param elements The elements that should be shown when clicking on the [MultiplePagesElement]
         * @param image The image that should be rendered in the [MultiplePagesElement]
         */
        constructor(title: String, elements: ArrayList<OptionElement>, image: Identifier) {
            this.title = title
            this.elements = elements
            this.image = image
            this.description = null
        }

        /**
         * You can either add an image or a description
         *
         * @param title The title of the [MultiplePagesElement]
         * @param elements The elements that should be shown when clicking on the [MultiplePagesElement]
         * @param description The description of the [MultiplePagesElement]
         */
        constructor(title: String, elements: ArrayList<OptionElement>, description: String) {
            this.title = title
            this.elements = elements
            this.image = null
            this.description = description
        }

        override fun getSearchKeywords(): ArrayList<String> {
            return arrayListOf(Bewisclient.getTranslatedString(title))
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(hoveredElement!=-1) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.openNewSlice(elementList[hoveredElement].elements)
        }
        if(widgetHoveredElement!=-1) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            val enabled = (SettingsLoader.get("widgets",ENABLED,
                elementList[widgetHoveredElement].title.replace("widgets.","")
            ))
            SettingsLoader.set("widgets", !enabled,ENABLED,
                elementList[widgetHoveredElement].title.replace("widgets.","")
            )
        }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }

    override fun getElementByKeywordLamba(): (String) -> OptionElement? {
        val collection = Search.collect((elementList.toList()))

        return {
            val results = Search.search(it,collection)

            if(results.isNotEmpty())
                MultiplePagesOptionElement(results.toArray(arrayOf()),minElementWidth,widgetEnableSetter)
            else
                null
        }
    }

    override fun getChildElementsForSearch(): ArrayList<OptionElement> {
        val l = arrayListOf<OptionElement>()

        elementList.forEach {
            l.addAll(it.elements)
        }

        return l
    }
}