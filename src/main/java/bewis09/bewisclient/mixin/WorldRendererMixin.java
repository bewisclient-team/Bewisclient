package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.util.ColorSaver;
import bewis09.bewisclient.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Objects;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    /**
     * @author Mojang
     * @reason Why not
     */

    @SuppressWarnings("DataFlowIssue")
    @Overwrite
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state) {
        int in = ((((ColorSaver)Objects.requireNonNull(JavaSettingsSender.Companion.getDesignSettings().getValue(Settings.Companion.getSettings().getBLOCKHIT())).getValue("color")).getColor())+0x1000000)%0x1000000;
        String str = MathUtil.Companion.zeroBefore(in,6,16);
        try {
            float r = Integer.decode("0x" + str.charAt(0) + str.charAt(1)) / 256f;
            float g = Integer.decode("0x" + str.charAt(2) + str.charAt(3)) / 256f;
            float b = Integer.decode("0x" + str.charAt(4) + str.charAt(5)) / 256f;
            float a = JavaSettingsSender.Companion.getDesignSettings().getValue(Settings.Companion.getSettings().getBLOCKHIT()).getValue("alpha");
            if (!((boolean)JavaSettingsSender.Companion.getDesignSettings().getValue(Settings.Companion.getSettings().getBLOCKHIT()).getValue("enabled"))) {
                r = b = g = 0;
                a = 0.4f;
            }
            draw(matrices, vertexConsumer, state.getOutlineShape(MinecraftClient.getInstance().world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, r, g, b, a);
        } catch (Exception e) {
            draw(matrices, vertexConsumer, state.getOutlineShape(MinecraftClient.getInstance().world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, 0.0f, 0.0f, 0.0f, 0.4f);
        }
    }

    @Invoker("drawCuboidShapeOutline")
    @SuppressWarnings("all")
    static void draw(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {}
}
