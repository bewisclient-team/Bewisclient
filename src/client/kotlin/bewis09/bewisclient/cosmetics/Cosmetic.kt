package bewis09.bewisclient.cosmetics

import net.minecraft.util.Identifier

open class Cosmetic(val type: CosmeticsType, val id: String) {
    open fun getTexture(): Identifier? {
        return Identifier.of("bewisclient","cosmetic_"+type.typeId+"_"+id)
    }
}