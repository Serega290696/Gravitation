package being.universe;

import being.elements.Atom;
import being.mathematics.ThreeVector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public abstract class AbstractUniverse<P, O> {
    P physics;
    Set<O> objects;
    UniverseType type;
    UniverseStage stage = UniverseStage.UNBORN;
    ThreeVector mousePosition;
    Map<String, List<O>> savedStates = new HashMap<>();
    O focusedAtom;
    volatile ObjectProcessingStage objectProcessingStage = ObjectProcessingStage.DISTRIBUTION;

//    Map<String, List<O>> defaultStates = new HashMap<>();

    public Map<String, List<O>> getSavedStates() {
        return savedStates;
    }

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

    public Set<O> getObjects() {
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

    public abstract void mouseClick(double x, double y, double clickDuration, int mouseKeyCode, boolean onlyCreation);

    public void setMousePosition(double x, double y) {
        mousePosition = new ThreeVector(x, y, 0);
    }

    public boolean isPaused() {
        return stage == UniverseStage.PAUSE;
    }

    public abstract void createDefaultAtoms();

    public abstract void deleteObject(Atom selected);

    abstract void restartUniverseInState(List<O> newObjects);

    public void restoreState(String stateTitle) {
        restartUniverseInState(savedStates.get(stateTitle));
    }

    public void saveState(String stateTitle) {
        saveState(stateTitle, new ArrayList<>(objects));
    }

    public void saveState(String stateTitle, List<O> objects) {
        savedStates.put(stateTitle, new ArrayList<>(objects));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("states.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedStates);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void addObjects(O... object);

    public void setFocusedAtom(O focusedAtom) {
        this.focusedAtom = focusedAtom;
    }

    public O getFocusedAtom() {
        return focusedAtom;
    }

    public abstract boolean areObjectsAvailable();

    public abstract void setObjectsAvailable(boolean objectsAvailable);

    public  ObjectProcessingStage getObjectProcessingStage(){
        return objectProcessingStage;
    }

    public  void setObjectProcessingStage(ObjectProcessingStage objectProcessingStage) {
        this.objectProcessingStage = objectProcessingStage;
    }

    public abstract boolean mousePush(double x, double y, double clickDuration, int mouseKeyCode, boolean onlyCreation);

    public abstract void clearAllAtoms();
}
