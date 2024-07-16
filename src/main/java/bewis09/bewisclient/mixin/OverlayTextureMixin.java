package bewis09.bewisclient.mixin;

import bewis09.bewisclient.kfj.KFJ;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Shadow @Final private NativeImageBackedTexture texture;

    @Inject(method = "<init>",at=@At("RETURN"))
    private void inject(CallbackInfo ci) {
        if(SettingsLoader.INSTANCE.getBoolean("design","blockhit.hit_overlay.enabled"))
            KFJ.INSTANCE.overlayTexture(texture);
    }
}
