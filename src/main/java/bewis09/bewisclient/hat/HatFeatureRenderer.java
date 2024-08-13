package bewis09.bewisclient.hat;

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

public class HatFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    
    final ModelPart head;
    final ModelPart hat;
    final PlayerEntityModel<AbstractClientPlayerEntity> model;
    
    public HatFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
        model = context.getModel();
        head = context.getModel().getHead();
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        hat = modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(0,0).cuboid(-4,-8,-4,8,8,8,new Dilation(0.51f)),ModelTransform.pivot(0.0F, 0.0F, 0.0F)).createPart(32,16);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isInvisible() && !entity.isSpectator() && entity == MinecraftClient.getInstance().player) {
            {
                hat.yaw = head.yaw;
                hat.pitch = head.pitch;
                hat.roll = head.roll;
                hat.pivotX = head.pivotX;
                hat.pivotY = head.pivotY;
                hat.pivotZ = head.pivotZ;
            }
            hat.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(getTexture(entity))), light, 1);
        }
    }

    @Override
    protected Identifier getTexture(AbstractClientPlayerEntity entity) {
        return Hat.current_hat.texture();
    }
}
