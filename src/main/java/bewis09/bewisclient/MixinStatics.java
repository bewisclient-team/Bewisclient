package bewis09.bewisclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;

// TODO Document
@SuppressWarnings("ALL")
public class MixinStatics {
    public static Boolean isZoomed = true;

    public static ArrayList<Integer> OwnPlayerSkinTextures = new ArrayList<>();

    @Environment(value = EnvType.CLIENT)
    public static final class SidebarEntry {
        public final Text name;
        public final Text score;
        public final int scoreWidth;

        public SidebarEntry(Text name, Text score, int scoreWidth) {
            this.name = name;
            this.score = score;
            this.scoreWidth = scoreWidth;
        }
    }
}
