package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.donate.DonationAcquirer
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.ScreenValuedTypedAnimation
import bewis09.bewisclient.util.Util
import bewis09.bewisclient.util.applyAlpha
import bewis09.bewisclient.util.fillHoverableWithBorder
import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.text.OrderedText
import net.minecraft.text.Text

class DonateElement : OptionElement("", "") {
    var viewSelected = false
    var donateSelected = false
    var websiteSelected = false

    override fun render(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float
    ): Int {
        if (DonationAcquirer.data_loading_status == DonationAcquirer.State.UNSTARTED) {
            DonationAcquirer.loadDonationLink()
        }

        return when (DonationAcquirer.data_loading_status) {
            DonationAcquirer.State.LOADING -> renderLoading(context, x, y, width, mouseX, mouseY, alpha)
            DonationAcquirer.State.FINISHED -> renderData(context, x, y, width, mouseX, mouseY, alpha)
            DonationAcquirer.State.FALSE_API_LEVEL -> renderFalseLevel(context, x, y, width, mouseX, mouseY, alpha)
            DonationAcquirer.State.ERROR -> renderError(context, x, y, width, mouseX, mouseY, alpha)
            DonationAcquirer.State.UNSTARTED -> 0
        }
    }

    @Suppress("unused_parameter")
    fun renderLoading(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Bewisclient.getTranslationText("gui.donate.loading"), x + width / 2, y + 40, applyAlpha(0xFFFFFF, alpha))

        return 100
    }

    fun renderData(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float
    ): Int {
        var yz = y + 40
        val var8: Iterator<*> = MinecraftClient.getInstance().textRenderer.wrapLines(Text.of(DonationAcquirer.donation_data.description), width - 80).iterator()
        while (var8.hasNext()) {
            val orderedText = var8.next() as OrderedText
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText, x + width / 2, yz, applyAlpha(0xFFFFFF, alpha))
            yz += 9
        }

        yz += 18

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer, Text.translatable(
                "bewisclient.gui.donate.current_goal",
                DonationAcquirer.donation_data.amount_raised.value,
                DonationAcquirer.donation_data.goal.value,
                DonationAcquirer.donation_data.amount_raised.currency
            ), x + width / 2, yz, applyAlpha(0xFFFFFF, alpha)
        )

        yz += 27

        viewSelected = context.fillHoverableWithBorder(
            x + 40, yz, width / 2 - 45, 19,
            mouseX, mouseY,
            applyAlpha(0, alpha),
            applyAlpha(0, alpha),
            applyAlpha(0xAAAAAA, alpha),
            applyAlpha(0xAAAAFF, alpha),
        )

        donateSelected = context.fillHoverableWithBorder(
            x + width / 2 + 5, yz, width / 2 - 45, 19,
            mouseX, mouseY,
            applyAlpha(0, alpha),
            applyAlpha(0, alpha),
            applyAlpha(0xAAAAAA, alpha),
            applyAlpha(0xAAAAFF, alpha),
        )

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            Bewisclient.getTranslationText("gui.donate.view"),
            x + (width / 2 - 45) / 2 + 40,
            yz + 6,
            applyAlpha(0xFFFFFF, alpha)
        )
        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            Bewisclient.getTranslationText("gui.donate.donate"),
            x + (width / 2 - 45) / 2 + 5 + width / 2,
            yz + 6,
            applyAlpha(0xFFFFFF, alpha)
        )

        yz += 56

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            Text.translatable("bewisclient.gui.donate.about", DonationAcquirer.donation_data.cause.name),
            x + width / 2,
            yz,
            applyAlpha(0xFFFFFF, alpha)
        )

        yz += 27
        val var9: Iterator<*> = MinecraftClient.getInstance().textRenderer.wrapLines(Text.of(DonationAcquirer.donation_data.cause.description), width - 80).iterator()
        while (var9.hasNext()) {
            val orderedText = var9.next() as OrderedText
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText, x + width / 2, yz, applyAlpha(0xFFFFFF, alpha))
            yz += 9
        }

        yz += 18

        websiteSelected = context.fillHoverableWithBorder(
            x + width / 4, yz, width / 2, 19,
            mouseX, mouseY,
            applyAlpha(0, alpha),
            applyAlpha(0, alpha),
            applyAlpha(0xAAAAAA, alpha),
            applyAlpha(0xAAAAFF, alpha)
        )

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Bewisclient.getTranslationText("gui.donate.website"), x + width / 2, yz + 6, applyAlpha(0xFFFFFF, alpha))

        return yz + 40 - y
    }

    @Suppress("unused_parameter")
    fun renderError(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Bewisclient.getTranslationText("gui.donate.error"), x + width / 2, y + 40, applyAlpha(0xFFFFFF, alpha))
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, DonationAcquirer.error, x + width / 2, y + 49, applyAlpha(0xFFFFFF, alpha))

        return 100
    }

    @Suppress("unused_parameter")
    fun renderFalseLevel(
        context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float
    ): Int {
        var yz = y + 40
        val var8: Iterator<*> = MinecraftClient.getInstance().textRenderer.wrapLines(
            Text.translatable(
                "bewisclient.gui.donate.api_level",
                Bewisclient.API_LEVEL,
                DonationAcquirer.error,
                SharedConstants.getGameVersion().name
            ), width - 40
        ).iterator()
        while (var8.hasNext()) {
            val orderedText = var8.next() as OrderedText
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, orderedText, x + width / 2, yz, applyAlpha(0xFFFFFF, alpha))
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