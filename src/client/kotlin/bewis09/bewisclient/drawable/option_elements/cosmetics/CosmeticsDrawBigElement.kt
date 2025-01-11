package bewis09.bewisclient.drawable.option_elements.cosmetics

import bewis09.bewisclient.cosmetics.Cosmetics
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.cosmetics.CosmeticsElement.Companion.playerEntityRenderState
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.MainOptionsScreen.Companion.scale
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

class CosmeticsDrawBigElement(val right: Boolean = false): OptionElement("", "") {
    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        Cosmetics.types.forEach {
            it.currentOverwrite = Pair(true,if(it.currentlySelected == null) null else it.cosmetics[it.currentlySelected])
        }

        val m = context.matrices.peek()

        context.matrices.pop()
        context.matrices.push()

        if(MinecraftClient.getInstance().currentScreen is MainOptionsScreen) {
            if((MinecraftClient.getInstance().currentScreen as MainOptionsScreen).animation.getType() == "SLIDE") {
                val a = (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).animation.getValue()

                context.matrices.translate((1-a)*MinecraftClient.getInstance().currentScreen!!.width.toFloat()/4*(if(right) 1f else -1f),0f,0f)
            }
        }

        context.matrices.translate((MinecraftClient.getInstance().currentScreen!!.width)*(if(right) 7/8f else 1/8f),(MinecraftClient.getInstance().currentScreen!!.height)/2f + 50,500f)
        context.matrices.scale(60f,60f,-60f)

        val f = atan(((mouseX - ((MinecraftClient.getInstance().currentScreen!!.width*scale)*(if(right) 7/8f else 1/8f))) / 40.0f).toDouble().toFloat()) * if(right) 1f else -1f
        val g = -atan(((mouseY - ((MinecraftClient.getInstance().currentScreen!!.height)/2)) / 40.0f).toDouble().toFloat() * -1)

        val skinTextures = MinecraftClient.getInstance().skinProvider.getSkinTextures(MinecraftClient.getInstance().gameProfile)

        playerEntityRenderState.bodyYaw = f * 10.0f
        playerEntityRenderState.yawDegrees = f * 20f
        playerEntityRenderState.pitch = g * 10.0f
        playerEntityRenderState.skinTextures = skinTextures

        val quaternionf = Quaternionf().rotateZ(3.1415927f).rotateX((if(right) 1f else -1f) * g * 10.0f * 0.017453292f).rotateY(if(right) 0f else 3.1415927f)

        RenderSystem.setShaderLights(Vector3f(0f, 0f, 0f), Vector3f(1f, 1f, 10f))

        context.matrices.multiply(quaternionf)

        context.disableScissor()

        context.draw {
            renderEntity(it,alphaModifier,context)
        }

        context.fill(0,0,0,0,0)

        RenderSystem.setShaderColor(1f,1f,1f, 1f)

        context.matrices.pop()
        context.matrices.push()
        context.matrices.multiplyPositionMatrix(m.positionMatrix)

        context.enableScissor(MainOptionsScreen.currentScissors.x, MainOptionsScreen.currentScissors.y, MainOptionsScreen.currentScissors.x2, MainOptionsScreen.currentScissors.y2)

        Cosmetics.types.forEach {
            it.currentOverwrite = Pair(false, null)
        }

        return -8
    }
}