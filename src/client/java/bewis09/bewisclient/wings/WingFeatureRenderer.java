package bewis09.bewisclient.wings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static java.lang.Math.cos;

// TODO fix

/**
 * The {@link FeatureRenderer} for the wing
 */
//public class WingFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
//
//    final ModelPart body;
//    final ModelPart part1;
//    final ModelPart part2;
//
//    /**
//     * Indicates in which state the animation is in
//     */
//    public static int wing_animation_frame;
//
//    public WingFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
//        super(context);
//        body = context.getModel().body;
//        ModelData modelData = new ModelData();
//        ModelPartData modelPartData = modelData.getRoot();
//        part1 = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 0)
//                        .cuboid(1, -2, 2, 0, 16, 16, new Dilation(0f)),
//                ModelTransform.pivot(0.0f, 0, 2)).createPart(32,16);
//        part2 = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 0)
//                        .cuboid(-1, -2, 2, 0, 16, 16, new Dilation(0f)),
//                ModelTransform.pivot(0.0f, 0, 2)).createPart(32,16);
//    }
//
//    @Override
//    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
//        if (!entity.isInvisible() && !entity.isSpectator() && entity == MinecraftClient.getInstance().player) {
//            var clamped_value = MathHelper.clamp((wing_animation_frame -30)/30f,-1f,1f);
//            clamped_value = (float) (1- cos(Math.PI * clamped_value))/2;
//            clamped_value = (clamped_value+1)*30;
//
//            part1.yaw = body.yaw + Math.abs(clamped_value - 30) / 30f;
//            part1.pitch = body.pitch;
//            part1.roll = body.roll;
//            part1.pivotX = body.pivotX;
//            part1.pivotY = body.pivotY;
//            part1.pivotZ = body.pivotZ + 2;
//            part2.yaw = body.yaw - Math.abs(clamped_value - 30) / 30f;
//            part2.pitch = body.pitch;
//            part2.roll = body.roll;
//            part2.pivotX = body.pivotX;
//            part2.pivotY = body.pivotY;
//            part2.pivotZ = body.pivotZ + 2;
//            part1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(getTexture(entity))), light, 1);
//            part2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(getTexture(entity))), light, 1);
//        }
//    }
//
//    @Override
//    protected Identifier getTexture(AbstractClientPlayerEntity entity) {
//        return Wing.current_wing.texture();
//    }
//}
//