@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.api.JavaAPIEntryPoint.EntityListener
import bewis09.bewisclient.mixin.ClientPlayerInteractionManagerMixin
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.widgets.Widget
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager
import net.minecraft.block.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.AxolotlEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.FrogEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.entity.passive.LlamaEntity
import net.minecraft.entity.passive.PandaEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.passive.TraderLlamaEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.Property
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ColorHelper
import java.util.*
import java.util.Map

class TiwylaWidget: Widget("tiwyla") {

    var identifier: Identifier = Identifier("extra")

    companion object {
        val extraInfo = hashMapOf<Block, Property<*>>(
                Pair(Blocks.REDSTONE_WIRE, RedstoneWireBlock.POWER),
                Pair(Blocks.SCULK_SENSOR, SculkSensorBlock.POWER),
                Pair(Blocks.NOTE_BLOCK, NoteBlock.NOTE),
                Pair(Blocks.HOPPER, HopperBlock.ENABLED),
                Pair(Blocks.TNT, TntBlock.UNSTABLE),
                Pair(Blocks.TRIPWIRE_HOOK, TripwireBlock.ATTACHED),
                Pair(Blocks.TRIPWIRE, TripwireBlock.ATTACHED),
                Pair(Blocks.SCULK_SHRIEKER, SculkShriekerBlock.CAN_SUMMON)
        )

        val entityExtraInfo = hashMapOf<EntityType<*>, EntityListener>(
                Pair(EntityType.CAT, EntityListener {
                    if(it is CatEntity) Registries.CAT_VARIANT.getId(it.variant)?.path else ""
                }),
                Pair(EntityType.FROG, EntityListener {
                    if(it is FrogEntity) Registries.FROG_VARIANT.getId(it.variant)?.path else ""
                }),
                Pair(EntityType.AXOLOTL, EntityListener {
                    if(it is AxolotlEntity) it.variant.name else ""
                }),
                Pair(EntityType.HORSE, EntityListener {
                    if(it is HorseEntity) it.variant.name.toLowerCase()+", "+it.marking.name.toLowerCase() else ""
                }),
                Pair(EntityType.RABBIT, EntityListener {
                    if(it is RabbitEntity) it.variant.name else ""
                }),
                Pair(EntityType.LLAMA, EntityListener {
                    if(it is LlamaEntity) it.variant.name.toLowerCase()+", ${Bewisclient.getTranslatedString("strength")}: "+it.strength.toString() else ""
                }),
                Pair(EntityType.TRADER_LLAMA, EntityListener {
                    if(it is TraderLlamaEntity) it.variant.name.toLowerCase()+", ${Bewisclient.getTranslatedString("strength")}: "+it.strength.toString() else ""
                }),
                Pair(EntityType.WITHER, EntityListener {if(it.health<150) Bewisclient.getTranslatedString("wither.second_stage") else Bewisclient.getTranslatedString("wither.first_stage")})
        )
    }

    fun getText(): ArrayList<String> {
        return getTextFromType(MinecraftClient.getInstance().crosshairTarget)
    }

