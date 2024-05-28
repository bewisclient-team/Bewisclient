package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntityRenderer.class)
public abstract class TNTEntityRendererMixin extends EntityRenderer<TntEntity> {
    protected TNTEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/TntEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at=@At("RETURN"))
    public void inject(TntEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(!JavaSettingsSender.Companion.getSettings().getBoolean("general","tnt_timer")) return;

        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (d > 64.0) {
            return;
        }
        Vec3d vec3d = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, ((Entity)entity).getYaw(tickDelta));
        if (vec3d == null) {
            return;
        }
        matrices.push();
        matrices.translate(vec3d.x, vec3d.y+0.2, vec3d.z);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-0.015f, -0.015f, 0.015f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float f = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int j = (int)(f * 255.0f) << 24;
        TextRenderer textRenderer = this.getTextRenderer();
        final var s = MathUtil.Companion.withAfterCommaZero(entity.getFuse()/20f,2)+"s";
        float g = (float) -textRenderer.getWidth(s) / 2;
        textRenderer.draw(s, g, 0, Colors.WHITE, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, i);
        matrices.pop();
    }
}
