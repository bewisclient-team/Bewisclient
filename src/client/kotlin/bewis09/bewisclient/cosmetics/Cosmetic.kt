package bewis09.bewisclient.cosmetics

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier

open class Cosmetic(val type: CosmeticsType, val id: String, val texture: ByteArray) {
    var initialized = false

    open fun getTexture(): Identifier? {
        if (!initialized) {
            val identifier = Identifier.of("bewisclient", "cosmetic_" + type.typeId + "_" + id)

            MinecraftClient.getInstance().textureManager.registerTexture(
                identifier,
                NativeImageBackedTexture(identifier::toString, NativeImage.read(texture))
            )

            initialized = true
        }

        return Identifier.of("bewisclient", "cosmetic_${type.typeId}_${id}")
    }
}