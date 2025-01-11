package bewis09.bewisclient.cosmetics

object Cosmetics {
    val capes = CosmeticsType("cape")
    val wings = CosmeticsType("wing")
    val hats = CosmeticsType("hat")

    val types = arrayListOf(capes, wings, hats)

    fun getCosmeticsType(typeId: String): CosmeticsType {
        return types.first {
            return@first typeId == it.typeId
        }
    }

    fun register() {
        registerCosmetic(Cosmetic(capes, "world"))
        registerCosmetic(Cosmetic(capes, "portal"))
        registerCosmetic(Cosmetic(capes, "creaking"))
        registerCosmetic(Cosmetic(capes, "breeze"))
        registerCosmetic(Cosmetic(capes, "minecon2011"))
        capes.registerCosmetic("golden_creeper", AnimatedCape(capes, "golden_creeper", 32, 80))

        registerCosmetic(Cosmetic(wings, "ender_dragon"))
        registerCosmetic(Cosmetic(wings, "fire"))

        registerCosmetic(Cosmetic(hats, "technoblade"))
        registerCosmetic(Cosmetic(hats, "christmas"))
        registerCosmetic(Cosmetic(hats, "headphone"))
    }

    fun registerCosmetic(cosmetic: Cosmetic) {
        cosmetic.type.registerCosmetic(cosmetic.id,cosmetic)
    }
}