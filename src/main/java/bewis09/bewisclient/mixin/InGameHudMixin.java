package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.kfj.KFJ;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import bewis09.bewisclient.widgets.WidgetRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.scoreboard.*;
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

    private static final Identifier EFFECT_WIDGET = Identifier.of("bewisclient", "gui/effect_widget.png");
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
                    int yOff = tooltipList.size()*9-b*9;
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
        if((JavaSettingsSender.Companion.getSettings().getBoolean("design","shulker_box_tooltip")))
            appendShulkerBoxInfo(stack,list);
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

    /**
     * @author Mojang
     * @reason Why Not?
     */

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    protected void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (JavaSettingsSender.Companion.getSettings().getBoolean("design", "extend_status_effect_info")) {
            KFJ.INSTANCE.renderEffectHUDExtended(context, InGameHudMixin.EFFECT_BACKGROUND_AMBIENT_TEXTURE, InGameHudMixin.EFFECT_BACKGROUND_TEXTURE);
            ci.cancel();
        } else if (WidgetRenderer.Companion.getEffectWidget().getOriginalPosY() != 0) {
            KFJ.INSTANCE.renderEffectHUD(context, InGameHudMixin.EFFECT_BACKGROUND_AMBIENT_TEXTURE, InGameHudMixin.EFFECT_BACKGROUND_TEXTURE);
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",at=@At("HEAD"),cancellable = true)
    private void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if(SettingsLoader.INSTANCE.getFloat("design","scoreboard.scale")!=1 || SettingsLoader.INSTANCE.getBoolean("design","scoreboard.hide_numbers")) {
            KFJ.INSTANCE.renderScoreboard(context,objective,SCOREBOARD_ENTRY_COMPARATOR,SCOREBOARD_JOINER);
            ci.cancel();
        }
    }
}