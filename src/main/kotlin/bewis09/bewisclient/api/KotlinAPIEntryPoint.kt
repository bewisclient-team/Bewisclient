package bewis09.bewisclient.api

import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget
import bewis09.bewisclient.widgets.specialWidgets.TiwylaWidget.Companion.entityExtraInfo
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.state.property.Property

object KotlinAPIEntryPoint {
    fun addBlockExtraInfoPair(block:Block, property: Property<*>) {
        TiwylaWidget.extraInfo[block] = property
    }

    fun addEntityExtraInfoPair(entityType: EntityType<*>, entityListener: JavaAPIEntryPoint.EntityListener) {
        entityExtraInfo[entityType] = entityListener
    }
}