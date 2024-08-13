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
    fun render(drawContext: DrawContext) {
        render(drawContext,getPosX(),getPosY())
    }
    abstract fun render(drawContext: DrawContext,x:Int,y:Int)

    abstract fun getOriginalWidth(): Int
    abstract fun getOriginalHeight(): Int

    open fun getScale(): Float = getProperty(Settings.SIZE)

    open fun isEnabled(): Boolean = getProperty(Settings.ENABLED)

    private fun getSavedPosX(): Float = getProperty(Settings.POSX)
    private fun getSavedPosY(): Float = getProperty(Settings.POSY)

    fun getScreenWidth(): Int = MinecraftClient.getInstance().window.scaledWidth
    private fun getScreenHeight(): Int = MinecraftClient.getInstance().window.scaledHeight

    private fun getSavedPartX(): Int = getProperty(Settings.PARTX).toInt()
    private fun getSavedPartY(): Int = getProperty(Settings.PARTY).toInt()

    open fun getOriginalPosX(): Int {
        if(getSavedPartX()==-1)
            return (getSavedPosX()).roundToInt()
        if(getSavedPartX()==0) {
            return ((getScreenWidth()/2+getSavedPosX()-getWidth()/2)).roundToInt()
        }
        return ((getScreenWidth()-getSavedPosX()-getWidth())).roundToInt()
    }

    fun getOriginalPosY(): Int {
        if(getSavedPartY()==-1)
            return (getSavedPosY()).roundToInt()
        if(getSavedPartY()==0)
            return ((getScreenHeight()/2+getSavedPosY()-getHeight()/2)).roundToInt()
        return ((getScreenHeight()-getSavedPosY()-getHeight())).roundToInt()
    }

    fun getPosX(): Int {
        if(getSavedPartX()==-1)
            return (getSavedPosX()/getScale()).roundToInt()
        if(getSavedPartX()==0)
            return ((getScreenWidth()/2+getSavedPosX()-getWidth()/2)/getScale()).roundToInt()
        return ((getScreenWidth()-getSavedPosX()-getWidth())/getScale()).roundToInt()
    }

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

    fun getWidth(): Int = (getScale() * getOriginalWidth()).roundToInt()
    fun getHeight(): Int = (getScale() * getOriginalHeight()).roundToInt()

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

    @Suppress("unchecked_cast")
    inline fun <reified K> setProperty(setting: SettingsLoader.TypedSettingID<K>, value: K) {
        when (value) {
            is Number -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<Float>,id)
            is Boolean -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<Boolean>,id)
            is ColorSaver -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<ColorSaver>,id)
            is String -> SettingsLoader.set("widgets",value,setting as SettingsLoader.TypedSettingID<String>,id)
            else -> {}
        }
    }

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

    abstract fun getWidgetSettings(): JsonObject

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