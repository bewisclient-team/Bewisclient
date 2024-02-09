package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    /**
     * @author Mojang
     * @reason Why Not
     */

    @Overwrite
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Sprite sprite = ModelLoader.FIRE_1.getSprite();
        RenderSystem.setShaderTexture(0, sprite.getAtlasId());
        float f = sprite.getMinU();
        float g = sprite.getMaxU();
        float h = (f + g) / 2.0f;
        float i = sprite.getMinV();
        float j = sprite.getMaxV();
        float k = (i + j) / 2.0f;
        float l = sprite.getAnimationFrameDelta();
        float m = MathHelper.lerp(l, f, h);
        float n = MathHelper.lerp(l, g, h);
        float o = MathHelper.lerp(l, i, k);
        float p = MathHelper.lerp(l, j, k);
        for (int r = 0; r < 2; ++r) {
            float d = JavaSettingsSender.Companion.getDesignSettings().getValue("fire_height");
            matrices.push();
            matrices.translate((float)(-(r * 2 - 1)) * 0.24f, -0.3f, 0.0f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(r * 2 - 1) * 10.0f));
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            bufferBuilder.vertex(matrix4f, -0.5f, -0.5f+d-1f, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(n, p).next();
            bufferBuilder.vertex(matrix4f, 0.5f, -0.5f+d-1f, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(m, p).next();
            bufferBuilder.vertex(matrix4f, 0.5f, -0.5f+d, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(m, o).next();
            bufferBuilder.vertex(matrix4f, -0.5f, -0.5f+d, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(n, o).next();
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            matrices.pop();
        }
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
}
