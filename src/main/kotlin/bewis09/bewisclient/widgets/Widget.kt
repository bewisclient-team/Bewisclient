package bewis09.bewisclient.widgets

import bewis09.bewisclient.mixin.BossBarHudMixin
import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import kotlin.math.abs
import kotlin.math.roundToInt

abstract class Widget(val id: String): Settings() {
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
     * @return The scaling of the widget
     */
    open fun getScale(): Float = getProperty(Settings.SIZE)

    /**
     * @return If the widget should be shown
     */
    open fun isEnabled(): Boolean = getProperty(Settings.ENABLED)

    /**
     * @return The x position saved in the settings (Not the real x position)
     */
    private fun getSavedPosX(): Float = getProperty(Settings.POSX)

    /**
     * @return The y position saved in the settings (Not the real x position)
     */
    private fun getSavedPosY(): Float = getProperty(Settings.POSY)

    /**
     * @return The scaled width of the screen
     */
    fun getScreenWidth(): Int = MinecraftClient.getInstance().window.scaledWidth

    /**
     * @return The scaled height of the screen
     */
    fun getScreenHeight(): Int = MinecraftClient.getInstance().window.scaledHeight

    /**
     * @return The horizontal part of the screen where the widget should be shown (-1; 0; 1)
     */
    private fun getSavedPartX(): Int = getProperty(Settings.PARTX).toInt()

    /**
     * @return The vertical part of the screen where the widget should be shown (-1; 1)
     */
    private fun getSavedPartY(): Int = getProperty(Settings.PARTY).toInt()

    /**
     * Use [getPosX] instead of this, if you want scaling
     *
     * @return the real x-position without scaling
     */
    open fun getOriginalPosX(): Int {
        if(getSavedPartX()==-1)
            return (getSavedPosX()).roundToInt()
        if(getSavedPartX()==0) {
            return ((getScreenWidth()/2+getSavedPosX()-getWidth()/2)).roundToInt()
        }
        return ((getScreenWidth()-getSavedPosX()-getWidth())).roundToInt()
    }

    /**
     * Use [getPosY] instead of this, if you want scaling
     *
     * @return the real y-position without scaling
     */
    fun getOriginalPosY(): Int {
        if(getSavedPartY()==-1)
            return (getSavedPosY()).roundToInt()
        if(getSavedPartY()==0)
            return ((getScreenHeight()/2+getSavedPosY()-getHeight()/2)).roundToInt()
        return ((getScreenHeight()-getSavedPosY()-getHeight())).roundToInt()
    }

    /**
     * @return The x-coordinate where the widget should start to be drawn after applying the scaling
     */
    fun getPosX(): Int {
        if(getSavedPartX()==-1)
            return (getSavedPosX()/getScale()).roundToInt()
        if(getSavedPartX()==0)
            return ((getScreenWidth()/2+getSavedPosX()-getWidth()/2)/getScale()).roundToInt()
        return ((getScreenWidth()-getSavedPosX()-getWidth())/getScale()).roundToInt()
    }

    /**
     * @return The y-coordinate where the widget should start to be drawn after applying the scaling
     */
    fun getPosY(): Int {
        if(getSavedPartY()==-1) {
            if(getSavedPartX()==0 && MinecraftClient.getInstance().currentScreen !is WidgetConfigScreen) {
                return ((getSavedPosY()+(MinecraftClient.getInstance().inGameHud.bossBarHud as BossBarHudMixin).bossBars.size*19)/getScale()).roundToInt()
            }
            return (getSavedPosY()/getScale()).roundToInt()
        }
        if(getSavedPartY()==0)
            return ((getScreenHeight()/2+getSavedPosY()-getHeight()/2)/getScale()).roundToInt()
        return ((getScreenHeight()-getSavedPosY()-getHeight())/getScale()).roundToInt()
    }

    /**
     * @return The scaled width of the widget
     */
    fun getWidth(): Int = (getScale() * getOriginalWidth()).roundToInt()

    /**
     * @return The scaled height of the widget
     */
    fun getHeight(): Int = (getScale() * getOriginalHeight()).roundToInt()

