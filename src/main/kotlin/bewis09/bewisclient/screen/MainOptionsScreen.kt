package bewis09.bewisclient.screen

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.dialog.ClickDialog
import bewis09.bewisclient.dialog.Dialog
import bewis09.bewisclient.dialog.TextDialog
import bewis09.bewisclient.drawable.UsableTexturedButtonWidget
import bewis09.bewisclient.drawable.option_elements.ContactElement
import bewis09.bewisclient.drawable.option_elements.HRElement
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.mixin.ScreenMixin
import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.Settings.Companion.AUTO_UPDATE
import bewis09.bewisclient.settingsLoader.Settings.Companion.DESIGN
import bewis09.bewisclient.settingsLoader.Settings.Companion.EXPERIMENTAL
import bewis09.bewisclient.settingsLoader.Settings.Companion.GENERAL
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.Search
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.screen.ConfirmLinkScreen
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
import net.minecraft.util.Util
import net.minecraft.util.math.MathHelper
import org.joml.Matrix4f
import java.util.*
import kotlin.math.*

/**
 * The main options screen for Bewisclient options
 */
open class MainOptionsScreen : Screen(Text.empty()) {

    /**
     * The time when the animation started
     *
     * @see System.currentTimeMillis
     */
    var animationStart = 0L

    /**
     * The screen that should be shown after the animation, if it isn't another page of the [MainOptionsScreen]
     */
    var animatedScreen: Screen? = null

    /**
     * The current state of the animation
     */
    var animationState = AnimationState.STABLE

    /**
     * The searchbar at the bottom of the screen
     */
    private var searchBar: TextFieldWidget? = null

    /**
     * An [ArrayList] of all elements that should be animated to the bottom
     */
    private var bottomAnimation: ArrayList<ClickableWidget> = arrayListOf()

    /**
     * Indicates on which page the screen is currently on (0=Main Menu, then increasing)
     */
    var slice = 0

    /**
     * The height of all elements used to limit the amount that can be scrolled down
     */
    private var totalHeight = 0

    /**
     * An [ArrayList] of the amount that is scrolled down for each slice
     */
    var scrolls = arrayListOf(0F)

    /**
     * Indicates if the [searchBar] text can be changed without changing the content of the screen
     */
    var shouldNotNotifyChange = false

    /**
     * Indicates if the focus should be reset to the [searchBar] if [init] is executed. When creating the screen, it is true, after the first [init] it gets set to false
     */
    var shouldNotRedoFocus = false

    /**
     * The textures of the close button
     */
    private val closeTextures: ButtonTextures = ButtonTextures(Identifier.of("bewisclient","textures/sprites/close_button.png"), Identifier.of("bewisclient","textures/sprites/close_button_highlighted.png"))

    /**
     * The textures of the back button
     */
    private val backTextures: ButtonTextures = ButtonTextures(Identifier.of("bewisclient","textures/sprites/back_button.png"),Identifier.of("bewisclient","textures/sprites/back_button_highlighted.png"))

    /**
     * An [ArrayList] of type [ArrayList] which collects the collection of every [OptionElement] on each slice
     */
    var allElements = arrayListOf(ElementList.main())

    init {
        animationState = AnimationState.TO_MAIN_SCREEN_UNSTARTED

        if(Bewisclient.update!=null && !Bewisclient.updateInformed) {
            Dialog.addDialog(ClickDialog(Bewisclient.getTranslationText("info.new_update"), Bewisclient.getTranslationText("info.download")) {
                Dialog.pause()

                client?.setScreen(ConfirmLinkScreen({ confirmed: Boolean ->
                    Dialog.proceed()

                    if (confirmed) {
                        Util.getOperatingSystem().open("https://modrinth.com/mod/bewisclient")
                    }

                    MinecraftClient.getInstance().setScreen(this)
                }, "https://modrinth.com/mod/bewisclient", true))
            })

            if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
                Dialog.addDialog(ClickDialog(Bewisclient.getTranslationText("info.enable_auto_update"), Bewisclient.getTranslationText("info.enable")) {
                    SettingsLoader.set(GENERAL,true, AUTO_UPDATE, *EXPERIMENTAL)

                    Dialog.addDialog(TextDialog(Bewisclient.getTranslationText("info.auto_update_enable")))

                    it()
                })
            }
        }
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        if (client!!.world == null) {
            this.renderPanoramaBackground(context, delta)
        }

        context!!

        correctScroll()
        var animationFrame = 1F
        val animationSpeed = MathHelper.clamp(SettingsLoader.get(
            DESIGN, 
            Settings.OPTIONS_MENU,
            Settings.ANIMATION_TIME
        ),1f,500f)
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
        if(animationState==AnimationState.LEFT || animationState==AnimationState.RIGHT)
            animationFrame = 1f

        if (client!!.world != null) {
            context.fill(0, 0, width, height, ((0xAA * animationFrame).toLong() * 0x1000000).toInt())
        }

