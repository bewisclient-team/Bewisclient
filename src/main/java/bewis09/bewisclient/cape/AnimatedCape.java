package bewis09.bewisclient.cape;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * The class for the animated capes
 */
public class AnimatedCape extends AbstractCape {

    /**
     * The number of frames
     */
    private final int frameCount;

    /**
     * The list of Identifiers
     */
    private final List<Identifier> identifierList;

    /**
     * The duration of the animation
     */
    private final int frameDuration;

    /**
     * @param frameCount The number of frames
     * @param name The name of the cape
     * @param frameDuration The duration of the animation
     */
    public AnimatedCape(int frameCount, String name, int frameDuration) {
        this.frameCount = frameCount;
        this.frameDuration = frameDuration;
        identifierList = new ArrayList<>();
        for (int i = 0; i < frameCount; i++) {
            identifierList.add(Identifier.of("bewisclient","cape/"+name.replace("%20", String.valueOf(i))+".png"));
        }
        startTimer();
    }

    @Override
    int getFrameDuration() {
        return frameDuration;
    }

    @Override
    int getFrameCount() {
        return frameCount;
    }

    @Override
    public Identifier getIdentifier(int frame) {
        return identifierList.get(frame);
    }
}
