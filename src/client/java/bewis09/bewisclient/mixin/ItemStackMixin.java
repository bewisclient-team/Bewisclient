package bewis09.bewisclient.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public interface ItemStackMixin {
//    @Invoker("appendTooltip")
//    <T extends TooltipAppender> void invokeAppendTooltip(ComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type);
}
