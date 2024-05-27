package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    @Unique
    private static boolean hasFileCloned = false;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",at=@At("HEAD"))
    private void init(CallbackInfo ci) {
        if(!hasFileCloned) {
            hasFileCloned = true;
            JavaSettingsSender.Companion.cC();
        }

        if(JavaSettingsSender.Companion.getSettings().getBoolean("design","fullbright.enabled"))
            MinecraftClient.getInstance().options.getGamma().setValue((double) JavaSettingsSender.Companion.getSettings().getFloat("design","fullbright.value"));
    }
}
