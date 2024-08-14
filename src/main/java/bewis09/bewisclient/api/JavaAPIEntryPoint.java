package bewis09.bewisclient.api;

import bewis09.bewisclient.drawable.option_elements.OptionsElement;
import bewis09.bewisclient.screen.ElementList;
import bewis09.bewisclient.widgets.Widget;
import bewis09.bewisclient.widgets.WidgetRenderer;
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.Property;

@SuppressWarnings("unused")
public class JavaAPIEntryPoint {
    /**
     * Allows you to add your custom external information about the block the player is looking at in the TIWYLA Widget
     *
     * @param block The block for which the information should be added
     * @param property The property that should be regarded
     */
    public static void addBlockExtraInfoPair(Block block, Property<?> property) {
        TiwylaWidget.Companion.getExtraInfo().put(block,property);
    }

    /**
     * Allows you to add your custom external information about the entity the player is looking at in the TIWYLA Widget
     *
     * @param entityType The {@link EntityType} for which this information should be added
     * @param entityListener The {@link EntityListener} that produces the {@link String} that should be displayed
     */
    public static void addEntityExtraInfoPair(EntityType<?> entityType, EntityListener entityListener) {
        TiwylaWidget.Companion.getEntityExtraInfo().put(entityType,entityListener);
    }

    /**
     * An Interface that produces the {@link String} for the entity information
     */
    public interface EntityListener {

        /**
         * @param entity The {@link LivingEntity} that is being looked at
         * @return The {@link String} that should be displayed
         */
        String getExtra(LivingEntity entity);
    }

    /**
     * Allows you to add your own element in the Main Options Screen
     * @param m The {@link OptionsElement} which should be added
     */
    public static void addMainOptionsElement(OptionsElement m) {
        ElementList.INSTANCE.getNewMainOptionsElements().add(() -> m);
    }

    /**
     * Allows you to add your own widget
     * @param w The {@link Widget} that should be added
     */
    public static void addWidget(Widget w) {
        WidgetRenderer.Companion.getWidgets().add(w);
    }
}
