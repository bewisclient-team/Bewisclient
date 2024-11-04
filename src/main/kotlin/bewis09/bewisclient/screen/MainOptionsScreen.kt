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
import bewis09.bewisclient.pop_up.PopUp
import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.Settings.Companion.AUTO_UPDATE
import bewis09.bewisclient.settingsLoader.Settings.Companion.DESIGN
import bewis09.bewisclient.settingsLoader.Settings.Companion.EXPERIMENTAL
import bewis09.bewisclient.settingsLoader.Settings.Companion.GENERAL
import bewis09.bewisclient.settingsLoader.Settings.Companion.OPTIONS_MENU
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.*
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
import org.joml.Matrix4f
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * The main options screen for Bewisclient options
 */
open class MainOptionsScreen : Screen(Text.empty()) {

    /**
     * The [Animation]
     */
    var animation = ScreenValuedTypedAnimation(1f,0f, AnimationState.MAIN_UNSTARTED)

    /**
     * The screen that should be shown after the animation, if it isn't another page of the [MainOptionsScreen]
     */
    var animatedScreen: Screen? = null

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

    private var popUp: PopUp? = null

    private var popUpAnimation: ScreenValuedAnimation = ScreenValuedAnimation(1f,1f)

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

    override fun render(context: DrawContext?, mX: Int, mY: Int, delta: Float) {
        var mouseX = mX
        var mouseY = mY

        if (client!!.world == null) {
            this.renderPanoramaBackground(context, delta)
        }

        if(popUp!=null) {
            mouseX=Int.MIN_VALUE
            mouseY=Int.MIN_VALUE
        }

        context!!

        correctScroll()
        var animationFrame = 1F
        if(animation.hasEnded() && animation.getType()!=AnimationState.STABLE) {
            if(animation.getType()==AnimationState.MAIN && animation.endValue==0f) {
                client?.setScreen(animatedScreen)
                return
            }
            if (animation.getType()==AnimationState.SLIDE) {
                if (animation.endValue == 0f) {
                    scrolls.removeAt(scrolls.size-1)
                    allElements.removeAt(allElements.size-1)
                } else {
                    slice++
                }
            }
            animation = JustTypedScreenAnimation(AnimationState.STABLE)
        }
        if(animation.getType()!=AnimationState.STABLE) {
            animationFrame = animation.getValue()
        }

        var middleAnimationFrame = 1-animationFrame
        if(animation.getType()==AnimationState.SLIDE)
            animationFrame = 1f
        else
            middleAnimationFrame = 1f

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

        val normalOffset: Int = (width/2*(middleAnimationFrame-1)).roundToInt()

        context.matrices.translate(normalOffset.toFloat(),0f,0f)

        context.enableScissor((this.width/4+4*animationFrame).toInt(),0, (this.width-(this.width/4+4*animationFrame)).toInt(),(this.height))

        allElements[slice].forEach {element ->
            h+=8+element.render(context,
                    width/4+10,
                    h,
                    width/2-20,
                    (mouseX* scale).toInt(),
                    (mouseY* scale).toInt(),
                    max(10,floor(animationFrame*255).toLong() )*0x1000000L)
        }

        totalHeight = h - scrolls[slice].toInt() + 8

        if(animation.getType()==AnimationState.SLIDE) {
            h = 4  + scrolls[slice+1].toInt()
            allElements[slice+1].forEach {element ->
                h+=8+element.render(context,
                        width/4+10 + width/2,
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
        context.matrices.translate(0f,0f,1000f)

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
        context.matrices.translate(0f,0f,-100f)

        if(popUpAnimation.getProgress()==1f && popUpAnimation.getValue()==0f) {
            popUp = null
        }


        context.matrices.translate(0f,0f,10000f)
        popUp?.render(context, mouseX, mouseY, delta, (width/2-popUp!!.getWidth()/2), (height/3-popUp!!.getHeight()/2),popUpAnimation.getValue())
        context.matrices.translate(0f,0f,-10000f)

        context.matrices.pop()
    }

    fun setPopUp(popUp: PopUp?, animate: Boolean) {
        if(!animate) {
            this.popUp = popUp
            popUpAnimation = ScreenValuedAnimation(1f,1f)
        } else if(popUp!=null) {
            this.popUp = popUp
            popUpAnimation = ScreenValuedAnimation(0f,1f)
        } else {
            popUpAnimation = ScreenValuedAnimation(1f,0f)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(popUp!=null) {
            popUp?.mouseClicked((mouseX.toInt()* scale).toInt(),
                (mouseY.toInt()* scale).toInt(), ((width* scale/2-popUp!!.getWidth()/2).roundToInt()), ((height* scale/3-popUp!!.getHeight()/2).roundToInt()))
            return true
        }
        clicked = true
        if(animation.getType()==AnimationState.STABLE && mouseX>width/4 && mouseX<width/4*3 && mouseY<height-28) {
            allElements[slice].forEach {it.mouseClicked(mouseX* scale, mouseY* scale, button, this)}
        }
        Dialog.mouseClicked(mouseX, mouseY, button)
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        popUp?.mouseReleased((mouseX.toInt()* scale).toInt(),
            (mouseY.toInt()* scale).toInt(), (((width/2-popUp!!.getWidth())* scale/2).roundToInt()), (((height/3-popUp!!.getHeight())* scale/2).roundToInt()))
        clicked = false
        allElements[slice].forEach {it.mouseReleased(mouseX* scale, mouseY* scale, button)}
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        if(popUp!=null) {
            return false
        }
        ArrayList(allElements[slice]).forEach {it.charTyped(chr, modifiers)}
        return super.charTyped(chr, modifiers)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(popUp==null) {
            ArrayList(allElements[slice]).forEach {it.keyPressed(keyCode, scanCode, modifiers)}
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if(popUp!=null) {
            popUp?.mouseDragged(mouseX.toInt(),mouseY.toInt(),deltaX,deltaY, (((width/2-popUp!!.getWidth())* scale/2).roundToInt()), (((height/3-popUp!!.getHeight())* scale/2).roundToInt()))
            return false
        }
        if(animation.getType()==AnimationState.STABLE && mouseX>width/4 && mouseX<width/4*3 && mouseY<height-28) {
            allElements[slice].forEach {it.onDrag(mouseX* scale, mouseY* scale, deltaX* scale, deltaY* scale, button)}
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun init() {
        bottomAnimation = arrayListOf()

        if(animation.getType()==AnimationState.MAIN_UNSTARTED) {
            animation = ScreenValuedTypedAnimation(0f,1f,AnimationState.MAIN)
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
            if(animation.getType()!=AnimationState.STABLE) {
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
        animation = ScreenValuedTypedAnimation(1f,0f,AnimationState.MAIN)
        animatedScreen = screen
    }

    /**
     * Goes back to the last slice or closes the screen if the current slice is the first one
     */
    fun goBack() {
        if(animation.getType()==AnimationState.STABLE)
            if(slice>0) {
                shouldNotNotifyChange = true
                searchBar?.text = ""
                animation = ScreenValuedTypedAnimation(1f,0f,AnimationState.SLIDE)
                slice--
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
        if(animation.getType() == AnimationState.STABLE) {
            allElements.add(elements)
            scrolls.add(0F)
            animation = ScreenValuedTypedAnimation(0f,1f,AnimationState.SLIDE)
        }
    }

    override fun close() {
        if(popUp!=null) return setPopUp(null,true)
        goBack()
    }

    /**
     * An object for the Strings indicating the state of the animation
     */
    object AnimationState {

        /**
         * Indicates that there is no animation running
         */
        const val STABLE = "STABLE"

        /**
         * Indicates that there is an animation to/from another screen
         */
        const val MAIN = "MAIN"

        /**
         * Indicates that there is an animation from another screen that hasn't started. Only used when creating the screen
         */
        const val MAIN_UNSTARTED = "MAIN_UNSTARTED"

        /**
         * Indicates that there is an animation to the slice before/after the current one
         */
        const val SLIDE = "SLIDE"
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
        if(animation.getType()==AnimationState.STABLE)
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
        var laS = 1f/ SettingsLoader.get(DESIGN, OPTIONS_MENU, Settings.SCALE)

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
                    laS = 1f/SettingsLoader.get(DESIGN, OPTIONS_MENU, Settings.SCALE)
                }
                return laS
            }
    }
}