package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
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
        if(SettingsLoader.INSTANCE.get("design", Settings.Companion.getFULLBRIGHT(),Settings.Companion.getENABLED()))
            MinecraftClient.getInstance().options.getGamma().setValue((double) SettingsLoader.INSTANCE.get("design",Settings.Companion.getFULLBRIGHT(),Settings.Companion.getFULLBRIGHT_VALUE()));
    }
}
