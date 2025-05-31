package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.Settings;
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
        if(Settings.Companion.getHit_overlay().getAlpha().get()!=alpha || Settings.Companion.getHit_overlay().getColor().get().getColor()!=hitColor || Settings.Companion.getHit_overlay().get()!=hitColorEnabled) {
            hitColor = Settings.Companion.getHit_overlay().getColor().get().getColor();
            hitColorEnabled = Settings.Companion.getHit_overlay().get();
            alpha = Settings.Companion.getHit_overlay().getAlpha().get();
            overlayTexture.close();
            overlayTexture = new OverlayTexture();
        }
    }
}