    override fun render(drawContext: DrawContext) {
        if(getText().size==0) return
        drawContext.matrices.scale(getScale(),getScale(),1F)
        drawContext.fill(getPosX(),getPosY(),getPosX()+getOriginalWidth(),getPosY()+getOriginalHeight(), ColorHelper.Argb.getArgb(((getProperty(Settings.TRANSPARENCY)?.times(255F))?.toInt()!!),0,0,0))
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, getText()[0],getPosX()+getOriginalWidth()/2,getPosY()+4,-1)
        drawContext.matrices.scale(0.7F,0.7F,1F)
        for ((index, text) in getText().iterator().withIndex()) {
            if(index!=0)
                drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,if (text.split("%")[0]=="cTH") convertToHearths(text.split("%")[1].toDouble(),text.split("%")[2].toDouble(),text.split("%")[3].toDouble()) else Text.of(text),((getPosX()+getOriginalWidth()/2)/0.7F).toInt(),((getPosY()+8*index+8)/0.7F).toInt(),-1)
        }
        drawContext.matrices.scale(1/0.7F,1/0.7F,1F)
        drawContext.matrices.scale(1/getScale(),1/getScale(),1F)
    }

    override fun getOriginalWidth(): Int {
        return 150
    }

    override fun getOriginalHeight(): Int {
        return if(getText().size==4) 41 else 33
    }

    private fun getTextFromType(hitResult: HitResult?):ArrayList<String> {
        if(hitResult==null)
            return arrayListOf()
        if(hitResult.type==HitResult.Type.BLOCK)
            return getTextFromBlock(hitResult as BlockHitResult,MinecraftClient.getInstance().world!!.getBlockState(hitResult.blockPos))
        if(hitResult.type==HitResult.Type.ENTITY && (hitResult as EntityHitResult).entity is LivingEntity)
            return getTextFromEntity(hitResult, hitResult.entity as LivingEntity)
        return arrayListOf()
    }

    private fun getTextFromBlock(hitResult: BlockHitResult, blockState: BlockState):ArrayList<String> {
        val firstLine = blockState.block.name.string
        val secondLine = getBlockInformation(getProperty(Settings.FIRST_LINE)!!.toInt(),blockState,hitResult.blockPos)
        val thirdLine = getBlockInformation(getProperty(Settings.SECOND_LINE)!!.toInt(),blockState,hitResult.blockPos)
        val fourthLine = getBlockInformation(getProperty(Settings.THIRD_LINE)!!.toInt(),blockState,hitResult.blockPos)

        return arrayListOf(firstLine,secondLine,thirdLine,fourthLine)
    }

    private fun getBlockInformation(i: Int, blockState: BlockState, blockPos: BlockPos): String {
        when (i) {
            0 -> return getTool(blockState.block)
            1 -> return getLevel(blockState.block)
            2 -> return getBreakingTime(blockPos,blockState)
            3 -> return getProgress() ?: getTool(blockState.block)
            4 -> return getProgress() ?: getLevel(blockState.block)
            5 -> return getProgress() ?: getBreakingTime(blockPos,blockState)
            6 -> return getExtra(blockState) ?: getTool(blockState.block)
            7 -> return getExtra(blockState) ?: getLevel(blockState.block)
            8 -> return getExtra(blockState) ?: getBreakingTime(blockPos,blockState)
        }
        return ""
    }

    private fun getProgress(): String? {
        val s = ((MinecraftClient.getInstance().interactionManager as ClientPlayerInteractionManagerMixin?)!!.getCurrentBreakingProgress() * 100)
        if(s==0F) {
            return null
        }
        return "${Bewisclient.getTranslatedString("progress")}: ${Math.round(s)}%"
    }

    private fun getExtra(entity: LivingEntity): String? {
        return if(entityExtraInfo.contains(entity.type)) {
            entityExtraInfo[entity.type]?.getExtra(entity)
        } else null
    }

    private fun getExtra(blockState: BlockState): String? {
        return if(extraInfo.contains(blockState.block)) {
            "${firstStringUp("${extraInfo[blockState.block]?.name}")}: ${blockState.get(extraInfo[blockState.block])}"
        } else null
    }

    private fun firstStringUp(str: String): String {
        val strings = str.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val s = StringBuilder()

        for (st in strings) {
            s.append(st.replaceFirst(".".toRegex(), st[0].toString().uppercase(Locale.getDefault())))
        }

        return s.toString()
    }

    private fun getBreakingTime(blockPos: BlockPos,blockState: BlockState): String {
        val player: ClientPlayerEntity = MinecraftClient.getInstance().player!!
        if(blockState.calcBlockBreakingDelta(player,MinecraftClient.getInstance().world,blockPos)>1) {
            return Bewisclient.getTranslatedString("instant")
        }
        val secs = Math.round(1f/blockState.calcBlockBreakingDelta(player,MinecraftClient.getInstance().world,blockPos)*5F)/100F
        if(secs>(3600*24)) {
            return "${Math.round(secs/36/24)/100F} ${Bewisclient.getTranslatedString("days")}"
        }
        if(secs>3600) {
            return "${Math.round(secs/36)/100F} ${Bewisclient.getTranslatedString("hours")}"
        }
        if(secs>60) {
            return "${Math.round(secs/6*10)/100F} ${Bewisclient.getTranslatedString("minutes")}"
        }
        return "$secs ${Bewisclient.getTranslatedString("seconds")}"
    }

    private fun getTool(block: Block): String {
        if (block.defaultState.isIn(BlockTags.AXE_MINEABLE)) return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.axe")}"
        if (block.defaultState.isIn(BlockTags.PICKAXE_MINEABLE)) return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.pickaxe")}"
        if (block.defaultState.isIn(BlockTags.HOE_MINEABLE)) return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.hoe")}"
        if (block.defaultState.isIn(BlockTags.SHOVEL_MINEABLE)) return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.shovel")}"
        if (block.defaultState.isIn(FabricMineableTags.SHEARS_MINEABLE)) return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.shears")}"
        if (block.defaultState.isIn(FabricMineableTags.SWORD_MINEABLE)) return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.sword")}"
        return "${Bewisclient.getTranslatedString("tool")}: ${Bewisclient.getTranslatedString("tool.none")}"
    }

    private fun getLevel(block: Block): String {
        val map = Map.of(
                0, "${Bewisclient.getTranslatedString("mining_level")}: ${Bewisclient.getTranslatedString("mining_level.none")}",
                1, "${Bewisclient.getTranslatedString("mining_level")}: ${Bewisclient.getTranslatedString("mining_level.wood")}",
                2, "${Bewisclient.getTranslatedString("mining_level")}: ${Bewisclient.getTranslatedString("mining_level.stone")}",
                3, "${Bewisclient.getTranslatedString("mining_level")}: ${Bewisclient.getTranslatedString("mining_level.iron")}",
                4, "${Bewisclient.getTranslatedString("mining_level")}: ${Bewisclient.getTranslatedString("mining_level.diamond")}"
        )
        val level = (if (MiningLevelManager.getRequiredMiningLevel(block.defaultState) == -1) if (block.defaultState.isToolRequired) 0 else -1 else MiningLevelManager.getRequiredMiningLevel(block.defaultState)) + 1
        return map.getOrDefault(level, "${Bewisclient.getTranslatedString("mining_level")}: ${Bewisclient.getTranslatedString("mining_level.other")}")
    }

    private fun getTextFromEntity(hitResult: EntityHitResult, entity: LivingEntity):ArrayList<String> {
        return arrayListOf(
                entity.name.string,
                "cTH%${entity.health}%${entity.maxHealth}%${entity.absorptionAmount}",
                getExtra(entity) ?: Registries.ENTITY_TYPE.getId(entity.type).toString()
        )
    }

    @Suppress("NAME_SHADOWING")
    fun convertToHearths(health: Double, maxhealth: Double, absorbtion: Double): Text {
        var health = health
        var maxhealth = maxhealth
        var absorbtion = absorbtion
        try {
            maxhealth = roundUpAndHalf(maxhealth)
            health = ((health * 10).toInt().toDouble()) / 10f
            absorbtion = ((absorbtion * 10).toInt().toDouble()) / 10f
            if (maxhealth > 13.0) {
                return Text.literal((health.toString() + " / " + maxhealth * 2 + " HP"))
            }
            health = roundUpAndHalf(health)
            absorbtion = roundUpAndHalf(absorbtion)
            val isHalf = health != health.toInt().toDouble()
            val isAbso = absorbtion != absorbtion.toInt().toDouble()
            val isMaxHalf = maxhealth != (((maxhealth * 2).toInt().toDouble()) / 2).toInt().toDouble()
            val maxhealthleft = (maxhealth - ((health.toInt()) + (if (isHalf) 1 else 0)) + (if (isMaxHalf) 1 else 0)).toInt()
            return Text.literal("❤".repeat(health.toInt())).setStyle(Style.EMPTY.withColor(0xFF0000))
                    .append(Text.literal(if (isHalf) "\uE0aa" else "").setStyle(Style.EMPTY.withFont(identifier).withColor(0xFFFFFF)))
                    .append(Text.literal("❤".repeat(maxhealthleft)).setStyle(Style.EMPTY.withColor(0xFFFFFF)))
                    .append(Text.literal("❤".repeat(absorbtion.toInt())).setStyle(Style.EMPTY.withColor(0xFFFF00)))
                    .append(Text.literal(if (isAbso) "\uE0ab" else "").setStyle(Style.EMPTY.withFont(identifier).withColor(0xFFFFFF)))
        } catch (e: Exception) {
            return Text.of("")
        }
    }

    fun roundUpAndHalf(i: Double): Double {
        return 500 - ((1000 - i).toInt()) / 2.0
    }
}