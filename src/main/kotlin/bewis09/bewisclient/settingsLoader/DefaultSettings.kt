package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

// TODO Document
object DefaultSettings {

    val gson = Gson()

    class SliderInfo(val start: Float, val end: Float, val decimalPoints: Int)

    val TiwylaArray = arrayListOf("tiwyla.tool","tiwyla.level","tiwyla.time","tiwyla.tool.progress","tiwyla.level.progress","tiwyla.time.progress","tiwyla.tool.extra","tiwyla.level.extra","tiwyla.time.extra")

    val sliders: HashMap<String,SliderInfo> = hashMapOf(
            Pair(".transparency",SliderInfo(0f,1f,2)),
            Pair(".size",SliderInfo(0.2f,2f,2)),
            Pair("options_menu.animation_time",SliderInfo(0f,500f,0)),
            Pair(".maxinfolength",SliderInfo(1f,10f,0)),
            Pair(".fire_height",SliderInfo(0.6f,1f,2)),
            Pair("options_menu.scale",SliderInfo(0.5f,1f,2)),
            Pair("scoreboardsize",SliderInfo(0.2f,1.2f,2)),
            Pair("blockhit.alpha",SliderInfo(0.0f,1.0f,2)),
            Pair("blockhit.hit_overlay.alpha",SliderInfo(0.0f,1.0f,2)),
            Pair(".lava_view",SliderInfo(0.0f,1.0f,2)),
            Pair("fullbright.value",SliderInfo(0.0f,10.0f,1)),
            Pair("scoreboard.scale",SliderInfo(0.5f,1.5f,2))
    )

    val arrays: HashMap<String,ArrayList<String>> = hashMapOf(
            Pair(".first_line", TiwylaArray),
            Pair(".second_line", TiwylaArray),
            Pair(".third_line", TiwylaArray),
            Pair(".cps_elements", arrayListOf(
                    "cps.both","cps.left","cps.right"
            ))
    )

    val widgets: JsonObject = WidgetRenderer.getOptionsObject()

    val design: JsonObject = JsonObject()

    val general: JsonObject = JsonObject()

    init {
        val BV = JsonObject()
        BV.add("powder_snow", JsonPrimitive(false))
        BV.add("lava", JsonPrimitive(false))
        BV.add("nether", JsonPrimitive(false))
        BV.add("lava_view", JsonPrimitive(0.5))
        BV.add("water", JsonPrimitive(false))
        val BH = JsonObject()
        BH.add("color", JsonPrimitive(0))
        BH.add("alpha", JsonPrimitive(0.4))
        BH.add("enabled", JsonPrimitive(false))
        val HO = JsonObject()
        HO.add("color", JsonPrimitive(0))
        HO.add("alpha", JsonPrimitive(0.33))
        HO.add("enabled", JsonPrimitive(false))
        BH.add("hit_overlay", HO)
        val OM = JsonObject()
        OM.add("animation_time", JsonPrimitive(200.0))
        OM.add("scale", JsonPrimitive(0.75))
        OM.add("all_click", JsonPrimitive(true))
        val FB = JsonObject()
        FB.add("night_vision", JsonPrimitive(false))
        FB.add("value", JsonPrimitive(1.0))
        FB.add("enabled", JsonPrimitive(false))
        val HI = JsonObject()
        HI.add("maxinfolength", JsonPrimitive(5.0))
        HI.add("held_item_info", JsonPrimitive(false))
        val SC = JsonObject()
        SC.add("scale", JsonPrimitive(1.0))
        SC.add("hide_numbers", JsonPrimitive(false))
        design.add("better_visibility",BV)
        design.add("blockhit",BH)
        design.add("options_menu",OM)
        design.add("fullbright",FB)
        design.add("held_item_info",HI)
        design.add("scoreboard",SC)
        design.add("fire_height", JsonPrimitive(1.0))
        design.add("disable_pumpkin_overlay", JsonPrimitive(false))
        design.add("shulker_box_tooltip", JsonPrimitive(false))
        design.add("extend_status_effect_info", JsonPrimitive(false))
        design.add("cleaner_debug_menu", JsonPrimitive(false))
        design.add("show_pumpkin_icon", JsonPrimitive(true))

        val EX = JsonObject()
        EX.add("auto_update", JsonPrimitive(false))
        general.add("experimental", EX)
        general.add("instant_zoom", JsonPrimitive(false))
        general.add("zoom_enabled", JsonPrimitive(true))
        general.add("hard_zoom", JsonPrimitive(false))
        general.add("tnt_timer", JsonPrimitive(false))
        general.add("screenshot_folder_open", JsonPrimitive(false))
    }

    fun getDefault(string: String): JsonObject {
        when (string) {
            "widgets" -> return widgets
            "general" -> return general
            "design" -> return design
        }
        return JsonObject()
    }
}