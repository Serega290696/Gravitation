package universe;

import universe.mathematics.MathUtils;
import universe.mathematics.ThreeVector;

import java.util.List;

public class NewtonPhysics implements Physics {
  private List<Angel> angels;
  private final Object angelsLock = new Object();

  public void gravity(List<Atom> allAtoms, List<Atom> updatedAtoms) {

    //todo too slow. Vector optimization
    for (Atom updatedAtom : updatedAtoms) {
      ThreeVector power = new ThreeVector(0, 0, 0);
      for (Atom otherAtom : allAtoms) {
        if (updatedAtom != otherAtom) {
          ThreeVector distance = otherAtom.getPosition().minus(updatedAtom.getPosition());
          ThreeVector plus = distance.normalize().multiply(
              (UniverseConfigurations.G *
                  updatedAtom.getWeight() *
                  otherAtom.getWeight()) /
                  Math.pow(distance.module() * 1000, 2));
          System.out.println("1");
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
    for (Atom atom : updatedAtoms) {
      for (Atom otherAtom : allAtoms) {
        if (atom != otherAtom && intersect(atom, otherAtom)) {
          atom.returnOnPreviousState();
        }
      }
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

  private boolean intersect(Atom atom, Atom otherAtom) throws Exception {
    switch (atom.getFigure()) {
      case CIRCLE:
        switch (otherAtom.getFigure()) {
          case CIRCLE:
            return atom.getPosition().minus(otherAtom.getPosition()).module() <
                MathUtils.max(atom.getSize().x, atom.getSize().y) + MathUtils.max(otherAtom.getSize().x, otherAtom.getSize().y);
        }
    }
    throw new Exception("Figure type doesn't supported");
  }
}