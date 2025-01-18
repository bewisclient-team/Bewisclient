package bewis09.bewisclient.util

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URI
import javax.imageio.ImageIO


object Util {
    fun isIn(mouseX: Number, mouseY: Number, x1: Int, y1: Int, x2: Int, y2: Int) = mouseX.toDouble()>=x1&&mouseX.toDouble()<=x2&&mouseY.toDouble()>=y1&&mouseY.toDouble()<=y2

    fun downloadToFile(address: String, file: File) {
        val url = URI(address).toURL()
        val connection = url.openConnection()

        connection.connect()

        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeBytes(connection.getInputStream().readAllBytes())
    }

    @Throws(IOException::class)
    fun getFrames(input: File?): ArrayList<BufferedImage> {
        val reader = ImageIO.getImageReadersByFormatName("gif").next()
        val stream = ImageIO.createImageInputStream(input)
        val frames = arrayListOf<BufferedImage>()

        reader.input = stream

        val count = reader.getNumImages(true)
        for (index in 0..<count) {
            val frame = reader.read(index)

            frames.add(frame)
        }

        return frames
    }
}

fun DrawContext.drawTexture(
    sprite: Identifier?,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    this.drawTexture(
        { texture: Identifier? ->
            RenderLayer.getGuiTexturedOverlay( texture )
        },
        sprite,
        x,
        y,
        0f,
        0f,
        width,
        height,
        width,
        height,
        width,
        height
    )
}