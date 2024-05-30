package bewis09.bewisclient.kfj

import bewis09.bewisclient.util.MathUtil
import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.common.collect.Lists
import com.google.common.collect.Ordering
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.StatusEffectSpriteManager
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import java.util.function.Consumer

object KFJ {
    val EFFECT_WIDGET_TEXTURE = Identifier("bewisclient", "gui/effect_widget.png")
    val EFFECT_WIDGET_AMBIENT_TEXTURE = Identifier("bewisclient", "gui/effect_widget_ambient.png")

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
}