package being.universe;

import being.elements.Atom;

import java.util.List;
import java.util.Set;

public abstract class AbstractUniverse<P> {
    P physics;
    Set<Object> objects;
    UniverseType type;
    boolean paused;
    UniverseStage stage = UniverseStage.UNBORN;

    public abstract void bigBang();

    public void resume() {
        if (stage == UniverseStage.PAUSE) {
            stage = UniverseStage.ALIVE;
        }
    }

    public void pause() {
        if (stage == UniverseStage.ALIVE) {
            stage = UniverseStage.PAUSE;
        }
    }

    public Set<Object> getObjects() {
        return objects;
    }

    public UniverseStage getStage() {
        return stage;
    }

    public void backFrame() {
        stage = UniverseStage.BACK_FRAME;
    }

    public void nextFrame() {
        stage = UniverseStage.NEXT_FRAME;
    }
}
