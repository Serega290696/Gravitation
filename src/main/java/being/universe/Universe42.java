package being.universe;

import being.God;
import being.physics.PhysicsConfigurations;
import being.elements.Angel;
import being.elements.Atom;
import being.elements.Spacetime;
import being.mathematics.ThreeVector;
import being.physics.NewtonPhysics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Universe42 extends AbstractUniverse<NewtonPhysics> {
    private final Spacetime spacetime;
    private final List<Atom> atoms = new ArrayList<>();
    private final List<Angel> angels = new ArrayList<>();
    private long absoluteTime;
    private long overallElapsedTime;

    private static final int ANGELS_AMOUNT = 7;

    private final Object angelsLock = new Object();
    private boolean stopUniverse;
    private final Object stopMonitor = new Object();

    public Universe42() {
        this.type = UniverseType.UNIVERSE_42;
        this.physics = new NewtonPhysics();
        this.objects = new HashSet<>(physics.basalObjectsGeneration());
        this.spacetime = (Spacetime) objects.stream().filter(o -> o instanceof Spacetime).findFirst().get();
//        this.atoms.addAll(objects.stream().filter(o -> o instanceof Atom)
//                .map(o -> (Atom) o).collect(Collectors.toList()));
    }

    @Override
    public void bigBang() {
        System.out.println("Universe42.bigBang");
        stage = UniverseStage.ALIVE;
        objects.clear();
        atoms.clear();
        objects.add(spacetime);
        this.spacetime.bigBang();

        createAngels();

        Atom adam = new Atom.AtomCreator()
                .setName("Adam")
                .setWeight(225)
                .setSpeed(new ThreeVector(0, 0, 0))
                .setPosition(new ThreeVector(60, 50, 0))
                .setMarkedByGod(true)
                .build();
        Atom eve = new Atom.AtomCreator()
                .setName("Eve")
                .setWeight(225)
                .setSpeed(new ThreeVector(0, 0, 0))
                .setPosition(new ThreeVector(50, 60, 0))
                .setMarkedByGod(true)
                .build();

        objects.add(adam);
        objects.add(eve);

//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                objects.add(new Atom.AtomCreator()
//                        .setName("Atom-" + i+j)
//                        .setWeight(25)
//                        .setSpeed(new ThreeVector(0, 0, 0))
//                        .setPosition(new ThreeVector(10 + i *5, 10 + j *5, 0))
//                        .setMarkedByGod(false)
//                        .build());
//            }
//        }

        this.atoms.addAll(objects.stream().filter(o -> o instanceof Atom)
                .map(o -> (Atom) o).collect(Collectors.toList()));
        long lastInstant = System.currentTimeMillis();
        long beginTime = System.currentTimeMillis();
        long elapsedTime;
        long waitingTime;
        while (!God.ONE.isGodsWrath()) {
            if (!paused) {
                nextInstant();
                // todo wait angels
                elapsedTime = System.currentTimeMillis() - lastInstant;
                waitingTime = (long) (PhysicsConfigurations.MOMENT_SIZE * 1000 - elapsedTime);
                if (waitingTime > 0) {
                    absoluteTime += waitingTime;
                    try {
                        Thread.sleep(waitingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                }
                lastInstant = System.currentTimeMillis();
            }
        }
    }

    private void createAngels() {
        angels.clear();
        System.out.println("Space.createAngels");
        Angel anael = new Angel("Anael", physics, this, atoms);
        Angel sashiel = new Angel("Sashiel", physics,this, atoms);
        Angel raphael = new Angel("Raphael", physics,this, atoms);
        Angel michael = new Angel("Michael", physics,this, atoms);
        Angel gabriel = new Angel("Gabriel", physics,this, atoms);
        Angel cassiel = new Angel("Cassiel", physics,this, atoms);
        Angel metatron = new Angel("Metatron", physics, this, atoms);

        angels.add(anael);
        angels.add(sashiel);
        angels.add(raphael);
        angels.add(michael);
        angels.add(gabriel);
        angels.add(cassiel);
        angels.add(metatron);
        if (ANGELS_AMOUNT < angels.size()) {
            angels.removeAll(angels.subList(ANGELS_AMOUNT, angels.size() - 1));
        }
        for (Angel angel : angels) {
            angel.start();
        }
    }

    private void nextInstant() {
        for (Atom atom : atoms) {
            if (!atom.hasGuardianAngel()) {
                Angel chosenAngel = chooseGuardianAngel();
                chosenAngel.addAtom(atom);
                atom.setHasGuardianAngel(true);
                atom.setAngelGuardian(chosenAngel);
            }
        }
        if (atoms.stream().filter(Atom::isMarkedByGod).count() == 1) {
            God.ONE.MIND.setShift(atoms.stream().filter(Atom::isMarkedByGod).findFirst().get().getPosition().plus(new ThreeVector(-50, -50, 0)));
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
        if (stage == UniverseStage.NEXT_FRAME || stage == UniverseStage.BACK_FRAME) {
            stage = UniverseStage.PAUSE;
        }
    }

    private Angel chooseGuardianAngel() {
        Angel angel = angels.stream().min(Comparator.comparingInt(Angel::getAtomsAmount)).get();
        System.out.println("Assign atom on angel. " + angel);
        return angel;
    }

}
