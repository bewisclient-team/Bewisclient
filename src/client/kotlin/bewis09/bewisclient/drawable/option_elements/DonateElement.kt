package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.donate.DonationAcquirer
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.ScreenValuedTypedAnimation
import bewis09.bewisclient.util.Util
import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.text.OrderedText
import net.minecraft.text.Text

class DonateElement: OptionElement("","") {
    var viewSelected = false
    var donateSelected = false
    var websiteSelected = false

    override fun render(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long
    ): Int {
        if (DonationAcquirer.data_loading_status == DonationAcquirer.State.UNSTARTED) {
            DonationAcquirer.loadDonationLink()
        }

        return when (DonationAcquirer.data_loading_status) {
            DonationAcquirer.State.LOADING -> renderLoading(context, x, y, width, mouseX, mouseY, alphaModifier)
            DonationAcquirer.State.FINISHED -> renderData(context, x, y, width, mouseX, mouseY, alphaModifier)
            DonationAcquirer.State.FALSE_API_LEVEL -> renderFalseLevel(context, x, y, width, mouseX, mouseY, alphaModifier)
            DonationAcquirer.State.ERROR -> renderError(context, x, y, width, mouseX, mouseY, alphaModifier)
            DonationAcquirer.State.UNSTARTED -> 0
        }
    }

    @Suppress("unused_parameter")
    fun renderLoading(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("gui.donate.loading"),x+width/2,y+40,(alphaModifier+0xFFFFFF).toInt())

        return 100
    }

    fun renderData(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long
    ): Int {
        var yz = y + 40
        val var8: Iterator<*> = MinecraftClient.getInstance().textRenderer.wrapLines(Text.of(DonationAcquirer.donation_data.description), width - 80).iterator()
        while (var8.hasNext()) {
            val orderedText = var8.next() as OrderedText
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText, x + width / 2, yz, (alphaModifier + 0xFFFFFF).toInt())
            yz += 9
        }

        yz += 18

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer, Text.translatable(
                "bewisclient.gui.donate.current_goal",
                DonationAcquirer.donation_data.amount_raised.value,
                DonationAcquirer.donation_data.goal.value,
                DonationAcquirer.donation_data.amount_raised.currency
            ), x + width / 2, yz, (alphaModifier + 0xFFFFFF).toInt()
        )

        yz += 27

        viewSelected = Util.isIn(mouseX,mouseY,x+40,yz,x+width/2-5,yz+19)
        donateSelected = Util.isIn(mouseX,mouseY,x+width/2+5,yz,x+width-20,yz+19)

        context.fill(x+40,yz,x+width/2-5,yz+19,alphaModifier.toInt())
        context.fill(x+width/2+5,yz,x+width-40,yz+19,alphaModifier.toInt())

        context.drawBorder(x+40,yz,width/2-45,19,(alphaModifier+(if(viewSelected) 0xAAAAFF else 0xAAAAAA)).toInt())
        context.drawBorder(x+width/2+5,yz,width/2-45,19,(alphaModifier+(if(donateSelected) 0xAAAAFF else 0xAAAAAA)).toInt())

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("gui.donate.view"),x+(width/2-45)/2+40,yz+6,(alphaModifier+0xFFFFFF).toInt())
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("gui.donate.donate"),x+(width/2-45)/2+5+width/2,yz+6,(alphaModifier+0xFFFFFF).toInt())

        yz += 56

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Text.translatable("bewisclient.gui.donate.about",DonationAcquirer.donation_data.cause.name),x+width/2,yz,(alphaModifier+0xFFFFFF).toInt())

        yz += 27
        val var9: Iterator<*> = MinecraftClient.getInstance().textRenderer.wrapLines(Text.of(DonationAcquirer.donation_data.cause.description), width - 80).iterator()
        while (var9.hasNext()) {
            val orderedText = var9.next() as OrderedText
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText, x + width / 2, yz, (alphaModifier + 0xFFFFFF).toInt())
            yz += 9
        }

        yz += 18

        websiteSelected = Util.isIn(mouseX,mouseY,x+width/4,yz,x+width/2+width/4,yz+19)

        context.fill(x+width/4,yz,x+width/2+width/4,yz+19,alphaModifier.toInt())

        context.drawBorder(x+width/2-width/4,yz,width/2,19,(alphaModifier+(if(websiteSelected) 0xAAAAFF else 0xAAAAAA)).toInt())

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("gui.donate.website"),x+width/2,yz+6,(alphaModifier+0xFFFFFF).toInt())

        return yz+40-y
    }

    @Suppress("unused_parameter")
    fun renderError(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText("gui.donate.error"),x+width/2,y+40,(alphaModifier+0xFFFFFF).toInt())
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,DonationAcquirer.error,x+width/2,y+49,(alphaModifier+0xFFFFFF).toInt())

        return 100
    }

    @Suppress("unused_parameter")
    fun renderFalseLevel(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long
    ): Int {
        var yz = y+40
        val var8: Iterator<*> = MinecraftClient.getInstance().textRenderer.wrapLines(Text.translatable("bewisclient.gui.donate.api_level",Bewisclient.API_LEVEL,DonationAcquirer.error,SharedConstants.getGameVersion().name), width - 40).iterator()
        while (var8.hasNext()) {
            val orderedText = var8.next() as OrderedText
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText,x+width/2,yz, (alphaModifier+0xFFFFFF).toInt())
            yz += 9
        }

        return 100
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (viewSelected || donateSelected || websiteSelected) {
            val url = if (viewSelected) DonationAcquirer.donation_data.url else if (donateSelected) DonationAcquirer.donation_data.donate_url else DonationAcquirer.donation_data.cause.website

            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.startAllAnimation(ConfirmLinkScreen({ confirmed: Boolean ->
                if (confirmed) {
                    net.minecraft.util.Util.getOperatingSystem().open(url)
                }
                screen.animation = ScreenValuedTypedAnimation(0f, 1f, MainOptionsScreen.AnimationState.MAIN)
                screen.animatedScreen = null
                MinecraftClient.getInstance().setScreen(screen)
            }, url, true))
        }
    }
}