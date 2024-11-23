package bewis09.bewisclient.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Objects;

@Mixin(WolfCollarFeatureRenderer.class)
public abstract class WolfCollarFeatureRendererMixin extends FeatureRenderer<WolfEntityRenderState, WolfEntityModel> {
    @Shadow @Final private static Identifier SKIN;

    public WolfCollarFeatureRendererMixin(FeatureRendererContext<WolfEntityRenderState, WolfEntityModel> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/WolfEntityRenderState;FF)V",at=@At("HEAD"), cancellable = true)
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntityRenderState wolfEntityRenderState, float f, float g, CallbackInfo ci) {
        if(wolfEntityRenderState.collarColor!=null && wolfEntityRenderState.customName!=null && !wolfEntityRenderState.invisible && wolfEntityRenderState.customName.getString().equals("Rainbow")) {
            float hue = ((System.currentTimeMillis())%10000)/10000f;
            Color color = new Color(Color.HSBtoRGB(hue,1f,1f));
            renderModel(this.getContextModel(), SKIN, matrixStack, vertexConsumerProvider, i, wolfEntityRenderState, color.getRGB());
            ci.cancel();
        }
    }
}