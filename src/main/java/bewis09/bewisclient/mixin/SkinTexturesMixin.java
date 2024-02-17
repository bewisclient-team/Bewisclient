package bewis09.bewisclient.mixin;

import bewis09.bewisclient.MixinStatics;
import bewis09.bewisclient.cape.AbstractCape;
import bewis09.bewisclient.cape.Cape;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(SkinTextures.class)
public abstract class SkinTexturesMixin {

    @Shadow public abstract int hashCode();

    @Inject(method = "capeTexture",at=@At("HEAD"), cancellable = true)
    void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        AbstractCape cape = Cape.getCurrentCape();
        if(cape!=null && MixinStatics.OwnPlayerSkinTextures.contains(this.hashCode())) {
            cir.setReturnValue(cape.getIdentifier(cape.getFrame()));
        }
    }
}
