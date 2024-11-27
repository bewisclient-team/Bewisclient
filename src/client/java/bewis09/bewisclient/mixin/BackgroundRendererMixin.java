package bewis09.bewisclient.mixin;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow
    @Nullable
    private static BackgroundRenderer.StatusEffectFogModifier getFogModifier(Entity entity, float tickDelta) {
        return null;
    }

    @Unique
    private static boolean isDisabled(SettingsLoader.TypedSettingID<Boolean> settingID) {
        return (SettingsLoader.INSTANCE.get(Settings.DESIGN, Settings.Companion.getBETTER_VISIBILITY(),settingID));
    }

    @Inject(method = "applyFog",at=@At("RETURN"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        BackgroundRenderer.FogData fogData = new BackgroundRenderer.FogData(fogType);
        BackgroundRenderer.StatusEffectFogModifier statusEffectFogModifier = getFogModifier(entity, tickDelta);
        if(statusEffectFogModifier != null) return;
        if (cameraSubmersionType == CameraSubmersionType.LAVA && isDisabled(Settings.Companion.getLAVA())) {
           fogData.fogStart = -8.0F;
           fogData.fogEnd = viewDistance * ((SettingsLoader.INSTANCE.get(Settings.DESIGN,Settings.Companion.getBETTER_VISIBILITY(),Settings.Companion.getLAVA_VIEW())));
           cir.setReturnValue(new Fog(fogData.fogStart, fogData.fogEnd, fogData.fogShape, color.x, color.y, color.z, color.w));
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW && isDisabled(Settings.Companion.getPOWDER_SNOW())) {
            fogData.fogStart = -8.0F;
            fogData.fogEnd = viewDistance * 0.5F;
            cir.setReturnValue(new Fog(fogData.fogStart, fogData.fogEnd, fogData.fogShape, color.x, color.y, color.z, color.w));
        } else if (cameraSubmersionType == CameraSubmersionType.WATER && isDisabled(Settings.Companion.getWATER())) {
            fogData.fogStart = -8.0F;
            fogData.fogEnd = viewDistance;
            fogData.fogShape = FogShape.CYLINDER;
            cir.setReturnValue(new Fog(fogData.fogStart, fogData.fogEnd, fogData.fogShape, color.x, color.y, color.z, color.w));
        } else if (cameraSubmersionType == CameraSubmersionType.NONE && thickenFog && isDisabled(Settings.Companion.getNETHER())) {
            fogData.fogStart = 0.0F;
            fogData.fogEnd = viewDistance;
            fogData.fogShape = FogShape.CYLINDER;
            cir.setReturnValue(new Fog(fogData.fogStart, fogData.fogEnd, fogData.fogShape, color.x, color.y, color.z, color.w));
        } else if (cameraSubmersionType == CameraSubmersionType.NONE && !thickenFog && fogType == BackgroundRenderer.FogType.FOG_TERRAIN && isDisabled(Settings.Companion.getTERRAIN_FOG())) {
            float f = MathHelper.clamp(viewDistance / 10.0F, 4.0F, 64.0F);
            fogData.fogStart = viewDistance*2 - f;
            fogData.fogEnd = viewDistance*2;
            fogData.fogShape = FogShape.CYLINDER;
            cir.setReturnValue(new Fog(fogData.fogStart, fogData.fogEnd, fogData.fogShape, color.x, color.y, color.z, color.w));
        }
    }
}
