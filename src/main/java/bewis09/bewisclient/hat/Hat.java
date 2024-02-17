package bewis09.bewisclient.hat;

import bewis09.bewisclient.JavaSettingsSender;
import net.minecraft.util.Identifier;

public record Hat(Identifier texture) {
    public static final Hat EMPTY = new Hat(new Identifier("bewisclient", "gui/nothing.png"));
    public static final Hat TECHNOBLADE = new Hat(new Identifier("bewisclient","hat/hat1.png"));
    public static final Hat CHRISTMAS = new Hat(new Identifier("bewisclient","hat/christmas.png"));
    public static final Hat HEADPHONES = new Hat(new Identifier("bewisclient","hat/headphones.png"));
    public static final Hat[] HATS = {
            EMPTY, TECHNOBLADE, CHRISTMAS, HEADPHONES
    };
    public static Hat current_hat;

    static {
        try {
            current_hat = HATS[Math.round(JavaSettingsSender.Companion.getDesignSettings().getValue("hat"))];
        } catch (Exception e) {
            e.printStackTrace();
            current_hat = EMPTY;
        }
    }
}