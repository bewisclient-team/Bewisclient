package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.autoUpdate.UpdateClass
import bewis09.bewisclient.dialog.ClickDialog
import bewis09.bewisclient.dialog.Dialog
import bewis09.bewisclient.dialog.TextDialog
import bewis09.bewisclient.drawable.ScalableButtonWidget
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.drawTexture
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.util.Util
import org.apache.commons.io.FileUtils
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*
import kotlin.io.path.pathString
import kotlin.math.roundToInt


class SingleScreenshotElement(val image: ScreenshotElement.SizedIdentifier): OptionElement("","") {
    val open = ScalableButtonWidget.builder(Bewisclient.getTranslationText("open_screenshot")){
        Util.getOperatingSystem().open(File(FabricLoader.getInstance().gameDir.toString()+"\\screenshots\\"+image.name))
    }.build()

    val open_folder = ScalableButtonWidget.builder(Bewisclient.getTranslationText("open_screenshot_folder")){
        Util.getOperatingSystem().open(File(FabricLoader.getInstance().gameDir.toString()+"\\screenshots"))
    }.build()

    val copy = ScalableButtonWidget.builder(Bewisclient.getTranslationText("copy_screenshot")) {
        if (!System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
            Dialog.addDialog(ClickDialog(Bewisclient.getTranslationText("info.screenshot.copy_not_win"),Bewisclient.getTranslationText("info.screenshot.github")){
                val screen = MinecraftClient.getInstance().currentScreen

                MinecraftClient.getInstance().setScreen(ConfirmLinkScreen({ confirmed: Boolean ->
                    Dialog.proceed()

                    if (confirmed) {
                        Util.getOperatingSystem().open("https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Other&assignee=Bewis09&title=Copying%20image%20with%20glfw")
                    }

                    MinecraftClient.getInstance().setScreen(screen)
                }, "https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Other&assignee=Bewis09&title=Copying%20image%20with%20glfw", true))
            })

            return@builder
        }

        Dialog.addDialog(TextDialog(Bewisclient.getTranslationText("info.screenshot.copied")))

        val imagePath = (FabricLoader.getInstance().gameDir.toString()+"\\screenshots\\"+image.name).replace("{COMPUTER_USERNAME}",System.getenv("USERNAME"))

        val f2 = File(FabricLoader.getInstance().gameDir.pathString+"\\bewisclient\\java\\ImageCopy\$TransferableImage.class")

        f2.parentFile.mkdirs()
        f2.createNewFile()

        FileUtils.copyInputStreamToFile(ByteArrayInputStream(UpdateClass.COPY_SUB_CLASS), f2)

        val f = File(FabricLoader.getInstance().gameDir.pathString+"\\bewisclient\\java\\ImageCopy.class")

        f.parentFile.mkdirs()
        f.createNewFile()

        FileUtils.copyInputStreamToFile(ByteArrayInputStream(UpdateClass.COPY_CLASS), f)

        val javaHome = System.getProperty("java.home")
        var l = File(javaHome)
        l = File(l, "bin")
        l = File(l, "java.exe")

        val builder = ProcessBuilder(
            "cmd.exe", "/c",
            "cd " + FabricLoader.getInstance().gameDir + "\\bewisclient\\java\\ "
                    + "&& " +
                    l + " ImageCopy \"" + imagePath+"\""
        )

        builder.redirectErrorStream(true)
        builder.start()
    }.build()

    val delete = ScalableButtonWidget.builder(Bewisclient.getTranslationText("delete_screenshot")){
        Dialog.addDialog(ClickDialog(Bewisclient.getTranslationText("info.screenshot.really_delete"),Bewisclient.getTranslationText("info.screenshot.delete")){
            (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).goBack()
            ScreenshotElement.screenshots.remove(image)
            File(FabricLoader.getInstance().gameDir.toString()+"\\screenshots\\"+image.name).delete()
            it()
            Dialog.addDialog(TextDialog(Bewisclient.getTranslationText("info.screenshot.deleted")))
        })
    }.build()

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val img_height = if(image.width/(image.height.toFloat())<16/9f) width*9f/16 else image.height/(image.width.toFloat())*width
        val img_width = if(image.width/(image.height.toFloat())<16/9f) image.width/(image.height.toFloat())*width*9f/16 else width.toFloat()

        context.fill(x,y,x+width,y+(width*9/16f).roundToInt(), 0xFF000000.toInt())

        context.drawTexture(image.identifier,x+(width-img_width.roundToInt())/2,
            (y+(((width*9/16f)).roundToInt()-img_height)/2).roundToInt(), img_width.roundToInt(),img_height.roundToInt())

        context.drawBorder(x-1,y-1,width+2, ((width*9/16f)+2).roundToInt(), -1)

        open.setPosition(x,y+(width*9/16f).roundToInt()+4)
        open_folder.setPosition((x+(width-6)/4f+2).toInt(),y+(width*9/16f).roundToInt()+4)
        copy.setPosition((x+(width-6)/4f*2+4).toInt(),y+(width*9/16f).roundToInt()+4)
        delete.setPosition((x+(width-6)/4f*3+6).toInt(),y+(width*9/16f).roundToInt()+4)

        open.setDimensions(((width-6)/4f).toInt(),20)
        open_folder.setDimensions(((width-6)/4f).toInt(),20)
        copy.setDimensions(((width-6)/4f).toInt(),20)
        delete.setDimensions(((width-6)/4f).toInt(),20)

        open.render(context,mouseX,mouseY,0f)
        open_folder.render(context,mouseX,mouseY,0f)
        copy.render(context,mouseX,mouseY,0f)
        delete.render(context,mouseX,mouseY,0f)

        return img_height.roundToInt()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        open.mouseClicked(mouseX,mouseY,button)
        open_folder.mouseClicked(mouseX,mouseY,button)
        copy.mouseClicked(mouseX,mouseY,button)
        delete.mouseClicked(mouseX,mouseY,button)

        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}