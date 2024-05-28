package bewis09.bewisclient.util

import kotlin.math.pow

class MathUtil {
    companion object {
        fun inRangeThen(i: Double, range: Double, distance: Double): Double {
            if (i > range - distance && i < range + distance) {
                return range
            }
            return i
        }

        fun withAfterCommaZero(number: Double, afterComma: Int): String {
            val v: Double = Math.round(number * 10.0.pow(afterComma)) / 10.0.pow(afterComma)
            return zeroAfterComma(v, afterComma)
        }

        fun withAfterComma(number: Double, afterComma: Double): String {
            val v: Double = Math.round(number * 10.0.pow(afterComma)) / 10.0.pow(afterComma)
            return zeroAfter(v, afterComma + 1 + v.toInt().toString().length)
        }

        fun zeroAfter(number: Double, count: Double): String {
            val str = StringBuilder(number.toString())
            while (str.length < count) {
                str.append("0")
            }
            return str.toString()
        }

        fun zeroAfterComma(number: Double, count: Int): String {
            val str = StringBuilder(number.toString())
            while (str.split(".").size==1 || str.split(".")[1].length < count) {
                if(str.split(".").size==1) str.append(".")
                str.append("0")
            }
            return str.toString()
        }

        fun zeroBefore(number: Int, count: Int): String {
            val str = StringBuilder(number.toString())
            while (str.length < count) {
                str.insert(0, "0")
            }
            return str.toString()
        }

        fun zeroBefore(number: Int, count: Int, radix: Int): String {
            val str = StringBuilder(number.toString(radix.coerceIn(2, 36)))
            while (str.length < count) {
                str.insert(0, "0")
            }
            return str.toString()
        }
    }
}