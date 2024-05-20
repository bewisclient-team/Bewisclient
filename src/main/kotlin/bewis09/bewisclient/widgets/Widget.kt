package bewis09.bewisclient.widgets

import bewis09.bewisclient.mixin.BossBarHudMixin
import bewis09.bewisclient.screen.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.util.*
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.roundToInt

abstract class Widget(val id: String): Settings() {
    abstract fun render(drawContext: DrawContext)

    abstract fun getOriginalWidth(): Int
    abstract fun getOriginalHeight(): Int

    open fun getScale(): Float = getProperty(Settings.SCALE)

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

    inline fun <reified K> getProperty(setting: SettingsLoader.TypedSettingID<K>): K {
        when (true) {
            K::class.java.name.lowercase().contains("float") -> return SettingsLoader.getFloat("widgets","$id.${setting.id}") as K
            K::class.java.name.lowercase().contains("boolean") -> return SettingsLoader.getBoolean("widgets","$id.${setting.id}") as K
            K::class.java.name.lowercase().contains("colorsaver") -> return ColorSaver(SettingsLoader.getString("widgets","$id.${setting.id}")) as K
            K::class.java.name.lowercase().contains("string") -> return SettingsLoader.getString("widgets","$id.${setting.id}") as K
            else -> {}
        }
        throw ClassCastException()
    }

    inline fun <reified K> setProperty(setting: SettingsLoader.TypedSettingID<K>, value: K) {
        when (true) {
            K::class.java.name.lowercase().contains("float") -> SettingsLoader.set("widgets","$id.${setting.id}",value as Float)
            K::class.java.name.lowercase().contains("boolean") -> SettingsLoader.set("widgets","$id.${setting.id}",value as Boolean)
            K::class.java.name.lowercase().contains("colorsaver") -> SettingsLoader.set("widgets","$id.${setting.id}",value as ColorSaver)
            K::class.java.name.lowercase().contains("string") -> SettingsLoader.set("widgets","$id.${setting.id}",value as String)
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
        SettingsLoader.set("widgets","$id.partX", part)
        SettingsLoader.disableAutoSave()
        SettingsLoader.set("widgets","$id.posX", pos)
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
        SettingsLoader.set("widgets","$id.partY", part)
        SettingsLoader.disableAutoSave()
        SettingsLoader.set("widgets","$id.posY", pos)
    }

    abstract fun getWidgetSettings(): JsonObject

    fun getWidgetSettings(size: Float,posX: Float,partX: Float,posY: Float,partY: Float): JsonObject {
        val jsonObject = JsonObject()

        jsonObject.add("enabled", JsonPrimitive(true))
        jsonObject.add("transparency", JsonPrimitive(0.43f))
        jsonObject.add("size", JsonPrimitive(size))
        jsonObject.add("posX", JsonPrimitive(posX))
        jsonObject.add("partX", JsonPrimitive(partX))
        jsonObject.add("posY", JsonPrimitive(posY))
        jsonObject.add("partY", JsonPrimitive(partY))
        jsonObject.add("text_color", JsonPrimitive("0xFFFFFF"))

        return jsonObject
    }
}