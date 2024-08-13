package bewis09.bewisclient.screen

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.MainOptionsElement
import bewis09.bewisclient.drawable.UsableTexturedButtonWidget
import bewis09.bewisclient.mixin.ScreenMixin
import bewis09.bewisclient.screen.elements.ElementList
import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.Search
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.MathHelper
import org.joml.Matrix4f
import java.util.*
import kotlin.math.*

@Suppress("CAST_NEVER_SUCCEEDS")
open class MainOptionsScreen : Screen(Text.empty()) {

    var animationStart = 0L
    var animatedScreen: Screen? = null
    var animationState = AnimationState.STABLE

    private var searchBar: TextFieldWidget? = null
    private var bottomAnimation: ArrayList<ClickableWidget> = arrayListOf()

    var slice = 0

    private var totalHeight = 0

    var scrolls = arrayListOf(0F)

    var shouldNotNotifyChange = false

    var shouldNotRedoFocus = false

    private val closeTextures: ButtonTextures = ButtonTextures(Identifier.of("bewisclient","textures/sprites/close_button.png"), Identifier.of("bewisclient","textures/sprites/close_button_highlighted.png"))
    private val backTextures: ButtonTextures = ButtonTextures(Identifier.of("bewisclient","textures/sprites/back_button.png"),Identifier.of("bewisclient","textures/sprites/back_button_highlighted.png"))

    var allElements = arrayListOf(ElementList.main())

