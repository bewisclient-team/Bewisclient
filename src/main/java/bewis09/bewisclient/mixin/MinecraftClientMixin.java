package bewis09.bewisclient.mixin;

import bewis09.bewisclient.Bewisclient;
import bewis09.bewisclient.JavaSettingsSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "stop",at = @At("HEAD"))
    private void inject(CallbackInfo ci) throws IOException {
        if(Bewisclient.Companion.Companion.getUpdate()!=null && JavaSettingsSender.Companion.getSettings().getBoolean("general","experimental.auto_update")) {
            var javaHome = System.getProperty("java.home");
            var f = new File(javaHome);
            f = new File(f, "bin");
            f = new File(f, "javaw.exe");

            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c",
                    "cd " + FabricLoader.getInstance().getGameDir() + "\\bewisclient\\java\\ "
                            + "&& " +
                    f + " JavaUpdater " + FabricLoader.getInstance().getGameDir() + " " + Bewisclient.Companion.Companion.getUpdate().get("name").getAsString().toLowerCase().replace(" ","-")
            );
            builder.redirectErrorStream(true);
            builder.start();
        }
    }
}
