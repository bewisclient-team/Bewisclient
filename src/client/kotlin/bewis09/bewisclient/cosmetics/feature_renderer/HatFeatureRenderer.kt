package bewis09.bewisclient.cosmetics.feature_renderer

import bewis09.bewisclient.cosmetics.Cosmetics
import net.minecraft.client.model.*
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack

/**
 * The [FeatureRenderer] for the hat
 */
class HatFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel?>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel?>(context) {
    val head: ModelPart = context.model!!.getHead()
    val hat: ModelPart

    init {
        val modelData = ModelData()
        val modelPartData = modelData.root
        hat = modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(0, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f, Dilation(0.51f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f))
            .createPart(32, 16)
    }

    override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
        if (!state.invisible && !state.spectator) {
            val texture = getTexture(state.name)

            if (texture != null) {
                hat.yaw = head.yaw
                hat.pitch = head.pitch
                hat.roll = head.roll
                hat.pivotX = head.pivotX
                hat.pivotY = head.pivotY
                hat.pivotZ = head.pivotZ

                hat.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture)), light, OverlayTexture.DEFAULT_UV)
            }
        }
    }

    fun getTexture(name: String) = Cosmetics.hats.getTexture(name)
}