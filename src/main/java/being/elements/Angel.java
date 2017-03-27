package being.elements;

import being.God;
import being.physics.Physics;
import being.physics.PhysicsConfigurations;
import being.universe.AbstractUniverse;
import being.universe.UniverseStage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Angel extends Thread implements Serializable {
    private final Collection<Atom> allAtoms;
    private final Collection<Atom> ownAtoms;
    private final Collection<Atom> atomsForDeletion = new HashSet<>();
    private final Collection<Atom> atomsForAdding = new HashSet<>();
    private final Physics physics;
    private final AbstractUniverse universe;
    private int pip;

    public Angel(String name, Physics physics, AbstractUniverse universe, Collection<Atom> atoms) {
        super.setName(name);
        this.allAtoms = atoms;
        this.physics = physics;
        this.universe = universe;

        setDaemon(true);
        this.ownAtoms = new CopyOnWriteArrayList<>();
    }

    public void addAtom(Atom atom) {
//        System.out.println("Angel.addAtom" + ownAtoms.size() + ", " + allAtoms.size());
        ownAtoms.add(atom);
//        atomsForAdding.add(atom);
    }

    @Override
    public void run() {
        System.out.println(this);
//        synchronized (physics.getLock()) {
        while (!God.ONE.isGodsWrath()) {
            if(pip == 2000) {
                pip = 0;
                System.out.println("PIP: " + ownAtoms.size());
            }else {
                pip+= 1000* PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION;
            }
            long begin = System.currentTimeMillis();
            if (atomsForDeletion != null && atomsForDeletion.size() > 0) {
                ownAtoms.removeAll(atomsForDeletion);
                atomsForDeletion.clear();
            }
            if (atomsForAdding != null && atomsForAdding.size() > 0) {
                ownAtoms.addAll(atomsForAdding);
                atomsForAdding.clear();
            }
            physics.finishWork(this);
            while (!physics.isPrepared(this));
            physics.startWork(this);
//             while(universe.getObjectProcessingStage() != ObjectProcessingStage.MOVING);
//                do {
//                    try {
//                        physics.getLock().wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                } while (physics.isPrepared());

            if (!ownAtoms.isEmpty() && (universe.getStage() == UniverseStage.ALIVE ||
//                        universe.getStage() == UniverseStage.BACK_FRAME ||
                    universe.getStage() == UniverseStage.NEXT_FRAME
            )) {
//                    if (ownAtoms.stream().filter(Atom::isMarkedByGod).count() > 0) {
//                        System.out.println("Real atoms: ");
//                        ownAtoms.stream().filter(Atom::isMarkedByGod).forEach(a -> System.out.println("\t\t" + a));
//                    }
                try {
//                    while (!universe.areObjectsAvailable()) ;
//                    universe.setObjectsAvailable(false);
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.IS_GRAVITY_ENABLE) {
                        physics.gravity(allAtoms, ownAtoms);
                    }
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.IS_ELECTROMAGNETISM_ENABLE) {
                        physics.electromagnetism(allAtoms, ownAtoms);
                    }
                    ownAtoms.forEach(Atom::move);
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.IS_ELECTROMAGNETISM_ENABLE) {
//                        physics.push(allAtoms, ownAtoms);
                    }
//                    universe.setObjectsAvailable(true);
                    ownAtoms.forEach(Atom::fixEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!ownAtoms.isEmpty() && universe.getStage() == UniverseStage.BACK_FRAME) {
                System.out.println("BACK");
                ownAtoms.forEach(Atom::backForward);
            }

            long remainingTime = (long) (PhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION * 1000 - (System.currentTimeMillis() - begin));
            if (remainingTime > 0) {
                try {
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        }
        System.out.println(getName() + " disappeared");
    }

    public int getAtomsAmount() {
        return ownAtoms.size();
    }

    @Override
    public String toString() {
        return "Hello, God. I'm " + getName() + ". I have " + ownAtoms.size() + " ownAtoms.";
    }

    public void removeAtoms() {
        ownAtoms.clear();
    }

    public void removeAtomsIfExist(List<Atom> atom) {
        ownAtoms.removeAll(atom);
//        atomsForDeletion.addAll(atom);
    }
}
