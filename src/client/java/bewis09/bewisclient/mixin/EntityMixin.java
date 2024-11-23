package bewis09.bewisclient.mixin;

import bewis09.bewisclient.Bewisclient;
import bewis09.bewisclient.MixinStatics;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "changeLookDirection",at=@At("HEAD"),cancellable = true)
    public void inject(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if(!MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && Objects.requireNonNull(Bewisclient.INSTANCE.getFreeLookKeyBinding()).isPressed() && SettingsLoader.INSTANCE.get(Settings.GENERAL,new String[0],Settings.Companion.getPERSPECTIVE())) {
            MixinStatics.cameraAddPitch += (float) (cursorDeltaY * 0.15f);
            MixinStatics.cameraAddYaw += (float) (cursorDeltaX * 0.15f);
            ci.cancel();
        }
    }
}
