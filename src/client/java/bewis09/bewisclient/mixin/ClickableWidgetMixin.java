package bewis09.bewisclient.mixin;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClickableWidget.class)
abstract
class ClickableWidgetMixin implements Drawable, Element, Widget, Selectable {

    @Shadow public boolean visible;

    @Shadow protected boolean hovered;

    @Shadow protected abstract void renderWidget(DrawContext context, int mouseX, int mouseY, float delta);

    @Shadow @Final private TooltipState tooltip;

    @Shadow public abstract boolean isHovered();

    @Shadow public abstract boolean isFocused();

    @Shadow public abstract ScreenRect getNavigationFocus();

    @Shadow public abstract int getX();

    @Shadow public abstract int getY();

    @Shadow protected int width;

    @Shadow protected int height;

    @Inject(method = "render",at=@At("HEAD"),cancellable = true)
    public final void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.visible && Objects.equals(toString(), "§disable_scissors")) {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            this.renderWidget(context, mouseX, mouseY, delta);
            this.tooltip.render(this.isHovered(), this.isFocused(), this.getNavigationFocus());

            ci.cancel();
        }
    }
}