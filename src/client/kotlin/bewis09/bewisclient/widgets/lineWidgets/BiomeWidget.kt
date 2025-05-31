package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.SettingTypes
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys

/**
 * A [LineWidget] which displays the current biome
 */
class BiomeWidget: LineWidget<SettingTypes.ColorcodeBiomeWidgetSettingsObject>("biome",150, true) {

    override fun getText(): ArrayList<String> {
        return arrayListOf(getText(settings.colorcode_biome.get()))
    }

    companion object {

        /**
         * The corresponding color codes for each biome to render the correct text color
         */
        val biomeCodes = hashMapOf(
                Pair(BiomeKeys.THE_VOID,"§7"),
                Pair(BiomeKeys.PLAINS,"§a"),
                Pair(BiomeKeys.SUNFLOWER_PLAINS, "§a"),
                Pair(BiomeKeys.SNOWY_PLAINS, "§9"),
                Pair(BiomeKeys.ICE_SPIKES, "§9"),
                Pair(BiomeKeys.DESERT, "§e"),
                Pair(BiomeKeys.SWAMP, "§a"),
                Pair(BiomeKeys.MANGROVE_SWAMP, "§a"),
                Pair(BiomeKeys.FOREST, "§a"),
                Pair(BiomeKeys.FLOWER_FOREST, "§a"),
                Pair(BiomeKeys.BIRCH_FOREST, "§a"),
                Pair(BiomeKeys.DARK_FOREST, "§a"),
                Pair(BiomeKeys.OLD_GROWTH_BIRCH_FOREST, "§a"),
                Pair(BiomeKeys.OLD_GROWTH_PINE_TAIGA, "§a"),
                Pair(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, "§a"),
                Pair(BiomeKeys.TAIGA, "§a"),
                Pair(BiomeKeys.SNOWY_TAIGA, "§9"),
                Pair(BiomeKeys.SAVANNA, "§a"),
                Pair(BiomeKeys.SAVANNA_PLATEAU, "§a"),
                Pair(BiomeKeys.WINDSWEPT_HILLS, "§8"),
                Pair(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, "§8"),
                Pair(BiomeKeys.WINDSWEPT_FOREST, "§a"),
                Pair(BiomeKeys.WINDSWEPT_SAVANNA, "§a"),
                Pair(BiomeKeys.JUNGLE, "§2"),
                Pair(BiomeKeys.SPARSE_JUNGLE, "§2"),
                Pair(BiomeKeys.BAMBOO_JUNGLE, "§2"),
                Pair(BiomeKeys.BADLANDS, "§6"),
                Pair(BiomeKeys.ERODED_BADLANDS, "§6"),
                Pair(BiomeKeys.WOODED_BADLANDS, "§6"),
                Pair(BiomeKeys.MEADOW, "§a"),
                Pair(BiomeKeys.CHERRY_GROVE, "§d"),
                Pair(BiomeKeys.GROVE, "§a"),
                Pair(BiomeKeys.SNOWY_SLOPES, "§9"),
                Pair(BiomeKeys.FROZEN_PEAKS, "§9"),
                Pair(BiomeKeys.JAGGED_PEAKS, "§9"),
                Pair(BiomeKeys.STONY_PEAKS, "§8"),
                Pair(BiomeKeys.RIVER,"§9"),
                Pair(BiomeKeys.FROZEN_RIVER, "§9"),
                Pair(BiomeKeys.BEACH, "§e"),
                Pair(BiomeKeys.SNOWY_BEACH, "§9"),
                Pair(BiomeKeys.STONY_SHORE, "§8"),
                Pair(BiomeKeys.WARM_OCEAN, "§9"),
                Pair(BiomeKeys.LUKEWARM_OCEAN, "§9"),
                Pair(BiomeKeys.DEEP_LUKEWARM_OCEAN, "§9"),
                Pair(BiomeKeys.OCEAN, "§9"),
                Pair(BiomeKeys.DEEP_OCEAN, "§9"),
                Pair(BiomeKeys.COLD_OCEAN, "§9"),
                Pair(BiomeKeys.DEEP_COLD_OCEAN, "§9"),
                Pair(BiomeKeys.FROZEN_OCEAN, "§9"),
                Pair(BiomeKeys.DEEP_FROZEN_OCEAN, "§9"),
                Pair(BiomeKeys.MUSHROOM_FIELDS, "§8"),
                Pair(BiomeKeys.DRIPSTONE_CAVES, "§8"),
                Pair(BiomeKeys.LUSH_CAVES, "§2"),
                Pair(BiomeKeys.DEEP_DARK, "§8"),
                Pair(BiomeKeys.NETHER_WASTES,"§4"),
                Pair(BiomeKeys.WARPED_FOREST,"§3"),
                Pair(BiomeKeys.CRIMSON_FOREST, "§4"),
                Pair(BiomeKeys.SOUL_SAND_VALLEY, "§c"),
                Pair(BiomeKeys.BASALT_DELTAS, "§8"),
                Pair(BiomeKeys.THE_END, "§7"),
                Pair(BiomeKeys.END_HIGHLANDS, "§7"),
                Pair(BiomeKeys.END_MIDLANDS, "§7"),
                Pair(BiomeKeys.SMALL_END_ISLANDS, "§7"),
                Pair(BiomeKeys.END_BARRENS, "§7")
        )

        /**
         * @param colorCode Indicates if the name should be color coded
         *
         * @return The biome name with the color code, when it is enabled
         */
        fun getText(colorCode: Boolean): String  {
            return (if(colorCode) colorCode() else "") + Text.translatable(Identifier.of(MinecraftClient.getInstance().world?.getBiome(MinecraftClient.getInstance().player?.blockPos
                    ?: BlockPos(0, 0, 0))?.let { getBiomeString(it) }).toTranslationKey("biome")).string
        }

        /**
         * @param biome The biome of which the identifier should be returned
         *
         * @return The identifier string of the [biome]
         */
        private fun getBiomeString(biome: RegistryEntry<Biome>): String {
            return biome.keyOrValue.map({ biomeKey: RegistryKey<Biome> -> biomeKey.value.toString() }, { biome_: Biome -> "[unregistered $biome_]" }) as String
        }

        /**
         * @return The color code of a biome or an empty [String] if the biome is not in [biomeCodes]
         */
        private fun colorCode():String {
            val biome = MinecraftClient.getInstance().world?.getBiome(MinecraftClient.getInstance().player?.blockPos)
                    ?: return ""

            return biomeCodes.getOrDefault(biome.key.orElseGet{null},"")
        }
    }

    override fun getWidgetSettings(): SettingTypes.ColorcodeBiomeWidgetSettingsObject {
        return SettingTypes.ColorcodeBiomeWidgetSettingsObject(id,5f,-1f,5f,1f,0.43f,.7f)
    }
}