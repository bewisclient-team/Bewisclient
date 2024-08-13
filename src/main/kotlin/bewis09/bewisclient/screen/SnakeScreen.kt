package bewis09.bewisclient.screen

import bewis09.bewisclient.Bewisclient
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW
import kotlin.math.ceil

class SnakeScreen: Screen(Text.empty()) {

    val GREEN = Identifier.of("textures/block/green_terracotta.png")!!
    val RED = Identifier.of("textures/block/red_terracotta.png")!!
    val BLUE = Identifier.of("textures/block/blue_terracotta.png")!!
    val MELON = Identifier.of("textures/block/melon_side.png")!!

    var shouldRemoveNext = true

    var isDead = false
    var hasWon = false

    var score = 0

    val SNAKE = arrayListOf(
            Pair(5,11)
    )

    var direction = 0
    var lastDirection = direction

    init {
        move(true)
        move(true)
    }

    var APPLE = newApple()

    var o = -20

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {

        super.render(context, mouseX, mouseY, delta)

        val size = (width.coerceAtMost(height)*0.7f).toInt()

        RenderSystem.setShaderColor(0.5f,0.5f,0.5f,1f)
        context?.drawTexture(GREEN,(width-size)/2-size/10,(height-size)/2-size/10,0,0f,0f,size+size/5,size+size/5,size/10,size/10)
        RenderSystem.setShaderColor(1f,1f,1f,1f)

        context!!.drawTexture(GREEN,(width-size)/2,(height-size)/2,0,0f,0f,size,size,size/10,size/10)

        context.fillGradient((width-size)/2,(height-size)/2,(width+size)/2,(height-size)/2+10, 0xAA000000.toInt(),0)
        context.fillGradient((width-size)/2,(height+size)/2-10,(width+size)/2,(height+size)/2,0, 0xAA000000.toInt())

        fillGradient(context,(width-size)/2,(height-size)/2,(width-size)/2+10,(height+size)/2, 0xAA000000.toInt(),0)
        fillGradient(context,(width+size)/2-10,(height-size)/2,(width+size)/2,(height+size)/2,0, 0xAA000000.toInt())

        var i = -1
        SNAKE.forEach {
            i++

            context.drawTexture(if(i==SNAKE.size-1) BLUE else RED,
                    (width-size)/2+(size/21f*it.first).toInt(),
                    (height-size)/2+(size/21f*it.second).toInt(),
                    0,0f,0f,
                    ceil(size/21f).toInt(),
                    ceil(size/21f).toInt(),
                    size/21,
                    size/21
            )
        }

        context.drawTexture(MELON,
                (width-size)/2+(size/21f*APPLE.first).toInt(),
                (height-size)/2+(size/21f*APPLE.second).toInt(),
                0,0f,0f,
                ceil(size/21f).toInt(),
                ceil(size/21f).toInt(),
                ceil(size/21f).toInt(),
                ceil(size/21f).toInt()
        )

        if(isDead) {
            context.matrices.scale(4f,4f,4f)
            context.drawCenteredTextWithShadow(textRenderer,Bewisclient.getTranslationText("you_died"),width/8,height/8-9,-1)
            context.matrices.scale(0.5f,0.5f,0.5f)
            context.drawCenteredTextWithShadow(textRenderer,Bewisclient.getTranslatedString("score")+": "+score,width/4,height/4,-1)
            context.matrices.scale(0.5f,0.5f,0.5f)
        }

        if(hasWon) {
            context.matrices.scale(4f,4f,4f)
            context.drawCenteredTextWithShadow(textRenderer,Bewisclient.getTranslationText("you_won"),width/8,height/8-9,-1)
            context.matrices.scale(0.25f,0.25f,0.25f)
        }
    }

    private fun fillGradient(context: DrawContext, startX: Int, startY: Int, endX: Int, endY: Int, colorStart: Int, colorEnd: Int) {
        val vertexConsumer: VertexConsumer = context.vertexConsumers.getBuffer(RenderLayer.getGui())
        val matrix4f: Matrix4f = context.matrices.peek().positionMatrix
        vertexConsumer.vertex(matrix4f, endX.toFloat(), startY.toFloat(), 5f).color(colorEnd)
        vertexConsumer.vertex(matrix4f, startX.toFloat(), startY.toFloat(), 5f).color(colorStart)
        vertexConsumer.vertex(matrix4f, startX.toFloat(), endY.toFloat(), 5f).color(colorStart)
        vertexConsumer.vertex(matrix4f, endX.toFloat(), endY.toFloat(), 5f).color(colorEnd)
    }

    override fun tick() {
        o++
        val space = 2
        if(o==space) {
            move(!shouldRemoveNext)
        }
        o%=space
    }

    fun newApple(): Pair<Int,Int> {
        var a = Pair((Math.random()*10).toInt()*2+1,(Math.random()*10).toInt()*2+1)
        if(SNAKE.contains(a))
            a = newApple()
        return a
    }

    fun move(shouldNotRemove: Boolean) {
        shouldRemoveNext = true
        if(SNAKE[SNAKE.size-1].first%2==1 && SNAKE[SNAKE.size-1].second%2==1)
            lastDirection = direction
        val new = direct(SNAKE[SNAKE.size-1])
        if(SNAKE.contains(new) || new.first<0 || new.second<0 || new.second>20 || new.first>20) {
            isDead = true
            return
        }
        SNAKE.add(new)
        if(APPLE != new && !shouldNotRemove) SNAKE.removeAt(0)
        else if(APPLE == new) {
            MinecraftClient.getInstance().soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BELL, 1.0f))
            score++
            APPLE=newApple()
            shouldRemoveNext = false
        }
    }

    fun direct(i: Pair<Int,Int>): Pair<Int,Int> {
        var j = i.copy()
        if(lastDirection==0) j = Pair(j.first+1,j.second)
        if(lastDirection==1) j = Pair(j.first-1,j.second)
        if(lastDirection==2) j = Pair(j.first,j.second-1)
        if(lastDirection==3) j = Pair(j.first,j.second+1)
        return j
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(keyCode==GLFW.GLFW_KEY_W) direction=2
        if(keyCode==GLFW.GLFW_KEY_A) direction=1
        if(keyCode==GLFW.GLFW_KEY_S) direction=3
        if(keyCode==GLFW.GLFW_KEY_D) direction=0
        if((direction<2)==(lastDirection<2)) direction=lastDirection
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}