        context.fill(
            ((this.width/4) +4-6+6*animationFrame).toInt(),0,
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

        context.enableScissor(((this.width/4) +4-6+6*animationFrame).toInt(),(this.height-28*animationFrame).toInt(),
            ((this.width-this.width/4-2)-2+6-6*animationFrame).toInt(),this.height)
        context.fill(0,0,width,height, ((0xD0*animationFrame).toLong()*0x1000000).toInt())
        context.disableScissor()

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

        context.matrices.push()
        context.matrices.translate(0f,0f,100f)
        context.matrices.scale(1f/ scale,1f/ scale,1f/ scale)
        Dialog.render(context,width,mouseX,mouseY)
        context.matrices.pop()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        clicked = true
        if(animationState==AnimationState.STABLE && mouseX>width/4 && mouseX<width/4*3 && mouseY<height-28) {
            allElements[slice].forEach {it.mouseClicked(mouseX* scale, mouseY* scale, button, this)}
        }
        Dialog.mouseClicked(mouseX, mouseY, button)
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
        val gui_button = (addDrawableChild(ButtonWidget.builder(Bewisclient.getTranslationText("gui.edit_hud")) {
            startAllAnimation(WidgetConfigScreen(this))
        }.dimensions(width/4+30,height-24,width/6-29,20).build()))
        if(MinecraftClient.getInstance().world == null) {
            gui_button.active = false
        }
        bottomAnimation.add(gui_button)
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
                    allElements[0].add(HRElement())
                    allElements[0].add(ContactElement("find_no_option","https://github.com/Bewis09/Bewisclient-2/issues/new?labels=Type:%20Enhancement,Part:%20Option&assignee=Bewis09&title=New%20Option:%20"))
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

    /**
     * Start an animation to another screen
     *
     * @param screen The screen that the animation should go to or null if the screen should close
     */
    fun startAllAnimation(screen: Screen?) {
        animationState = AnimationState.TO_OTHER_SCREEN
        animationStart = System.currentTimeMillis()
        animatedScreen = screen
    }

    /**
     * Goes back to the last slice or closes the screen if the current slice is the first one
     */
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

    /**
     * Adds a new slice and starts an animation to it
     *
     * @param elements Every [OptionElement] of the new slice
     */
    fun openNewSlice(elements: ArrayList<OptionElement>) {
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

    /**
     * An enum for indicating the state of the animation
     */
    enum class AnimationState {

        /**
         * Indicates that there is no animation running
         */
        STABLE,

        /**
         * Indicates that there is an animation to another screen
         */
        TO_OTHER_SCREEN,

        /**
         * Indicates that there is an animation from another screen
         */
        TO_MAIN_SCREEN,

        /**
         * Indicates that there is an animation from another screen that hasn't started. Only used when creating the screen
         */
        TO_MAIN_SCREEN_UNSTARTED,

        /**
         * Indicates that there is an animation to the slice before the current one
         */
        LEFT,

        /**
         * Indicates that there is an animation to a new slice
         */
        RIGHT
    }

    /**
     * Fills a gradient from the left to the right
     *
     * @param context The [DrawContext] for drawing
     * @param startX The x-coordinate of the start of the gradient
     * @param startY The y-coordinate of the start of the gradient
     * @param endX The x-coordinate of the end of the gradient
     * @param endY The y-coordinate of the end of the gradient
     * @param colorStart The color on the left of the gradient
     * @param colorEnd The color on the right of the gradient
     */
    private fun fillGradient(context: DrawContext, startX: Int, startY: Int, endX: Int, endY: Int, colorStart: Int, colorEnd: Int) {
        val vertexConsumer: VertexConsumer = context.vertexConsumers.getBuffer(RenderLayer.getGui())
        val matrix4f: Matrix4f = context.matrices.peek().positionMatrix
        vertexConsumer.vertex(matrix4f, endX.toFloat(), startY.toFloat(), 5f).color(colorEnd)
        vertexConsumer.vertex(matrix4f, startX.toFloat(), startY.toFloat(), 5f).color(colorStart)
        vertexConsumer.vertex(matrix4f, startX.toFloat(), endY.toFloat(), 5f).color(colorStart)
        vertexConsumer.vertex(matrix4f, endX.toFloat(), endY.toFloat(), 5f).color(colorEnd)
    }

    /**
     * Corrects the scroll in the correct range
     */
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

    /**
     * Plays the button click sound
     *
     * @param soundManager The [SoundManager] used for playing the sound
     */
    fun playDownSound(soundManager: SoundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
    }

    companion object {
        /**
         * A collection of the lambas for the search
         *
         * @see [Search]
         */
        val searchCollection = Search.collect(ElementList.main())

        /**
         * The cached scale, that cannot change while clicking a mouse button for preventing scale change, while fading the scale fader
         */
        var laS = 1f/ SettingsLoader.get(DESIGN, Settings.OPTIONS_MENU, Settings.SCALE)

        /**
         * Indicates if a mouse button is clicked
         */
        var clicked = false

        /**
         * The scale factor of the [MainOptionsScreen]
         */
        val scale: Float
            get() {
                if(!clicked) {
                    laS = 1f/SettingsLoader.get(DESIGN, Settings.OPTIONS_MENU, Settings.SCALE)
                }
                return laS
            }
    }
}