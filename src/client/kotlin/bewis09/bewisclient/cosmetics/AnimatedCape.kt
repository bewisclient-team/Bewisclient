package bewis09.bewisclient.cosmetics

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class AnimatedCape(type: CosmeticsType, id: String, texture: ByteArray) : Cosmetic(type, id, texture) {
    val identifiers = arrayListOf<Identifier>()

    override fun getTexture(): Identifier {
        if (!initialized) {
            val gif = bewis09.bewisclient.util.Util.getFrames(texture)

            gif.forEachIndexed { i, image ->
                val baos = ByteArrayOutputStream()

                ImageIO.write(image, "png", baos)

                val bytes = baos.toByteArray()
                val identifier = Identifier.of("bewisclient", "cosmetic_" + type.typeId + "_" + id + "_" + i)

                MinecraftClient.getInstance().textureManager.registerTexture(
                    identifier,
                    NativeImageBackedTexture(identifier::toString, NativeImage.read(bytes))
                )
            }

            for (a in 0 until gif.size) {
                identifiers.add(Identifier.of("bewisclient", "cosmetic_${type.typeId}_${id}_$a"))
            }

            initialized = true
        }

        return identifiers[(System.currentTimeMillis() / 80 % identifiers.size).toInt()]
    }
}