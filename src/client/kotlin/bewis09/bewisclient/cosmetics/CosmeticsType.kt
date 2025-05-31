package bewis09.bewisclient.cosmetics

import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

open class CosmeticsType(val typeId: String) {
    val cosmetics = sortedMapOf<String, Cosmetic>()
    val defaultCosmetics = sortedMapOf<String, Cosmetic>()

    var currentlySelected: String? = try { SettingsLoader.get(Settings.DESIGN, typeId, arrayOf(), JsonPrimitive("")).asString } catch (e: Exception) { null }
        set(value) {
            field = value
            SettingsLoader.set(Settings.DESIGN, arrayListOf(typeId), JsonPrimitive(value ?: ""))
        }

    var currentOverwrite: Pair<Boolean, Cosmetic?> = Pair(false, null)

    fun registerCosmetic(id: String, cosmetic: Cosmetic, default: Boolean) {
        cosmetics[id] = cosmetic

        if(default)
            defaultCosmetics[id] = cosmetic
    }

    fun getTexture(): Identifier? {
        if(currentOverwrite.first)
            return currentOverwrite.second?.getTexture()
        if(currentlySelected == null || !defaultCosmetics.contains(currentlySelected))
            return null
        return defaultCosmetics[currentlySelected]?.getTexture()
    }

    fun getTexture(name: String): Identifier? {
        if(currentOverwrite.first)
            return currentOverwrite.second?.getTexture()
        if(MinecraftClient.getInstance().gameProfile.name == name && currentlySelected != null)
            return defaultCosmetics[currentlySelected]?.getTexture()
        return null
    }
}