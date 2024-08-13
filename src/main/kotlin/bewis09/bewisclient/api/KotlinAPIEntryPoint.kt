package bewis09.bewisclient.api

import bewis09.bewisclient.drawable.option_elements.OptionsElement
import bewis09.bewisclient.exception.FalseClassException
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
    fun addBlockExtraInfoPair(block:Block, property: Property<*>) {
        TiwylaWidget.extraInfo[block] = property
    }

    fun addEntityExtraInfoPair(entityType: EntityType<*>, entityListener: JavaAPIEntryPoint.EntityListener) {
        entityExtraInfo[entityType] = entityListener
    }

    fun addMainOptionsElement(m: OptionsElement) {
        if(m.javaClass!=OptionsElement::class.java) {
            throw FalseClassException(m,OptionsElement::class.java)
        }
        newMainOptionsElements.add { m }
    }

    fun addWidget(w: Widget) {
        WidgetRenderer.widgets.add(w)
    }
}