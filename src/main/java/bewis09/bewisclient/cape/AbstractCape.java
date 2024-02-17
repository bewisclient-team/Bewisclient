package bewis09.bewisclient.cape;

import net.minecraft.util.Identifier;

import javax.swing.*;

public abstract class AbstractCape {

    int frame = 0;

    public void startTimer() {
        if(getFrameCount()!=1) {
            new Timer(getFrameDuration(), actionEvent -> frame = (frame + 1) % getFrameCount()).start();
        }
    }

    public int getFrame() {
        return frame;
    }

    abstract int getFrameDuration();

    abstract int getFrameCount();

    public abstract Identifier getIdentifier(int frame);
}
