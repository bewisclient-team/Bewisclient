package bewis09.bewisclient.util

import bewis09.bewisclient.settingsLoader.Settings.Companion.ANIMATION_TIME
import bewis09.bewisclient.settingsLoader.Settings.Companion.DESIGN
import bewis09.bewisclient.settingsLoader.Settings.Companion.OPTIONS_MENU
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToLong

open class Animation(val start: Long, val length: Long, val easeMode: EaseMode) {
    fun getProgress(): Float {
        return easeMode.function(MathHelper.clamp(-(start - System.currentTimeMillis()) / length.toFloat(),0f,1f))
    }

    fun hasEnded(): Boolean {
        return start + length <= System.currentTimeMillis()
    }
}

class ScreenAnimation: Animation(System.currentTimeMillis(), SettingsLoader.get(DESIGN, OPTIONS_MENU, ANIMATION_TIME).roundToLong(), EaseMode.EASE_IN_OUT)

open class ValuedAnimation(start: Long, length: Long, easeMode: EaseMode, val startValue: Float, val endValue: Float): Animation(start,length, easeMode) {
    fun getValue(): Float {
        return endValue*getProgress()+startValue*(1-getProgress())
    }
}

open class ScreenValuedTypedAnimation(startValue: Float, endValue: Float, private val type: String): ValuedAnimation(System.currentTimeMillis(), SettingsLoader.get(DESIGN, OPTIONS_MENU, ANIMATION_TIME).roundToLong(), EaseMode.EASE_IN_OUT, startValue, endValue) {
    fun getType(): String {
        return type
    }
}

class JustTypedScreenAnimation(type: String): ScreenValuedTypedAnimation(1f,1f,type)

class EaseMode(val function: (Float)->Float) {
    companion object {
        val CONST = EaseMode { 1f }
        val LINEAR = EaseMode { it }
        val SMOOTH_IN_OUT = EaseMode { (1 - cos(Math.PI / 2 * it).pow(2)).toFloat() }
        val EASE_IN_OUT = EaseMode { ((1 - cos(Math.PI / 2 * it).pow(2) + it) / 2).toFloat() }
    }
}