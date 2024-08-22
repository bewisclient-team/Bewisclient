package bewis09.bewisclient.screen

import bewis09.bewisclient.cape.Cape
import bewis09.bewisclient.cape.CapePlayer
import bewis09.bewisclient.cape.Capes
import bewis09.bewisclient.hat.Hat
import bewis09.bewisclient.mixin.ScreenMixin
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.wings.Wing
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import java.util.*
import kotlin.math.*

// No Documentation, because this will get a rework soon
class CosmeticsScreen(private val parent: MainOptionsScreen) : Screen(Text.empty()) {
    var isReversed: Boolean = false
    private var scrollY = 0.0

    var alphaStart = -1L
    var alphaDirection = 0

    var player: PlayerEntity = Objects.requireNonNullElseGet(MinecraftClient.getInstance().player) { CapePlayer(null) }!!

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

        if(alphaStart==-1L) {
            alphaStart= System.currentTimeMillis()
        }

        val animationSpeed = MathHelper.clamp(SettingsLoader.get(
            Settings.DESIGN,
            Settings.OPTIONS_MENU,
            Settings.ANIMATION_TIME
        ).toInt(),1,500).toFloat()

        if(alphaDirection ==1 && (System.currentTimeMillis() - alphaStart)/animationSpeed>1) {
            MinecraftClient.getInstance().setScreen(parent)
            parent.animationStart = System.currentTimeMillis()
            parent.animatedScreen = null
            parent.animationState = MainOptionsScreen.AnimationState.TO_MAIN_SCREEN
            return
        }

        var clamped_value = MathHelper.clamp((System.currentTimeMillis() - alphaStart)/animationSpeed,0f,1f)
        clamped_value = abs(alphaDirection-clamped_value)
        clamped_value = ((1- cos(Math.PI * clamped_value))/2).toFloat()

