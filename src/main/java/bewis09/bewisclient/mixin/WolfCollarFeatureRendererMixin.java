package bewis09.bewisclient.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
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
public abstract class WolfCollarFeatureRendererMixin extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {
    @Shadow @Final private static Identifier SKIN;

    public WolfCollarFeatureRendererMixin(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",at=@At("HEAD"), cancellable = true)
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntity wolfEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if(wolfEntity.isTamed() && !wolfEntity.isInvisible() && Objects.requireNonNull(wolfEntity.getDisplayName()).getString().equals("Rainbow")) {
            float hue = ((System.currentTimeMillis())%10000)/10000f;
            Color color = new Color(Color.HSBtoRGB(hue,1f,1f));
            renderModel(this.getContextModel(), SKIN, matrixStack, vertexConsumerProvider, i, wolfEntity, color.getRGB());
            ci.cancel();
        }
    }
}