package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static bewis09.bewisclient.settingsLoader.Settings.DESIGN;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    /**
     * @author Mojang
     * @reason Why Not
     */

    @Overwrite
    private static void renderFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        float d = SettingsLoader.INSTANCE.get(DESIGN, Settings.Companion.getFIRE_HEIGHT());
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        Sprite sprite = ModelBaker.FIRE_1.getSprite();
        RenderSystem.setShaderTexture(0, sprite.getAtlasId());
        float f = sprite.getMinU();
        float g = sprite.getMaxU();
        float h = (f + g) / 2.0F;
        float i = sprite.getMinV();
        float j = sprite.getMaxV();
        float k = (i + j) / 2.0F;
        float l = sprite.getAnimationFrameDelta();
        float m = MathHelper.lerp(l, f, h);
        float n = MathHelper.lerp(l, g, h);
        float o = MathHelper.lerp(l, i, k);
        float p = MathHelper.lerp(l, j, k);

        for(int r = 0; r < 2; ++r) {
            matrices.push();
            matrices.translate((float)(-(r * 2 - 1)) * 0.24F, -0.3F+d-1f, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(r * 2 - 1) * 10.0F));
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).texture(n, p).color(1.0F, 1.0F, 1.0F, 0.9F);
            bufferBuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).texture(m, p).color(1.0F, 1.0F, 1.0F, 0.9F);
            bufferBuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).texture(m, o).color(1.0F, 1.0F, 1.0F, 0.9F);
            bufferBuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).texture(n, o).color(1.0F, 1.0F, 1.0F, 0.9F);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            matrices.pop();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
}
