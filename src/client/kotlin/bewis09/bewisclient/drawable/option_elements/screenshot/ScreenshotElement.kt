package bewis09.bewisclient.drawable.option_elements.screenshot

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.util.JustTextOptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.applyAlpha
import bewis09.bewisclient.util.drawTexture
import bewis09.bewisclient.util.getRelativeGameFile
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.io.File
import java.io.FileInputStream
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

class ScreenshotElement : OptionElement("", "") {
    class SizedIdentifier(val identifier: Identifier, val width: Int, val height: Int, val name: String, var initialized: Boolean, val nativeImage: NativeImage) {
        fun getInitializedIdentifier(): Identifier {
            if (!initialized) {
                initialized = true
                MinecraftClient.getInstance().textureManager.registerTexture(
                    identifier,
                    NativeImageBackedTexture(identifier::toString, nativeImage)
                )
            }
            return identifier
        }
    }

    var hoveredShot = -1

    companion object {
        fun addScreenshot(f: File) {
            try {
                addNew = true
                val n = NativeImage.read(FileInputStream(f))
                val identifier = Identifier.of("bewisclient", "screenshot_" + ((++id).toString()))

                val a = SizedIdentifier(
                    identifier, n.width, n.height, f.name, false, n
                )

                screenshots.add(a)
            } catch (_: Exception) {}
        }

        val screenshots: ArrayList<SizedIdentifier> = arrayListOf()
            get() {
                if (!loaded) {
                    loaded = true

                    Util.getIoWorkerExecutor().execute {
                        val s = arrayListOf<SizedIdentifier>()

                        for (f in getRelativeGameFile("screenshots").listFiles() ?: arrayOf()) {
                            try {
                                addNew = true
                                val n = NativeImage.read(FileInputStream(f))
                                val identifier = Identifier.of("bewisclient", "screenshot_" + ((++id).toString()))

                                val a = SizedIdentifier(
                                    identifier, n.width, n.height, f.name, false, n
                                )

                                s.add(a)
                            } catch (_: Exception) {}
                        }

                        screenshots.addAll(s)
                    }
                }

                return field
            }

        var loaded = false

        var addNew = false

        var id = 0
    }

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alpha: Float
    ): Int {
        hoveredShot = -1

        val columns = floor(width / 120f)
        val imgWidth = (width - 6 * columns + 6) / columns
        val imgHeight = imgWidth * 9 / 16

        screenshots.reversed().forEachIndexed { index, it ->
            val row = floor(index / columns)
            val column = floor(index % columns)

            val startX = (x + column / columns * (width - 6 * columns + 6) + 6 * column).toInt()
            val startY = (y + row * (imgHeight + 20)).toInt()

            var iWidth: Float = it.width.toFloat()
            var iHeight: Float = it.height.toFloat()

            if (iWidth * 9 > iHeight * 16) {
                iWidth = imgWidth
                iHeight *= imgWidth / it.width.toFloat()
            } else {
                iHeight = imgHeight
                iWidth *= imgHeight / it.height.toFloat()
            }

            val hovered = mouseX > startX &&
                    mouseX < startX + imgWidth.toInt() &&
                    mouseY > startY &&
                    mouseY < (startY + imgHeight).toInt()

            if (hovered)
                hoveredShot = index

            context.matrices.push()

            if (hovered) {
                val scale = 1 + 2 / iWidth

                context.matrices.scale(scale, scale, scale)

                context.matrices.translate(
                    ((startX * (1 / scale - 1)) - 1).toDouble(),
                    (startY * (1 / scale - 1) - iHeight / iWidth).toDouble(),
                    0.0
                )
            }

            context.drawTexture(
                it.getInitializedIdentifier(), startX + ((imgWidth - iWidth) / 2).toInt(), startY + ((imgHeight - iHeight) / 2).toInt(), iWidth.toInt(),
                iHeight.toInt(),
                alpha = alpha
            )

            context.matrices.pop()

            context.matrices.push()

            context.matrices.translate(
                (startX + imgWidth / 2).toDouble(),
                (startY + imgHeight).toDouble(),
                0.0
            )

            context.matrices.scale(0.7f, 0.7f, 0.7f)

            context.matrices.translate(
                -(startX + imgWidth / 2).toDouble(),
                -(startY + imgHeight).toDouble(),
                0.0
            )

            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, it.name, (startX + imgWidth / 2).toInt(), (startY + imgHeight + 5).toInt(), applyAlpha(0xFFFFFF, alpha))

            context.matrices.pop()
        }

        return ((imgHeight + 20) * ceil(screenshots.size / columns) - 6).roundToInt()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (hoveredShot != -1) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            screen.openNewSlice(
                arrayOf(
                    JustTextOptionElement(screenshots.reversed()[hoveredShot].name),
                    SingleScreenshotElement(screenshots.reversed()[hoveredShot])
                )
            )
        }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}