package being.physics;

import being.elements.Angel;
import being.elements.Atom;
import being.elements.Spacetime;
import being.mathematics.MathUtils;
import being.mathematics.ThreeVector;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewtonPhysics extends MetricsPhysics {
    private List<Angel> angels;
    private final Object angelsLock = new Object();

    public NewtonPhysics() {
        dimensionality = 3;
    }

    public void gravity(List<Atom> allAtoms, List<Atom> updatedAtoms) {

        //todo too slow. Vector optimization
        for (Atom updatedAtom : updatedAtoms) {
            ThreeVector power = new ThreeVector(0, 0, 0);
            for (Atom otherAtom : allAtoms) {
                if (updatedAtom != otherAtom) {
                    ThreeVector distance = otherAtom.getPosition().minus(updatedAtom.getPosition());
                    ThreeVector plus = distance.normalize().multiply(
                            (PhysicsConfigurations.NewtonPhysicsConfigurations.G *
                                    updatedAtom.getWeight() *
                                    otherAtom.getWeight()) /
                                    Math.pow(distance.module(), 2));
//                    System.out.println("1");
                    if (distance.module() == 0) {
                        System.err.println("Distance is zero");
                        System.exit(2);
                    }
                    power.plusAndUpdate(plus);
                }
            }
            updatedAtom.update(power);
        }
    }

    public void electromagnetism(List<Atom> allAtoms, List<Atom> updatedAtoms) throws Exception {
//        for (Atom atom : updatedAtoms) {
//            for (Atom otherAtom : allAtoms) {
//                if (atom != otherAtom && intersect(atom, otherAtom)) {
//                    atom.returnOnPreviousState();
//                }
//            }
//        }
        for (Atom updatedAtom : updatedAtoms) {
            ThreeVector power = new ThreeVector(0, 0, 0);
            for (Atom otherAtom : allAtoms) {
                if (updatedAtom != otherAtom) {
                    ThreeVector distanceNorma = otherAtom.getPosition().minus(updatedAtom.getPosition());
                    double distanceModule = otherAtom.getPosition().minus(updatedAtom.getPosition()).module() - otherAtom.getSize().minus(updatedAtom.getSize()).module();
                    ThreeVector plus = distanceNorma.normalize().multiply(
                            (PhysicsConfigurations.NewtonPhysicsConfigurations.G *
                                    updatedAtom.getWeight() *
                                    otherAtom.getWeight()) /
                                    Math.pow(distanceModule, 2));
//                    System.out.println("1");
                    if (distanceNorma.module() == 0) {
                        System.err.println("Distance is zero");
                        System.exit(2);
                    }
                    power.plusAndUpdate(plus);
                }
            }
            updatedAtom.update(power);
        }
    }

    public void nextInstant() {
        synchronized (angelsLock) {
            angelsLock.notifyAll();
        }
    }

    public Object getLock() {
        return angelsLock;
    }

    @Override
    public Set basalObjectsGeneration() {
        return new HashSet<>(Collections.singleton(new Spacetime()));
    }

    private boolean intersect(Atom atom, Atom otherAtom) throws Exception {
        switch (atom.getFigure()) {
            case CIRCLE:
                switch (otherAtom.getFigure()) {
                    case CIRCLE:
                        return atom.getPosition().minus(otherAtom.getPosition()).module() <
                                MathUtils.max(atom.getSize().x, atom.getSize().y) / 2 + MathUtils.max(otherAtom.getSize().x, otherAtom.getSize().y) / 2;
                }
        }
        throw new Exception("Figure type doesn't supported");
    }
}