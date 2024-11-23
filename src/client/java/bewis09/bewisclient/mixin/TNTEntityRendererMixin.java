package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import bewis09.bewisclient.util.NumberFormatter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
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
public abstract class TNTEntityRendererMixin extends EntityRenderer<TntEntity, TntEntityRenderState> {
    protected TNTEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/TntEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at=@At("RETURN"))
    public void inject(TntEntityRenderState tntEntityRenderState, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(!SettingsLoader.INSTANCE.get(Settings.GENERAL, Settings.Companion.getTNT_TIMER())) return;

        double d = tntEntityRenderState.squaredDistanceToCamera;
        if (d > 64.0) {
            return;
        }
        matrices.push();
        matrices.translate(tntEntityRenderState.x, tntEntityRenderState.y+0.2, tntEntityRenderState.z);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(0.015f, -0.015f, 0.015f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        TextRenderer textRenderer = this.getTextRenderer();
        final var s = NumberFormatter.INSTANCE.withAfterPointZero(tntEntityRenderState.fuse/20f,2)+"s";
        float g = (float) -textRenderer.getWidth(s) / 2;
        textRenderer.draw(s, g, 0, Colors.WHITE, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, i);
        matrices.pop();
    }
}