    init {
        animationState = AnimationState.TO_MAIN_SCREEN_UNSTARTED
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {

        context!!

        context.fill(0,0,width,height, 0xAA000000.toInt())

        correctScroll()
        var animationFrame = 1F
        val animationSpeed = MathHelper.clamp(SettingsLoader.getInt("design","options_menu.animation_time"),1,500)
        if(System.currentTimeMillis() - animationStart >= animationSpeed) {
            if(animationState==AnimationState.TO_OTHER_SCREEN) {
                client?.setScreen(animatedScreen)
                return
            }
            if(animationState==AnimationState.LEFT) {
                slice--
                scrolls.removeAt(scrolls.size-1)
                allElements.removeAt(allElements.size-1)
            }
            if(animationState==AnimationState.RIGHT) {
                slice++
            }
            animationState = AnimationState.STABLE
        }
        if(animationState!=AnimationState.STABLE) {
            animationFrame = ((System.currentTimeMillis() - animationStart).toFloat())/animationSpeed
            animationFrame = (1-cos(Math.PI * animationFrame).toFloat())/2F
            if(animationState==AnimationState.TO_OTHER_SCREEN) {
                animationFrame = 1F-animationFrame
            }
            animationFrame = MathHelper.clamp(0F,animationFrame,1F)
        }

        val middleAnimationFrame = animationFrame
        if(animationState.animation==AnimationState.MIDDLE_ANIMATION)
            animationFrame = 1f

        context.fill(
            ((this.width/4) +4+6-6*animationFrame).toInt(),0,
            ((this.width-this.width/4-2)-2+6-6*animationFrame).toInt(),this.height,
            ((0x88*animationFrame).toLong()*0x1000000).toInt()
        )

        bottomAnimation.forEach {
            it.y = (height - (24 * animationFrame)).toInt()
        }

        val width = (this.width* scale.toDouble()).toInt()
        val height = (this.height* scale.toDouble()).toInt()

        context.matrices.push()
        context.matrices.scale(1f/ scale,1f/ scale,1f/ scale)

        var h = 4 + scrolls[slice].toInt()

        val normalOffset: Int = (if (animationState == AnimationState.LEFT) width/2*middleAnimationFrame else if (animationState == AnimationState.RIGHT) -width/2*middleAnimationFrame else 0F).roundToInt()

        if(animationState!=AnimationState.STABLE)
            context.enableScissor((this.width/4+4*animationFrame).toInt(),0, (this.width-(this.width/4+4*animationFrame)).toInt(),(this.height))

        allElements[slice].forEach {element ->
            h+=4+element.render(context,
                    width/4+10 + normalOffset,
                    h,
                    width/2-20,
                    (mouseX* scale).toInt(),
                    (mouseY* scale).toInt(),
                    max(10,floor(animationFrame*255).toLong() )*0x1000000L)
        }

        if(animationState==AnimationState.STABLE)
            context.enableScissor((this.width/4+4*animationFrame).toInt(),0, (this.width-(this.width/4+4*animationFrame)).toInt(),(this.height))

        totalHeight = h - scrolls[slice].toInt() + 8

        if(animationState==AnimationState.RIGHT) {
            h = 4  + scrolls[slice+1].toInt()
            allElements[slice+1].forEach {element ->
                h+=4+element.render(context,
                        width/4+10 + normalOffset + width/2,
                        h,
                        width/2-20,
                        (mouseX* scale).toInt(),
                        (mouseY* scale).toInt(),
                        max(10,floor(animationFrame*255).toLong() )*0x1000000L)
            }
        } else if(animationState==AnimationState.LEFT) {
            h = 4 + scrolls[slice-1].toInt()
            allElements[slice-1].forEach {element ->
                h+=4+element.render(context,
                        width/4+10 + normalOffset - width/2,
                        h,
                        width/2-20,
                        (mouseX* scale).toInt(),
                        (mouseY* scale).toInt(),
                        max(10,floor(animationFrame*255).toLong() )*0x1000000L)
            }
        }

        context.disableScissor()

        context.matrices.pop()

        context.matrices.push()
        context.matrices.translate(0f,0f,3f)

        context.enableScissor((this.width/4) +4,(this.height-28*animationFrame).toInt(), (this.width-this.width/4-2)-2,this.height)
        context.fill(0,0,width,height, 0xAA000000.toInt())
        context.disableScissor()

        context.fill((this.width/4) +4,(this.height-28*animationFrame).toInt(), (this.width-this.width/4-2)-2,this.height,
            0xAA000000.toInt()
        )

        for (drawable in (this as ScreenMixin).getDrawables()) {
            if(drawable is ClickableWidget)
                drawable.setAlpha(max(0.05f,animationFrame))
            drawable.render(context, mouseX, mouseY, delta)
        }

        context.matrices.pop()

        fillGradient(context, (this.width/4) -2,0, ((this.width/4)+6*animationFrame).toInt()-2,this.height,0,
            ((0xFF*animationFrame).toLong()*0x1000000).toInt()
        )
        fillGradient(context, (this.width-(this.width/4)-6*animationFrame).toInt()+2,0,this.width- (this.width/4) +2,this.height,
            ((0xFF*animationFrame).toLong()*0x1000000).toInt(), 0)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        clicked = true
        if(animationState==AnimationState.STABLE && mouseX>width/4 && mouseX<width/4*3 && mouseY<height-28) {
            allElements[slice].forEach {it.mouseClicked(mouseX* scale, mouseY* scale, button, this)}
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        clicked = false
        if(animationState==AnimationState.STABLE && mouseX>width/4 && mouseX<width/4*3 && mouseY<height-28) {
            allElements[slice].forEach {it.mouseReleased(mouseX* scale, mouseY* scale, button)}
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        ArrayList(allElements[slice]).forEach {it.charTyped(chr, modifiers)}
        return super.charTyped(chr, modifiers)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        ArrayList(allElements[slice]).forEach {it.keyPressed(keyCode, scanCode, modifiers)}
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if(animationState==AnimationState.STABLE && mouseX>width/4 && mouseX<width/4*3 && mouseY<height-28) {
            allElements[slice].forEach {it.onDrag(mouseX* scale, mouseY* scale, deltaX* scale, deltaY* scale, button)}
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun init() {
        bottomAnimation = arrayListOf()

        if(animationState==AnimationState.TO_MAIN_SCREEN_UNSTARTED) {
            animationState=AnimationState.TO_MAIN_SCREEN
            animationStart=System.currentTimeMillis()
        }
        bottomAnimation.add(addDrawableChild(UsableTexturedButtonWidget(width/4+8,height-24,20,20, backTextures) {
            goBack()
        }))
        bottomAnimation.add(addDrawableChild(UsableTexturedButtonWidget(width/4*3-28,height-24,20,20,closeTextures) {
            startAllAnimation(null)
        }))
        bottomAnimation.add(addDrawableChild(ButtonWidget.builder(Bewisclient.getTranslationText("gui.edit_hud")) {
            startAllAnimation(WidgetConfigScreen(this))
        }.dimensions(width/4+30,height-24,width/6-29,20).build()))
        bottomAnimation.add(addDrawableChild(ButtonWidget.builder(Bewisclient.getTranslationText("gui.load_from_file")) {
            SettingsLoader.loadSettings()
        }.dimensions(width/4*3-1-width/6,height-24,width/6-29,20).build()))
        searchBar = addDrawableChild(TextFieldWidget(MinecraftClient.getInstance().textRenderer,width/2+4-width/12,height-24,width/6-8,20,Text.empty()))
        searchBar?.setChangedListener {
            if(animationState!=AnimationState.STABLE) {
                if(it!="")
                    searchBar!!.text=""
                return@setChangedListener
            }
            if (!shouldNotNotifyChange) {
                if (it == "") {
                    allElements = arrayListOf(ElementList.main())
                    scrolls = arrayListOf(0f)
                    slice = 0
                } else {
                    allElements = arrayListOf(Search.search(it, searchCollection))
                    scrolls = arrayListOf(0f)
                    slice = 0
                }
            }
        }
        if(!shouldNotRedoFocus) {
            searchBar?.isFocused = true
            focused = searchBar
        }
        shouldNotRedoFocus = true
        bottomAnimation.add(searchBar!!)
    }

    fun startAllAnimation(screen: Screen?) {
        animationState = AnimationState.TO_OTHER_SCREEN
        animationStart = System.currentTimeMillis()
        animatedScreen = screen
    }

    fun goBack() {
        if(animationState==AnimationState.STABLE)
            if(slice>0) {
                shouldNotNotifyChange = true
                searchBar?.text = ""
                animationState = AnimationState.LEFT
                animationStart = System.currentTimeMillis()
                shouldNotNotifyChange = false
            } else {
                startAllAnimation(null)
            }
    }

    fun openNewSlice(elements: ArrayList<MainOptionsElement>) {
        if(animationState == AnimationState.STABLE) {
            allElements.add(elements)
            scrolls.add(0F)
            animationState = AnimationState.RIGHT
            animationStart = System.currentTimeMillis()
        }
    }

    override fun close() {
        goBack()
    }

    enum class AnimationState(val animation: AnimationState?) {
        ALL_ANIMATION(null),
        MIDDLE_ANIMATION(null),
        STABLE(null),
        TO_OTHER_SCREEN(ALL_ANIMATION),
        TO_MAIN_SCREEN(ALL_ANIMATION),
        TO_MAIN_SCREEN_UNSTARTED(ALL_ANIMATION),
        LEFT(MIDDLE_ANIMATION),
        RIGHT(MIDDLE_ANIMATION)
    }

    private fun fillGradient(context: DrawContext, startX: Int, startY: Int, endX: Int, endY: Int, colorStart: Int, colorEnd: Int) {
        val vertexConsumer: VertexConsumer = context.vertexConsumers.getBuffer(RenderLayer.getGui())
        val matrix4f: Matrix4f = context.matrices.peek().positionMatrix
        vertexConsumer.vertex(matrix4f, endX.toFloat(), startY.toFloat(), 5f).color(colorEnd)
        vertexConsumer.vertex(matrix4f, startX.toFloat(), startY.toFloat(), 5f).color(colorStart)
        vertexConsumer.vertex(matrix4f, startX.toFloat(), endY.toFloat(), 5f).color(colorStart)
        vertexConsumer.vertex(matrix4f, endX.toFloat(), endY.toFloat(), 5f).color(colorEnd)
    }

    fun correctScroll() {
        scrolls[slice]=max((height* scale -32-totalHeight),scrolls[slice])
        scrolls[slice]=min(0f,scrolls[slice])
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if(animationState==AnimationState.STABLE)
            scrolls[slice]+=verticalAmount.toFloat()*20
        correctScroll()
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
    }

    companion object {
        val searchCollection = Search.collect(ElementList.main())

        var laS = 1f/ SettingsLoader.getFloat("design","options_menu.scale")

        var clicked = false

        val scale: Float
            get() {
                if(!clicked) {
                    laS = 1f/SettingsLoader.getFloat("design","options_menu.scale")
                }
                return laS
            }
    }
}