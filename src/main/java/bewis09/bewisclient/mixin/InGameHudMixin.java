package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    private static final Identifier EFFECT_WIDGET = new Identifier("bewisclient", "gui/effect_widget.png");
    @Shadow
    @Final
    private static Identifier PUMPKIN_BLUR;

    private int scaledHeight;

    private int scaledWidth;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int heldItemTooltipFade;

    @Shadow
    private ItemStack currentStack;

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Shadow
    @Final
    private static String SCOREBOARD_JOINER;

    @Shadow
    protected abstract boolean shouldRenderSpectatorCrosshair(HitResult hitResult);

    @Shadow
    @Final
    private DebugHud debugHud;

    @Shadow
    @Final
    private static Identifier EFFECT_BACKGROUND_TEXTURE;

    @Shadow
    @Final
    private static Identifier EFFECT_BACKGROUND_AMBIENT_TEXTURE;

    @Shadow @Final private static Comparator<ScoreboardEntry> SCOREBOARD_ENTRY_COMPARATOR;

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    public void inject(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if (((boolean) JavaSettingsSender.Companion.getSettings().getBoolean("design","disable_pumpkin_overlay")) && texture == PUMPKIN_BLUR) {
            if (JavaSettingsSender.Companion.getSettings().getBoolean("design","show_pumpkin_icon"))
                context.drawItem(Items.CARVED_PUMPKIN.getDefaultStack(), scaledWidth / 2 + 94, scaledHeight - 20);
            RenderSystem.enableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    public void renderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
        if (JavaSettingsSender.Companion.getSettings().getBoolean("design","held_item_info.held_item_info")) {
            this.client.getProfiler().push("selectedItemName");
            if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
                List<Text> tooltipList = getTooltipFromItem(this.currentStack);
                int b = 0;
                for (Text t : tooltipList) {
                    b++;
                    int l;
                    int yOff = tooltipList.size()*11-b*11;
                    int i = this.getTextRenderer().getWidth((StringVisitable)t);
                    int j = (context.getScaledWindowWidth() - i) / 2;
                    int k = context.getScaledWindowHeight() - 59;
                    if (!this.client.interactionManager.hasStatusBars()) {
                        k += 14;
                    }
                    if ((l = (int)((float)this.heldItemTooltipFade * 256.0f / 10.0f)) > 255) {
                        l = 255;
                    }
                    if (l > 0) {
                        Objects.requireNonNull(this.getTextRenderer());
                        context.fill(j - 2, k - 2 - yOff, j + i + 2, k + 9 + 2 - yOff, this.client.options.getTextBackgroundColor(0));
                        context.drawTextWithShadow(this.getTextRenderer(), (Text)t, j, k - yOff, 0xFFFFFF + (l << 24));
                    }
                }

            }
            this.client.getProfiler().pop();
            ci.cancel();
        }
    }

    public List<Text> getTooltipFromItem(ItemStack stack) {
        ArrayList<Text> list = new ArrayList<>();
        ArrayList<Text> list2 = new ArrayList<>();
        MutableText mutableText = Text.empty().append(stack.getName()).formatted(this.currentStack.getRarity().getFormatting());
        if (this.currentStack.getComponents().get(DataComponentTypes.CUSTOM_NAME)!=null) {
            mutableText.formatted(Formatting.ITALIC);
        }
        list2.add(mutableText);
        stack.getItem().appendTooltip(stack, Item.TooltipContext.DEFAULT, list, TooltipType.BASIC);
        ((ItemStackMixin)(Object) stack).invokeAppendTooltip(DataComponentTypes.STORED_ENCHANTMENTS, Item.TooltipContext.DEFAULT, list::add, TooltipType.BASIC);
        ((ItemStackMixin)(Object) stack).invokeAppendTooltip(DataComponentTypes.ENCHANTMENTS, Item.TooltipContext.DEFAULT, list::add, TooltipType.BASIC);
        ((ItemStackMixin)(Object) stack).invokeAppendTooltip(DataComponentTypes.DYED_COLOR, Item.TooltipContext.DEFAULT, list::add, TooltipType.BASIC);
        ((ItemStackMixin)(Object) stack).invokeAppendTooltip(DataComponentTypes.UNBREAKABLE, Item.TooltipContext.DEFAULT, list::add, TooltipType.BASIC);

        for (Text t : list) {
            list2.add(Text.literal(t.getString()).formatted(Formatting.GRAY));
        }
        boolean b = false;
        int i = 0;
        if ((JavaSettingsSender.Companion.getSettings().getFloat("design","held_item_info.maxinfolength") != 10f)) {
            while (list2.size() > (((float) JavaSettingsSender.Companion.getSettings().getFloat("design","held_item_info.maxinfolength"))) + 1) {
                i++;
                list2.remove(list2.size() - 1);
                b = true;
            }
        }
        if (b)
            list2.add(Text.translatable("bewisclient.hud.andmore", i).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        return list2;
    }

    public void appendBookInfo(ItemStack i, List<Text> list) {
        if (i.itemMatches(Items.WRITTEN_BOOK.getRegistryEntry())) {
            i.getItem().appendTooltip(i, Item.TooltipContext.DEFAULT, list, TooltipType.BASIC);
        }
    }

    public void appendShulkerBoxInfo(ItemStack d, List<Text> list) {
        try {
            if (d.getItem() instanceof BlockItem) {
                if (((BlockItem) d.getItem()).getBlock() instanceof ShulkerBoxBlock) {
                    DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                    d.getComponents().getOrDefault(DataComponentTypes.CONTAINER,null).copyTo(defaultedList);
                    for (ItemStack itemStack : defaultedList) {
                        if (itemStack.getCount() != 0) {
                            MutableText mutableText = itemStack.getName().copy();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount())).formatted(Formatting.GRAY);
                            list.add(mutableText);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    int getValue(double value, int min, int max) {
        return (int) Math.round((value * (max - min) + min));
    }

    ///**
    // * @author Mojang
    // * @reason Why Not?
    // */
//
    //@Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    //protected void renderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
    //    if (JavaSettingsSender.Companion.getDesignSettings().getValue("extend_status_effect_info")) {
    //        Collection<StatusEffectInstance> collection;
    //        label40:
    //        {
    //            assert this.client.player != null;
    //            collection = this.client.player.getStatusEffects();
    //            if (!collection.isEmpty()) {
    //                Screen var4 = this.client.currentScreen;
    //                if (!(var4 instanceof AbstractInventoryScreen<?> abstractInventoryScreen)) {
    //                    break label40;
    //                }
//
    //                if (!abstractInventoryScreen.hideStatusEffectHud()) {
    //                    break label40;
    //                }
    //            }
//
    //            return;
    //        }
//
    //        RenderSystem.enableBlend();
    //        int i = 0;
    //        int j = 0;
    //        StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
    //        List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
//
    //        for (StatusEffectInstance statusEffectInstance : Ordering.natural().reverse().sortedCopy(collection)) {
    //            int n;
    //            StatusEffect statusEffect = statusEffectInstance.getEffectType();
    //            if (!statusEffectInstance.shouldShowIcon()) continue;
    //            int k = this.scaledWidth + 4;
    //            int l = WidgetRenderer.Companion.getEffectWidget().getPosY() + 1;
    //            if (this.client.isDemo()) {
    //                l += 15;
    //            }
    //            if (statusEffect.isBeneficial()) {
    //                k -= 33 * ++i;
    //            } else {
    //                k -= 33 * ++j;
    //                l += 37;
    //            }
    //            float f = 1.0f;
    //            if (statusEffectInstance.isAmbient()) {
    //                context.drawGuiTexture(EFFECT_BACKGROUND_AMBIENT_TEXTURE, k, l, 24, 24);
    //                context.drawTexture(EFFECT_WIDGET, k - 4, l + 21, 32, 15, 32, 0, 32, 15, 64, 15);
    //            } else {
    //                context.drawGuiTexture(EFFECT_BACKGROUND_TEXTURE, k, l, 24, 24);
    //                context.drawTexture(EFFECT_WIDGET, k - 4, l + 21, 32, 15, 0, 0, 32, 15, 64, 15);
    //                if (statusEffectInstance.isDurationBelow(200)) {
    //                    int m = statusEffectInstance.getDuration();
    //                    n = 10 - m / 20;
    //                    f = MathHelper.clamp((float) m / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + MathHelper.cos((float) m * (float) Math.PI / 5.0f) * MathHelper.clamp((float) n / 10.0f * 0.25f, 0.0f, 0.25f);
    //                }
    //            }
    //            context.drawCenteredTextWithShadow(getTextRenderer(), statusEffectInstance.isDurationBelow(120000) ? MathUtil.Companion.zeroBefore(statusEffectInstance.getDuration() / 1200, 2) + ":" + MathUtil.Companion.zeroBefore((statusEffectInstance.getDuration() % 1200) / 20, 2) : "**:**", k + 12, l + 25, -1);
//
    //            Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
    //            n = k;
    //            int o = l;
    //            float g = f;
    //            int finalN = n;
    //            list.add(() -> {
    //                context.setShaderColor(1.0f, 1.0f, 1.0f, g);
    //                context.drawSprite(finalN + 3, o + 3, 0, 18, 18, sprite);
    //                context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    //            });
    //        }
//
    //        list.forEach(Runnable::run);
    //        ci.cancel();
    //    } else {
    //        AbstractInventoryScreen abstractInventoryScreen;
    //        Screen screen;
    //        Collection<StatusEffectInstance> collection = this.client.player.getStatusEffects();
    //        if (collection.isEmpty() || (screen = this.client.currentScreen) instanceof AbstractInventoryScreen && (abstractInventoryScreen = (AbstractInventoryScreen) screen).hideStatusEffectHud()) {
    //            return;
    //        }
    //        RenderSystem.enableBlend();
    //        int i = 0;
    //        int j = 0;
    //        StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
    //        ArrayList<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
    //        for (StatusEffectInstance statusEffectInstance : Ordering.natural().reverse().sortedCopy(collection)) {
    //            int n;
    //            StatusEffect statusEffect = statusEffectInstance.getEffectType();
    //            if (!statusEffectInstance.shouldShowIcon()) continue;
    //            int k = this.scaledWidth;
    //            int l = WidgetRenderer.Companion.getEffectWidget().getPosY() + 1;
    //            if (this.client.isDemo()) {
    //                l += 15;
    //            }
    //            if (statusEffect.isBeneficial()) {
    //                k -= 25 * ++i;
    //            } else {
    //                k -= 25 * ++j;
    //                l += 26;
    //            }
    //            float f = 1.0f;
    //            if (statusEffectInstance.isAmbient()) {
    //                context.drawGuiTexture(EFFECT_BACKGROUND_AMBIENT_TEXTURE, k, l, 24, 24);
    //            } else {
    //                context.drawGuiTexture(EFFECT_BACKGROUND_TEXTURE, k, l, 24, 24);
    //                if (statusEffectInstance.isDurationBelow(200)) {
    //                    int m = statusEffectInstance.getDuration();
    //                    n = 10 - m / 20;
    //                    f = MathHelper.clamp((float) m / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + MathHelper.cos((float) m * (float) Math.PI / 5.0f) * MathHelper.clamp((float) n / 10.0f * 0.25f, 0.0f, 0.25f);
    //                }
    //            }
    //            Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
    //            n = k;
    //            int o = l;
    //            float g = f;
    //            int finalN = n;
    //            list.add(() -> {
    //                context.setShaderColor(1.0f, 1.0f, 1.0f, g);
    //                context.drawSprite(finalN + 3, o + 3, 0, 18, 18, sprite);
    //                context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    //            });
    //        }
    //        list.forEach(Runnable::run);
//
    //        ci.cancel();
    //    }
    //}
}