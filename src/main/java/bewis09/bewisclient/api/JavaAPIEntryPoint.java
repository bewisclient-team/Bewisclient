package bewis09.bewisclient.api;

import bewis09.bewisclient.widgets.lineWidgets.TiwylaWidget;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.Property;

import javax.swing.text.html.parser.Entity;
import java.util.EventListener;

public class JavaAPIEntryPoint {
    public static void addBlockExtraInfoPair(Block block, Property<?> property) {
        TiwylaWidget.Companion.getExtraInfo().put(block,property);
    }

    public static void addEntityExtraInfoPair(EntityType<?> entityType, EntityListener entityListener) {
        TiwylaWidget.Companion.getEntityExtraInfo().put(entityType,entityListener);
    }

    public interface EntityListener {
        String getExtra(LivingEntity entity);
    }
}
