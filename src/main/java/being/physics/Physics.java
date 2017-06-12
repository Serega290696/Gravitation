package being.physics;

import being.elements.Angel;
import being.elements.Atom;

import java.util.Collection;
import java.util.Set;

public interface Physics {
    void gravity(Collection<Atom> allAtoms, Collection<Atom> updatedAtoms);

    void electromagnetism(Collection<Atom> allAtoms, Collection<Atom> updatedAtoms) throws Exception;

    void awaitAngels();

    Object getLock();

    Set basalObjectsGeneration();

    boolean isPrepared(Angel angel);

    void startWork(Angel angel);

    void finishWork(Angel angel);

    void push(Collection<Atom> allAtoms, Collection<Atom> ownAtoms) throws Exception;
}
