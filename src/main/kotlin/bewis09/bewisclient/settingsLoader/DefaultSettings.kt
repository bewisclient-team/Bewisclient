package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.util.ColorSaver
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object DefaultSettings {

    private val dateFormat: DateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault())
    private val d = !((dateFormat as SimpleDateFormat).toPattern().contains("a"))

    class SliderInfo(val start: Float, val end: Float, val decimalPoints: Int)

    val TiwylaArray = arrayListOf("tiwyla.tool","tiwyla.level","tiwyla.time","tiwyla.tool.progress","tiwyla.level.progress","tiwyla.time.progress","tiwyla.tool.extra","tiwyla.level.extra","tiwyla.time.extra")

    val sliders: HashMap<String,SliderInfo> = hashMapOf(
            Pair(".transparency",SliderInfo(0f,1f,2)),
            Pair(".size",SliderInfo(0.2f,2f,2)),
            Pair("options_menu.animation_time",SliderInfo(0f,500f,0)),
            Pair(".maxinfolength",SliderInfo(1f,10f,0)),
            Pair(".fire_height",SliderInfo(0.6f,1f,2)),
            Pair("options_menu.scale",SliderInfo(0.5f,1f,2))
    )

    val arrays: HashMap<String,ArrayList<String>> = hashMapOf(
            Pair(".first_line", TiwylaArray),
            Pair(".second_line", TiwylaArray),
            Pair(".third_line", TiwylaArray),
            Pair(".cps_elements", arrayListOf(
                    "cps.both","cps.left","cps.right"
            ))
    )

    val widgets: Array<Pair<String,Any>> = arrayOf(
        Pair("coordinates",SettingsLoader.Settings()),
            Pair("coordinates.enabled",true),
            Pair("coordinates.transparency",0.43F),
            Pair("coordinates.size",0.7F),
            Pair("coordinates.posX",5.0F),
            Pair("coordinates.partX",1.0F),
            Pair("coordinates.posY",5.0F),
            Pair("coordinates.partY",-1.0F),
            Pair("coordinates.text_color",ColorSaver(0xFFFFFF)),
            Pair("coordinates.show_biome",false),
            Pair("coordinates.show_direction",false),
            Pair("coordinates.colorcode_biome",false),
        Pair("fps",SettingsLoader.Settings()),
            Pair("fps.enabled",true),
            Pair("fps.transparency",0.43F),
            Pair("fps.size",0.7F),
            Pair("fps.posX",5.0F),
            Pair("fps.partX",1.0F),
            Pair("fps.posY",35.0F),
            Pair("fps.partY",-1.0F),
            Pair("fps.text_color",ColorSaver(0xFFFFFF)),
        Pair("effect",SettingsLoader.Settings()),
            Pair("effect.enabled",true),
            Pair("effect.transparency",1F),
            Pair("effect.size",1F),
            Pair("effect.posX",0F),
            Pair("effect.partX",0F),
            Pair("effect.posY",2F),
            Pair("effect.partY",-1.0F),
        Pair("cps",SettingsLoader.Settings()),
            Pair("cps.enabled",true),
            Pair("cps.transparency",0.43F),
            Pair("cps.size",0.7F),
            Pair("cps.posX",5.0F),
            Pair("cps.partX",1.0F),
            Pair("cps.posY",47.0F),
            Pair("cps.partY",-1.0F),
            Pair("cps.text_color",ColorSaver(0xFFFFFF)),
            Pair("cps.cps_elements",0F),
        Pair("biome",SettingsLoader.Settings()),
            Pair("biome.enabled",true),
            Pair("biome.transparency",0.43F),
            Pair("biome.size",0.7F),
            Pair("biome.posX",5.0F),
            Pair("biome.partX",-1.0F),
            Pair("biome.posY",5.0F),
            Pair("biome.partY",1.0F),
            Pair("biome.text_color",ColorSaver(0xFFFFFF)),
            Pair("biome.colorcode_biome",false),
        Pair("days",SettingsLoader.Settings()),
            Pair("days.enabled",true),
            Pair("days.transparency",0.43F),
            Pair("days.size",0.7F),
            Pair("days.posX",5.0F),
            Pair("days.partX",1.0F),
            Pair("days.posY",59.0F),
            Pair("days.partY",-1.0F),
            Pair("days.text_color",ColorSaver(0xFFFFFF)),
        Pair("ping",SettingsLoader.Settings()),
            Pair("ping.enabled",true),
            Pair("ping.transparency",0.43F),
            Pair("ping.size",0.7F),
            Pair("ping.posX",5.0F),
            Pair("ping.partX",1.0F),
            Pair("ping.posY",95.0F),
            Pair("ping.partY",-1.0F),
            Pair("ping.text_color",ColorSaver(0xFFFFFF)),
        Pair("daytime",SettingsLoader.Settings()),
            Pair("daytime.enabled",true),
            Pair("daytime.transparency",0.43F),
            Pair("daytime.size",0.7F),
            Pair("daytime.posX",5.0F),
            Pair("daytime.partX",1.0F),
            Pair("daytime.posY",83.0F),
            Pair("daytime.partY",-1.0F),
            Pair("daytime.text_color",ColorSaver(0xFFFFFF)),
            Pair("daytime.24Clock", d),
        Pair("speed",SettingsLoader.Settings()),
            Pair("speed.enabled",true),
            Pair("speed.transparency",0.43F),
            Pair("speed.size",0.7F),
            Pair("speed.posX",5.0F),
            Pair("speed.partX",1.0F),
            Pair("speed.posY",71.0F),
            Pair("speed.partY",-1.0F),
            Pair("speed.text_color",ColorSaver(0xFFFFFF)),
            Pair("speed.vertical_speed",false),
        Pair("keys",SettingsLoader.Settings()),
            Pair("keys.enabled",true),
            Pair("keys.transparency",0.43F),
            Pair("keys.size",1F),
            Pair("keys.posX",5.0F),
            Pair("keys.partX",-1.0F),
            Pair("keys.posY",17.0F),
            Pair("keys.partY",1.0F),
            Pair("keys.text_color",ColorSaver(0xFFFFFF)),
        Pair("tiwyla",SettingsLoader.Settings()),
            Pair("tiwyla.enabled",true),
            Pair("tiwyla.transparency",0.43F),
            Pair("tiwyla.size",1F),
            Pair("tiwyla.posX",5.0F),
            Pair("tiwyla.partX",0.0F),
            Pair("tiwyla.posY",5.0F),
            Pair("tiwyla.partY",-1.0F),
            Pair("tiwyla.top_color",ColorSaver(0xFFFFFF)),
            Pair("tiwyla.bottom_color",ColorSaver(0xFFFFFF)),
            Pair("tiwyla.first_line",6F),
            Pair("tiwyla.second_line",1F),
            Pair("tiwyla.third_line",5F),
    )

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
            Pair("cleaner_debug_menu",false)
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