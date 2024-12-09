package bewis09.bewisclient.drawable

import bewis09.bewisclient.screen.MainOptionsScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import net.minecraft.util.Util
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * A ButtonWidget that allows for scaling in the [MainOptionsScreen]
 */
open class ScalableButtonWidget(x: Int, y: Int, width: Int, height: Int, message: Text?, onPress: PressAction?) : ButtonWidget(x, y, width, height, message, onPress, null) {

    override fun drawScrollableText(context: DrawContext?, textRenderer: TextRenderer?, xMargin: Int, color: Int) {
        val i = this.x + xMargin
        val j = this.x + this.getWidth() - xMargin
        drawScrollableTextT(context, textRenderer, this.message, i, this.y, j, this.y + this.getHeight(), color)
    }

    override fun toString(): String {
        return "§disable_scissors"
    }

    /**
     * Copied from [ClickableWidget]
     */
    private fun drawScrollableTextT(context: DrawContext?, textRenderer: TextRenderer?, text: Text?, startX: Int, startY: Int, endX: Int, endY: Int, color: Int) {
        this.drawScrollableTextT(context!!, textRenderer!!, text, (startX + endX) / 2, startX, startY, endX, endY, color)
    }

    /**
     * Copied from [ClickableWidget]
     */
    private fun drawScrollableTextT(context: DrawContext, textRenderer: TextRenderer, text: Text?, centerX: Int, startX: Int, startY: Int, endX: Int, endY: Int, color: Int) {
        val i = textRenderer.getWidth(text)
        val j = (startY + endY - textRenderer.fontHeight) / 2 + 1
        val k = endX - startX
        if (i > k) {
            val l = i - k
            val d = Util.getMeasuringTimeMs().toDouble() / 1000.0
            val e = max(l.toDouble() * 0.5, 3.0)
            val f = sin(1.5707963267948966 * cos(Math.PI * 2 * d / e)) / 2.0 + 0.5
            val g = MathHelper.lerp(f, 0.0, l.toDouble())
            context.enableScissor(
                    (startX/ MainOptionsScreen.scale).toInt(),
                    (startY/ MainOptionsScreen.scale).toInt(),
                    (endX/ MainOptionsScreen.scale).toInt(),
                    (endY/ MainOptionsScreen.scale).toInt())
            context.drawTextWithShadow(textRenderer, text, startX - g.toInt(), j, color)
            context.disableScissor()
        } else {
            val l = MathHelper.clamp(centerX, startX + i / 2, endX - i / 2)
            context.drawCenteredTextWithShadow(textRenderer, text, l, j, color)
        }
    }

    /**
     * A builder for the [ScalableButtonWidget] just like [ButtonWidget.Builder]
     */
    @Environment(value = EnvType.CLIENT)
    class Builder(private val message: Text, private val onPress: PressAction) {
        private var tooltip: Tooltip? = null
        private var x = 0
        private var y = 0
        private var width = 150
        private var height = 20

        fun width(width: Int): Builder {
            this.width = width
            return this
        }

        fun size(width: Int, height: Int): Builder {
            this.width = width
            this.height = height
            return this
        }

        fun dimensions(x: Int, y: Int, width: Int, height: Int): Builder {
            return this.position(x, y).size(width, height)
        }

        fun position(x: Int, y: Int): Builder {
            this.x = x
            this.y = y
            return this
        }

        fun build(): ButtonWidget {
            val buttonWidget = ScalableButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress)
            buttonWidget.tooltip = tooltip
            return buttonWidget
        }
    }

    companion object {
        /**
         * @return A newly created [Builder]
         */
        fun builder(message: Text?, onPress: PressAction?): Builder {
            return Builder(message!!, onPress!!)
        }
    }
}