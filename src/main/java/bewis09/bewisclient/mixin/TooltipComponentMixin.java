package bewis09.bewisclient.mixin;

import bewis09.bewisclient.tooltip.ShulkerBoxTooltip;
import bewis09.bewisclient.tooltip.ShulkerBoxTooltipData;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipComponent.class)
public interface TooltipComponentMixin {
    @Inject(method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;",at=@At("HEAD"),cancellable = true)
    private static void inject(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
        if (data instanceof ShulkerBoxTooltipData) {
            cir.setReturnValue(new ShulkerBoxTooltip((ShulkerBoxTooltipData)data));
        }
    }
}
