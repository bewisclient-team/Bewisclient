package bewis09.bewisclient.api

import bewis09.bewisclient.drawable.option_elements.OptionsElement
import bewis09.bewisclient.screen.ElementList.newMainOptionsElements
import bewis09.bewisclient.widgets.Widget
import bewis09.bewisclient.widgets.WidgetRenderer
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget.Companion.entityExtraInfo
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.state.property.Property

@Suppress("unused")
object KotlinAPIEntryPoint {

    /**
     * Allows you to add your custom external information about the block the player is looking at in the TIWYLA Widget
     *
     * @param block The block for which the information should be added
     * @param property The property that should be regarded
     */
    fun addBlockExtraInfoPair(block:Block, property: Property<*>) {
        TiwylaWidget.extraInfo[block] = property
    }

    /**
     * Allows you to add your custom external information about the entity the player is looking at in the TIWYLA Widget
     *
     * @param entityType The [EntityType] for which this information should be added
     * @param entityListener The [JavaAPIEntryPoint.EntityListener] that produces the {@link String} that should be displayed
     */
    fun addEntityExtraInfoPair(entityType: EntityType<*>, entityListener: JavaAPIEntryPoint.EntityListener) {
        entityExtraInfo[entityType] = entityListener
    }

    /**
     * Allows you to add your own element in the Main Options Screen
     * @param m The [OptionsElement] which should be added
     */
    fun addMainOptionsElement(m: OptionsElement) {
        newMainOptionsElements.add { m }
    }

    /**
     * Allows you to add your own widget
     * @param w The [Widget] that should be added
     */
    fun addWidget(w: Widget) {
        WidgetRenderer.widgets.add(w)
    }
}