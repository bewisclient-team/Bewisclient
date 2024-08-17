package bewis09.bewisclient.mixin;

import bewis09.bewisclient.drawable.UsableTexturedButtonWidget;
import bewis09.bewisclient.screen.MainOptionsScreen;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class InGameScreenMixin extends Screen {
    protected InGameScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets",at=@At("HEAD"))
    private void inject(CallbackInfo ci) {
        addDrawableChild(new UsableTexturedButtonWidget(width/2+106,height/4+56,20,20,new ButtonTextures(Identifier.of("bewisclient","textures/sprites/bewisclient_button.png"),Identifier.of("bewisclient","textures/sprites/bewisclient_button_highlighted.png")),(b)->{
            assert client != null;
            client.setScreen(new MainOptionsScreen());
        }));
    }
}