    /**
     * Returns a property of the widget
     *
     * @param setting the [SettingsLoader.TypedSettingID] of the setting you are searching for
     * @param K the type of the setting you are searching for
     *
     * @return The value of the Setting as [K]
     *
     * @throws ClassCastException if K is not of type [Float], [Boolean], [ColorSaver], [String]
     *
     * @see [Settings]
     */
    @Suppress("unchecked_cast")
    inline fun <reified K> getProperty(setting: SettingsLoader.TypedSettingID<K>): K {
        when (true) {
            K::class.java.name.lowercase().contains("float") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<Float>,id) as K
            K::class.java.name.lowercase().contains("boolean") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<Boolean>,id) as K
            K::class.java.name.lowercase().contains("colorsaver") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<ColorSaver>,id) as K
            K::class.java.name.lowercase().contains("string") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<String>,id) as K
            else -> {}
        }
        throw ClassCastException()
    }

    /**
     * Returns a property of the widget
     *
     * @param setting the [SettingsLoader.TypedSettingID] of the setting you are searching for
     * @param path the path of the setting. Should be relative to [id]
     * @param K the type of the setting you are searching for
     *
     * @return The value of the Setting as [K]
     *
     * @throws ClassCastException if K is not of type [Float], [Boolean], [ColorSaver], [String]
     *
     * @see [Settings]
     */
    @Suppress("unchecked_cast")
    inline fun <reified K> getProperty(setting: SettingsLoader.TypedSettingID<K>, vararg path: String): K {
        when (true) {
            K::class.java.name.lowercase().contains("float") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<Float>,id,*path) as K
            K::class.java.name.lowercase().contains("boolean") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<Boolean>,id,*path) as K
            K::class.java.name.lowercase().contains("colorsaver") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<ColorSaver>,id,*path) as K
            K::class.java.name.lowercase().contains("string") -> return SettingsLoader.get("widgets",setting as SettingsLoader.TypedSettingID<String>,id,*path) as K
            else -> {}
        }
        throw ClassCastException()
    }

    /**
     * Sets a property of the widget
     *
     * @param setting the [SettingsLoader.TypedSettingID] of the setting you are searching for
     * @param value The new value of the Setting as [K]
     * @param K the type of the setting you are searching for
     *
     * @see [Settings]
     */
    @Suppress("unchecked_cast")
    inline fun <reified K> setProperty(setting: SettingsLoader.TypedSettingID<K>, value: K) {
        when (value) {
            is Number -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<Number>,id)
            is Boolean -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<Boolean>,id)
            is ColorSaver -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<ColorSaver>,id)
            is String -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<String>,id)
            else -> {}
        }
    }

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
        setProperty(PARTX,part)
        SettingsLoader.disableAutoSave()
        setProperty(POSX,pos)
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
        setProperty(PARTY,part)
        SettingsLoader.disableAutoSave()
        setProperty(POSY,pos)
    }

    /**
     * @return The settings of the widget as a [JsonObject]
     */
    abstract fun getWidgetSettings(): JsonObject

    /**
     * Use [getWidgetSettings] if you want to add additional settings
     *
     * @param size The default size of the widget
     * @param posX The default saved x-position
     * @param partX The default horizontal part of the screen where the widget is originally located in
     * @param posY The default saved y-position
     * @param partY The default vertical part of the screen where the widget is originally located in
     *
     * @return The default settings of the widget as a [JsonObject]
     */
    fun getWidgetSettings(size: Float,posX: Float,partX: Float,posY: Float,partY: Float): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.add(ENABLED.id, JsonPrimitive(true))
        jsonObject.add(TRANSPARENCY.id, JsonPrimitive(0.43f))
        jsonObject.add(SIZE.id, JsonPrimitive(size))
        jsonObject.add(POSX.id, JsonPrimitive(posX))
        jsonObject.add(PARTX.id, JsonPrimitive(partX))
        jsonObject.add(POSY.id, JsonPrimitive(posY))
        jsonObject.add(PARTY.id, JsonPrimitive(partY))
        jsonObject.add(TEXT_COLOR.id, JsonPrimitive("0xFFFFFF"))

        return jsonObject
    }
}