package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement
import bewis09.bewisclient.settingsLoader.settings.*
import bewis09.bewisclient.util.ColorSaver

/**
 * A collection of all settings and setting paths
 */
open class Settings {
    companion object {
        val scoreboard = ScoreboardSettings()
        val experimental = ExperimentalSettings()
        val fullbright = FullbrightSettings()
        val better_visibility = BetterVisibilitySettings()
        val zoom = ZoomSettings()
        val blockhit = BlockhitSettings()
        val hit_overlay = HitOverlaySettings()
        val pumpkin = PumpkinSettings()
        val held_item_info = HeldItemInfoSettings()
        val options_menu = OptionMenuSettings()
        val cosmetics = CosmeticsSettings()
        val perspective = PerspectiveSettings()
        val utilitiesSettings = UtilitiesSettings()
        val cleanerDebugMenuSettings = CleanerDebugMenuSettings()
        val shulkerBoxTooltipSettings = ShulkerBoxTooltipSettings()
        val tntTimerSettings = TNTTimerSettings()

        interface SettingToElementProvider {
            fun getElements(): Array<OptionElement> {
                return getElementSettings().mapNotNull { it.createOptionElement() }.toTypedArray()
            }

            fun getElementSettings(): Array<Setting<*,*>>

            fun getTitle(): String? = null
        }

        class ScoreboardSettings: SettingToElementProvider {
            private val path = arrayOf("scoreboard")

            val scale = FloatSetting(DESIGN, path, "scale", 1f)
            val hide_numbers = BooleanSetting(DESIGN, path, "hide_numbers", false)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(scale, hide_numbers)
            }

            override fun getTitle(): String {
                return "scorboard"
            }
        }

        class ExperimentalSettings: SettingToElementProvider {
            private val path = arrayOf("experimental")

            val auto_update = BooleanSetting(GENERAL, path, "auto_update", false)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(auto_update)
            }

            override fun getTitle(): String {
                return "experimental"
            }
        }

        class FullbrightSettings: BooleanSetting(DESIGN, arrayOf("fullbright"), "enabled", false), SettingToElementProvider {
            val fullbright_value = FloatSetting(DESIGN, path, "value", 1f)
            val night_vision = BooleanSetting(DESIGN, path, "fullbright", false)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(fullbright_value, night_vision)
            }
        }

        class BetterVisibilitySettings: SettingToElementProvider {
            private val path = arrayOf("better_visibility")

            val lava = BooleanSetting(DESIGN, path, "lava", false, null)
            val water = BooleanSetting(DESIGN, path, "water", false)
            val nether = BooleanSetting(DESIGN, path, "nether", false)
            val powder_snow = BooleanSetting(DESIGN, path, "powder_snow", false)
            val terrain_fog = BooleanSetting(DESIGN, path, "terrain_fog", false)

            val multiple = MultipleBooleanSetting(DESIGN, path, arrayOf(
                lava, water, nether, powder_snow, terrain_fog
            ))

            val lava_view = FloatSetting(DESIGN, path, "lava_view", 0.5f, FloatOptionElement.SliderInfo(0.0f, 1.0f, 2), false, {
                return@FloatSetting lava.get()
            }, null)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(multiple, lava_view)
            }

            override fun getTitle(): String {
                return "better_visibility"
            }
        }

        class ZoomSettings: BooleanSetting(GENERAL, arrayOf(), "zoom_enabled", true), SettingToElementProvider {
            val instant_zoom = BooleanSetting(GENERAL, arrayOf(), "instant_zoom", false)
            val hard_zoom = BooleanSetting(GENERAL, arrayOf(), "hard_zoom", false)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title(), instant_zoom, hard_zoom)
            }
        }

        class BlockhitSettings: BooleanSetting(DESIGN, arrayOf("blockhit"), "enabled", false), SettingToElementProvider {
            val color = ColorSaverSetting(DESIGN, path, "color", ColorSaver.of(0))
            val alpha = FloatSetting(DESIGN, path, "alpha", 0.4f)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title(), color, alpha, hit_overlay.title(), color, alpha)
            }
        }

        class HitOverlaySettings: BooleanSetting(DESIGN, arrayOf("blockhit","hit_overlay"), "enabled", false) {
            val color = ColorSaverSetting(DESIGN, path, "color", ColorSaver.of(0))
            val alpha = FloatSetting(DESIGN, path, "alpha", 0.33f)
        }

        class PumpkinSettings: BooleanSetting(DESIGN, arrayOf(), "disable_pumpkin_overlay", false), SettingToElementProvider {
            val show_pumpkin_icon = BooleanSetting(DESIGN, arrayOf(), "show_pumpkin_icon", false)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title(), show_pumpkin_icon)
            }
        }

        class HeldItemInfoSettings: BooleanSetting(DESIGN, arrayOf("held_item_info"), "held_item_info", false), SettingToElementProvider {
            val maxinfolength = FloatSetting(DESIGN, path, "maxinfolength", 5.0f)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title(), maxinfolength)
            }
        }

        class OptionMenuSettings: SettingToElementProvider {
            private val path = arrayOf("options_menu")

            val animation_time = FloatSetting(DESIGN, path, "animation_time", 200f)
            val show_title_menu = BooleanSetting(DESIGN, path, "show_title_menu", true)
            val show_game_menu = BooleanSetting(DESIGN, path, "show_game_menu", true)
            val shown_start_menu = BooleanSetting(DESIGN, path, "shown_start_menu", true)
            val scale = FloatSetting(DESIGN, path, "scale", 0.75f)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(animation_time,scale,show_game_menu,show_title_menu)
            }

            override fun getTitle(): String {
                return "option_menu"
            }
        }

        class UtilitiesSettings: SettingToElementProvider {
            val screenshot_folder_open = BooleanSetting(GENERAL, arrayOf(), "screenshot_folder_open", false)
            val fire_height = FloatSetting(DESIGN, arrayOf(), "fire_height", 1.0f)
            val extend_status_effect_info = BooleanSetting(DESIGN, arrayOf(), "extend_status_effect_info", false)

            override fun getTitle(): String {
                return "utilities"
            }

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(screenshot_folder_open, fire_height, extend_status_effect_info)
            }
        }

        class PerspectiveSettings: BooleanSetting(GENERAL, arrayOf(), "perspective", false), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title())
            }
        }

        class TNTTimerSettings: BooleanSetting(GENERAL, arrayOf(), "tnt_timer", false), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title())
            }
        }

        class ShulkerBoxTooltipSettings: BooleanSetting(DESIGN, arrayOf(), "shulker_box_tooltip", false), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title())
            }
        }

        class CleanerDebugMenuSettings: BooleanSetting(DESIGN, arrayOf(), "cleaner_debug_menu", false), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(title())
            }
        }

        class CosmeticsSettings {
            val cape = StringSetting(DESIGN, arrayOf(), "cape", "")
            val wing = StringSetting(DESIGN, arrayOf(), "wing", "")
            val hat = StringSetting(DESIGN, arrayOf(), "hat", "")
        }

        const val WIDGETS = "widgets"
        const val DESIGN = "design"
        const val GENERAL = "general"
    }
}