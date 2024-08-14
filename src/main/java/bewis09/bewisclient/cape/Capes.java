package bewis09.bewisclient.cape;

import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;

import java.util.ArrayList;
import java.util.List;

// TODO Document
public class Capes {

    public static final AbstractCape GOLDEN_CREEPER = new AnimatedCape(32,"golden_creeper_%20",80);
    public static final AbstractCape MINECON_2011 = new Cape("minecon2011");
    public static final AbstractCape WORLD = new Cape("world");

    public static final AbstractCape[] CAPES;

    static {
        List<AbstractCape> capes = new ArrayList<>();
        capes.add(null);
        capes.add(GOLDEN_CREEPER);
        capes.add(MINECON_2011);
        capes.add(WORLD);
        CAPES = capes.toArray(new AbstractCape[0]);

        try {
            Cape.setCurrentCape(Capes.CAPES[(int) SettingsLoader.INSTANCE.get("design", Settings.Companion.getSettings().getCAPE())]);
            Cape.setCurrentRealCape(Capes.CAPES[(int) SettingsLoader.INSTANCE.get("design", Settings.Companion.getSettings().getCAPE())]);
        } catch (Exception ignored){
            Cape.setCurrentRealCape(null);
            Cape.setCurrentCape(null);
        }
    }

    public static void register() {
    }
}
