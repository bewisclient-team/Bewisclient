package bewis09.bewisclient.widgets

import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

abstract class Widget(val id: String): Settings() {
    abstract fun render(drawContext: DrawContext)

    abstract fun getOriginalWidth(): Int
    abstract fun getOriginalHeight(): Int

    open fun getScale(): Float = getProperty(Settings.SCALE)!!

    open fun isEnabled(): Boolean = getProperty(Settings.ENABLED)!!

    private fun getSavedPosX(): Float = getProperty(Settings.POSX)!!
    private fun getSavedPosY(): Float = getProperty(Settings.POSY)!!

    fun getScreenWidth(): Int = MinecraftClient.getInstance().window.scaledWidth
    private fun getScreenHeight(): Int = MinecraftClient.getInstance().window.scaledHeight

    private fun getSavedPartX(): Int = getProperty(Settings.PARTX)?.toInt()!!
    private fun getSavedPartY(): Int = getProperty(Settings.PARTY)?.toInt()!!

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
        if(getSavedPartY()==-1)
            return (getSavedPosY()/getScale()).roundToInt()
        if(getSavedPartY()==0)
            return ((getScreenHeight()/2+getSavedPosY()-getHeight()/2)/getScale()).roundToInt()
        return ((getScreenHeight()-getSavedPosY()-getHeight())/getScale()).roundToInt()
    }

    fun getWidth(): Int = (getScale() * getOriginalWidth()).roundToInt()
    fun getHeight(): Int = (getScale() * getOriginalHeight()).roundToInt()

    fun <K> getProperty(setting: SettingsLoader.TypedSettingID<K>): K? =
            SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.getValue(setting)

    fun <K> setProperty(setting: SettingsLoader.TypedSettingID<K>, value:K): K? =
            SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValue(setting,value)

    open fun setPropertyPosX(value: Float, allV: Int, wV: Int, sneak: Boolean): Float? {
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
        SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValueWithoutSave(Settings.PARTX, part)
        return SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValueWithoutSave(Settings.POSX, pos)
    }

    open fun setPropertyPosY(value: Float, allV: Int, wV: Int, sneak: Boolean): Float? {
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
        SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValueWithoutSave(Settings.PARTY, part)
        return SettingsLoader.WidgetSettings.getValue(SettingsLoader.TypedSettingID<SettingsLoader.Settings>(id))?.setValueWithoutSave(Settings.POSY, pos)
    }
}