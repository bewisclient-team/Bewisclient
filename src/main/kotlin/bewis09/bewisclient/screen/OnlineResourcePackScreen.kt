package bewis09.bewisclient.screen

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.UsableTexturedButtonWidget
import bewis09.bewisclient.screen.AddResourcePackScreen.Companion.LOADING_ID
import bewis09.bewisclient.screen.AddResourcePackScreen.Companion.loadedImages
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.text.OrderedText
import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Matrix4f

class OnlineResourcePackScreen(val parent: Screen, val resourcePack: AddResourcePackScreen.ResourcePack): Screen(Text.empty()) {
    val drawables = arrayListOf<Drawable>()

    override fun close() {
        MinecraftClient.getInstance().setScreen(parent)
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(context,mouseX, mouseY, delta)

        context!!.fill((this.width/4)-2, 0, (this.width/4f*3+2).toInt(),this.height,
            0xAA000000.toInt()
        )

        fillGradient(context, (this.width/4) -8,0, ((this.width/4)) -2,this.height,0,
            (0xAA000000).toInt()
        )

        fillGradient(context, (this.width/4f*3+2).toInt(),0,(this.width/4f*3+2).toInt()+6,this.height,
            (0xAA000000).toInt(), 0)

        val description = textRenderer.wrapLines(StringVisitable.plain(resourcePack.description),width/2-60)

        context.fill(width/4,2,width/4*3,38, 0xFF000000.toInt())
        context.drawBorder(width/4,2,width/2,38, -1)

        context.drawTextWithShadow(textRenderer,resourcePack.title,width/4+44,6,-1)
        if(description.size!=0)
            context.drawTextWithShadow(textRenderer,description[0],width/4+44,17,0xFF777777.toInt())
        if(description.size>1)
            context.drawTextWithShadow(textRenderer, OrderedText.concat(description[1], OrderedText.styledForwardsVisitedString(if(description.size>2) "..." else "",
                Style.EMPTY)),width/4+44,17+11,0xFF777777.toInt())

        if (loadedImages[resourcePack.icon_url] != LOADING_ID) {
            context.drawTexture(loadedImages[resourcePack.icon_url], width / 4 + 1, 3, 36, 36, 0f, 0f, 36, 36, 36, 36)
        }

        context.drawBorder(width/4,2,38,38, -1)

        for (drawable in this.drawables) {
            drawable.render(context, mouseX, mouseY, delta)
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

    override fun init() {
        drawables.clear()

        addDrawableChild(ButtonWidget.builder(Bewisclient.getTranslationText("gui.resource_pack.description")) {

        }.dimensions(width/4,42,(width-40)/6,20).build())

        addDrawableChild(ButtonWidget.builder(Bewisclient.getTranslationText("gui.resource_pack.gallery")) {

        }.dimensions((width/4f+(width-40)/6).toInt(),42,(width-40)/6,20).build())

        addDrawableChild(ButtonWidget.builder(Bewisclient.getTranslationText("gui.resource_pack.versions")) {

        }.dimensions((width/4f+(width-40)/3).toInt(),42,(width-40)/6,20).build())

        addDrawableChild(UsableTexturedButtonWidget((width/4f*3).toInt()-20,42,20,20, ButtonTextures(
            Identifier.of("bewisclient","textures/sprites/download.png"),
            Identifier.of("bewisclient","textures/sprites/download_highlighted.png"))){

        })
    }

    override fun <T> addDrawableChild(drawableElement: T): T where T : Element?, T : Drawable?, T : Selectable? {
        drawables.add(drawableElement as Drawable)
        return super.addDrawableChild(drawableElement)
    }
}