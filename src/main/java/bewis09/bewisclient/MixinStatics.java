package bewis09.bewisclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.FogShape;
import net.minecraft.text.Text;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class MixinStatics {
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

    @Environment(value=EnvType.CLIENT)
    public static class FogData {
        public final BackgroundRenderer.FogType fogType;
        public float fogStart;
        public float fogEnd;
        public FogShape fogShape = FogShape.SPHERE;

        public FogData(BackgroundRenderer.FogType fogType) {
            this.fogType = fogType;
        }
    }
}
