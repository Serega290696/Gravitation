package universe;

import java.util.ArrayList;
import java.util.List;

public class Angel extends Thread {
  private final List<Atom> allAtoms;
  private final List<Atom> ownAtoms;
  private final Physics physics;

  public Angel(String name, Spacetime space) {
    super.setName(name);
    this.allAtoms = space.getAtoms();
    this.ownAtoms = new ArrayList<>();
    this.physics = space.getPhysics();
    setDaemon(true);
  }

  public void addAtom(Atom atom) {
    ownAtoms.add(atom);
  }

  @Override
  public void run() {
    synchronized (physics.getLock()) {
      while (!God.ONE.isGodsWrath()) {
        try {
          physics.getLock().wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (!ownAtoms.isEmpty()) {
          try {
            physics.gravity(allAtoms, ownAtoms);
            physics.electromagnetism(allAtoms, ownAtoms);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public int getAtomsAmount() {
    return ownAtoms.size();
  }

  @Override
  public String toString() {
    return "Hello, God. I'm " + getName() + ". I have " + ownAtoms.size() + " ownAtoms.";
  }
}
