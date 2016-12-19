package universe;

import universe.mathematics.ThreeVector;

import java.util.List;

public class NewtonPhysics implements Physics {
  private List<Angel> angels;
  private final Object angelsLock = new Object();

  public void gravity(List<Atom> allAtoms, List<Atom> updatedAtoms) {

    //todo too slow. Vector optimization
    for (Atom atom : updatedAtoms) {
      ThreeVector power = new ThreeVector(0, 0, 0);
      for (Atom atom2 : allAtoms) {
        if (atom != atom2) {
          ThreeVector distance = atom2.getPosition().minus(atom.getPosition());
          ThreeVector plus = distance.divide(distance.module()).multiply(
              (UniverseConfigurations.G *
                  atom.getWeight() *
                  atom2.getWeight()) /
                  Math.pow(distance.module(), 2));
          if (distance.module() == 0) {
            System.err.println("Distance is zero");
            System.exit(2);
          }
          power.plusAndUpdate(plus);
        }
      }
      atom.update(power);
    }
  }

  public void electromagnetism(List<Atom> atoms) {

  }

  public void nextInstant() {
    synchronized (angelsLock) {
      angelsLock.notifyAll();
    }
  }

  public Object getLock() {
    return angelsLock;
  }
}