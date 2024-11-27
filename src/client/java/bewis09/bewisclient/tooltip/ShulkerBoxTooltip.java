package bewis09.bewisclient.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.awt.*;

/**
 * The {@link TooltipComponent} for the ShulkerBoxTooltip
 */
public class ShulkerBoxTooltip implements TooltipComponent {
    public static final Identifier TEXTURE = Identifier.of("bewisclient","gui/slot.png");
    private final DefaultedList<ItemStack> inventory;
    private final Color color;

    /**
     * @param data The {@link ShulkerBoxTooltipData} of the Shulker Box
     */
    public ShulkerBoxTooltip(ShulkerBoxTooltipData data) {
        this.inventory = data.getInventory();
        this.color = new Color(data.color().getEntityColor());
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return 3 * 18 + 6+16;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 9 * 18 + 2+16;
    }



    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        context.drawTexture(RenderLayer::getGuiTexturedOverlay,Identifier.of("bewisclient","gui/shulker_box_background.png"),x+1, y+1, 0, 0, 178, 70,178,70);
        RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f);
        int i = 0;
        for(int k = 0; k<3; k++) {
            for(int l = 0; l<9; l++) {
                int n = x + l * 18 + 1+8;
                int o = y + k * 18 + 1+8;
                this.drawSlot(n, o, i, textRenderer, context);
                i++;
            }
        }
    }

    /**
     * Draws a slot
     *
     * @param x The x-position of the slot
     * @param y The y-position of the slot
     * @param index The index of the slot
     * @param textRenderer The {@link TextRenderer}
     * @param context The {@link DrawContext} that the slot gets rendered on
     */
    private void drawSlot(int x, int y, int index, TextRenderer textRenderer, DrawContext context) {
        if (index >= this.inventory.size()) {
            return;
        }
        ItemStack itemStack = this.inventory.get(index);
        this.draw(context, x, y);
        RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f);
        context.fill(0,0,0,0,0);
        RenderSystem.setShaderColor(1,1,1,1);
        context.drawStackOverlay(textRenderer,itemStack, x + 1, y + 1);
        context.drawItem(itemStack, x + 1, y + 1);
    }

    /**
     * Draws the background texture
     *
     * @param x The x-position of the texture
     * @param y The y-position of the texture
     * @param context The {@link DrawContext} that the texture gets rendered on
     */
    private void draw(DrawContext context, int x, int y) {
        RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f);
        context.drawTexture(RenderLayer::getGuiTexturedOverlay,TEXTURE, x, y, 0, 0, 18, 18, 18, 18);
    }
}
