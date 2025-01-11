package bewis09.bewisclient.mixin;

import bewis09.bewisclient.MixinStatics;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow protected abstract @Nullable PlayerListEntry getPlayerListEntry();

    @Inject(method = "getSkinTextures",at = @At("HEAD"), cancellable = true)
    public void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        if(Objects.equals(getGameProfile().getName(), MinecraftClient.getInstance().getGameProfile().getName())) {
            PlayerListEntry playerListEntry = this.getPlayerListEntry();
            cir.setReturnValue(playerListEntry == null ?
                    withCape(DefaultSkinHelper.getSkinTextures(this.getUuid())) :
                    withCape(playerListEntry.getSkinTextures()));
        }
    }

    @Unique
    public SkinTextures withCape(SkinTextures textures) {
        if(!MixinStatics.OwnPlayerSkinTextures.contains(textures.hashCode()))
            MixinStatics.OwnPlayerSkinTextures.add(textures.hashCode());
        return textures;
    }
}
