package bewis09.bewisclient.cape;

import bewis09.bewisclient.mixin.SkinTexturesMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class Cape extends AbstractCape {

    final Identifier identifier;

    private static AbstractCape currentCape;

    private static AbstractCape currentRealCape;

    public Cape(String name) {
        this.identifier = Identifier.of("bewisclient","cape/"+name+".png");
    }

    public Cape(Identifier identifier) {
        this.identifier = identifier;
    }

    public static AbstractCape getCurrentCape() {
        return currentCape;
    }

    public static void setCurrentCape(AbstractCape currentCape) {
        Cape.currentCape = currentCape;
    }

    public static void setCurrentRealCape(AbstractCape currentCape) {
        Cape.currentCape = currentCape;
        Cape.currentRealCape = currentCape;
    }

    public static AbstractCape getCurrentRealCape() {
        return currentRealCape;
    }

    @Override
    int getFrameDuration() {
        return 0;
    }

    @Override
    int getFrameCount() {
        return 1;
    }

    @Override
    public Identifier getIdentifier(int frame) {
        return identifier;
    }
}
