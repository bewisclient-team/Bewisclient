package bewis09.bewisclient.hat;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.util.Identifier;

// TODO Document
public record Hat(Identifier texture) {
    public static final Hat EMPTY = new Hat(Identifier.of("bewisclient", "gui/nothing.png"));
    public static final Hat TECHNOBLADE = new Hat(Identifier.of("bewisclient","hat/hat1.png"));
    public static final Hat CHRISTMAS = new Hat(Identifier.of("bewisclient","hat/christmas.png"));
    public static final Hat HEADPHONES = new Hat(Identifier.of("bewisclient","hat/headphones.png"));
    public static final Hat[] HATS = {
            EMPTY, TECHNOBLADE, CHRISTMAS, HEADPHONES
    };
    public static Hat current_hat;

    static {
        try {
            current_hat = HATS[(int) SettingsLoader.INSTANCE.get("design", Settings.Companion.getSettings().getHAT())];
        } catch (Exception e) {
            current_hat = EMPTY;
        }
    }
}