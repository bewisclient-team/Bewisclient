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
     * The information for a slider in a [bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement]
     *
     * @param start The minimum value
     * @param end The maximum value
     * @param decimalPoints The number of digits after the decimal point
     */
    class SliderInfo(val start: Float, val end: Float, val decimalPoints: Int)

    /**
     * The array for cycling through the possible options for the Tiwyla lines
     */
    val TiwylaArray = arrayListOf("tiwyla.tool","tiwyla.level","tiwyla.time","tiwyla.tool.progress","tiwyla.level.progress","tiwyla.time.progress","tiwyla.tool.extra","tiwyla.level.extra","tiwyla.time.extra")

    /**
     * The sliders and corresponding data
     */
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

    val widgets: JsonObject = WidgetRenderer.getOptionsObject()

    val design: JsonObject = JsonObject()

    val general: JsonObject = JsonObject()

    init {
        val BV = JsonObject()
        BV.add(POWDER_SNOW, JsonPrimitive(false))
        BV.add(LAVA, JsonPrimitive(false))
        BV.add(NETHER, JsonPrimitive(false))
        BV.add(LAVA_VIEW, JsonPrimitive(0.5))
        BV.add(WATER, JsonPrimitive(false))
        val BH = JsonObject()
        BH.add(COLOR, JsonPrimitive(0))
        BH.add(ALPHA, JsonPrimitive(0.4))
        BH.add(ENABLED, JsonPrimitive(false))
        val HO = JsonObject()
        HO.add(COLOR, JsonPrimitive(0))
        HO.add(ALPHA, JsonPrimitive(0.33))
        HO.add(ENABLED, JsonPrimitive(false))
        BH.add(HIT_OVERLAY.last(), HO)
        val OM = JsonObject()
        OM.add(ANIMATION_TIME, JsonPrimitive(200.0))
        OM.add(SCALE, JsonPrimitive(0.75))
        OM.add(ALL_CLICK, JsonPrimitive(true))
        OM.add(SHOW_TITLE_MENU, JsonPrimitive(true))
        OM.add(SHOW_GAME_MENU, JsonPrimitive(true))
        val FB = JsonObject()
        FB.add(NIGHT_VISION, JsonPrimitive(false))
        FB.add(FULLBRIGHT_VALUE, JsonPrimitive(1.0))
        FB.add(ENABLED, JsonPrimitive(false))
        val HI = JsonObject()
        HI.add(MAX_INFO_LENGTH, JsonPrimitive(5.0))
        HI.add(HELD_ITEM_INFO_ENABLED, JsonPrimitive(false))
        val SC = JsonObject()
        SC.add(SCALE, JsonPrimitive(1.0))
        SC.add(HIDE_NUMBERS, JsonPrimitive(false))
        design.add(BETTER_VISIBILITY.last(),BV)
        design.add(BLOCKHIT.last(),BH)
        design.add(OPTIONS_MENU.last(),OM)
        design.add(FULLBRIGHT.last(),FB)
        design.add(HELD_ITEM_INFO.last(),HI)
        design.add(SCOREBOARD.last(),SC)
        design.add(FIRE_HEIGHT, JsonPrimitive(1.0))
        design.add(DISABLE_PUMPKIN_OVERLAY, JsonPrimitive(false))
        design.add(SHULKER_BOX_TOOLTIP, JsonPrimitive(false))
        design.add(EXTEND_STATUS_EFFECT_INFO, JsonPrimitive(false))
        design.add(CLEANER_DEBUG_MENU, JsonPrimitive(false))
        design.add(SHOW_PUMPKIN_ICON, JsonPrimitive(true))

        val EX = JsonObject()
        EX.add(AUTO_UPDATE, JsonPrimitive(false))
        general.add(EXPERIMENTAL.last(), EX)
        general.add(INSTANT_ZOOM, JsonPrimitive(false))
        general.add(ZOOM_ENABLED, JsonPrimitive(true))
        general.add(HARD_ZOOM, JsonPrimitive(false))
        general.add(TNT_TIMER, JsonPrimitive(false))
        general.add(PERSPECTIVE, JsonPrimitive(false))
        general.add(SCREENSHOT_OPEN_FOLDER, JsonPrimitive(false))
    }

    /**
     * @param string The settings category id
     *
     * @return The default values for the category id
     */
    fun getDefault(string: String): JsonObject {
        when (string) {
            WIDGETS -> return widgets
            GENERAL -> return general
            DESIGN -> return design
        }
        return JsonObject()
    }
}

private fun JsonObject.add(element: SettingsLoader.TypedSettingID<*>, jsonElement: JsonElement) {
    this.add(element.id,jsonElement)
}
