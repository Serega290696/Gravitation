package universe;

import universe.mathematics.ThreeVector;

import java.util.ArrayList;
import java.util.List;

public class Spacetime {
  private final Physics physics;
  private final int dimensionality;
  private long absoluteTime;
  private List<Angel> angels;
  private final Object angelsLock = new Object();
  private List<Atom> atoms = new ArrayList<>();
  private boolean stopUniverse;

  private final Object stopMonitor = new Object();

  public Spacetime(Physics physics, int dimensionality) {
    this.physics = physics;
    this.dimensionality = dimensionality;
  }

  public void bigBang() {
    System.out.println("Space.bigBang");
    if (atoms == null) {
      atoms = new ArrayList<Atom>();
    }
    atoms.clear();

    createAngels();

    Atom adam = new Atom.AtomCreator()
        .setName("Adam")
        .setWeight(25)
        .setSpeed(new ThreeVector(0, 0, 0))
        .setPosition(new ThreeVector(30, 50, 0))
        .setMarkedByGod(true)
        .build();
    Atom eve = new Atom.AtomCreator()
        .setName("Eve")
        .setWeight(25)
        .setSpeed(new ThreeVector(0, 0, 0))
        .setPosition(new ThreeVector(50, 60, 0))
        .setMarkedByGod(true)
        .build();

    atoms.add(adam);
    atoms.add(eve);
    absoluteTime = 0;
  }

  private void createAngels() {
    System.out.println("Space.createAngels");
    angels = new ArrayList<Angel>();
    Angel anael = new Angel("Anael", this);
    Angel sashiel = new Angel("Sashiel", this);
    Angel raphael = new Angel("Raphael", this);
    Angel michael = new Angel("Michael", this);
    Angel gabriel = new Angel("Gabriel", this);
    Angel cassiel = new Angel("Cassiel", this);
    Angel metatron = new Angel("Metatron", this);
    angels.add(anael);
    angels.add(sashiel);
    angels.add(raphael);
    angels.add(michael);
    angels.add(gabriel);
    angels.add(cassiel);
    angels.add(metatron);
    for (Angel angel : angels) {
      angel.start();
    }
  }

  public void live() {
    long lastInstant = System.currentTimeMillis();
    long elapsedTime;
    long waitingTime;
    while (!God.ONE.isGodsWrath()) {
      System.out.println("!God.ONE.isGodsWrath() = " + !God.ONE.isGodsWrath());
      nextInstant();
      // todo wait angels
      elapsedTime = System.currentTimeMillis() - lastInstant;
      waitingTime = (long) (UniverseConfigurations.MOMENT_SIZE * 1000 - elapsedTime);
      if (waitingTime > 0) {
        absoluteTime += waitingTime;
        try {
          Thread.sleep(waitingTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        absoluteTime += waitingTime;
      }
      lastInstant = System.currentTimeMillis();
    }
  }

  private void nextInstant() {
    for (Atom atom : atoms) {
      if (!atom.hasGuardianAngel()) {
        chooseGuardianAngel().addAtom(atom);
        atom.setHasGuardianAngel(true);
      }
    }
    synchronized (stopMonitor) {
      if (stopUniverse) {
        System.out.println("Stopped");
        try {
          stopMonitor.wait();
          System.out.println("Resumed");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    physics.nextInstant();
  }

  private Angel chooseGuardianAngel() {
    Angel angel = angels.stream().min((o1, o2) -> o1.getAtomsAmount() - o2.getAtomsAmount()).get();
    System.out.println(angel);
    return angel;
  }

  public Physics getPhysics() {
    return physics;
  }

  public List<Atom> getAtoms() {
    return atoms;
  }

  public boolean isStopUniverse() {
    return stopUniverse;
  }

  public void resume() {
    stopUniverse = false;
    synchronized (stopMonitor) {
      stopMonitor.notifyAll();
    }
  }

  public void stop() {
    stopUniverse = true;
  }
}
