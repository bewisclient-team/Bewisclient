package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.widgets.WidgetRenderer
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

/**
 * Lists the default values for the settings
 */
object DefaultSettings: Settings() {

    /**
     * Isn't that self-explanatory?
     */
    val gson = Gson()

    /**
     * The array for cycling through the possible options for the Tiwyla lines
     */
    val TiwylaArray = arrayListOf("tiwyla.tool","tiwyla.level","tiwyla.time","tiwyla.tool.progress","tiwyla.level.progress","tiwyla.time.progress","tiwyla.tool.extra","tiwyla.level.extra","tiwyla.time.extra")

    /**
     * The sliders and corresponding data
     */
    val sliders: HashMap<String,SliderInfo> = hashMapOf(
            Pair("options_menu.animation_time",SliderInfo(0f,500f,0)),
            Pair(".maxinfolength",SliderInfo(1f,10f,0)),
            Pair(".fire_height",SliderInfo(0.6f,1f,2)),
            Pair("options_menu.scale",SliderInfo(0.5f,1f,2)),
            Pair("scoreboardsize",SliderInfo(0.2f,1.2f,2)),
            Pair("blockhit.alpha",SliderInfo(0.0f,1.0f,2)),
            Pair("blockhit.hit_overlay.alpha",SliderInfo(0.0f,1.0f,2)),
            Pair("fullbright.value",SliderInfo(0.0f,10.0f,1)),
            Pair("scoreboard.scale",SliderInfo(0.5f,1.5f,2))
    )

    /**
     * All arrays for [bewis09.bewisclient.drawable.option_elements.settings.ArrayOptionElement]
     */
    val arrays: HashMap<String,ArrayList<String>> = hashMapOf(
            Pair(".first_line", TiwylaArray),
            Pair(".second_line", TiwylaArray),
            Pair(".third_line", TiwylaArray),
            Pair(".cps_elements", arrayListOf(
                    "cps.both","cps.left","cps.right"
            ))
    )
}