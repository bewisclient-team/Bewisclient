package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome

class BiomeWidget: LineWidget("biome",150, true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(Text.translatable(Identifier(MinecraftClient.getInstance().world?.getBiome(MinecraftClient.getInstance().player?.blockPos ?: BlockPos(0,0,0))?.let { getBiomeString(it) }).toTranslationKey("biome")).string)
    }

    private fun getBiomeString(biome: RegistryEntry<Biome>): String {
        return biome.keyOrValue.map({ biomeKey: RegistryKey<Biome> -> biomeKey.value.toString() }, { biome_: Biome -> "[unregistered $biome_]" }) as String
    }
}