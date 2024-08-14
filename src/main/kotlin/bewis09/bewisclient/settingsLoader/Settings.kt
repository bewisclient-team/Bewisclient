package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.settingsLoader.SettingsLoader.TypedSettingID
import bewis09.bewisclient.util.ColorSaver

// TODO Document
open class Settings {
    
    // General
    val ENABLED = createBoolean("enabled")
    val TEXT_COLOR = createColor("text_color")
    val COLOR = createColor("color")
    val ALPHA = createFloat("alpha")
    val SCALE = createFloat("scale")

    // Widgets
    val SIZE = createFloat("size")
    val POSX = createFloat("posX")
    val POSY = createFloat("posY")
    val PARTX = createFloat("partX")
    val TRANSPARENCY = createFloat("transparency")
    val PARTY = createFloat("partY")
    val CLOCK24 = createBoolean("24Clock")
    val FIRST_LINE = createFloat("first_line")
    val SECOND_LINE = createFloat("second_line")
    val THIRD_LINE = createFloat("third_line")
    val SHOW_BIOME = createBoolean("show_biome")
    val SHOW_DIRECTION = createBoolean("show_direction")
    val COLORCODE_BIOME = createBoolean("colorcode_biome")
    val CPS_ELEMENTS = createFloat("cps_elements")
    val TOP_COLOR = createColor("top_color")
    val BOTTOM_COLOR = createColor("bottom_color")
    val SHOW_BLOCK_ICON = createBoolean("show_block_icon")
    val SHOW_HEALTH_INFORMATION = createBoolean("show_health_information")
    val SHOW_PROGRESS_BAR = createBoolean("show_progress_bar")
    val VERTICAL_SPEED = createBoolean("vertical_speed")
    val SHOW_SPACE_BAR = createBoolean("show_space_bar")
    val SHOW_MOVEMENT_KEYS = createBoolean("show_movement_keys")
    val SHOW_MOUSE_BUTTON = createBoolean("show_mouse_button")
    val SHOW_CPS = createBoolean("show_cps")
    val SELECT_PARTS = arrayOf("select_parts")

    val SPEED = arrayOf("speed")

    // Options Menu
    val OPTIONS_MENU = arrayOf("options_menu")
    val ANIMATION_TIME = createFloat("animation_time")
    val ALL_CLICK = createBoolean("all_click")
    
    // Scoreboard
    val SCOREBOARD = arrayOf("scoreboard")
    val HIDE_NUMBERS = createBoolean("hide_numbers")
    
    // Experimental
    val EXPERIMENTAL = arrayOf("experimental")
    val AUTO_UPDATE = createBoolean("auto_update")

    // Blockhit
    val BLOCKHIT = arrayOf("blockhit")
    val HIT_OVERLAY = arrayOf("blockhit","hit_overlay")

    // Fullbright
    val FULLBRIGHT = arrayOf("fullbright")
    val FULLBRIGHT_VALUE = createFloat("value")
    val NIGHT_VISION = createBoolean("night_vision")

    // Categories
    val WIDGETS = "widgets"
    val DESIGN = "design"
    val GENERAL = "general"
    
    // Better Visibility
    val BETTER_VISIBILITY = arrayOf("better_visibility")
    val LAVA_VIEW = createFloat("lava_view")
    val LAVA = createBoolean("lava")
    val NETHER = createBoolean("nether")
    val WATER = createBoolean("water")
    val POWDER_SNOW = createBoolean("powder_snow")
    
    // Zoom
    val ZOOM_ENABLED = createBoolean("zoom_enabled")
    val INSTANT_ZOOM = createBoolean("instant_zoom")
    val HARD_ZOOM = createBoolean("hard_zoom")
    
    // Pumpkin
    val DISABLE_PUMPKIN_OVERLAY = createBoolean("disable_pumpkin_overlay")
    val SHOW_PUMPKIN_ICON = createBoolean("show_pumpkin_icon")

    // Held Item Info
    val HELD_ITEM_INFO = arrayOf("held_item_info")
    val HELD_ITEM_INFO_ENABLED = createBoolean("held_item_info")
    val MAX_INFO_LENGTH = createFloat("maxinfolength")
    
    // Util
    val EXTEND_STATUS_EFFECT_INFO = createBoolean("extend_status_effect_info")
    val FIRE_HEIGHT = createFloat("fire_height")
    val SCREENSHOT_OPEN_FOLDER = createBoolean("screenshot_folder_open")

    // Cleaner Debug Menu
    val CLEANER_DEBUG_MENU = createBoolean("cleaner_debug_menu")

    // Shulker Box Tooltip
    val SHULKER_BOX_TOOLTIP = createBoolean("shulker_box_tooltip")

    // TNT Timer
    val TNT_TIMER = createBoolean("tnt_timer")

    // Cosmetics
    val CAPE = createFloat("cape")
    val WING = createFloat("wing")
    val HAT = createFloat("hat")
    
    fun toPointNotation(path: Array<String>, id: TypedSettingID<*>) = path.joinToString(".")+"."+id.id

    companion object {
        val Settings: Settings = Settings()
    }
    
    fun createBoolean(s: String) = TypedSettingID<Boolean>(s)
    fun createColor(s: String) = TypedSettingID<ColorSaver>(s)
    fun createFloat(s: String) = TypedSettingID<Float>(s)
}