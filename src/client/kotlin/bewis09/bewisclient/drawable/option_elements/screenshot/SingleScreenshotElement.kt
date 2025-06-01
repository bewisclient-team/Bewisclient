package bewis09.bewisclient.drawable.option_elements.screenshot

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.autoUpdate.UpdateClass
import bewis09.bewisclient.dialog.ClickDialog
import bewis09.bewisclient.dialog.Dialog
import bewis09.bewisclient.dialog.TextDialog
import bewis09.bewisclient.drawable.ScalableButtonWidget
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.*
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.util.Util
import org.apache.commons.io.FileUtils
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*
import kotlin.math.roundToInt

class SingleScreenshotElement(val image: ScreenshotElement.SizedIdentifier) : OptionElement("", "") {
    val open = ScalableButtonWidget.builder(Bewisclient.getTranslationText("open_screenshot")) {
        Util.getOperatingSystem().open(File(FabricLoader.getInstance().gameDir.toString() + "\\screenshots\\" + image.name))
    }.build()

    val openFolder = ScalableButtonWidget.builder(Bewisclient.getTranslationText("open_screenshot_folder")) {
        Util.getOperatingSystem().open(File(FabricLoader.getInstance().gameDir.toString() + "\\screenshots"))
    }.build()

    val copy = ScalableButtonWidget.builder(Bewisclient.getTranslationText("copy_screenshot")) {
        if (Util.getOperatingSystem() != Util.OperatingSystem.WINDOWS) {
            Dialog.addDialog(ClickDialog(Bewisclient.getTranslationText("info.screenshot.copy_not_win"), Bewisclient.getTranslationText("info.screenshot.github")) {
                val screen = MinecraftClient.getInstance().currentScreen

                MinecraftClient.getInstance().setScreen(ConfirmLinkScreen({ confirmed: Boolean ->
                    Dialog.proceed()

                    if (confirmed) {
                        Util.getOperatingSystem()
                            .open("https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Other&assignee=Bewis09&title=Copying%20image%20with%20glfw")
                    }

                    MinecraftClient.getInstance().setScreen(screen)
                }, "https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Other&assignee=Bewis09&title=Copying%20image%20with%20glfw", true))
            })

            return@builder
        }

        Dialog.addDialog(TextDialog(Bewisclient.getTranslationText("info.screenshot.copied")))

        val imagePath = getRelativeGamePath("screenshots\\" + image.name)

        val f2 = getRelativeGameFile("bewisclient\\java\\ImageCopy\$TransferableImage.class")

        f2.parentFile.mkdirs()
        f2.createNewFile()

        FileUtils.copyInputStreamToFile(ByteArrayInputStream(Base64.getDecoder().decode(UpdateClass.COPY_SUB_CLASS)), f2)

        val f = getRelativeGameFile("bewisclient\\java\\ImageCopy.class")

        f.parentFile.mkdirs()
        f.createNewFile()

        FileUtils.copyInputStreamToFile(ByteArrayInputStream(Base64.getDecoder().decode(UpdateClass.COPY_CLASS)), f)

        val javaHome = System.getProperty("java.home")
        var l = File(javaHome)
        l = File(l, "bin")
        l = File(l, "java.exe")

        val builder = ProcessBuilder(
            "cmd.exe", "/c",
            "cd " + getRelativeGamePath("bewisclient\\java")
                    + " && " +
                    l + " ImageCopy \"" + imagePath + "\""
        )

        builder.redirectErrorStream(true)
        builder.start()
    }.build()

    val delete = ScalableButtonWidget.builder(Bewisclient.getTranslationText("delete_screenshot")) {
        Dialog.addDialog(ClickDialog(Bewisclient.getTranslationText("info.screenshot.really_delete"), Bewisclient.getTranslationText("info.screenshot.delete")) {
            (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).goBack()
            ScreenshotElement.screenshots.remove(image)
            getRelativeGameFile("screenshots\\" + image.name).delete()
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
        alpha: Float
    ): Int {
        val imgHeight = if (image.width / (image.height.toFloat()) < 16 / 9f) width * 9f / 16 else image.height / (image.width.toFloat()) * width
        val imgWidth = if (image.width / (image.height.toFloat()) < 16 / 9f) image.width / (image.height.toFloat()) * width * 9f / 16 else width.toFloat()

        context.fillWithBorder(
            x - 1, y - 1, width + 2, ((width * 9 / 16f) + 2).roundToInt(), applyAlpha(0, alpha), applyAlpha(0xFFFFFF, alpha)
        )

        context.drawTexture(
            image.getInitializedIdentifier(), x + (width - imgWidth.roundToInt()) / 2,
            (y + (((width * 9 / 16f)).roundToInt() - imgHeight) / 2).roundToInt(), imgWidth.roundToInt(), imgHeight.roundToInt(),
            alpha = alpha
        )

        val buttonY = y + (width * 9 / 16f).roundToInt() + 4
        val buttonWidth = ((width - 6) / 4f).toInt()

        open.setDimensionsAndPosition(buttonWidth, 20, x, buttonY)
        openFolder.setDimensionsAndPosition(buttonWidth, 20, (x + (buttonWidth + 2)), buttonY)
        copy.setDimensionsAndPosition(buttonWidth, 20, (x + (buttonWidth + 2) * 2), buttonY)
        delete.setDimensionsAndPosition(buttonWidth, 20, (x + (buttonWidth + 2) * 3), buttonY)

        open.render(context, mouseX, mouseY, 0f)
        openFolder.render(context, mouseX, mouseY, 0f)
        copy.render(context, mouseX, mouseY, 0f)
        delete.render(context, mouseX, mouseY, 0f)

        return imgHeight.roundToInt()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        open.mouseClicked(mouseX, mouseY, button)
        openFolder.mouseClicked(mouseX, mouseY, button)
        copy.mouseClicked(mouseX, mouseY, button)
        delete.mouseClicked(mouseX, mouseY, button)

        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}