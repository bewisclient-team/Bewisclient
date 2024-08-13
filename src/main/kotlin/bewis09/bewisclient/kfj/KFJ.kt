package bewis09.bewisclient.kfj

import bewis09.bewisclient.MixinStatics
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader.get
import bewis09.bewisclient.util.MathUtil
import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.common.collect.Lists
import com.google.common.collect.Ordering
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.StatusEffectSpriteManager
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.scoreboard.*
import net.minecraft.scoreboard.number.NumberFormat
import net.minecraft.scoreboard.number.StyledNumberFormat
import net.minecraft.text.StringVisitable
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

object KFJ: Settings() {
    val EFFECT_WIDGET_TEXTURE = Identifier.of("bewisclient", "gui/effect_widget.png")!!
    val EFFECT_WIDGET_AMBIENT_TEXTURE = Identifier.of("bewisclient", "gui/effect_widget_ambient.png")!!

    fun renderEffectHUD(
        context: DrawContext,
        EFFECT_BACKGROUND_AMBIENT_TEXTURE: Identifier,
        EFFECT_BACKGROUND_TEXTURE: Identifier
    ) {
        val collection: Collection<StatusEffectInstance> =
            MinecraftClient.getInstance().player?.statusEffects ?: arrayListOf()
        if (collection.isEmpty() || MinecraftClient.getInstance().currentScreen is AbstractInventoryScreen<*> && (MinecraftClient.getInstance().currentScreen as AbstractInventoryScreen<*>).hideStatusEffectHud()) {
            return
        }
        RenderSystem.enableBlend()
        var i = 0
        var j = 0
        val statusEffectSpriteManager: StatusEffectSpriteManager =
            MinecraftClient.getInstance().statusEffectSpriteManager
        val list = Lists.newArrayListWithExpectedSize<Runnable>(collection.size)
        for (sInstance in Ordering.natural<Comparable<*>>().reverse<Comparable<*>>()
            .sortedCopy<Comparable<*>>(collection)) {
            val statusEffectInstance = sInstance as StatusEffectInstance
            var n: Int
            val registryEntry: RegistryEntry<StatusEffect> = statusEffectInstance.effectType
            if (!statusEffectInstance.shouldShowIcon()) continue
            var k = context.scaledWindowWidth
            var l = WidgetRenderer.effectWidget.getPosY() + 1
            if ((registryEntry.value() as StatusEffect).isBeneficial) {
                k -= 25 * ++i
            } else {
                k -= 25 * ++j
                l += 26
            }
            var f = 1.0f
            if (statusEffectInstance.isAmbient) {
                context.drawGuiTexture(EFFECT_BACKGROUND_AMBIENT_TEXTURE, k, l, 24, 24)
            } else {
                context.drawGuiTexture(EFFECT_BACKGROUND_TEXTURE, k, l, 24, 24)
                if (statusEffectInstance.isDurationBelow(200)) {
                    val m = statusEffectInstance.duration
                    n = 10 - m / 20
                    f = MathHelper.clamp((m.toFloat() / 10.0f / 5.0f * 0.5f), 0.0f, 0.5f) + MathHelper.cos(
                        (m.toFloat() * Math.PI.toFloat() / 5.0f)
                    ) * MathHelper.clamp((n.toFloat() / 10.0f * 0.25f), 0.0f, 0.25f)
                }
            }
            val sprite: Sprite = statusEffectSpriteManager.getSprite(registryEntry)
            n = k
            val o = l
            val g = f
            val finalN = n
            list.add(Runnable {
                context.setShaderColor(1.0f, 1.0f, 1.0f, g)
                context.drawSprite(finalN + 3, o + 3, 0, 18, 18, sprite)
                context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
            })
        }
        list.forEach(Consumer { obj: Runnable -> obj.run() })
        RenderSystem.disableBlend()
    }

