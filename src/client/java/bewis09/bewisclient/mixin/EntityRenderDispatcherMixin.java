package bewis09.bewisclient.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderDispatcher.class)
public interface EntityRenderDispatcherMixin {
    @Accessor
    EquipmentModelLoader getEquipmentModelLoader();
}
