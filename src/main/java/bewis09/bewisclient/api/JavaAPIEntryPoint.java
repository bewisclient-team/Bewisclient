package bewis09.bewisclient.api;

import bewis09.bewisclient.drawable.option_elements.MainOptionsElement;
import bewis09.bewisclient.exception.FalseClassException;
import bewis09.bewisclient.screen.ElementList;
import bewis09.bewisclient.widgets.Widget;
import bewis09.bewisclient.widgets.WidgetRenderer;
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.Property;

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

    public static void addMainOptionsElement(MainOptionsElement m) {
        if(m.getClass()!=MainOptionsElement.class) {
            throw new FalseClassException(m,MainOptionsElement.class);
        }
        ElementList.INSTANCE.getNewMainOptionsElements().add(() -> m);
    }

    public static void addWidget(Widget w) {
        WidgetRenderer.Companion.getWidgets().add(w);
    }
}
