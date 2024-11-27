package bewis09.bewisclient.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawContext.class)
public interface DrawContextMixin {
    @Accessor
    public VertexConsumerProvider.Immediate getVertexConsumers();
}
