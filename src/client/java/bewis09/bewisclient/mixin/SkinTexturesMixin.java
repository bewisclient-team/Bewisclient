package bewis09.bewisclient.mixin;

import bewis09.bewisclient.MixinStatics;
import bewis09.bewisclient.cosmetics.Cosmetics;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkinTextures.class)
public abstract class SkinTexturesMixin {

    @Shadow public abstract int hashCode();

    @Inject(method = "capeTexture",at=@At("HEAD"), cancellable = true)
    void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        if(Cosmetics.INSTANCE.getCapes().getTexture() != null && MixinStatics.OwnPlayerSkinTextures.contains(this.hashCode())) {
            cir.setReturnValue(Cosmetics.INSTANCE.getCapes().getTexture());
        }
    }
}
