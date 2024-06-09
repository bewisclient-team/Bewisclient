package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Mutable
    @Shadow @Final private OverlayTexture overlayTexture;

    @Unique
    private double hitColor = -0.2;

    @Unique
    private float alpha = -0.2f;

    @Unique
    private boolean hitColorEnabled = false;

    @Inject(method = "getOverlayTexture", at = @At("HEAD"))
    public void getOverlayTexture(CallbackInfoReturnable<OverlayTexture> cir) {
        if(SettingsLoader.INSTANCE.getFloat("design", "blockhit.hit_overlay.alpha")!=alpha || SettingsLoader.INSTANCE.getColorSaver("design", "blockhit.hit_overlay.color").getColor()!=hitColor || SettingsLoader.INSTANCE.getBoolean("design","blockhit.hit_overlay.enabled")!=hitColorEnabled) {
            hitColor = SettingsLoader.INSTANCE.getColorSaver("design", "blockhit.hit_overlay.color").getColor();
            hitColorEnabled = SettingsLoader.INSTANCE.getBoolean("design","blockhit.hit_overlay.enabled");
            alpha = SettingsLoader.INSTANCE.getFloat("design", "blockhit.hit_overlay.alpha");
            overlayTexture.close();
            overlayTexture = new OverlayTexture();
        }
    }
}
