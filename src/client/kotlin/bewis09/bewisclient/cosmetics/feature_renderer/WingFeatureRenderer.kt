package bewis09.bewisclient.cosmetics.feature_renderer

import bewis09.bewisclient.cosmetics.Cosmetics
import net.minecraft.client.model.*
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.cos

/**
 * The [FeatureRenderer] for the wing
 */
class WingFeatureRenderer(context: FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel?>) : FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel?>(context) {
    val body: ModelPart = context.model!!.body
    val part1: ModelPart
    val part2: ModelPart

    init {
        val modelData = ModelData()
        val modelPartData = modelData.root
        part1 = modelPartData.addChild(
            "wing", ModelPartBuilder.create().uv(0, 0)
                .cuboid(1f, -2f, 2f, 0f, 16f, 16f, Dilation(0.001f)),
            ModelTransform.origin(0.0f, 0f, 2f)
        ).createPart(32, 16)
        part2 = modelPartData.addChild(
            "wing", ModelPartBuilder.create().uv(0, 0)
                .cuboid(-1f, -2f, 2f, 0f, 16f, 16f, Dilation(0.001f)),
            ModelTransform.origin(0.0f, 0f, 2f)
        ).createPart(32, 16)
    }

    override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, state: PlayerEntityRenderState, limbAngle: Float, limbDistance: Float) {
        if (!state.invisible && !state.spectator) {
            val texture = getTexture(state.name)

            if (texture != null) {
                var clampedValue = MathHelper.clamp((wing_animation_frame - 30) / 30f, -1f, 1f)
                clampedValue = (1 - cos(Math.PI * clampedValue)).toFloat() / 2
                clampedValue = (clampedValue + 1) * 30

                part1.yaw = (body.yaw + abs((clampedValue - 30).toDouble()) / 30f).toFloat()
                part1.pitch = body.pitch
                part1.roll = body.roll
                part1.originX = body.originX
                part1.originY = body.originY
                part1.originZ = body.originZ + 2
                part2.yaw = (body.yaw - abs((clampedValue - 30).toDouble()) / 30f).toFloat()
                part2.pitch = body.pitch
                part2.roll = body.roll
                part2.originX = body.originX
                part2.originY = body.originY
                part2.originZ = body.originZ + 2
                part1.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture, false)), light, OverlayTexture.DEFAULT_UV)
                part2.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture, false)), light, OverlayTexture.DEFAULT_UV)
            }
        }
    }

    fun getTexture(name: String) = Cosmetics.wings.getTexture(name)

    companion object {
        /**
         * Indicates in which state the animation is in
         */
        var wing_animation_frame: Int = 0
    }
}