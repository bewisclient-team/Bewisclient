package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.tooltip.ShulkerBoxTooltipData;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {

   @Shadow public abstract Block getBlock();

   public BlockItemMixin(Settings settings) {
       super(settings);
   }

   @SuppressWarnings("all")
   @Override
   public Optional<TooltipData> getTooltipData(ItemStack stack) {
       if(getBlock() instanceof ShulkerBoxBlock && (JavaSettingsSender.Companion.getSettings().getBoolean("design","shulker_box_tooltip"))) {
           DefaultedList<ItemStack> stacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
           if(stack.getComponents().getOrDefault(DataComponentTypes.CONTAINER,null)!=null) {
               stack.getComponents().getOrDefault(DataComponentTypes.CONTAINER,null).copyTo(stacks);
               return Optional.of(new ShulkerBoxTooltipData(stacks,((ShulkerBoxBlock) getBlock()).getColor()==null ? DyeColor.PURPLE : ((ShulkerBoxBlock) getBlock()).getColor()));
           }
       }
       return super.getTooltipData(stack);
   }

   @Inject(method = "appendTooltip",at=@At("HEAD"),cancellable = true)
   public void inject(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
       if(getBlock() instanceof ShulkerBoxBlock && JavaSettingsSender.Companion.getSettings().getBoolean("design","shulker_box_tooltip")) ci.cancel();
   }
}
