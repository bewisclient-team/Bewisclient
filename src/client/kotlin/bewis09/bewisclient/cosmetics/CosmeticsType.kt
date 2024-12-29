package bewis09.bewisclient.cosmetics

import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

open class CosmeticsType(val typeId: String) {
    val cosmetics = hashMapOf<String, Cosmetic>()
    var currentlySelected: String? = null
    var currentOverwrite: Cosmetic? = null

    fun registerCosmetic(id: String, cosmetic: Cosmetic) {
        cosmetics[id] = cosmetic
    }

    fun getTexture(): Identifier? {
        if(currentOverwrite != null)
            return currentOverwrite?.getTexture()
        return cosmetics[currentlySelected]?.getTexture()
    }

    fun getTexture(name: String): Identifier? {
        if(currentOverwrite != null)
            return currentOverwrite?.getTexture()
        if(MinecraftClient.getInstance().gameProfile.name == name)
            return cosmetics[currentlySelected]?.getTexture()
        return null
    }
}