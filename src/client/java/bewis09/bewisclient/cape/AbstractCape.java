package bewis09.bewisclient.cape;

import net.minecraft.util.Identifier;

import javax.swing.*;

/**
 * Class used for the capes used by Bewisclient
 * <p>
 * Implemented in {@link Cape} and {@link AnimatedCape}
 */
public abstract class AbstractCape {
    /**
     * The current frame
     */
    int frame = 0;

    /**
     * Starts the Timer to animate the Cape
     */
    public void startTimer() {
        if(getFrameCount()!=1) {
            new Timer(getFrameDuration(), actionEvent -> frame = (frame + 1) % getFrameCount()).start();
        }
    }

    /**
     * @return The current frame
     */
    public int getFrame() {
        return frame;
    }

    /**
     * @return The duration of the animation
     */
    abstract int getFrameDuration();

    /**
     * @return The frame count
     */
    abstract int getFrameCount();

    /**
     * @param frame The current frame
     * @return The {@link Identifier} of the current frame
     */
    public abstract Identifier getIdentifier(int frame);
}
