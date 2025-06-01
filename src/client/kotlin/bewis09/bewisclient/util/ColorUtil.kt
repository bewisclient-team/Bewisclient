package bewis09.bewisclient.util

fun applyAlpha(color: Int, alpha: Float): Int {
    val a = (alpha * 255).toInt()
    return (color and 0x00FFFFFF) or (a shl 24)
}