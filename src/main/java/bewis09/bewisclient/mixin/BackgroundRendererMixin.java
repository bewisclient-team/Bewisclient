package bewis09.bewisclient.mixin;

import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.Settings;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

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
        if (cameraSubmersionType == CameraSubmersionType.LAVA && (JavaSettingsSender.Companion.getSettings().get("design", Settings.Companion.getSettings().getBETTER_VISIBILITY(),Settings.Companion.getSettings().getLAVA()))) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = viewDistance * ((JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getBETTER_VISIBILITY(),Settings.Companion.getSettings().getLAVA_VIEW())));
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW && (JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getBETTER_VISIBILITY(),Settings.Companion.getSettings().getPOWDER_SNOW()))) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = viewDistance * 0.5f;
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        } else if (cameraSubmersionType == CameraSubmersionType.WATER && (JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getBETTER_VISIBILITY(),Settings.Companion.getSettings().getWATER()))) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = viewDistance;
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        } else if (thickFog && (JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getBETTER_VISIBILITY(),Settings.Companion.getSettings().getNETHER()))) {
            fogData.fogStart = viewDistance-1;
            fogData.fogEnd = viewDistance;
            RenderSystem.setShaderFogStart(fogData.fogStart);
            RenderSystem.setShaderFogEnd(fogData.fogEnd);
            RenderSystem.setShaderFogShape(fogData.fogShape);
        }
    }
}
