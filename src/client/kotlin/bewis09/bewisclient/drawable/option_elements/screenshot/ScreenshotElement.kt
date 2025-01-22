package bewis09.bewisclient.drawable.option_elements.screenshot

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.util.JustTextOptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.drawTexture
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.loader.api.FabricLoader
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

class ScreenshotElement: OptionElement("","") {
    class SizedIdentifier(val identifier: Identifier, val width: Int, val height: Int, val name: String)

    var hoveredShot = -1

    companion object {
        val screenshots: ArrayList<SizedIdentifier> = arrayListOf()
            get() {
                if(!loaded) {
                    loaded = true

                    Util.getIoWorkerExecutor().execute {
                        val s = arrayListOf<SizedIdentifier>()

                        for (f in File(FabricLoader.getInstance().gameDir.toString() + "\\screenshots").listFiles() ?: arrayOf()) {
                            try {
                                addNew = true
                                val n = NativeImage.read(FileInputStream(f))

                                MinecraftClient.getInstance().textureManager.registerTexture(
                                    Identifier.of("bewisclient","screenshot_" + ((++id).toString())),
                                    NativeImageBackedTexture(n)
                                )

                                val a = SizedIdentifier(
                                    Identifier.of("bewisclient", "screenshot_$id"), n.width, n.height, f.name
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
        alphaModifier: Long
    ): Int {
        hoveredShot = -1

        RenderSystem.setShaderColor(1f,1f,1f, ((alphaModifier/0x1000000)/255f))

        val columns = floor(width/120f)
        val img_width = (width - 6 * columns + 6)/columns
        val img_height = img_width * 9 / 16

        screenshots.reversed().forEachIndexed { index, it ->
            val row = floor(index/columns)
            val column = floor(index%columns)

            val startX = (x + column/columns*(width - 6 * columns + 6) + 6 * column).toInt()
            val startY = (y + row * (img_height + 20)).toInt()

            var i_width: Float = it.width.toFloat()
            var i_height: Float = it.height.toFloat()

            if(i_width*9 > i_height*16) {
                i_width = img_width
                i_height *= img_width/it.width.toFloat()
            } else {
                i_height = img_height
                i_width *= img_height/it.height.toFloat()
            }

            val hovered = mouseX>startX &&
                    mouseX<startX + img_width.toInt() &&
                    mouseY>startY &&
                    mouseY<(startY + img_height).toInt()

            if(hovered)
                hoveredShot = index

            context.matrices.push()

            if(hovered) {
                val scale = 1+2/i_width

                context.matrices.scale(scale, scale, scale)

                context.matrices.translate(
                    ((startX*(1/scale-1))-1).toDouble(),
                    (startY*(1/scale-1)-i_height/i_width).toDouble(),
                    0.0
                )
            }

            RenderSystem.enableBlend()

            context.drawTexture(it.identifier, startX + ((img_width - i_width) / 2).toInt(), startY + ((img_height - i_height) / 2).toInt(), i_width.toInt(),
                i_height.toInt()
            )

            RenderSystem.disableBlend()

            context.matrices.pop()

            context.matrices.push()

            context.matrices.translate(
                (startX + img_width/2).toDouble(),
                (startY + img_height).toDouble(),
                0.0
            )

            context.matrices.scale(0.7f, 0.7f, 0.7f)

            context.matrices.translate(
                -(startX + img_width/2).toDouble(),
                -(startY + img_height).toDouble(),
                0.0
            )

            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,it.name,(startX + img_width/2).toInt(),(startY + img_height + 5).toInt(),-1)

            context.matrices.pop()
        }
        RenderSystem.setShaderColor(1f,1f,1f, 1f)

        return ((img_height + 20) * ceil(screenshots.size/columns) - 6).roundToInt()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(hoveredShot != -1) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            screen.openNewSlice(arrayOf(
                JustTextOptionElement(screenshots.reversed()[hoveredShot].name),
                SingleScreenshotElement(screenshots.reversed()[hoveredShot])
            )
            )
        }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}