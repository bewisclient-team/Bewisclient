package bewis09.bewisclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class MixinStatics {

    /**
     * Indicates if the game is currently zoomed in
     */
    public static Boolean isZoomed = true;

    /**
     * A collection of hashCodes of the {@link net.minecraft.client.util.SkinTextures} that have been loaded
     */
    public static ArrayList<Integer> OwnPlayerSkinTextures = new ArrayList<>();

    /**
     * A record originally used in {@linkplain net.minecraft.client.gui.hud.InGameHud#renderScoreboardSidebar(DrawContext, ScoreboardObjective)}
     */
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
