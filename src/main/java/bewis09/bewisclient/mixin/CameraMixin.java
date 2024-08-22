package bewis09.bewisclient.mixin;

import bewis09.bewisclient.MixinStatics;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private boolean thirdPerson;

    @ModifyArg(method = "update",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0),index = 0)
    private float injectYaw(float yaw) {
        if(thirdPerson && SettingsLoader.INSTANCE.get(Settings.GENERAL,new String[0],Settings.Companion.getPERSPECTIVE()))
            return yaw + MixinStatics.cameraAddYaw;
        return yaw;
    }

    @ModifyArg(method = "update",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0),index = 1)
    private float injectPitch(float pitch) {
        if(thirdPerson && SettingsLoader.INSTANCE.get(Settings.GENERAL,new String[0],Settings.Companion.getPERSPECTIVE()))
            return pitch + MixinStatics.cameraAddPitch;
        return pitch;
    }
}
