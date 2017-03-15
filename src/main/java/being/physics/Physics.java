package being.physics;

import being.elements.Atom;

import java.util.List;
import java.util.Set;

public interface Physics {
    void gravity(List<Atom> allAtoms, List<Atom> updatedAtoms);

    void electromagnetism(List<Atom> allAtoms, List<Atom> updatedAtoms) throws Exception;

    void nextInstant();

    Object getLock();

    Set basalObjectsGeneration();
}
