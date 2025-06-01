package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.BooleanSetting
import bewis09.bewisclient.util.*
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.OrderedText
import net.minecraft.util.Identifier
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * An [OptionElement] for displaying multiple elements in a grid
 *
 * @param elementList An [Array] of the [MultiplePagesElement] that should be displayed
 * @param minElementWidth The minimum width of a single [MultiplePagesElement]
 */
class MultiplePagesOptionElement(val elementList: Array<MultiplePagesElement>, val minElementWidth: Int) : OptionElement("", "") {

    /**
     * The index of the [MultiplePagesElement] that is currently hovered over
     */
    var hoveredElement = -1

    /**
     * The Array of [ValuedAnimation] for the scaling when hovered
     */
    var animation: Array<ValuedAnimation> = Array(elementList.size) {
        ValuedAnimation(System.currentTimeMillis(), options_menu.animation_time.get().roundToLong() / 2, EaseMode.CONST, 0f, 0f)
    }

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
        alpha: Float
    ): Int {
        val elementsPerRow = (width + 4) / minElementWidth
        val elementWidth: Int = (width + 4) / elementsPerRow - 4
        val elementWidthFloat: Float = ((width + 4).toFloat()) / elementsPerRow

        hoveredElement = -1
        widgetHoveredElement = -1

        val height = 80

        elementList.forEachIndexed { i: Int, multiplePagesElement: MultiplePagesElement ->
            context.matrices.push()

            context.matrices.translate(x + (elementWidthFloat * (i % elementsPerRow)), y + (i / elementsPerRow) * height + 2f, 0f)

            multiplePagesElement.onRender(
                context,
                (x + (elementWidthFloat * (i % elementsPerRow))).roundToInt(),
                (y + (i / elementsPerRow) * height + 2f).roundToInt(),
                elementWidth,
                height,
                mouseX,
                mouseY,
                alpha
            )

            context.matrices.pop()
        }

        context.fill(0, 0, 0, 0, -1)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)

        return ceil(elementList.size / (elementsPerRow.toDouble())).toInt() * height - 4
    }

    class DescriptionedMultiplePagesElement(elements: Array<OptionElement>, val settings: BooleanSetting) : MultiplePagesElement(elements) {
        val title: String = settings.createOptionElement().title
        val description: String = settings.createOptionElement().description
        val id: String = settings.id

        override fun render(context: DrawContext, width: Int, height: Int, mouseX: Int, mouseY: Int, alpha: Float) {
            val l = MinecraftClient.getInstance().textRenderer.wrapLines(
                Bewisclient.getTranslationText(title),
                width
            )
            l.forEachIndexed { i1: Int, orderedText: OrderedText ->
                context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    orderedText,
                    width / 2,
                    6 + i1 * 9 + 9 - (l.size * 4.5).toInt(),
                    applyAlpha(0xFFFFFF, alpha)
                )
            }

            val d = MinecraftClient.getInstance().textRenderer.wrapLines(
                Bewisclient.getTranslationText(this.description), width
            )
            d.forEachIndexed { i1: Int, orderedText: OrderedText ->
                context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    orderedText,
                    width / 2,
                    40 + i1 * 9 + 9 - (d.size * 4.5).toInt(),
                    applyAlpha(0xAAAAAA, alpha)
                )
            }
        }

        override fun getSearchKeywords(): Array<String> {
            return arrayOf(title)
        }
    }

    class ImagedMultiplePagesElement : MultiplePagesElement {
        val settings: BooleanSetting?
        val title: String
        val id: String

        constructor(elements: Array<OptionElement>, settings: BooleanSetting) : super(elements) {
            this.settings = settings
            this.title = settings.createOptionElement().title
            this.id = settings.id
        }

        constructor(elements: Array<OptionElement>, id: String) : super(elements) {
            this.settings = null
            this.title = "setting.$id"
            this.id = id
        }

        override fun render(context: DrawContext, width: Int, height: Int, mouseX: Int, mouseY: Int, alpha: Float) {
            context.drawTexture(
                Identifier.of("bewisclient", "textures/main_icons/${id}.png"),
                width / 2 - 16,
                10,
                32,
                32,
            )
            RenderSystem.setShaderColor(1f, 1f, 1 - animation.getValue() / 6f, alpha)

            val l = MinecraftClient.getInstance().textRenderer.wrapLines(
                Bewisclient.getTranslationText(this.title),
                width - 8
            )

            l.forEachIndexed { i1: Int, orderedText: OrderedText ->
                context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    orderedText,
                    width / 2,
                    53 + i1 * 9 + 9 - (l.size * 4.5).toInt(),
                    applyAlpha(0xFFFFFF, alpha)
                )
            }
        }

        override fun getSearchKeywords(): Array<String> {
            return arrayOf(title)
        }
    }

    abstract class MultiplePagesElement(val elements: Array<OptionElement>) : Search.SearchableElement<MultiplePagesElement> {
        var x = 0
        var y = 0
        var width = 0
        var height = 0

        var animation = ValuedAnimation(System.currentTimeMillis(), options_menu.animation_time.get().roundToLong() / 2, EaseMode.CONST, 0f, 0f)

        fun onRender(context: DrawContext, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, alpha: Float) {
            this.width = width
            this.height = height
            this.x = x
            this.y = y

            val isHovered = mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height

            if (isHovered != (animation.endValue == 3f)) {
                animation = ValuedAnimation(
                    System.currentTimeMillis(),
                    options_menu.animation_time.get().roundToLong() / 3,
                    EaseMode.EASE_IN_OUT,
                    animation.getValue(),
                    if (isHovered) 3f else 0f
                )
            }

            context.matrices.scale(
                (1 + animation.getValue() / width * 2),
                (1 + animation.getValue() / width * 2), 0f
            )
            context.matrices.translate(-animation.getValue(), -animation.getValue(), 0f)
            render(context, width, height, mouseX - x, mouseY - y, alpha)
        }

        abstract fun render(context: DrawContext, width: Int, height: Int, mouseX: Int, mouseY: Int, alpha: Float)

        open fun mouseClicked(mouseX: Double, mouseY: Double, screen: MainOptionsScreen) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height) {
                screen.openNewSlice(elements)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        elementList.forEach { it.mouseClicked(mouseX, mouseY, screen) }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }

    override fun getElementByKeywordLamba(): (String) -> OptionElement? {
        val collection = Search.collect((elementList))

        return {
            val results = Search.search(it, collection)

            if (results.isNotEmpty())
                MultiplePagesOptionElement(results.toArray(arrayOf()), minElementWidth)
            else
                null
        }
    }

    override fun getChildElementsForSearch(): Array<OptionElement> {
        return elementList.reduce({ acc, multiplePagesElement -> acc.also { it.addAll(multiplePagesElement.elements) } }, arrayListOf<OptionElement>()).toTypedArray()
    }
}