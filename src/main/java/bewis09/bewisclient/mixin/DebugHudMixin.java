package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.Settings;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private @Nullable ChunkPos pos;

    @Shadow
    public abstract void resetChunk();

    @Shadow
    protected abstract WorldChunk getClientChunk();

    @Shadow
    @Nullable
    protected abstract WorldChunk getChunk();

    @Shadow
    @Nullable
    protected abstract ServerWorld getServerWorld();

    @Shadow
    private static String getBiomeString(RegistryEntry<Biome> biome) {
        return null;
    }

    @Inject(method = "getLeftText", at = @At("HEAD"), cancellable = true)
    public void inject(CallbackInfoReturnable<List<String>> cir) {
        if (JavaSettingsSender.Companion.getSettings().get("design", Settings.Companion.getSettings().getCLEANER_DEBUG_MENU())) {
            List<String> list = new ArrayList<>();
            String var10003 = SharedConstants.getGameVersion().getName();
            list.add("Minecraft " + var10003 + " (" + this.client.getGameVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.client.getVersionType()) ? "" : "/" + this.client.getVersionType()) + ")");
            String[] strings = this.client.fpsDebugString.split(" ");
            list.add(strings[0] + " " + strings[1] + ", " + strings[strings.length - 2] + " " + strings[strings.length - 1]);
            if (this.client.world != null) {
                list.add("Dimension: " + this.client.world.getDimensionEntry().getIdAsString());
                list.add("Entity Count: " + this.client.world.getRegularEntityCount());
            }

            list.add("");
            if (this.client.getCameraEntity() != null) {
                ChunkPos chunkPos = new ChunkPos(this.client.getCameraEntity().getBlockPos());
                if (!Objects.equals(this.pos, chunkPos)) {
                    this.pos = chunkPos;
                    this.resetChunk();
                }

                Direction direction = client.getCameraEntity().getHorizontalFacing();
                String string2 = switch ( direction ) {
                    case NORTH -> "Towards negative Z";
                    case SOUTH -> "Towards positive Z";
                    case WEST -> "Towards negative X";
                    case EAST -> "Towards positive X";
                    default -> "Invalid";
                };

                list.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", this.client.getCameraEntity().getX(), this.client.getCameraEntity().getY(), this.client.getCameraEntity().getZ()));
                list.add("Block: " + this.client.getCameraEntity().getBlockX() + " " + this.client.getCameraEntity().getBlockY() + " " + this.client.getCameraEntity().getBlockZ());
                list.add("Chuck: " + chunkPos.x + " " + ChunkSectionPos.getSectionCoord(this.client.getCameraEntity().getBlockPos().getY()) + " " + chunkPos.z);
                list.add("ChunkRelative: " + (this.client.getCameraEntity().getBlockX() & 15) + " " + (this.client.getCameraEntity().getBlockY() & 15) + " " + (this.client.getCameraEntity().getBlockZ() & 15));
                Entity entity = client.getCameraEntity();
                list.add("Facing: " + direction + " (" + string2 + ") (" + String.format(Locale.ROOT, "%.1f / %.1f",MathHelper.wrapDegrees(entity.getYaw()),MathHelper.wrapDegrees(entity.getPitch())) + ")");

            }
            BlockPos blockPos = this.client.getCameraEntity().getBlockPos();
            int j = this.client.world.getLightLevel(LightType.SKY, blockPos);
            int k = this.client.world.getLightLevel(LightType.BLOCK, blockPos);
            WorldChunk worldChunk = this.getClientChunk();
            if (worldChunk.isEmpty()) {
                list.add("Waiting for Chunk...");
            } else {
                WorldChunk worldChunk2 = this.getChunk();
                list.add((k==0?(j<8?"§c":"§6"):"§2")+ "Light: " + Math.max(j, k) + " (sky: " + j + " block: " + k + ")");
                list.add("Highest Block: " + worldChunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()));
                RegistryEntry<Biome> var27 = this.client.world.getBiome(blockPos);
                AtomicReference<String> text = new AtomicReference<>(getBiomeString(var27));
                var27.getKey().ifPresent(biomeRegistryKey -> text.set(Text.translatable(biomeRegistryKey.getValue().toTranslationKey("biome")).getString()));
                list.add("Biome: " + text.get() + " (" + getBiomeString(var27) + ")");
                list.add("Day: " + (this.client.world.getTimeOfDay() / 24000L));
                long l = 0L;
                float h = 0.0F;
                if (worldChunk2 != null) {
                    h = client.world.getMoonSize();
                    l = worldChunk2.getInhabitedTime();
                }
                LocalDifficulty localDifficulty = new LocalDifficulty(client.world.getDifficulty(), client.world.getTimeOfDay(), l, h);
                list.add("Local Difficulty: " + localDifficulty.getLocalDifficulty() + " // " + localDifficulty.getClampedLocalDifficulty());
                assert this.client.player != null;
                list.add("Mood: "+Math.round(this.client.player.getMoodPercentage() * 100.0F)+"%");
            }
            ServerWorld serverWorld = this.getServerWorld();
            if (serverWorld != null) {
                list.add("");
                ServerChunkManager serverChunkManager = serverWorld.getChunkManager();
                ChunkGenerator chunkGenerator = serverChunkManager.getChunkGenerator();
                NoiseConfig noiseConfig = serverChunkManager.getNoiseConfig();
                chunkGenerator.getDebugHudText(list, noiseConfig, blockPos);
                MultiNoiseUtil.MultiNoiseSampler multiNoiseSampler = noiseConfig.getMultiNoiseSampler();
                BiomeSource biomeSource = chunkGenerator.getBiomeSource();
                biomeSource.addDebugInfo(list, blockPos, multiNoiseSampler);
                SpawnHelper.Info info = serverChunkManager.getSpawnInfo();
                if (info != null) {
                    Object2IntMap<SpawnGroup> object2IntMap = info.getGroupToCount();
                    int m = info.getSpawningChunkCount();

                    list.add("SpawnChunks: "+m);
                    for (SpawnGroup spawnGroup : SpawnGroup.values()) {
                        StringBuilder str = new StringBuilder();
                        for (String s : spawnGroup.getName().split("_")) {
                            str.append(s.replaceFirst(".", String.valueOf(s.toUpperCase().charAt(0))));
                        }
                        list.add(str+": "+object2IntMap.getInt(spawnGroup));
                    }
                } else {
                    list.add("SC: N/A");
                }
            }
            cir.setReturnValue(list);
        }
    }
}
