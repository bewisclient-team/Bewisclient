package bewis09.bewisclient.pop_up

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import bewis09.bewisclient.util.Util
import bewis09.bewisclient.util.drawTexture
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import java.awt.Color
import kotlin.math.roundToInt

class ColorPickerPopup(screen: MainOptionsScreen,
                       val path: Array<String>,
                       val id: SettingsLoader.TypedSettingID<ColorSaver>,
                       val settings: String): PopUp(screen) {
    override fun getWidth() = 200

    override fun getHeight() = 92

    var clicked = false
    var boxClicked = false

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float, x: Int, y: Int, a: Float) {
        super.render(context, mouseX, mouseY, delta, x, y, a)

        var color = SettingsLoader.get(settings,path,id)
        
        val white = (a*0xFF).toInt()*0x1000000+0xFFFFFF
        val changing = color.getOriginalColor()<0

        RenderSystem.enableBlend()
        context.drawTexture(Identifier.of("bewisclient","textures/color_space.png"),x+getWidth()-64,y+6,58,58)
        RenderSystem.setShaderColor(1f,1f,1f,a)
        RenderSystem.disableBlend()

        context.drawBorder(x+getWidth()-65,y+5,60,60, white)
        RenderSystem.setShaderColor(1f,1f,1f,1f)

        context.fill(x+3+(if(!changing) getWidth()/2-2 else 0),y+getHeight()-20,x+getWidth()/2-1+(if(!changing) getWidth()/2-2 else 0),y+getHeight()-3,(a*0xFF).toInt()*0x1000000+0x777777)
        context.fill(x+3+(if(changing) getWidth()/2-2 else 0),y+getHeight()-20,x+getWidth()/2-1+(if(changing) getWidth()/2-2 else 0),y+getHeight()-3,(a*0xFF).toInt()*0x1000000+0x33333)

        context.drawBorder(x+3,y+getHeight()-20,getWidth()/2-4,17,(a*0xFF).toInt()*0x1000000+(if(Util.isIn(mouseX,mouseY,x+3,y+getHeight()-20,x-2+getWidth()/2,y+getHeight()-3)) 0xAAAAFF else 0xFFFFFF))
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("color.static"),x+1+getWidth()/4,y+getHeight()-15,(a*0xFF).toInt()*0x1000000+(if(changing) 0xAAAAAA else 0xFFFFFF))

        context.drawBorder(x+getWidth()/2+1,y+getHeight()-20,getWidth()/2-4,17,(a*0xFF).toInt()*0x1000000+(if(Util.isIn(mouseX,mouseY,x+getWidth()/2+1,y+getHeight()-20,x-3+getWidth(),y+getHeight()-3)) 0xAAAAFF else 0xFFFFFF))
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("color.change"),x-1+getWidth()/4+getWidth()/2,y+getHeight()-15,(a*0xFF).toInt()*0x1000000+(if(changing) 0xFFFFFF else 0xAAAAAA))

        context.drawHorizontalLine(x,x+getWidth()-1,y+getHeight()-23,white)
        context.drawVerticalLine(x+getWidth()-70,y,y+getHeight()-23,white)
        context.drawHorizontalLine(x,x+getWidth()-70,y+35,white)

        val offset: Int

        if(changing) {
            val value = (if(!clicked)
                    -color.getOriginalColor()
                else
                    ((MathHelper.clamp((mouseX-x-10)/(getWidth()-88f),0f,1f)*19000).toInt()+1000))

            color = ColorSaver.of(-value)

            offset = ((value-1000)/19000f*(getWidth()-88)).toInt()

            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslatedString("gui.speed")+": "+((value/100f).roundToInt()/10f)+"s",x+(getWidth()-64)/2,y+7,white)
        } else {
            val hsb = Color.RGBtoHSB(ColorHelper.getRed(color.getOriginalColor()),ColorHelper.getGreen(color.getOriginalColor()),ColorHelper.getBlue(color.getOriginalColor()), null)

            val bri = if(!clicked)
                hsb[2]
            else
                MathHelper.clamp((mouseX-x-10)/(getWidth()-88f),0f,1f)

            val hue = if(!boxClicked)
                hsb[0]
            else
                MathHelper.clamp((mouseX-x-getWidth()+65)/58f,0f,57f/58f)

            val sat = if(!boxClicked)
                hsb[1]
            else
                MathHelper.clamp((mouseY-y-6)/58f,0f,1f)

            color = ColorSaver.of(((Color.HSBtoRGB(hue,sat,bri)+(1L shl 32))%0x1000000).toInt())

            offset = (bri*(getWidth()-88)).toInt()

            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslatedString("gui.brightness")+": "+((bri*100f).roundToInt())+"%",x+(getWidth()-64)/2,y+7,white)
        }

        context.fill(x+3,y+20,x+getWidth()-72,y+33,(a*0xFF).toInt()*0x1000000+0x444444)
        context.drawBorder(x+3,y+20,getWidth()-75,13,white)

        context.fill(x+6+offset,y+23,x+13+offset,y+30,white)

        if(Util.isIn(mouseX,mouseY,x+3,y+20,x+getWidth()-70,y+33)) {
            context.drawBorder(x+5+offset,y+22,9,9,((a*0xFF).toInt() shl 24)+0xAAAAFF)
        }

        context.fill(x+3,y+38,x+getWidth()-72,y+getHeight()-25,color.getColor()+((a*0xFF).toInt() shl 24))

        val hsb = Color.RGBtoHSB(ColorHelper.getRed(color.getColor()),ColorHelper.getGreen(color.getColor()),ColorHelper.getBlue(color.getColor()), null)

        context.drawBorder(x+getWidth()-65 + ((if(!boxClicked)
            hsb[0]
        else
            MathHelper.clamp((mouseX-x-getWidth()+65)/58f,0f,57f/58f))*58).roundToInt(),y+5 + (hsb[1]*57).roundToInt(),3,3,0xFF shl 24)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, x: Int, y: Int) {
        val color = SettingsLoader.get(settings,path,id)
        if(Util.isIn(mouseX,mouseY,x+3,y+getHeight()-20,x-2+getWidth()/2,y+getHeight()-3)) {
            SettingsLoader.set(settings, ColorSaver.of(color.getColor()), path, id)
        }
        if(Util.isIn(mouseX,mouseY,x+getWidth()/2+1,y+getHeight()-20,x-3+getWidth(),y+getHeight()-3) && color.getOriginalColor()>=0) {
            SettingsLoader.set(settings, ColorSaver.of(-10051+(Math.random()*98).toInt()), path, id)
        }
        if(Util.isIn(mouseX,mouseY,x+3,y+20,x+getWidth()-70,y+33)) {
            clicked = true
        }
        if(Util.isIn(mouseX,mouseY,x+getWidth()-65,y+7,x+getWidth()-7,y+65)) {
            boxClicked = true
            SettingsLoader.set(settings, ColorSaver.of(color.getColor()), path, id)
        }
        super.mouseClicked(mouseX, mouseY, x, y)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, x: Int, y: Int) {
        if(clicked) {
            val color = SettingsLoader.get(settings,path,id)
            if(color.getOriginalColor()<0) {
                SettingsLoader.set(
                    settings,
                    ColorSaver.of(
                        (-((MathHelper.clamp(
                            (mouseX - x - 10).toFloat() / (getWidth() - 88f),
                            0f,
                            1f
                        )) * (19000) + 1000)).toInt()
                    ),
                    id,
                    *path
                )
            } else {
                val hsb = Color.RGBtoHSB(ColorHelper.getRed(color.getOriginalColor()),ColorHelper.getGreen(color.getOriginalColor()),ColorHelper.getBlue(color.getOriginalColor()), null)
                SettingsLoader.set(
                    settings,
                    ColorSaver.of(
                        ((Color.HSBtoRGB(hsb[0],hsb[1],MathHelper.clamp((mouseX-x-10)/(getWidth()-88f),0f,1f))+(1L shl 32))%0x1000000).toInt()
                    ),
                    id,
                    *path
                )
            }
        }

        if(boxClicked) {
            val color = SettingsLoader.get(settings,path,id)
            val hsb = Color.RGBtoHSB(ColorHelper.getRed(color.getOriginalColor()),ColorHelper.getGreen(color.getOriginalColor()),ColorHelper.getBlue(color.getOriginalColor()), null)
            SettingsLoader.set(
                settings,
                ColorSaver.of(
                    ((Color.HSBtoRGB(
                        MathHelper.clamp((mouseX-x-getWidth()+65)/58f,0f,57f/58),
                        MathHelper.clamp((mouseY-y-6)/58f,0f,1f),
                        hsb[2]
                    )+(1L shl 32))%0x1000000).toInt()
                ),
                id,
                *path
            )
        }

        clicked = false
        boxClicked = false
    }
}