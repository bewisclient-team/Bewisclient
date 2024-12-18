package bewis09.bewisclient.wings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

import static java.lang.Math.cos;

/**
 * The {@link FeatureRenderer} for the wing
 */
public class WingFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {

    final ModelPart body;
    final ModelPart part1;
    final ModelPart part2;

    /**
     * Indicates in which state the animation is in
     */
    public static int wing_animation_frame;

    public WingFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        body = context.getModel().body;
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        part1 = modelPartData.addChild("wing", ModelPartBuilder.create().uv(0, 0)
                        .cuboid(1, -2, 2, 0, 16, 16, new Dilation(0f)),
                ModelTransform.pivot(0.0f, 0, 2)).createPart(32,16);
        part2 = modelPartData.addChild("wing", ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-1, -2, 2, 0, 16, 16, new Dilation(0f)),
                ModelTransform.pivot(0.0f, 0, 2)).createPart(32,16);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (!state.invisible && !state.spectator) {
            if (Objects.equals(state.name, MinecraftClient.getInstance().getGameProfile().getName())) {
                var clamped_value = MathHelper.clamp((wing_animation_frame - 30) / 30f, -1f, 1f);
                clamped_value = (float) (1 - cos(Math.PI * clamped_value)) / 2;
                clamped_value = (clamped_value + 1) * 30;

                part1.yaw = body.yaw + Math.abs(clamped_value - 30) / 30f;
                part1.pitch = body.pitch;
                part1.roll = body.roll;
                part1.pivotX = body.pivotX;
                part1.pivotY = body.pivotY;
                part1.pivotZ = body.pivotZ + 2;
                part2.yaw = body.yaw - Math.abs(clamped_value - 30) / 30f;
                part2.pitch = body.pitch;
                part2.roll = body.roll;
                part2.pivotX = body.pivotX;
                part2.pivotY = body.pivotY;
                part2.pivotZ = body.pivotZ + 2;
                part1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture(),false)), light, OverlayTexture.DEFAULT_UV);
                part2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture(),false)), light, OverlayTexture.DEFAULT_UV);
            }
        }
    }

    protected Identifier getTexture() {
        return Wing.current_wing.texture();
    }
}
