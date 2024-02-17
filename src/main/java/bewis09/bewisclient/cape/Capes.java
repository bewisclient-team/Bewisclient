package bewis09.bewisclient.cape;

import bewis09.bewisclient.JavaSettingsSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Capes {

    public static final AbstractCape GOLDEN_CREEPER = new AnimatedCape(32,"golden_creeper_%20",80,false);
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
            Cape.setCurrentCape(Capes.CAPES[Math.round(JavaSettingsSender.Companion.getDesignSettings().getValue("cape"))]);
            Cape.setCurrentRealCape(Capes.CAPES[Math.round(JavaSettingsSender.Companion.getDesignSettings().getValue("cape"))]);
        } catch (Exception ignored){
            Cape.setCurrentRealCape(null);
            Cape.setCurrentCape(null);
        }
    }

    public static void register() {
    }
}
