package bewis09.bewisclient.cosmetics

import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

open class CosmeticsType(val typeId: String) {
    val cosmetics = sortedMapOf<String, Cosmetic>()

    var currentlySelected: String? = try { SettingsLoader.get(Settings.DESIGN, SettingsLoader.TypedSettingID<String>(typeId)) } catch (e: Exception) { null }
        set(value) {
            field = value
            SettingsLoader.set(Settings.DESIGN, value ?: "", SettingsLoader.TypedSettingID(typeId))
        }

    var currentOverwrite: Pair<Boolean, Cosmetic?> = Pair(false, null)

    fun registerCosmetic(id: String, cosmetic: Cosmetic) {
        cosmetics[id] = cosmetic
    }

    fun getTexture(): Identifier? {
        if(currentOverwrite.first)
            return currentOverwrite.second?.getTexture()
        return cosmetics[currentlySelected]?.getTexture()
    }

    fun getTexture(name: String): Identifier? {
        if(currentOverwrite.first)
            return currentOverwrite.second?.getTexture()
        if(MinecraftClient.getInstance().gameProfile.name == name)
            return cosmetics[currentlySelected]?.getTexture()
        return null
    }
}