package bewis09.bewisclient.widgets

import bewis09.bewisclient.mixin.BossBarHudMixin
import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A widget that gets rendered on the screen and displays in-game-information
 *
 * @param id The id of the widget
 */
abstract class Widget<K: SettingTypes.DefaultWidgetSettingsObject>(val id: String): Settings() {
    private lateinit var intern_settings: K

    val settings: K
        get() {
            if(!this::intern_settings.isInitialized) intern_settings = getWidgetSettings()

            return intern_settings
        }

    /**
     * Renders the widget that the position that is set in the settings
     *
     * @param drawContext The [DrawContext] used to render
     */
    fun render(drawContext: DrawContext) {
        render(drawContext,getPosX(),getPosY())
    }

    /**
     * Renders the widget that the given position
     *
     * @param drawContext The [DrawContext] used to render
     * @param x The x coordinate where the widget should start to get drawn
     * @param y The y coordinate where the widget should start to get drawn
     */
    abstract fun render(drawContext: DrawContext,x:Int,y:Int)

    /**
     * @return The original width of the widget when there is no scaling applied
     */
    abstract fun getOriginalWidth(): Int

    /**
     * @return The original height of the widget when there is no scaling applied
     */
    abstract fun getOriginalHeight(): Int

    /**
     * @return The scaled width of the screen
     */
    fun getScreenWidth(): Int = MinecraftClient.getInstance().window.scaledWidth

    /**
     * @return The scaled height of the screen
     */
    fun getScreenHeight(): Int = MinecraftClient.getInstance().window.scaledHeight

    /**
     * Use [getPosX] instead of this, if you want scaling
     *
     * @return the real x-position without scaling
     */
    open fun getOriginalPosX(): Int {
        if(settings.partX.get()==-1f)
            return (settings.posX.get()).roundToInt()
        if(settings.partX.get()==0f) {
            return ((getScreenWidth()/2+settings.posX.get()-getWidth()/2)).roundToInt()
        }
        return ((getScreenWidth()-settings.posX.get()-getWidth())).roundToInt()
    }

    /**
     * Use [getPosY] instead of this, if you want scaling
     *
     * @return the real y-position without scaling
     */
    fun getOriginalPosY(): Int {
        if(settings.partY.get()==-1f)
            return (settings.posY.get()).roundToInt()
        return ((getScreenHeight()-settings.posY.get()-getHeight())).roundToInt()
    }

    /**
     * @return The x-coordinate where the widget should start to be drawn after applying the scaling
     */
    fun getPosX(): Int {
        if(settings.partX.get()==-1f)
            return (settings.posX.get()/settings.size.get()).roundToInt()
        if(settings.partX.get()==0f)
            return ((getScreenWidth()/2+settings.posX.get()-getWidth()/2)/settings.size.get()).roundToInt()
        return ((getScreenWidth()-settings.posX.get()-getWidth())/settings.size.get()).roundToInt()
    }

    /**
     * @return The y-coordinate where the widget should start to be drawn after applying the scaling
     */
    fun getPosY(): Int {
        if(settings.partY.get()==-1f) {
            if(settings.partX.get()==0f && MinecraftClient.getInstance().currentScreen !is WidgetConfigScreen) {
                return ((settings.posY.get()+(MinecraftClient.getInstance().inGameHud.bossBarHud as BossBarHudMixin).bossBars.size*19)/settings.size.get()).roundToInt()
            }
            return (settings.posY.get()/settings.size.get()).roundToInt()
        }
        return ((getScreenHeight()-settings.posY.get()-getHeight())/settings.size.get()).roundToInt()
    }

    /**
     * @return The scaled width of the widget
     */
    fun getWidth(): Int = (settings.size.get() * getOriginalWidth()).roundToInt()

    /**
     * @return The scaled height of the widget
     */
    fun getHeight(): Int = (settings.size.get() * getOriginalHeight()).roundToInt()

    /**
     * Set the x-position of the widget
     *
     * Snaps to the border and the middle if [sneak] is false
     *
     * Doesn't save the setting to the file. Call [SettingsLoader.saveAllSettings] if you want to
     *
     * @param value The new real x-coordinate
     * @param allV The width of the screen
     * @param wV The width of the widget
     * @param sneak Sets if shift is pressed, which disables snapping
     */
    open fun setPropertyPosX(value: Float, allV: Int, wV: Int, sneak: Boolean) {
        var pos: Float
        var part = 0f
        if(allV/3>value) {
            pos = value
            part = -1F
        } else if(allV/3*2>value) {
            pos = value-allV/2+wV/2
        } else {
            pos = allV-value-wV
            part = 1F
        }
        if(abs(value+wV/2-allV/2) <5 && !sneak) {
            part = 0F
            pos = 0F
        }
        if(part!=0f && abs(pos-2.5)<5 && !sneak)
            pos = 5f
        SettingsLoader.disableAutoSave()
        settings.partX.set(part)
        SettingsLoader.disableAutoSave()
        settings.posX.set(pos)
    }

    /**
     * Set the y-position of the widget
     *
     * Snaps to the border if [sneak] is false
     *
     * Doesn't save the setting to the file. Call [SettingsLoader.saveAllSettings] if you want to
     *
     * @param value The new real y-coordinate
     * @param allV The height of the screen
     * @param wV The height of the widget
     * @param sneak Sets if shift is pressed, which disables snapping
     */
    open fun setPropertyPosY(value: Float, allV: Int, wV: Int, sneak: Boolean) {
        var pos: Float
        val part: Float
        if(allV/2>value) {
            pos = value
            part = -1F
        } else {
            pos = allV-value-wV
            part = 1F
        }
        if(abs(pos-2.5)<5 && !sneak)
            pos = 5f
        SettingsLoader.disableAutoSave()
        settings.partY.set(part)
        SettingsLoader.disableAutoSave()
        settings.posY.set(pos)
    }

    /**
     * @return The settings of the widget as a [SettingTypes.DefaultWidgetSettingsObject]
     */
    abstract fun getWidgetSettings(): K

    open fun isScalable() = true

    open fun isEnabled() = settings.enabled.get()
}