package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

    @Shadow public static void clearFog() {}

    @Invoker("getFogModifier")
    public static BackgroundRenderer.StatusEffectFogModifier getFogMod(Entity entity, float tickDelta){
        return null;
    }

    @Inject(method = "applyFog",at=@At("RETURN"))
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        BackgroundRenderer.FogData fogData = new BackgroundRenderer.FogData(fogType);
        if (cameraSubmersionType == CameraSubmersionType.LAVA && (JavaSettingsSender.Companion.getSettings().getBoolean("design","better_visibility.lava"))) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = viewDistance * ((JavaSettingsSender.Companion.getSettings().getFloat("design","better_visibility.lava_view")));
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW && (JavaSettingsSender.Companion.getSettings().getBoolean("design","better_visibility.powder_snow"))) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = viewDistance * 0.5f;
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        } else if (cameraSubmersionType == CameraSubmersionType.WATER && (JavaSettingsSender.Companion.getSettings().getBoolean("design","better_visibility.water"))) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = viewDistance;
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        } else if (thickFog && (JavaSettingsSender.Companion.getSettings().getBoolean("design","better_visibility.nether"))) {
            fogData.fogStart = viewDistance-1;
            fogData.fogEnd = viewDistance;
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        }
    }
}
