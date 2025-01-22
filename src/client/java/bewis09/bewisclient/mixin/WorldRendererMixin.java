package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import bewis09.bewisclient.util.ColorSaver;
import bewis09.bewisclient.util.NumberFormatter;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Nullable private ClientWorld world;

    /**
     * @author Mojang
     * @reason Why not
     */

    @Inject(method = "drawBlockOutline",at = @At("HEAD"), cancellable = true)
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, int color, CallbackInfo ci) {
        if(Settings.Companion.getBlockhit().get()) {
            int in = ((Settings.Companion.getBlockhit().getColor().get().getColor()) + 0x1000000) % 0x1000000;
            String str = NumberFormatter.INSTANCE.zeroBefore(in, 6, 16);
            try {
                float r = Integer.decode("0x" + str.charAt(0) + str.charAt(1)) / 256f;
                float g = Integer.decode("0x" + str.charAt(2) + str.charAt(3)) / 256f;
                float b = Integer.decode("0x" + str.charAt(4) + str.charAt(5)) / 256f;
                float a = Settings.Companion.getBlockhit().getAlpha().get();
                VertexRendering.drawOutline(matrices, vertexConsumer, state.getOutlineShape(this.world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, ColorHelper.fromFloats(a, r, g, b));
            } catch (Exception e) {
                VertexRendering.drawOutline(matrices, vertexConsumer, state.getOutlineShape(this.world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, color);
            }
            ci.cancel();
        }
    }
}
