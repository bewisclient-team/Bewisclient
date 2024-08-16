package bewis09.bewisclient.wings;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.util.Identifier;

/**
 * A record used for saving the wing textures
 *
 * @param texture The {@link Identifier} of the wing
 */
public record Wing(Identifier texture) {
    public static final Wing EMPTY = new Wing(Identifier.of("bewisclient", "gui/nothing.png"));
    public static final Wing WING1 = new Wing(Identifier.of("bewisclient","wing/wing1.png"));
    public static final Wing FIRE = new Wing(Identifier.of("bewisclient","wing/fire.png"));

    /**
     * A collection of all wings
     */
    public static final Wing[] WINGS = {
            EMPTY,WING1,FIRE
    };

    /**
     * The wing that is currently selected
     */
    public static Wing current_wing;

    static {
        try {
            current_wing = WINGS[(int) SettingsLoader.INSTANCE.get("design", Settings.Companion.getWING())];
        } catch (Exception e) {
            current_wing = EMPTY;
        }
    }
}