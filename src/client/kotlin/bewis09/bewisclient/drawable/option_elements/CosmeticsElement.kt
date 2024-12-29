package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.MixinStatics
import bewis09.bewisclient.cosmetics.Cosmetics
import bewis09.bewisclient.mixin.EntityRenderDispatcherMixin
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.SkinTextures
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

class CosmeticsElement(val type: String, val renderType: RenderType = RenderType.NORMAL): OptionElement("cosmetics.$type", "") {
    enum class RenderType {
        REVERSED,
        NORMAL,
        FAST_CHANGING
    }

    val cosmeticsType = Cosmetics.getCosmeticsType(type)

    val RendererContext = EntityRendererFactory.Context(
        MinecraftClient.getInstance().entityRenderDispatcher,
        MinecraftClient.getInstance().itemModelManager,
        MinecraftClient.getInstance().mapRenderer,
        MinecraftClient.getInstance().blockRenderManager,
        MinecraftClient.getInstance().resourceManager,
        MinecraftClient.getInstance().loadedEntityModels,
        (MinecraftClient.getInstance().entityRenderDispatcher as EntityRenderDispatcherMixin).equipmentModelLoader,
        MinecraftClient.getInstance().textRenderer
    )

    val entityRenderer = PlayerEntityRenderer(RendererContext, MinecraftClient.getInstance().skinProvider.getSkinTextures(MinecraftClient.getInstance().gameProfile).model == SkinTextures.Model.SLIM)

    val playerEntityRenderState = PlayerEntityRenderState()

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText(title),x+width/2,y+5,(alphaModifier+0xFFFFFF).toInt())

        context.fill(x, y + 25, x + width, y + 140, 0x80000000.toInt())

        context.fill(x, y + 25, x + 20, y + 140, (alphaModifier).toInt())
        context.drawBorder(x, y + 25, 20, 115, (alphaModifier + 0xFFFFFF).toInt())

        context.fill(x + width - 20, y + 25, x + width, y + 140, (alphaModifier).toInt())
        context.drawBorder(x + width - 20, y + 25, 20, 115, (alphaModifier + 0xFFFFFF).toInt())

        val shiftXOffset = 25

        cosmeticsType.cosmetics.toList().forEachIndexed { index, pair ->
            val xOffset = x + index * 65 + shiftXOffset

            val hovered = mouseX in xOffset..xOffset + 60 && mouseY in y + 122..y + 135

            context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                if(cosmeticsType.currentlySelected == pair.second.id) Bewisclient.getTranslationText("gui.disable") else Bewisclient.getTranslationText("gui.select"),
                xOffset + 30,
                y + 125,
                (if(!hovered) 0xFFFFFFFF else 0xFFFFFF70).toInt()
            )

            val f = if(renderType != RenderType.FAST_CHANGING)
                        atan(((mouseX - (xOffset + 30)) / 40.0f).toDouble()).toFloat() * (if(renderType == RenderType.REVERSED) 1 else -1)
                    else
                        ((mouseX - (xOffset + 30)) / 10f) % 360 - 180f
            val g = atan((((if(renderType != RenderType.FAST_CHANGING) (mouseY - (y + 50)) else 50)) / 40.0f).toDouble()).toFloat() * (if(renderType == RenderType.REVERSED) 1 else -1)
            val quaternionf = Quaternionf().rotateZ(3.1415927f)
            val quaternionf2 = Quaternionf().rotateX(g * 20.0f * 0.017453292f)
            val quaternionf3 = Quaternionf().rotateY(3.1415927f)

            val skinTextures = MinecraftClient.getInstance().skinProvider.getSkinTextures(MinecraftClient.getInstance().gameProfile)

            playerEntityRenderState.bodyYaw = f * 10.0f
            playerEntityRenderState.yawDegrees = f * (if(renderType != RenderType.FAST_CHANGING) 20.0f else 0.0f)
            playerEntityRenderState.pitch = g * 10.0f * (if(renderType == RenderType.REVERSED) 1 else -1)
            playerEntityRenderState.skinTextures = skinTextures

            if (!MixinStatics.OwnPlayerSkinTextures.contains(skinTextures.hashCode()))
                MixinStatics.OwnPlayerSkinTextures.add(skinTextures.hashCode())

            context.matrices.push()
            context.matrices.translate((xOffset + 30).toDouble(), (y + 25 + 87).toDouble(), 50.0)
            context.matrices.scale(40f, 40f, -40f)
            context.matrices.multiply(quaternionf)
            context.matrices.multiply(quaternionf2)

            if(renderType != RenderType.REVERSED)
                context.matrices.multiply(quaternionf3)

            RenderSystem.setShaderLights(Vector3f(0f, 0f, 0f), Vector3f(1f, 1f, 10f))

            cosmeticsType.currentOverwrite = pair.second

            context.draw {
                renderEntity(it, alphaModifier, context)
            }

            context.fill(0,0,0,0,0)

            RenderSystem.setShaderColor(1f,1f,1f, 1f)

            context.matrices.pop()
        }

        cosmeticsType.currentOverwrite = null

        return 140
    }

    fun renderEntity(vertexConsumerProvider: VertexConsumerProvider, alphaModifier: Long, context: DrawContext) {
        RenderSystem.setShaderColor(1f,1f,1f, (alphaModifier / 0xFF).toFloat() / 0x1000000)

        entityRenderer.render(
            playerEntityRenderState,
            context.matrices,
            vertexConsumerProvider,
            0xFFFFFF,
        )
    }
}