package bewis09.bewisclient.mixin;

import bewis09.bewisclient.MixinStatics;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

    // TODO fix

    //@Inject(method = "applyFog",at=@At("RETURN"))
    //private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
    //    CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
    //    MixinStatics.FogData fogData = new MixinStatics.FogData(fogType);
    //    if (cameraSubmersionType == CameraSubmersionType.LAVA && (SettingsLoader.INSTANCE.get(Settings.DESIGN, Settings.Companion.getBETTER_VISIBILITY(),Settings.Companion.getLAVA()))) {
    //        fogData.fogStart = -8.0f;
    //        fogData.fogEnd = viewDistance * ((SettingsLoader.INSTANCE.get(Settings.DESIGN,Settings.Companion.getBETTER_VISIBILITY(),Settings.Companion.getLAVA_VIEW())));
    //        RenderSystem.setShaderFogStart(fogData.fogStart);
    //        RenderSystem.setShaderFogEnd(fogData.fogEnd);
    //        RenderSystem.setShaderFogShape(fogData.fogShape);
    //    } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW && (SettingsLoader.INSTANCE.get(Settings.DESIGN,Settings.Companion.getBETTER_VISIBILITY(),Settings.Companion.getPOWDER_SNOW()))) {
    //        fogData.fogStart = -8.0f;
    //        fogData.fogEnd = viewDistance * 0.5f;
    //        RenderSystem.setShaderFogStart(fogData.fogStart);
    //        RenderSystem.setShaderFogEnd(fogData.fogEnd);
    //        RenderSystem.setShaderFogShape(fogData.fogShape);
    //    } else if (cameraSubmersionType == CameraSubmersionType.WATER && (SettingsLoader.INSTANCE.get(Settings.DESIGN,Settings.Companion.getBETTER_VISIBILITY(),Settings.Companion.getWATER()))) {
    //        fogData.fogStart = -8.0f;
    //        fogData.fogEnd = viewDistance;
    //        RenderSystem.setShaderFogStart(fogData.fogStart);
    //        RenderSystem.setShaderFogEnd(fogData.fogEnd);
    //        RenderSystem.setShaderFogShape(fogData.fogShape);
    //    } else if (cameraSubmersionType == CameraSubmersionType.NONE && thickFog && (SettingsLoader.INSTANCE.get(Settings.DESIGN,Settings.Companion.getBETTER_VISIBILITY(),Settings.Companion.getNETHER()))) {
    //        fogData.fogStart = viewDistance-1;
    //        fogData.fogEnd = viewDistance;
    //        RenderSystem.setShaderFogStart(fogData.fogStart);
    //        RenderSystem.setShaderFogEnd(fogData.fogEnd);
    //        RenderSystem.setShaderFogShape(fogData.fogShape);
    //    }
    //}
}
