package bewis09.bewisclient.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URI
import java.util.function.Predicate
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

    /**
     * @param versionConsumer A function which has an [Int] as an argument, which is positive if the current version is bigger than the specified one, negative if it is smaller and 0 if those are equal
     */
    fun <T> modFoundDependent(id: String, version: String, versionConsumer: Predicate<Int>, onTrue: ()->T, onFalse: ()->T): T {
        return if (FabricLoader.getInstance().allMods.any {
                return@any it.metadata.id == id && versionConsumer.test(compareVersion(it.metadata.version.friendlyString, version))
            }) onTrue() else onFalse()
    }

    fun compareVersion(version1: String, version2: String): Int {
        val v1 = version1.split(".")
        val v2 = version2.split(".")

        val maxLength = maxOf(v1.size, v2.size)

        for (i in 0 until maxLength) {
            val num1 = if (i < v1.size) v1[i].toInt() else 0
            val num2 = if (i < v2.size) v2[i].toInt() else 0

            if (num1 != num2) {
                return num1.compareTo(num2)
            }
        }

        return 0
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

inline fun <S, T> Array<out T>.reduce(operation: (acc: S, T) -> S, initial_accumulator: S): S {
    if (isEmpty())
        throw UnsupportedOperationException("Empty array can't be reduced.")
    var accumulator: S = initial_accumulator
    for (index in 1..lastIndex) {
        accumulator = operation(accumulator, this[index])
    }
    return accumulator
}