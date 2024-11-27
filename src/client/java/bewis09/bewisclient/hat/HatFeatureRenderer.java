package bewis09.bewisclient.hat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * The {@link FeatureRenderer} for the hat
 */
public class HatFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {

    final ModelPart head;
    final ModelPart hat;
    final PlayerEntityModel model;

    public HatFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        model = context.getModel();
        head = context.getModel().getHead();
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        hat = modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(0,0).cuboid(-4,-8,-4,8,8,8,new Dilation(0.51f)),ModelTransform.pivot(0.0F, 0.0F, 0.0F)).createPart(32,16);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (!state.invisible && !state.spectator) {
            if (Objects.equals(state.name, MinecraftClient.getInstance().getGameProfile().getName())) {
                {
                    hat.yaw = head.yaw;
                    hat.pitch = head.pitch;
                    hat.roll = head.roll;
                    hat.pivotX = head.pivotX;
                    hat.pivotY = head.pivotY;
                    hat.pivotZ = head.pivotZ;
                }
                hat.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture())), light, OverlayTexture.DEFAULT_UV);
            }
        }
    }

    protected Identifier getTexture() {
        return Hat.current_hat.texture();
    }
}
