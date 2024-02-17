package bewis09.bewisclient.tooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;

public record ShulkerBoxTooltipData(DefaultedList<ItemStack> inventory, DyeColor color) implements TooltipData {

    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }
}
