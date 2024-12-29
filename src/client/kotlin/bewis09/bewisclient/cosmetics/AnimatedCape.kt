package bewis09.bewisclient.cosmetics

import net.minecraft.util.Identifier

class AnimatedCape(type: CosmeticsType, id: String, val frames: Int, val length: Int): Cosmetic(type, id) {
    val identifiers = arrayListOf<Identifier>()

    init {
        for (a in 0 until frames) {
            identifiers.add(Identifier.of("bewisclient", "cosmetics/${type.typeId}/${id}_$a.png"))
        }
    }

    override fun getTexture(): Identifier {
        return identifiers[(System.currentTimeMillis() / length % frames).toInt()]
    }
}