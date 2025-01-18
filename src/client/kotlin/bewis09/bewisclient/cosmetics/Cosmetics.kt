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

    fun registerCosmetic(cosmetic: Cosmetic, default: Boolean) {
        cosmetic.type.registerCosmetic(cosmetic.id,cosmetic,default)
    }
}