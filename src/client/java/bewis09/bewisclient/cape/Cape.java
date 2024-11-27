package bewis09.bewisclient.cape;

import net.minecraft.util.Identifier;

/**
 * The class for non-animated capes
 */
public class Cape extends AbstractCape {

    /**
     * The {@link Identifier} of the cape texture
     */
    final Identifier identifier;

    /**
     * The current cape that should be displayed when rendering a player
     */
    private static AbstractCape currentCape;

    /**
     * The current cape that is selected
     */
    private static AbstractCape currentRealCape;

    /**
     * @param name The name of the cape
     */
    public Cape(String name) {
        this.identifier = Identifier.of("bewisclient","cape/"+name+".png");
    }

    /**
     * @return The current cape, that should be displayed when rendering a player
     */
    public static AbstractCape getCurrentCape() {
        return currentCape;
    }

    /**
     * Sets the cape that should be displayed when rendering a player
     *
     * @param currentCape The cape that should be displayed
     */
    public static void setCurrentCape(AbstractCape currentCape) {
        Cape.currentCape = currentCape;
    }

    /**
     * Sets the cape that should be displayed when rendering a player and the cape that is selected
     *
     * @param currentCape The cape that should be displayed
     */
    public static void setCurrentRealCape(AbstractCape currentCape) {
        Cape.currentCape = currentCape;
        Cape.currentRealCape = currentCape;
    }

    /**
     * @return The cape that is currently selected
     */
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
