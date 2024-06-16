package bewis09.bewisclient.mixin;

import bewis09.bewisclient.drawable.UsableTexturedButtonWidget;
import bewis09.bewisclient.screen.AddResourcePackScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackScreen.class)
public class PackScreenMixin extends Screen {
    @Unique
    UsableTexturedButtonWidget widget = null;

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Inject(method="init",at=@At("RETURN"))
    public void inject(CallbackInfo ci) {
        widget = addDrawableChild(new UsableTexturedButtonWidget(width-22,2,20,20, new ButtonTextures(Identifier.of("bewisclient","textures/sprites/modrinth_rp_button.png"),Identifier.of("bewisclient","textures/sprites/modrinth_rp_button_highlighted.png")),(b)->{
            assert this.client != null;
            this.client.setScreen(new AddResourcePackScreen(this));
        }));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(widget!=null) {
            widget.setX(width-22);
        }
        super.render(context, mouseX, mouseY, delta);
    }
}
