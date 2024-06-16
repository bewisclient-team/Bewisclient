@file:Suppress("ArrayInDataClass")

package bewis09.bewisclient.screen

import com.google.gson.Gson
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.sound.SoundEvents
import net.minecraft.text.OrderedText
import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import org.joml.Matrix4f
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class AddResourcePackScreen(val parent: Screen): Screen(Text.of("")) {
    var scroll = 0.0

    companion object {
        val gson = Gson()

        val LOADING_ID: Identifier = Identifier.of("loading")

        val loadedImages = hashMapOf<String, Identifier?>()

        val allHits = arrayListOf<ResourcePack>()

        init {
            var total_hits = 1
            var offset = 0

            while (offset < total_hits) {
                val connection =
                    (URI("https://api.modrinth.com/v2/search?limit=100&offset=${offset}&facets=[[%22project_type:resourcepack%22],[%22versions:${SharedConstants.getGameVersion().name}%22]]").toURL()
                        .openConnection()) as HttpURLConnection
                val scanner = Scanner(connection.inputStream)
                val rawRes = scanner.nextLine()
                val response = gson.fromJson(rawRes, ResourcePackResponse::class.java)
                offset+=100
                total_hits = response.total_hits
                allHits.addAll(response.hits)
            }
        }
    }

    data class ResourcePackResponse(val offset: Int, val limit: Int, val total_hits: Int, val hits: Array<ResourcePack>)

    data class ResourcePack(
        val slug: String,
        val title: String,
        val description: String,
        val categories: Array<String>,
        val client_side: String,
        val server_side: String,
        val body: String,
        val status: String,
        val requested_status: String,
        val additional_categories: Array<String>,
        val issues_url: String,
        val source_url: String,
        val wiki_url: String,
        val discord_url: String,
        val donation_urls: Array<String>,
        val project_type: String,
        val downloads: Int,
        val icon_url: String,
        val color: Int,
        val thread_id: String,
        val monetization_status: String,
        val id: String,
        val team: String,
        val published: String,
        val updated: String,
        val approved: String,
        val queued: String,
        val followers: Int,
        val license: String,
        val versions: Array<String>,
        val game_versions: Array<String>,
        val gallery: Array<String>
    )

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        context?.fill((this.width/4)-2, 0, (this.width/4*3+2),this.height,
            0xAA000000.toInt()
        )

        fillGradient(context!!, (this.width/4) -8,0, ((this.width/4)) -2,this.height,0,
            (0xAA000000).toInt()
        )

        fillGradient(context, (this.width-(this.width/4)) +2,0,this.width- (this.width/4) +8,this.height,
            (0xAA000000).toInt(), 0)

        var y = scroll.toInt()

        allHits.forEachIndexed{_,a->
            val hovered = mouseX>width/4&&mouseX<width/4*3&&y+2<mouseY&&y+38>mouseY

            val description = textRenderer.wrapLines(StringVisitable.plain(a.description),width/2-60)

            context.matrices?.push()
            if(hovered) {
                context.matrices?.translate(width / 2f, y + 19f, 1f)
                context.matrices?.scale((1 + 4f / width), (1 + 4f / width), 1f)
                context.matrices?.translate(-width / 2f, -y - 19f, 1f)
            }

            context.fill(width/4,y+2,width/4*3,y+38, 0xFF000000.toInt())
            context.drawBorder(width/4,y+2,width/2,38, if(!hovered) -1 else 0xFFAAAAFF.toInt())

            context.drawTextWithShadow(textRenderer,a.title,width/4+44,y+6,-1)
            if(description.size!=0)
                context.drawTextWithShadow(textRenderer,description[0],width/4+44,y+17,0xFF777777.toInt())
            if(description.size>1)
                context.drawTextWithShadow(textRenderer,OrderedText.concat(description[1],OrderedText.styledForwardsVisitedString(if(description.size>2) "..." else "",
                    Style.EMPTY)),width/4+44,y+17+11,0xFF777777.toInt())

            if(!(y+2>height || y+38<0)) {
                if (!loadedImages.containsKey(a.icon_url) && loadedImages[a.icon_url] != LOADING_ID){
                    downloadImage(a.icon_url)
                } else if (loadedImages[a.icon_url] != LOADING_ID) {
                    context.drawTexture(loadedImages[a.icon_url], width / 4 + 1, y + 3, 36, 36, 0f, 0f, 36, 36, 36, 36)
                }
            }
            context.drawBorder(width/4,y+2,38,38,  if(!hovered) -1 else 0xFFAAAAFF.toInt())

            y+=40

            context.matrices?.pop()
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

    var identifierSplit = 1054000

    fun downloadImage(url: String) {
        Thread {
            loadedImages[url] = LOADING_ID

            val file = File(
                FabricLoader.getInstance().gameDir.toString() + "\\bewisclient\\rp_icons\\" + url.split("/").last()
            )
            file.parentFile.mkdirs()

            if (!file.exists()) {
                val image = ImageIO.read(URL(url).openStream())
                if (image != null) ImageIO.write(image, "PNG", file)
            }

            if(!file.exists()) {
                return@Thread
            }

            val identifier = this.client?.textureManager?.registerDynamicTexture(
                identifierSplit++.toString(),
                NativeImageBackedTexture(
                    NativeImage.read(
                        FileInputStream(
                            File(
                                FabricLoader.getInstance().gameDir.toString() + "\\bewisclient\\rp_icons\\" + url.split(
                                    "/"
                                ).last()
                            )
                        )
                    )
                )
            )

            if (identifier != null)
                loadedImages[url] = identifier
        }.start()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        var y = scroll.toInt()

        allHits.forEachIndexed{_,a->
            val hovered = mouseX>width/4&&mouseX<width/4*3&&y+2<mouseY&&y+38>mouseY

            if(hovered) {
                MinecraftClient.getInstance().setScreen(OnlineResourcePackScreen(this,a))
                playDownSound(client?.soundManager!!)
            }

            y+=40
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        scroll += verticalAmount*20

        scroll = MathHelper.clamp(scroll.toFloat(), -(allHits.size*40+2).toFloat()+height,0f).toDouble()
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun close() {
        client?.setScreen(parent)
    }
}