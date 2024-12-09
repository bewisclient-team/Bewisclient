package bewis09.bewisclient.util

import kotlin.math.pow
import kotlin.math.round

/**
 * Some Methods to format numbers to strings
 */
object NumberFormatter {

    /**
     * Adds zeros to get to a specific number of digits after the decimal point
     *
     * @param number The original number
     * @param afterPoint The number of digits after the decimal point
     *
     * @return The [String] with the specific number of digits after the decimal point
     */
    fun withAfterPointZero(number: Double, afterPoint: Int): String {
        if(afterPoint == 0) return round(number).toString()
        val v: Double = Math.round(number * 10.0.pow(afterPoint)) / 10.0.pow(afterPoint)
        val str = StringBuilder(v.toString())
        while (str.split(".").size == 1 || str.split(".")[1].length < afterPoint) {
            if (str.split(".").size == 1) str.append(".")
            str.append("0")
        }
        return str.toString()
    }

    /**
     * Adds zeros at the beginning of a number to get to a specific number of digits
     *
     * @param number The number
     * @param count The number of digits
     *
     * @return The [String] with a specific number of digits
     */
    fun zeroBefore(number: Int, count: Int): String {
        return zeroBefore(number, count,10)
    }

    /**
     * Adds zeros at the beginning of a number to get to a specific number of digits
     *
     * @param number The number
     * @param count The number of digits
     * @param radix The radix of the number
     *
     * @return The [String] with a specific number of digits
     */
    fun zeroBefore(number: Int, count: Int, radix: Int): String {
        val str = StringBuilder(number.toString(radix.coerceIn(2, 36)))
        while (str.length < count) {
            str.insert(0, "0")
        }
        return str.toString()
    }
}