    fun renderEffectHUDExtended(
        context: DrawContext,
        EFFECT_BACKGROUND_AMBIENT_TEXTURE: Identifier,
        EFFECT_BACKGROUND_TEXTURE: Identifier
    ) {
        val collection: Collection<StatusEffectInstance> =
            MinecraftClient.getInstance().player?.statusEffects ?: arrayListOf()
        if (collection.isEmpty() || MinecraftClient.getInstance().currentScreen is AbstractInventoryScreen<*> && (MinecraftClient.getInstance().currentScreen as AbstractInventoryScreen<*>).hideStatusEffectHud()) {
            return
        }
        RenderSystem.enableBlend()
        var i = 0
        var j = 0
        val statusEffectSpriteManager: StatusEffectSpriteManager =
            MinecraftClient.getInstance().statusEffectSpriteManager
        val list = Lists.newArrayListWithExpectedSize<Runnable>(collection.size)
        for (sInstance in Ordering.natural<Comparable<*>>().reverse<Comparable<*>>()
            .sortedCopy<Comparable<*>>(collection)) {
            val statusEffectInstance = sInstance as StatusEffectInstance
            var n: Int
            val registryEntry: RegistryEntry<StatusEffect> = statusEffectInstance.effectType
            if (!statusEffectInstance.shouldShowIcon()) continue
            var k = context.scaledWindowWidth + 4
            var l = WidgetRenderer.effectWidget.getPosY() + 1
            if ((registryEntry.value() as StatusEffect).isBeneficial) {
                k -= 33 * ++i
            } else {
                k -= 33 * ++j
                l += 37
            }
            var f = 1.0f
            if (statusEffectInstance.isAmbient) {
                context.drawGuiTexture(EFFECT_BACKGROUND_AMBIENT_TEXTURE, k, l, 24, 24)
                context.drawTexture(EFFECT_WIDGET_AMBIENT_TEXTURE, k - 4, l + 21, 32, 15, 0f, 0f, 32, 15, 32, 15)
            } else {
                context.drawGuiTexture(EFFECT_BACKGROUND_TEXTURE, k, l, 24, 24)
                if (statusEffectInstance.isDurationBelow(200)) {
                    val m = statusEffectInstance.duration
                    n = 10 - m / 20
                    f = MathHelper.clamp((m.toFloat() / 10.0f / 5.0f * 0.5f), 0.0f, 0.5f) + MathHelper.cos(
                        (m.toFloat() * Math.PI.toFloat() / 5.0f)
                    ) * MathHelper.clamp((n.toFloat() / 10.0f * 0.25f), 0.0f, 0.25f)
                }
                context.drawTexture(EFFECT_WIDGET_TEXTURE, k - 4, l + 21, 32, 15, 0f, 0f, 32, 15, 32, 15)
            }
            context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                if (statusEffectInstance.isDurationBelow(120000)) MathUtil.zeroBefore(
                    statusEffectInstance.duration / 1200,
                    2
                ) + ":" + MathUtil.zeroBefore(
                    (statusEffectInstance.duration % 1200) / 20, 2
                ) else "**:**", k - 3 + 15, l + 25, -1
            )
            val sprite: Sprite = statusEffectSpriteManager.getSprite(registryEntry)
            n = k
            val o = l
            val g = f
            val finalN = n
            list.add(Runnable {
                context.setShaderColor(1.0f, 1.0f, 1.0f, g)
                context.drawSprite(finalN + 3, o + 3, 0, 18, 18, sprite)
                context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
            })
        }
        list.forEach(Consumer { obj: Runnable -> obj.run() })
        RenderSystem.disableBlend()
    }

    fun renderScoreboard(context: DrawContext, objective: ScoreboardObjective, SCOREBOARD_ENTRY_COMPARATOR: Comparator<ScoreboardEntry>, SCOREBOARD_JOINER: String) {
        context.matrices.push()

        val scale = get("design", SCOREBOARD, SCALE)

        context.matrices.scale(scale,scale,scale)
        context.matrices.translate(-MinecraftClient.getInstance().window.scaledWidth.toFloat()+MinecraftClient.getInstance().window.scaledWidth.toFloat()/scale,-MinecraftClient.getInstance().window.scaledWidth.toFloat()/4+MinecraftClient.getInstance().window.scaledWidth.toFloat()/scale/4,0f)
        val scoreboard: Scoreboard = objective.scoreboard
        val numberFormat: NumberFormat = objective.getNumberFormatOr(StyledNumberFormat.RED as NumberFormat)
        val sidebarEntrys =
            scoreboard.getScoreboardEntries(objective).stream().filter { score: ScoreboardEntry -> !score.hidden() }
                .sorted(SCOREBOARD_ENTRY_COMPARATOR).limit(15L)
                .map { scoreboardEntry: ScoreboardEntry ->
                    val team = scoreboard.getScoreHolderTeam(scoreboardEntry.owner())
                    val text = scoreboardEntry.name()
                    val text2 = Team.decorateName(team as AbstractTeam?, text as Text)
                    val text3 = scoreboardEntry.formatted(numberFormat)
                    val i: Int = MinecraftClient.getInstance().textRenderer.getWidth(text3 as StringVisitable)
                    MixinStatics.SidebarEntry(text2 as Text, text3 as Text, i)
                }.toArray<MixinStatics.SidebarEntry?> { size: Int ->
                    arrayOfNulls<MixinStatics.SidebarEntry>(
                        size
                    )
                } as Array<MixinStatics.SidebarEntry?>
        val text: Text = objective.displayName
        var i: Int
        var j: Int = MinecraftClient.getInstance().textRenderer.getWidth(text as StringVisitable).also { i = it }
        val k: Int = MinecraftClient.getInstance().textRenderer.getWidth(SCOREBOARD_JOINER)
        for (sidebarEntry in sidebarEntrys) {
            j = max(
                j.toDouble(),
                (MinecraftClient.getInstance().textRenderer
                    .getWidth(sidebarEntry!!.name as StringVisitable) + (if (sidebarEntry.scoreWidth > 0) k + sidebarEntry.scoreWidth else 0)).toDouble()
            )
                .toInt()
        }
        val l = j
        @Suppress("DEPRECATION")
        context.draw {
            val length = sidebarEntrys.size
            Objects.requireNonNull(MinecraftClient.getInstance().textRenderer)
            val lk = length * 9
            val m: Int = context.scaledWindowHeight / 2 + lk / 3
            val o: Int = context.scaledWindowWidth - l - 3
            val p: Int = context.scaledWindowWidth - 3 + 2
            val q: Int = MinecraftClient.getInstance().options.getTextBackgroundColor(0.3f)
            val r: Int = MinecraftClient.getInstance().options.getTextBackgroundColor(0.4f)
            Objects.requireNonNull(MinecraftClient.getInstance().textRenderer)
            val s = m - length * 9
            Objects.requireNonNull(MinecraftClient.getInstance().textRenderer)
            context.fill(o - 2, s - 9 - 1, p, s - 1, r)
            context.fill(o - 2, s - 1, p, m, q)
            val textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer
            val n2 = o + l / 2 - i / 2
            Objects.requireNonNull(MinecraftClient.getInstance().textRenderer)
            context.drawText(textRenderer, text, n2, s - 9, Colors.WHITE, false)
            for (t in 0 until length) {
                val sidebarEntry = sidebarEntrys[t]
                Objects.requireNonNull(MinecraftClient.getInstance().textRenderer)
                val u = m - (length - t) * 9
                context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    sidebarEntry!!.name,
                    o,
                    u,
                    Colors.WHITE,
                    false
                )
                if(get("design", SCOREBOARD, HIDE_NUMBERS)) continue
                context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    sidebarEntry.score,
                    p - sidebarEntry.scoreWidth,
                    u,
                    Colors.WHITE,
                    false
                )
            }
        }
        context.matrices.pop()
    }

    fun overlayTexture(texture: NativeImageBackedTexture) {
        val nativeImage: NativeImage = texture.image!!

        for (i in 0..15) {
            for (j in 0..15) {
                if (i < 8) {
                    nativeImage.setColor(
                        j, i, ColorHelper.Abgr.getAbgr((1- get("design", HIT_OVERLAY, ALPHA) *255).toInt(),ColorHelper.Argb.getBlue(
                            get("design", HIT_OVERLAY, COLOR)
                                .getColor()),ColorHelper.Argb.getGreen(
                            get("design", HIT_OVERLAY, COLOR)
                                .getColor()),ColorHelper.Argb.getRed(
                            get("design", HIT_OVERLAY, COLOR)
                                .getColor()))
                    )
                } else {
                    val k = ((1.0f - j.toFloat() / 15.0f * 0.75f) * 255.0f).toInt()
                    nativeImage.setColor(j, i, k shl 24 or 0xffffff)
                }
            }
        }

        RenderSystem.activeTexture(33985)
        texture.bindTexture()
        nativeImage.upload(0, 0, 0, 0, 0, nativeImage.width, nativeImage.height, false, true, false, false)
        RenderSystem.activeTexture(33984)
    }
}