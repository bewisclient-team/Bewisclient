package bewis09.bewisclient.mixin;

import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TooltipAppender;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public interface ItemStackMixin {
    @Invoker("appendTooltip")
    <T extends TooltipAppender> void invokeAppendTooltip(DataComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type);
}
