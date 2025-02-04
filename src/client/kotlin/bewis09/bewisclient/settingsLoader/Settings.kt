package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.settings.FloatOptionElement.SliderInfo
import bewis09.bewisclient.drawable.option_elements.util.TitleOptionElement
import bewis09.bewisclient.settingsLoader.settings.*
import bewis09.bewisclient.settingsLoader.settings.element_options.*
import bewis09.bewisclient.util.ColorSaver
import net.minecraft.client.MinecraftClient

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
        val utilities = UtilitiesSettings()
        val cleanerDebugMenu = CleanerDebugMenuSettings()
        val shulkerBoxTooltip = ShulkerBoxTooltipSettings()
        val tntTimer = TNTTimerSettings()

        interface SettingToElementProvider {
            fun getElements(): Array<OptionElement> {
                if(getTitle() != null) {
                    return (mutableListOf(TitleOptionElement("setting."+getTitle()!!)) + getElementSettings().mapNotNull { it.createOptionElement() }.toTypedArray()).toTypedArray()
                }

                return getElementSettings().mapNotNull { it.createOptionElement() }.toTypedArray()
            }

            fun getElementSettings(): Array<Setting<*,*>>

            fun getTitle(): String? = null
        }

        class ScoreboardSettings: SettingToElementProvider {
            private val path = arrayOf("scoreboard")

            val scale = FloatSetting(DESIGN, path, "scale", 1f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.5f,1.5f,2)))
            val hide_numbers = BooleanSetting(DESIGN, path, "hide_numbers", false, null)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(scale, hide_numbers)
            }

            override fun getTitle(): String {
                return "scorboard"
            }
        }

        class ExperimentalSettings: SettingToElementProvider {
            private val path = arrayOf("experimental")

            val auto_update = BooleanSetting(GENERAL, path, "auto_update", false, null)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(auto_update)
            }

            override fun getTitle(): String {
                return "experimental"
            }
        }

        class FullbrightSettings: BooleanSetting(DESIGN, arrayOf("fullbright"), "fullbright", false, BooleanSettingsElementOptions().asTitle().withValueChanger {
            if (fullbright.get())
                MinecraftClient.getInstance().options.gamma.value = fullbright.fullbright_value.get().toDouble()
            else
                MinecraftClient.getInstance().options.gamma.value = 1.0
        }), SettingToElementProvider {
            val fullbright_value: FloatSetting = FloatSetting(DESIGN, path, "fullbright_value", 1f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.0f, 10.0f, 1)).withValueChanger {
                if (fullbright.get())
                    MinecraftClient.getInstance().options.gamma.value = fullbright.fullbright_value.get().toDouble()
            }.withEnableFunction {
                fullbright.get()
            })
            val night_vision = BooleanSetting(DESIGN, path, "night_vision", false, null)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this, fullbright_value, night_vision)
            }
        }

        class BetterVisibilitySettings: SettingToElementProvider {
            private val path = arrayOf("better_visibility")

            val lava = BooleanSetting(DESIGN, path, "lava", false, null)
            val water = BooleanSetting(DESIGN, path, "water", false, null)
            val nether = BooleanSetting(DESIGN, path, "nether", false, null)
            val powder_snow = BooleanSetting(DESIGN, path, "powder_snow", false, null)
            val terrain_fog = BooleanSetting(DESIGN, path, "terrain_fog", false, null)

            val multiple = MultipleBooleanSetting(DESIGN, path, arrayOf(
                lava, water, nether, powder_snow, terrain_fog
            ))

            val lava_view = FloatSetting(DESIGN, path, "lava_view", 0.5f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.0f, 1.0f, 2)).withEnableFunction {
                return@withEnableFunction lava.get()
            })

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(multiple, lava_view)
            }

            override fun getTitle(): String {
                return "better_visibility"
            }
        }

        class ZoomSettings: BooleanSetting(GENERAL, arrayOf(), "zoom", true, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            val instant_zoom = BooleanSetting(GENERAL, arrayOf(), "instant_zoom", false, null)
            val hard_zoom = BooleanSetting(GENERAL, arrayOf(), "hard_zoom", false, null)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this, instant_zoom, hard_zoom)
            }
        }

        class BlockhitSettings: BooleanSetting(DESIGN, arrayOf("blockhit"), "blockhit", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            val color = ColorSaverSetting(DESIGN, path, "color", ColorSaver.of(0), null)
            val alpha = FloatSetting(DESIGN, path, "alpha", 0.4f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.0f,1.0f,2)))

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this, color, alpha, hit_overlay, color, alpha)
            }

            override fun getTitle(): String {
                return "blockhit_hit_overlay"
            }
        }

        class HitOverlaySettings: BooleanSetting(DESIGN, arrayOf("blockhit","hit_overlay"), "hit_overlay", false, BooleanSettingsElementOptions().asTitle()) {
            val color = ColorSaverSetting(DESIGN, path, "color", ColorSaver.of(0), null)
            val alpha = FloatSetting(DESIGN, path, "alpha", 0.33f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.0f,1.0f,2)))
        }

        class PumpkinSettings: BooleanSetting(DESIGN, arrayOf(), "disable_pumpkin_overlay", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            val show_pumpkin_icon = BooleanSetting(DESIGN, arrayOf(), "show_pumpkin_icon", false, null)

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this, show_pumpkin_icon)
            }
        }

        class HeldItemInfoSettings: BooleanSetting(DESIGN, arrayOf("held_item_info"), "held_item_info", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            val maxinfolength = FloatSetting(DESIGN, path, "maxinfolength", 5.0f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(1f,10f,0)))

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this, maxinfolength)
            }
        }

        class OptionMenuSettings: SettingToElementProvider {
            private val path = arrayOf("options_menu")

            val animation_time = FloatSetting(DESIGN, path, "animation_time", 200f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0f,500f,0)))
            val show_title_menu = BooleanSetting(DESIGN, path, "show_title_menu", true, null)
            val show_game_menu = BooleanSetting(DESIGN, path, "show_game_menu", true, null)
            val shown_start_menu = BooleanSetting(DESIGN, path, "shown_start_menu", true, null)
            val scale = FloatSetting(DESIGN, path, "scale", 0.75f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.5f,1f,2)))

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(animation_time,scale,show_game_menu,show_title_menu)
            }

            override fun getTitle(): String {
                return "option_menu"
            }
        }

        class UtilitiesSettings: SettingToElementProvider {
            val screenshot_folder_open = BooleanSetting(GENERAL, arrayOf(), "screenshot_folder_open", false, null)
            val fire_height = FloatSetting(DESIGN, arrayOf(), "fire_height", 1.0f, FloatSettingsElementOptions().withSliderInfo(SliderInfo(0.6f,1f,2)))
            val extend_status_effect_info = BooleanSetting(DESIGN, arrayOf(), "extend_status_effect_info", false, null)

            override fun getTitle(): String {
                return "utilities"
            }

            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(screenshot_folder_open, fire_height, extend_status_effect_info)
            }
        }

        class PerspectiveSettings: BooleanSetting(GENERAL, arrayOf(), "perspective", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this)
            }
        }

        class TNTTimerSettings: BooleanSetting(GENERAL, arrayOf(), "tnt_timer", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this)
            }
        }

        class ShulkerBoxTooltipSettings: BooleanSetting(DESIGN, arrayOf(), "shulker_box_tooltip", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this)
            }
        }

        class CleanerDebugMenuSettings: BooleanSetting(DESIGN, arrayOf(), "cleaner_debug_menu", false, BooleanSettingsElementOptions().asTitle()), SettingToElementProvider {
            override fun getElementSettings(): Array<Setting<*,*>> {
                return arrayOf(this)
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