        context.setShaderColor(1f,1f,1f,clamped_value)
        context.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680)
        assert(client != null)
        context.fill(13, height / 2 - 70, 83, height / 2 + 70, -0x1000000)
        context.drawBorder(13, height / 2 - 70, 70, 140, -1)
        context.enableScissor(0, height / 2 - 70, width, height)
        drawEntity(context, 48, height / 2 + 50-((1-clamped_value)*100).toInt(), 50, ((if (isReversed) 1 else -1) * (mouseX - 48)).toFloat(), height / 2f - mouseY, player, isReversed)
        context.disableScissor()
        context.fill(100, 0, width, height, 0x55000000)
        var z = 0
        var bl = false
        var stack: ItemStack? = null
        val d: Wing = Wing.current_wing
        if (player.inventory.getArmorStack(2).item === Items.ELYTRA) {
            bl = true
            stack = player.inventory.getArmorStack(2)
            player.inventory.armor[2] = ItemStack.EMPTY
        }
        Wing.current_wing = Wing.EMPTY
        context.drawBorder(109, (getScrollY() + 5).toInt(), 64 * Capes.CAPES.size + 5, 120, -1)
        context.fill(109, (getScrollY() + 5).toInt(), 109 + 64 * Capes.CAPES.size + 5, (getScrollY() + 125).toInt(), -0x56000000)
        context.drawBorder(109, (getScrollY() + 135).toInt(), 64 * Wing.WINGS.size + 5, 120, -1)
        context.fill(109, (getScrollY() + 135).toInt(), 109 + 64 * Wing.WINGS.size + 5, (getScrollY() + 255).toInt(), -0x56000000)
        context.drawBorder(109, (getScrollY() + 265).toInt(), 64 * Hat.HATS.size + 5, 120, -1)
        context.fill(109, (getScrollY() + 265).toInt(), 109 + 64 * Hat.HATS.size + 5, (getScrollY() + 385).toInt(), -0x56000000)
        val hat: Hat = Hat.current_hat
        Hat.current_hat = Hat.EMPTY
        for (c in Capes.CAPES) {
            z++
            Cape.setCurrentCape(c)
            context.fill(50 + z * 64, (getScrollY() + 10).toInt(), 50 + z * 64 + 60, (getScrollY() + 110).toInt(), -0x1000000)
            context.drawBorder(50 + z * 64, (getScrollY() + 10).toInt(), 60, 100, -1)
            context.enableScissor(0, (getScrollY() + 10).toInt(), width, height)
            drawEntity(context, 80 + z * 64, (getScrollY() + 90).toInt()-((1-clamped_value)*100).toInt(), 35, (mouseX - (30 + z * 64) - 32).toFloat(), (30 - mouseY + getScrollY()).toFloat(), player, true)
            context.disableScissor()
        }
        z = 0
        Cape.setCurrentCape(null)
        for (w in Wing.WINGS) {
            z++
            Wing.current_wing = w
            context.fill(50 + z * 64, (getScrollY() + 140).toInt(), 50 + z * 64 + 60, (getScrollY() + 240).toInt(), -0x1000000)
            context.drawBorder(50 + z * 64, (getScrollY() + 140).toInt(), 60, 100, -1)
            context.enableScissor(0, (getScrollY() + 140).toInt(), width, height)
            drawEntity(context, 80 + z * 64, (getScrollY() + 220).toInt()-((1-clamped_value)*100).toInt(), 35, (mouseX - (30 + z * 64) - 32).toFloat(), (160 - mouseY + getScrollY()).toFloat(), player, true)
            context.disableScissor()
        }
        Wing.current_wing = Wing.EMPTY
        z = 0
        for (w in Hat.HATS) {
            z++
            Hat.current_hat = w
            context.fill(50 + z * 64, (getScrollY() + 270).toInt(), 50 + z * 64 + 60, (getScrollY() + 370).toInt(), -0x1000000)
            context.drawBorder(50 + z * 64, (getScrollY() + 270).toInt(), 60, 100, -1)
            context.enableScissor(0, (getScrollY() + 270).toInt(), width, height)
            drawEntityForMoved(context, 80 + z * 64, (getScrollY() + 350).toInt()-((1-clamped_value)*100).toInt(), 35, (mouseX - (30 + z * 64) - 32).toFloat(), (290 - mouseY + getScrollY()).toFloat(), player, false)
            context.disableScissor()
        }
        if (bl) player.inventory.armor[2] = stack
        Cape.setCurrentCape(Cape.getCurrentRealCape())
        Wing.current_wing = d
        drawScrollbar(context)
        Hat.current_hat = hat
        RenderSystem.setShaderColor(1f,1f,1f,1f)
        renderButtons(context, mouseX, mouseY, delta, clamped_value)
    }

    fun renderButtons(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float, alpha: Float) {
        @Suppress("CAST_NEVER_SUCCEEDS")
        for (drawable in (this as ScreenMixin).getDrawables()) {
            if(alpha>0.1) {
                if (drawable is ClickableWidget)
                    drawable.setAlpha(alpha)
                drawable.render(context, mouseX, mouseY, delta)
            }
        }
    }

    private fun getScrollY(): Double {
        return -scrollY
    }

    override fun close() {
        alphaStart = System.currentTimeMillis()
        alphaDirection = 1
    }

    override fun init() {
        scrollY = max(0.0, min(-maxScrollY.toDouble(), scrollY))
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewisclient.option.rotate")) { isReversed = !isReversed }.dimensions(13, height / 2 + 74, 70, 20).build())
        var z = 0
        for (c in Capes.CAPES) {
            z++
            val finalZ = z
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewisclient.option.use")) {
                Cape.setCurrentRealCape(c)
                SettingsLoader.set(Settings.DESIGN,(finalZ-1).toFloat(),Settings.CAPE)
            }.dimensions(50 + z * 64 + 10, (getScrollY() + 100).toInt(), 40, 20).build())
        }
        z = 0
        for (c in Wing.WINGS) {
            z++
            val finalZ1 = z
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewisclient.option.use")) {
                Wing.current_wing = c
                SettingsLoader.set(Settings.DESIGN,(finalZ1-1).toFloat(),Settings.WING)
            }.dimensions(50 + z * 64 + 10, (getScrollY() + 230).toInt(), 40, 20).build())
        }
        z = 0
        for (c in Hat.HATS) {
            z++
            val finalZ2 = z
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewisclient.option.use")) {
                Hat.current_hat = c
                SettingsLoader.set(Settings.DESIGN,(finalZ2-1).toFloat(),Settings.HAT)
            }.dimensions(50 + z * 64 + 10, (getScrollY() + 360).toInt(), 40, 20).build())
        }
    }

    fun drawEntity(context: DrawContext?, x: Int, y: Int, size: Int, mouseX: Float, mouseY: Float, entity: LivingEntity, isReversed: Boolean) {
        val f = atan((mouseX / 40.0f).toDouble()).toFloat()
        val g = atan((mouseY / 40.0f).toDouble()).toFloat()
        val quaternionf = Quaternionf().rotateZ(3.1415927f)
        val quaternionf2 = Quaternionf().rotateX(g * 20.0f * 0.017453292f)
        quaternionf.mul(quaternionf2)
        val h = entity.bodyYaw
        val i = entity.yaw
        val j = entity.pitch
        val k = entity.prevHeadYaw
        val l = entity.headYaw
        entity.bodyYaw = (if (isReversed) 0f else (180.0f)) + f * 20.0f
        entity.yaw = (if (isReversed) 0f else (180.0f)) + f * 40.0f
        entity.pitch = -g * 20.0f
        entity.headYaw = entity.yaw
        entity.prevHeadYaw = entity.yaw
        InventoryScreen.drawEntity(context, x.toFloat(), y.toFloat(), size.toFloat(), Vector3f(), quaternionf, quaternionf2, entity)
        entity.bodyYaw = h
        entity.yaw = i
        entity.pitch = j
        entity.prevHeadYaw = k
        entity.headYaw = l
    }

    fun drawEntityForMoved(context: DrawContext?, x: Int, y: Int, size: Int, mouseX: Float, mouseY: Float, entity: LivingEntity, isReversed: Boolean) {
        val f = mouseX % 360 - 180
        val g = atan((mouseY / 40.0f).toDouble()).toFloat()
        val quaternionf = Quaternionf().rotateZ(3.1415927f)
        val quaternionf2 = Quaternionf().rotateX(g * 20.0f * 0.017453292f)
        quaternionf.mul(quaternionf2)
        val h = entity.bodyYaw
        val i = entity.yaw
        val j = entity.pitch
        val k = entity.prevHeadYaw
        val l = entity.headYaw
        entity.bodyYaw = (if (isReversed) 0f else (180.0f)) + f
        entity.yaw = (if (isReversed) 0f else (180.0f)) + f
        entity.pitch = -g * 20.0f
        entity.headYaw = entity.yaw
        entity.prevHeadYaw = entity.yaw
        InventoryScreen.drawEntity(context, x.toFloat(), y.toFloat(), size.toFloat(), Vector3f(), quaternionf, quaternionf2, entity)
        entity.bodyYaw = h
        entity.yaw = i
        entity.pitch = j
        entity.prevHeadYaw = k
        entity.headYaw = l
    }

    private fun drawScrollbar(context: DrawContext) {
        val i = this.scrollbarThumbHeight
        val j = 91 + 4
        val k = 91 + 4 + 8
        var l = 0
        if (maxScrollY != 0) l = max(0.0, (-this.scrollY) * (this.height - i) / this.maxScrollY).toInt()
        val m = l + i
        context.fill(j, l, k, m, -8355712)
        context.fill(j, l, k - 1, m - 1, -4144960)
    }

    private val maxScrollY: Int
        get() = (-max(0.0, (this.contentsHeight - height).toDouble())).toInt()

    private val scrollbarThumbHeight: Int
        get() = ((height * height) / contentsHeight.toFloat()).toInt()

    val contentsHeight: Int = 395

    private fun setScrollY(maxScrollY: Double) {
        scrollY = max(0.0, min(-this.maxScrollY.toDouble(), maxScrollY))
        clearAndInit()
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, h: Double, verticalAmount: Double): Boolean {
        if (mouseX > 91) setScrollY(this.scrollY - verticalAmount * this.deltaYPerScroll)
        return true
    }

    private val deltaYPerScroll: Double = 20.0

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        val bl = keyCode == GLFW.GLFW_KEY_UP
        val bl2 = keyCode == GLFW.GLFW_KEY_DOWN
        if (bl || bl2) {
            val d = this.scrollY
            this.setScrollY(this.scrollY + (if (bl) -1 else 1).toDouble() * this.deltaYPerScroll)
            if (d != this.scrollY) {
                return true
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}