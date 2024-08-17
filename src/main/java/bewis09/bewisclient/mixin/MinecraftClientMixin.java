package bewis09.bewisclient.mixin;

import bewis09.bewisclient.Bewisclient;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "stop",at = @At("HEAD"), cancellable = true)
    private void inject(CallbackInfo ci) throws IOException {
        if (!System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win")) {
            ci.cancel();
        }

        if(Bewisclient.INSTANCE.getUpdate()!=null && SettingsLoader.INSTANCE.get("general", Settings.Companion.getEXPERIMENTAL(),Settings.Companion.getAUTO_UPDATE())) {
            var javaHome = System.getProperty("java.home");
            var f = new File(javaHome);
            f = new File(f, "bin");
            f = new File(f, "javaw.exe");

            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c",
                    "cd " + FabricLoader.getInstance().getGameDir() + "\\bewisclient\\java\\ "
                            + "&& " +
                    f + " JavaUpdater " + FabricLoader.getInstance().getGameDir() + " " + Bewisclient.INSTANCE.getUpdate().get("name").getAsString().toLowerCase().replace(" ","-")
            );
            builder.redirectErrorStream(true);
            builder.start();
        }
    }
}
