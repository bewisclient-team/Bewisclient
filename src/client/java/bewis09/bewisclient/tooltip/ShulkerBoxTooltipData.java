package bewis09.bewisclient.tooltip;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;

/**
 * The {@link TooltipData} for the ShulkerBoxTooltip
 *
 * @param inventory The inventory of the Shulker Box
 * @param color The color of the Shulker Box
 */
public record ShulkerBoxTooltipData(DefaultedList<ItemStack> inventory, DyeColor color) implements TooltipData {

    /**
     * @return The Inventory of the Shulker Box
     */
    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }
}
