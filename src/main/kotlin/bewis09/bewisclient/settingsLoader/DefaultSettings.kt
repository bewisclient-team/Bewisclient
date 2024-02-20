package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.util.ColorSaver
import bewis09.bewisclient.widgets.WidgetRenderer

object DefaultSettings {

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
            Pair(".lava_view",SliderInfo(0.0f,1.0f,2)),
            Pair("fullbright.value",SliderInfo(0.0f,10.0f,1))
    )

    val arrays: HashMap<String,ArrayList<String>> = hashMapOf(
            Pair(".first_line", TiwylaArray),
            Pair(".second_line", TiwylaArray),
            Pair(".third_line", TiwylaArray),
            Pair(".cps_elements", arrayListOf(
                    "cps.both","cps.left","cps.right"
            ))
    )

    val widgets: Array<Pair<String,Any>> = WidgetRenderer.getOptionsArrayList().toTypedArray()

    val design: Array<Pair<String,Any>> = arrayOf(
        Pair("options_menu",SettingsLoader.Settings()),
            Pair("options_menu.animation_time",200F),
            Pair("options_menu.scale",0.75f),
            Pair("extend_status_effect_info",false),
            Pair("held_item_info",false),
            Pair("maxinfolength",5F),
            Pair("disable_pumpkin_overlay",false),
            Pair("show_pumpkin_icon",true),
            Pair("fire_height",1f),
            Pair("cleaner_debug_menu",false),
            Pair("options_menu.all_click",false),
            Pair("shulker_box_tooltip",false),
        Pair("blockhit",SettingsLoader.Settings()),
            Pair("blockhit.enabled",false),
            Pair("blockhit.color",ColorSaver(0)),
            Pair("blockhit.alpha",0.4f),
        Pair("better_visibility",SettingsLoader.Settings()),
            Pair("better_visibility.lava",false),
            Pair("better_visibility.lava_view",0.5f),
            Pair("better_visibility.water",false),
            Pair("better_visibility.nether",false),
            Pair("better_visibility.powder_snow",false),
        Pair("fullbright",SettingsLoader.Settings()),
            Pair("fullbright.enabled",false),
            Pair("fullbright.night_vision",false),
            Pair("fullbright.value",1f),
        Pair("background_texture","texture_minecraft:textures/gui/options_background.png")
    )

    val general: Array<Pair<String,Any>> = arrayOf(
        Pair("zoom_enabled",true),
        Pair("instant_zoom",false),
        Pair("hard_zoom",false)
    )

    fun getDefault(string: String): Array<Pair<String,Any>> {
        when (string) {
            "widgets" -> return widgets
            "general" -> return general
            "design" -> return design
        }
        return arrayOf()
    }
}