package being.elements;

import being.God;
import being.physics.Physics;
import being.physics.PhysicsConfigurations;
import being.universe.AbstractUniverse;
import being.universe.UniverseStage;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class Angel extends Thread {
    private final List<Atom> allAtoms;
    private final List<Atom> ownAtoms;
    private final Physics physics;
    private final AbstractUniverse universe;

    public Angel(String name, Physics physics, AbstractUniverse universe, List<Atom> atoms) {
        super.setName(name);
        this.allAtoms = atoms;
        this.physics = physics;
        this.universe = universe;

        setDaemon(true);
        this.ownAtoms = new ArrayList<>();
    }

    public void addAtom(Atom atom) {
        ownAtoms.add(atom);
    }

    @Override
    public void run() {
        System.out.println(this);
        synchronized (physics.getLock()) {
            while (!God.ONE.isGodsWrath()) {
                long begin = System.currentTimeMillis();
                try {
                    physics.getLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!ownAtoms.isEmpty() && (universe.getStage() == UniverseStage.ALIVE ||
//                        universe.getStage() == UniverseStage.BACK_FRAME ||
                        universe.getStage() == UniverseStage.NEXT_FRAME
                )) {
//                    if (ownAtoms.stream().filter(Atom::isMarkedByGod).count() > 0) {
//                        System.out.println("Real atoms: ");
//                        ownAtoms.stream().filter(Atom::isMarkedByGod).forEach(a -> System.out.println("\t\t" + a));
//                    }
                    try {
                        physics.gravity(allAtoms, ownAtoms);
                        physics.electromagnetism(allAtoms, ownAtoms);
                        ownAtoms.forEach(Atom::fixEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (!ownAtoms.isEmpty() && universe.getStage() == UniverseStage.BACK_FRAME) {
                    System.out.println("BACK");
                    ownAtoms.forEach(Atom::backForward);
                }
                long remainingTime = (long) (PhysicsConfigurations.ACTUAL_MOMENT_MIN_SIZE * 1000 - (System.currentTimeMillis() - begin));
                if (remainingTime > 0) {
                    try {
                        Thread.sleep(remainingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println(getName() + " disappeared");
    }

    public int getAtomsAmount() {
        return ownAtoms.size();
    }

    @Override
    public String toString() {
        return "Hello, God. I'm " + getName() + ". I have " + ownAtoms.size() + " ownAtoms.";
    }
}
