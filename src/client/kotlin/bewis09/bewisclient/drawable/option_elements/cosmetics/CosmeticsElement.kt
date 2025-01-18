package bewis09.bewisclient.drawable.option_elements.cosmetics

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.MixinStatics
import bewis09.bewisclient.cosmetics.Cosmetics
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement.Companion.RendererContext
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement.Companion.entityRenderer
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement.Companion.playerEntityRenderState
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement.Companion.slim
import bewis09.bewisclient.mixin.EntityRenderDispatcherMixin
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.ScreenValuedAnimation
import bewis09.bewisclient.util.Util
import bewis09.bewisclient.util.drawTexture
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.SkinTextures
import net.minecraft.util.Identifier
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

class CosmeticsElement(val type: String, val renderType: RenderType = RenderType.NORMAL): OptionElement("cosmetics.$type", "") {
    enum class RenderType {
        REVERSED,
        NORMAL,
        FAST_CHANGING
    }

    companion object {
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

        var entityRenderer: PlayerEntityRenderer? = null

        var slim: Boolean? = null

        val playerEntityRenderState = PlayerEntityRenderState()
    }

    var hoveredIndex = -1

    var xAnimation = ScreenValuedAnimation(0f,0f)

    val cosmeticsType = Cosmetics.getCosmeticsType(type)

    var left_selected = false
    var right_selected = false

    var maxX = 0f

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText(title),x+width/2,y+5,(alphaModifier+0xFFFFFF).toInt())

        context.fill(x, y + 25, x + width, y + 140, 0x80555555.toInt())

        left_selected = Util.isIn(mouseX, mouseY, x, y + 25, x + 20, y + 140)

        context.fill(x, y + 25, x + 20, y + 140, (alphaModifier).toInt())
        context.drawBorder(x, y + 25, 20, 115, (alphaModifier + ( if(left_selected) 0xAAAAFF else 0xFFFFFF )).toInt())

        right_selected = Util.isIn(mouseX, mouseY, x + width - 20, y + 25, x + width, y + 140)

        context.fill(x + width - 20, y + 25, x + width, y + 140, (alphaModifier).toInt())
        context.drawBorder(x + width - 20, y + 25, 20, 115, (alphaModifier + ( if(right_selected) 0xAAAAFF else 0xFFFFFF )).toInt())

        val shiftXOffset = 25 - xAnimation.getValue().toInt()

        Cosmetics.types.forEach {
            it.currentOverwrite = Pair(true, null)
        }

        context.drawTexture(Identifier.of(if(right_selected) "bewisclient:textures/sprites/select_highlighted.png" else "bewisclient:textures/sprites/select.png"),x+width-26,y+25+115/2-16,32,32)
        context.drawTexture(Identifier.of(if(left_selected) "bewisclient:textures/sprites/select_left_highlighted.png" else "bewisclient:textures/sprites/select_left.png"),x-6,y+25+115/2-16,32,32)

        context.enableScissor(x + 21, y + 25, x + width - 20, y + 140)

        hoveredIndex = -1

        maxX = - (width - 45f - 65f * cosmeticsType.defaultCosmetics.size)

        cosmeticsType.defaultCosmetics.toList().forEachIndexed { index, pair ->
            val xOffset = x + index * 65 + shiftXOffset

            val allHovered = mouseX in x + 20..x + width - 20 && mouseY in y + 25..y + 140
            val hovered = mouseX in xOffset..xOffset + 60 && mouseY in y + 25..y + 140 && allHovered

            context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                if(cosmeticsType.currentlySelected == pair.second.id) Bewisclient.getTranslationText("gui.disable") else Bewisclient.getTranslationText("gui.select"),
                xOffset + 30,
                y + 125,
                (if(!hovered) 0xFFFFFFFF else 0xFFFFFF70).toInt()
            )

            if(cosmeticsType.currentlySelected == pair.second.id) {
                context.fill(xOffset, y + 25, xOffset + 60, y + 140, 0x80555555.toInt())
            }

            if(hovered)
                hoveredIndex = index

            val f = if(renderType != RenderType.FAST_CHANGING)
                        atan(((mouseX - (xOffset + 30)) / 40.0f).toDouble()).toFloat() * (if(renderType == RenderType.REVERSED) 1 else -1)
                    else
                        ((mouseX - (xOffset + 30)) / 10f) % 360 - 180f
            val g = atan((((if(renderType != RenderType.FAST_CHANGING) (mouseY - (y + 50)) else 50)) / 40.0f).toDouble()).toFloat() * (if(renderType == RenderType.REVERSED) 1 else -1)
            val quaternionf = Quaternionf().rotateZ(3.1415927f)
            val quaternionf2 = Quaternionf().rotateX(g * 5.0f * 0.017453292f)
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

            cosmeticsType.currentOverwrite = Pair(true,pair.second)

            context.draw {
                renderEntity(it, alphaModifier, context)
            }

            context.fill(0,0,0,0,0)

            RenderSystem.setShaderColor(1f,1f,1f, 1f)

            context.matrices.pop()
        }

        context.disableScissor()

        Cosmetics.types.forEach {
            it.currentOverwrite = Pair(false, null)
        }

        return 140
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(hoveredIndex!=-1) {
            val pair = cosmeticsType.defaultCosmetics.toList()[hoveredIndex]
            if(cosmeticsType.currentlySelected == pair.second.id)
                cosmeticsType.currentlySelected = null
            else
                cosmeticsType.currentlySelected = pair.second.id
        }

        if(right_selected) {
            xAnimation = ScreenValuedAnimation(xAnimation.getValue(), 0f.coerceAtLeast(maxX.coerceAtMost(xAnimation.getValue() + 100f)))
        }

        if (left_selected) {
            xAnimation = ScreenValuedAnimation(xAnimation.getValue(), 0f.coerceAtLeast(maxX.coerceAtMost(xAnimation.getValue() - 100f)))
        }
    }
}

fun renderEntity(vertexConsumerProvider: VertexConsumerProvider, alphaModifier: Long, context: DrawContext) {
    RenderSystem.setShaderColor(1f, 1f, 1f, (alphaModifier / 0xFF).toFloat() / 0x1000000)

    if((MinecraftClient.getInstance().skinProvider.getSkinTextures(MinecraftClient.getInstance().gameProfile).model == SkinTextures.Model.SLIM) != slim) {
        entityRenderer = PlayerEntityRenderer(RendererContext, MinecraftClient.getInstance().skinProvider.getSkinTextures(MinecraftClient.getInstance().gameProfile).model == SkinTextures.Model.SLIM)
        slim = MinecraftClient.getInstance().skinProvider.getSkinTextures(MinecraftClient.getInstance().gameProfile).model == SkinTextures.Model.SLIM
    }

    entityRenderer?.render(
        playerEntityRenderState,
        context.matrices,
        vertexConsumerProvider,
        0xFFFFFF,
    )
}