package bewis09.bewisclient.wings;

import bewis09.bewisclient.JavaSettingsSender;
import net.minecraft.util.Identifier;

public record Wing(Identifier texture) {
    public static final Wing EMPTY = new Wing(Identifier.of("bewisclient", "gui/nothing.png"));
    public static final Wing WING1 = new Wing(Identifier.of("bewisclient","wing/wing1.png"));
    public static final Wing FIRE = new Wing(Identifier.of("bewisclient","wing/fire.png"));
    public static final Wing[] WINGS = {
            EMPTY,WING1,FIRE
    };
    public static Wing current_wing;

    static {
        try {
            current_wing = WINGS[JavaSettingsSender.Companion.getSettings().getInt("design","wing")];
        } catch (Exception e) {
            e.printStackTrace();
            current_wing = EMPTY;
        }
    }
}