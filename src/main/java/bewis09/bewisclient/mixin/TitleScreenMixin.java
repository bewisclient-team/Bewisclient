package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",at=@At("HEAD"))
    private void init(CallbackInfo ci) {
        if(JavaSettingsSender.Companion.getSettings().get("design", Settings.Companion.getSettings().getFULLBRIGHT(),Settings.Companion.getSettings().getENABLED()))
            MinecraftClient.getInstance().options.getGamma().setValue((double) JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getFULLBRIGHT(),Settings.Companion.getSettings().getFULLBRIGHT_VALUE()));
    }
}
