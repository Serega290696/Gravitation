package being.universe;

import being.God;
import being.elements.AngelsFactory;
import being.elements.OneMomentAngel;
import being.physics.PhysicsConfigurations;
import being.elements.Angel;
import being.elements.Atom;
import being.mathematics.ThreeVector;
import being.physics.physics_impls.AntigravityPhysics;
import being.physics.physics_impls.NewtonPhysics;
import being.physics.physics_impls.RightGravityLeftAntigravityPhysics;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Universe42 extends AbstractUniverse<NewtonPhysics, Atom> {
    private static final boolean FOLLOW_IF_ONE_MARKED_ATOM = false;
    private static final int ANGELS_AMOUNT = 1;
    public static final String STATE_FILE_NAME = "states.out";
    private static final boolean RESTORE_STATES_WHEN_START_UP = false;
    private static final boolean LEARNING_STATES_MODE = true;

    private final List<Angel> angels = new ArrayList<>();

    private Atom movedAtom;
    private double actualMomentDuration;
    private List<Atom> atomsForAdding = new LinkedList<>();
    private List<Atom> atomsForDeletion = new LinkedList<>();
    private volatile boolean objectsAvailable = true;
    private Atom changingSpeedAtom = null;
    private Class<? extends Angel> angelImpl;
    private int learningStateCounter = 0;


    public Universe42() {
        this.type = UniverseType.UNIVERSE_42;
        this.physics = new NewtonPhysics();
        this.objects = new CopyOnWriteArraySet<>();
//        this.objects = new HashSet<>(physics.basalObjectsGeneration());
//        this.spacetime = (Spacetime) objects.stream().filter(o -> o instanceof Spacetime).findFirst().get();
        generateStates();
        angelImpl = LEARNING_STATES_MODE ?
                OneMomentAngel.class : Angel.class;
    }

    @Override
    public void bigBang() {
        while (true) {// todo
            System.out.println("Universe42.bigBang");
            stage = UniverseStage.ALIVE;
            objects.clear();
            if (angels.size() == 0) {
                System.out.println("Angles creating(" + ANGELS_AMOUNT + "). . .");
                createAngels();
            }
            System.out.println("Atoms creating. . .");
            createDefaultAtoms();
            System.out.println("Objects generating complete\n");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long lastInstant = System.currentTimeMillis();
            long beginTime = System.currentTimeMillis();
            long elapsedTime;
            long waitingTime;
            int warningCounter = 0;
            for (Atom atom : objects) {
                if (atom.isRocket()) {
                    atom.setSpeed(new ThreeVector(rocketAngle, rocketSpeed));
                }
            }
            timeSinceLastPause = 0;

            for (Angel angel : angels) {
                if (!angel.isAlive()) {
                    angel.start();
                }
            }
            while (!God.ONE.isGodsWrath()) {
//                if (stage != UniverseStage.PAUSE) {
//                    if (LEARNING_STATES_MODE) {
//                        generateLearningState();
//                    }
//                }
                awaitAngels();
                nextInstant();
                if (LEARNING_STATES_MODE && learningStateCounter % 100 == 0) {
                    System.out.println("Learning state #" + learningStateCounter);
                }
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                elapsedTime = System.currentTimeMillis() - lastInstant;
                waitingTime = (long) (PhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION * 1000 - elapsedTime);
                if (warningCounter < 10) {
                    actualMomentDuration *= (warningCounter + 1);
                    actualMomentDuration += elapsedTime + Math.abs(waitingTime);
                    actualMomentDuration /= warningCounter + 2;
                    warningCounter++;
                } else {
                    warningCounter = 0;
                    actualMomentDuration = elapsedTime + Math.abs(waitingTime);
                }
                if (waitingTime > 0) {
                    try {
                        Thread.sleep(waitingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                }
                if (restart) {
                    restart = false;
                    bigBang();
                }
                if (God.ONE.getRocketMode() && timeToPause > 0 && timeSinceLastPause >= timeToPause) {
                    pause();
                } else {
                    timeSinceLastPause += elapsedTime;
                }
                lastInstant = System.currentTimeMillis();
            }
        }
    }

    private void awaitAngels() {
        physics.awaitAngels();
    }

    private void createAngels() {
        for (Angel angel : angels) {
            angel.setStopped(true);
        }
        angels.clear();
        ArrayList<Atom> atomsTmp = new ArrayList<>(objects);
        Angel anael = AngelsFactory.getInstance(angelImpl, "Anael", physics, this, objects);
        Angel sashiel = AngelsFactory.getInstance(angelImpl, "Sashiel", physics, this, objects);
        Angel raphael = AngelsFactory.getInstance(angelImpl, "Raphael", physics, this, objects);
        Angel michael = AngelsFactory.getInstance(angelImpl, "Michael", physics, this, objects);
        Angel gabriel = AngelsFactory.getInstance(angelImpl, "Gabriel", physics, this, objects);
        Angel cassiel = AngelsFactory.getInstance(angelImpl, "Cassiel", physics, this, objects);
        Angel metatron = AngelsFactory.getInstance(angelImpl, "Metatron", physics, this, objects);

        angels.add(anael);
        angels.add(sashiel);
        angels.add(raphael);
        angels.add(michael);
        angels.add(gabriel);
        angels.add(cassiel);
        angels.add(metatron);
        if (ANGELS_AMOUNT < angels.size()) {
            angels.retainAll(angels.subList(0, ANGELS_AMOUNT));
        }
        physics.loadAngles(angels);
    }

    private void nextInstant() {
        if (stage != UniverseStage.PAUSE) {
            if (LEARNING_STATES_MODE) {
                generateLearningState();
            }
        }
        if (atomsForDeletion != null && atomsForDeletion.size() > 0) {
            angels.forEach(a -> a.removeAtomsIfExist(atomsForDeletion));
            objects.removeAll(atomsForDeletion);
            atomsForDeletion.clear();
        }
        if (atomsForAdding != null && atomsForAdding.size() > 0) {
            objects.addAll(atomsForAdding);
            atomsForAdding.clear();
        }
        for (Atom atom : objects) {
            if (!atom.hasGuardianAngel()) {
                Angel chosenAngel = chooseGuardianAngel();
                chosenAngel.addAtom(atom);
                atom.setHasGuardianAngel(true);
                atom.setAngelGuardian(chosenAngel);
            }
        }

        if (FOLLOW_IF_ONE_MARKED_ATOM && objects.stream().filter(Atom::isMarkedByGod).count() == 1) {
            God.ONE.MIND.setShift(
                    objects.stream().
                            filter(Atom::isMarkedByGod)
                            .findFirst()
                            .get()
                            .getPosition()
                            .plus(new ThreeVector(-50, -50, 0))
            );
        }

        physics.goAngels();

        if (mousePosition != null && movedAtom != null) {
            movedAtom.setPosition(mousePosition);
        }
        if (stage == UniverseStage.NEXT_FRAME || stage == UniverseStage.BACK_FRAME) {
            stage = UniverseStage.PAUSE;
        }
    }

    @Override
    public boolean mousePush(double x, double y, double clickDuration, int mouseKeyCode, boolean onlyCreation) {
        if (clickDuration == 0) {
            boolean intersectSomeAtom = false;
            Atom intersectedAtom = hasAtomInCoords(x, y);
            if (intersectedAtom != null) {
                intersectSomeAtom = true;
            }
            if (intersectSomeAtom) {
                changingSpeedAtom = intersectedAtom;
            }
            return !intersectSomeAtom;
        } else if (changingSpeedAtom != null && clickDuration / God.ONE.MIND.FPS > 0.2) {// && clickDuration / God.ONE.MIND.FPS > 0.3
            double xSpeed = x - changingSpeedAtom.getPosition().x;
            double ySpeed = y - changingSpeedAtom.getPosition().y;
            if (Math.abs(xSpeed) < 1.5) {
                xSpeed = 0;
            }
            if (Math.abs(ySpeed) < 1.5) {
                ySpeed = 0;
            }
            changingSpeedAtom.setSpeed(new ThreeVector(
                    xSpeed,
                    ySpeed,
                    0
            ));
            return false;
        }
        return true;
    }

    @Override
    public void mouseClick(double x, double y, double clickDuration, int mouseKeyCode, boolean onlyCreation) {
        if (changingSpeedAtom != null && clickDuration / God.ONE.MIND.FPS > 0.2) {
            changingSpeedAtom.setSpeed(new ThreeVector(
                    x - changingSpeedAtom.getPosition().x,
                    y - changingSpeedAtom.getPosition().y,
                    0
            ));
            changingSpeedAtom = null;
        } else {
            boolean intersectSomeAtom = false;
            Atom intersectedAtom = null;
            if (movedAtom == null) { // if don't move Atom - find intersection
                intersectedAtom = hasAtomInCoords(x, y);
                if (intersectedAtom != null) {
                    intersectSomeAtom = true;
                }
            } else { // if moving atom now. Afterwards - end moving
                movedAtom = null;
                return;
            }


            if (!intersectSomeAtom) {
                Atom newAtom = new Atom.AtomCreator()
                        .generateName(objects)
                        .generateSize(Math.pow(clickDuration / 10, 2d) / God.ONE.MIND.em * God.ONE.MIND.DEFAULT_EM)
                        .setSpeed(new ThreeVector(0, 0, 0))
                        .setPosition(new ThreeVector(x, y, 0))
                        .setMarkedByGod(mouseKeyCode == 1)
                        .build();
                if (onlyCreation) {
                    for (Object object : objects) {
                        try {
                            if (object instanceof Atom && physics.intersect((Atom) object, newAtom)) {
                                intersectSomeAtom = true;
                                intersectedAtom = (Atom) object;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!intersectSomeAtom) {
                        addObjects(newAtom);
                    }
                } else {
                    addObjects(newAtom);
                }
            } else {
                if (mouseKeyCode == 0) { // start move Atom
                    if (!onlyCreation) {
                        movedAtom = intersectedAtom;
                    }
                } else if (mouseKeyCode == 1) { // remove Atom
                    deleteObject(intersectedAtom);
                }
            }
        }
    }

    private Atom hasAtomInCoords(double x, double y) {
        Atom intersectedAtom = null;
        for (Atom atom : objects) {
            try {
                if (physics.intersect(atom, x, y)) {
                    intersectedAtom = atom;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return intersectedAtom;
    }

    @Override
    public void createDefaultAtoms() {
        if (LEARNING_STATES_MODE) {
            generateLearningState();
        } else {
            restartUniverseInState(savedStates.get(DEFAULT_STATE_NAME));
        }
    }

    private double rand(double min, double max) {
        return min + Math.random() * (max - min);
    }

    private long rand(long min, long max) {
        return (long) (min + (max - min) * Math.random());
    }

    private void generateLearningState() {
        objects.clear();
        for (Angel angel : angels) {
            angel.removeAtoms();
        }
        long minAmount = 2;
        long maxAmount = 5;
        double minDistance = 20;
        double maxDistance = 50;
        long minWeight = (long) Math.pow(10, 11);
        long maxWeight = (long) Math.pow(10, 13);
        long rocketMinWeight = (long) Math.pow(10, 4);
        long rocketMaxWeight = (long) Math.pow(10, 6);
        double secondObjectPositionAngle = Math.PI * 2;
        long minSpeed = 0;
        long maxSpeed = 20;
        double speedAngleRadiansVariance = Math.PI * 2;
        ThreeVector markedAtomPosition = new ThreeVector(0, 0, 0);
        long amount = (long) (rand(minAmount, maxAmount));
        for (int i = 0; i < amount; i++) {
            double weight = rand(minWeight, maxWeight);
            double speedVectorAngle = Math.random() * speedAngleRadiansVariance;
            double speedModule = 0;
            double width = weight / maxWeight * 8;
            double height = weight / maxWeight * 8;
            double posX;
            double posY;
            double posZ;
            if (i == 0) {
                weight = rand(rocketMinWeight, rocketMaxWeight);
                speedModule = rand(minSpeed, maxSpeed);
                width = 7 * weight / rocketMaxWeight;
                height = 7 * weight / rocketMaxWeight;
                posX = 50;
                posY = 50;
                posZ = 50;
                markedAtomPosition = new ThreeVector(posX, posY, posZ);
            } else {
                ThreeVector atomPosition = markedAtomPosition.plus(new ThreeVector(
                        rand(0, secondObjectPositionAngle) + Math.PI / 2, rand(minDistance, maxDistance)));
                atomPosition.z = rand(0, secondObjectPositionAngle) / 2;
                posX = atomPosition.x;
                posY = atomPosition.y;
                posZ = atomPosition.z;
            }
            ThreeVector speed = new ThreeVector(speedVectorAngle, speedModule);
            speed.z = rand(0, speedModule) / 2;
            ThreeVector size = new ThreeVector(width, height, (width+height) / 2);
            ThreeVector position = new ThreeVector(posX, posY, posZ);
            Atom atom = new Atom.AtomCreator()
                    .generateName(objects)
                    .setSpeed(speed)
                    .setPosition(position)
                    .setMarkedByGod(i == 0)
                    .setWeight(weight)
                    .setSize(size)
                    .build();
            atom.setHasGuardianAngel(false);
            atom.resetTrace();
            addObjects(atom);
        }
        learningStateCounter++;
    }

    @Override
    public void clearAllAtoms() {
        atomsForDeletion.addAll(objects);
    }

    @Override
    public void deleteObject(Atom atom) {
        objects.remove(atom);
    }

    void restartUniverseInState(List<Atom> newObjects) {
        objects.clear();
//        atoms.clear();
        for (Angel angel : angels) {
            angel.removeAtoms();
        }

        for (Atom o : newObjects) {
            Atom clone = o.clone();
            clone.setHasGuardianAngel(false);
            clone.resetTrace();
            addObjects(clone);
        }
        System.out.println(objects.size());
    }

    @Override
    public void addObjects(Atom... newObjects) {
//        this.atomsForAdding.addAll(Arrays.asList(newObjects));
        objects.addAll(Arrays.asList(newObjects));
//        atoms.add(object);
    }

    private Angel chooseGuardianAngel() {
        Angel angel = angels.stream().min(Comparator.comparingInt(Angel::getAtomsAmount)).get();
//        System.out.println("Assign atom on angel. " + angel);
        return angel;
    }

    private void generateStates() {
        ArrayList<Atom> empty = new ArrayList<>();
        ArrayList<Atom> default1 = new ArrayList<>();
        ArrayList<Atom> default2 = new ArrayList<>();
        ArrayList<Atom> default3 = new ArrayList<>();
        ArrayList<Atom> default4 = new ArrayList<>();
        ArrayList<Atom> default5 = new ArrayList<>();
        ArrayList<Atom> default6 = new ArrayList<>();
        double earthAngle = Math.PI;
        double moonAngle = earthAngle;
        double sonAngle = 0;
        double marsAngle = -1d / 4 * Math.PI;
        double mercuryAngle = 7d / 6 * Math.PI;
        double venusAngle = 4d / 5 * Math.PI;
        double jupiterAngle = 3d / 7 * Math.PI;
        double saturnAngle = -2d / 6 * Math.PI;
        double uranusAngle = -3d / 4 * Math.PI;
        double neptuneAngle = 1d / 2 * Math.PI;
        double plutoAngle = Math.PI;

        double sonDistance = 0;
        double earthDistance = sonDistance + 150_000_000;
        double moonDistance = sonDistance + earthDistance + 385_000;
        double marsDistance = sonDistance + 228_000_000;
        double mercuryDistance = sonDistance + 58_000_000;
        double venusDistance = sonDistance + 108_000_000;
        double jupiterDistance = sonDistance + 765_000_000;
        double saturnDistance = sonDistance + 1430_000_000;
        double uranusDistance = sonDistance + 2_800_000_000L;
        double neptuneDistance = sonDistance + 4_555_000_000L;
        double plutoDistance = sonDistance + 7_500_000_000L;//7.5///4.4

        double earthPosX = earthDistance * Math.cos(earthAngle);
        double moonPosX = moonDistance * Math.cos(moonAngle);
        double sonPosX = sonDistance * Math.cos(sonAngle);
        double marsPosX = marsDistance * Math.cos(marsAngle);
        double mercuryPosX = mercuryDistance * Math.cos(mercuryAngle);
        double venusPosX = venusDistance * Math.cos(venusAngle);
        double jupiterPosX = jupiterDistance * Math.cos(jupiterAngle);
        double saturnPosX = saturnDistance * Math.cos(saturnAngle);
        double uranusPosX = uranusDistance * Math.cos(uranusAngle);
        double neptunePosX = neptuneDistance * Math.cos(neptuneAngle);
        double plutoPosX = plutoDistance * Math.cos(plutoAngle);

        double earthPosY = earthDistance * Math.sin(earthAngle);
        double moonPosY = moonDistance * Math.sin(moonAngle);
        double sonPosY = sonDistance * Math.sin(sonAngle);
        double marsPosY = marsDistance * Math.sin(marsAngle);
        double mercuryPosY = mercuryDistance * Math.sin(mercuryAngle);
        double venusPosY = venusDistance * Math.sin(venusAngle);
        double jupiterPosY = jupiterDistance * Math.sin(jupiterAngle);
        double saturnPosY = saturnDistance * Math.sin(saturnAngle);
        double uranusPosY = uranusDistance * Math.sin(uranusAngle);
        double neptunePosY = neptuneDistance * Math.sin(neptuneAngle);
        double plutoPosY = plutoDistance * Math.sin(plutoAngle);

        double earthSize = 12600;
        double moonSize = 3400;
        double sonSize = 14_000_000;
        double marsSize = 6800;
        double mercurySize = 5000;
        double venusSize = 12000;
        double jupiterSize = 140_000;
        double saturnSize = 120_000;
        double uranusSize = 51_000;
        double neptuneSize = 50_000;
        double plutoSize = 2_350;

        double earthWeight = 6 * Math.pow(10, 24);
        double moonWeight = 7 * Math.pow(10, 22);
        double sonWeight = 2 * Math.pow(10, 30);
        double marsWeight = 6.5 * Math.pow(10, 23);
        double mercuryWeight = 3.3 * Math.pow(10, 23);
        double venusWeight = 5 * Math.pow(10, 24);
        double jupiterWeight = 1.9 * Math.pow(10, 27);
        double saturnWeight = 5.7 * Math.pow(10, 26);
        double uranusWeight = 8.7 * Math.pow(10, 25);
        double neptuneWeight = 1 * Math.pow(10, 26);
        double plutoWeight = 1.3 * Math.pow(10, 22);

        double speedFactor = 111_111;
        double sonSpeed = 0;
        double earthSpeed = sonSpeed + 30 * speedFactor;
        double moonSpeed = sonSpeed + earthSpeed + 1 * speedFactor;
        double marsSpeed = sonSpeed + 24 * speedFactor;
        double mercurySpeed = sonSpeed + 47 * speedFactor;
        double venusSpeed = sonSpeed + 35 * speedFactor;
        double jupiterSpeed = sonSpeed + 13 * speedFactor;
        double saturnSpeed = sonSpeed + 9.5 * speedFactor;
        double uranusSpeed = sonSpeed + 6.8 * speedFactor;
        double neptuneSpeed = sonSpeed + 5.4 * speedFactor;
        double plutoSpeed = sonSpeed + 4.6 * speedFactor; //4.6

        double sonSpeedX = sonSpeed * Math.cos(sonAngle + Math.PI / 2);
        double earthSpeedX = earthSpeed * Math.cos(earthAngle + Math.PI / 2);
        double moonSpeedX = moonSpeed * Math.cos(moonAngle + Math.PI / 2);
        double marsSpeedX = marsSpeed * Math.cos(marsAngle + Math.PI / 2);
        double mercurySpeedX = mercurySpeed * Math.cos(mercuryAngle + Math.PI / 2);
        double venusSpeedX = venusSpeed * Math.cos(venusAngle + Math.PI / 2);
        double jupiterSpeedX = jupiterSpeed * Math.cos(jupiterAngle + Math.PI / 2);
        double saturnSpeedX = saturnSpeed * Math.cos(saturnAngle + Math.PI / 2);
        double uranusSpeedX = uranusSpeed * Math.cos(uranusAngle + Math.PI / 2);
        double neptuneSpeedX = neptuneSpeed * Math.cos(neptuneAngle + Math.PI / 2);
        double plutoSpeedX = plutoSpeed * Math.cos(plutoAngle + Math.PI / 2);


        double sonSpeedY = sonSpeed * Math.sin(sonAngle + Math.PI / 2);
        double earthSpeedY = earthSpeed * Math.sin(earthAngle + Math.PI / 2);
        double moonSpeedY = moonSpeed * Math.sin(moonAngle + Math.PI / 2);
        double marsSpeedY = marsSpeed * Math.sin(marsAngle + Math.PI / 2);
        double mercurySpeedY = mercurySpeed * Math.sin(mercuryAngle + Math.PI / 2);
        double venusSpeedY = venusSpeed * Math.sin(venusAngle + Math.PI / 2);
        double jupiterSpeedY = jupiterSpeed * Math.sin(jupiterAngle + Math.PI / 2);
        double saturnSpeedY = saturnSpeed * Math.sin(saturnAngle + Math.PI / 2);
        double uranusSpeedY = uranusSpeed * Math.sin(uranusAngle + Math.PI / 2);
        double neptuneSpeedY = neptuneSpeed * Math.sin(neptuneAngle + Math.PI / 2);
        double plutoSpeedY = plutoSpeed * Math.sin(plutoAngle + Math.PI / 2);

        Atom rocket = new Atom.AtomCreator()
                .setName("Rocket")
                .setSize(new ThreeVector(0.2, 0.2, 0.2))
//                .setSpeed(new ThreeVector(earthSpeedX, earthSpeedY, 0))//3_333_333
                .setPosition(new ThreeVector(earthPosX + earthSize, earthPosY, 0))
                .setMarkedByGod(true)
                .setWeight(100_000)
                .build();
        rocket.setRocket(true);
        default1.add(rocket);
        default1.add(new Atom.AtomCreator()
                .setName("Earth")
                .setSize(new ThreeVector(earthSize, earthSize, earthSize))
                .setSpeed(new ThreeVector(earthSpeedX, earthSpeedY, 0))//3_333_333
                .setPosition(new ThreeVector(earthPosX, earthPosY, 0))
                .setMarkedByGod(false)
                .setWeight(earthWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Moon")
                .setWeight(moonWeight)
                .setSize(new ThreeVector(moonSize, moonSize, moonSize))
                .setSpeed(new ThreeVector(moonSpeedX, moonSpeedY, 0))//3_333_333
                .setPosition(new ThreeVector(moonPosX, moonPosY, 0))
                .setMarkedByGod(false)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Son")
                .setWeight(sonWeight)
                .setSize(new ThreeVector(sonSize, sonSize, sonSize))
                .setSpeed(new ThreeVector(sonSpeedX, sonSpeedY, 0))
                .setPosition(new ThreeVector(sonPosX, sonPosY, 0))
                .setMarkedByGod(false)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Mars")
                .setSize(new ThreeVector(marsSize, marsSize, marsSize))
                .setSpeed(new ThreeVector(marsSpeedX, marsSpeedY, 0))
                .setPosition(new ThreeVector(marsPosX, marsPosY, 0))
                .setMarkedByGod(false)
                .setWeight(marsWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Mercury")
                .setSize(new ThreeVector(mercurySize, mercurySize, mercurySize))
                .setSpeed(new ThreeVector(mercurySpeedX, mercurySpeedY, 0))
                .setPosition(new ThreeVector(mercuryPosX, mercuryPosY, 0))
                .setMarkedByGod(false)
                .setWeight(mercuryWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Venus")
                .setSize(new ThreeVector(venusSize, venusSize, venusSize))
                .setSpeed(new ThreeVector(venusSpeedX, venusSpeedY, 0))
                .setPosition(new ThreeVector(venusPosX, venusPosY, 0))
                .setMarkedByGod(false)
                .setWeight(venusWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Jupiter")
                .setSize(new ThreeVector(jupiterSize, jupiterSize, jupiterSize))
                .setSpeed(new ThreeVector(jupiterSpeedX, jupiterSpeedY, 0))
                .setPosition(new ThreeVector(jupiterPosX, jupiterPosY, 0))
                .setMarkedByGod(false)
                .setWeight(jupiterWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Saturn")
                .setSize(new ThreeVector(saturnSize, saturnSize, saturnSize))
                .setSpeed(new ThreeVector(saturnSpeedX, saturnSpeedY, 0))
                .setPosition(new ThreeVector(saturnPosX, saturnPosY, 0))
                .setMarkedByGod(false)
                .setWeight(saturnWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Uranus")
                .setSize(new ThreeVector(uranusSize, uranusSize, uranusSize))
                .setSpeed(new ThreeVector(uranusSpeedX, uranusSpeedY, 0))
                .setPosition(new ThreeVector(uranusPosX, uranusPosY, 0))
                .setMarkedByGod(false)
                .setWeight(uranusWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Neptune")
                .setSize(new ThreeVector(neptuneSize, neptuneSize, neptuneSize))
                .setSpeed(new ThreeVector(neptuneSpeedX, neptuneSpeedY, 0))
                .setPosition(new ThreeVector(neptunePosX, neptunePosY, 0))
                .setMarkedByGod(false)
                .setWeight(neptuneWeight)
                .build());
        default1.add(new Atom.AtomCreator()
                .setName("Pluto")
                .setSize(new ThreeVector(plutoSize, plutoSize, plutoSize))
                .setSpeed(new ThreeVector(plutoSpeedX, plutoSpeedY, 0))
                .setPosition(new ThreeVector(plutoPosX, plutoPosY, 0))
                .setMarkedByGod(false)
                .setWeight(plutoWeight)
                .build());
//        default1.add(new Atom.AtomCreator()
//                .setWeight(1500)
//                .setSpeed(new ThreeVector(0, 0.0002, 0))
//                .setPosition(new ThreeVector(60, 50, 0))
//                .setMarkedByGod(true)
//                .build());
//        default1.add(new Atom.AtomCreator()
//                .setWeight(1500)
//                .setSpeed(new ThreeVector(0, 0.0003, 0))
//                .setPosition(new ThreeVector(80, 50, 0))
//                .setMarkedByGod(true)
//                .build());
//        default1.add(new Atom.AtomCreator()
//                .setWeight(1500)
//                .setSpeed(new ThreeVector(0, 0.0004, 0))
//                .setPosition(new ThreeVector(100, 50, 0))
//                .setMarkedByGod(true)
//                .build());
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                default2.add(new Atom.AtomCreator()
                        .setWeight(1)
                        .setPosition(new ThreeVector(5 + 100 / 10 * i, 5 + 100 / 10 * j, 0))
                        .build());
            }
        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                default3.add(new Atom.AtomCreator()
                        .setWeight(0.3)
                        .setPosition(new ThreeVector(1 + i * 2, 1 + j * 2, 0))
                        .build());
            }
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                default4.add(new Atom.AtomCreator()
                        .setWeight(0.5)
                        .setPosition(new ThreeVector(1 + i, 1 + j, 0))
                        .build());
            }
        }
        default5.add(new Atom.AtomCreator()
                .setWeight(150)
                .setPosition(new ThreeVector(4, 10, 0))
                .setSpeed(new ThreeVector(20, 0, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(150)
                .setPosition(new ThreeVector(30, 12, 0))
                .setSpeed(new ThreeVector(-20, 0, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(150)
                .setPosition(new ThreeVector(70, 10, 0))
                .setSpeed(new ThreeVector(4, 0, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(30)
                .setPosition(new ThreeVector(80, 12, 0))
                .setSpeed(new ThreeVector(-20, 0, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(150)
                .setPosition(new ThreeVector(20, 80, 0))
                .setSpeed(new ThreeVector(0, -10, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(30)
                .setPosition(new ThreeVector(30, 70, 0))
                .setSpeed(new ThreeVector(-10, 0, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(150)
                .setPosition(new ThreeVector(70, 70, 0))
                .setSpeed(new ThreeVector(-3, 0, 0))
                .build());
        default5.add(new Atom.AtomCreator()
                .setWeight(30)
                .setPosition(new ThreeVector(80, 72, 0))
                .setSpeed(new ThreeVector(-60, 0, 0))
                .build());

        Atom planet = new Atom.AtomCreator()
                .setName("Planet")
                .setWeight(10_000)
                .setPosition(new ThreeVector(50, 50, 0))
                .setSpeed(new ThreeVector(0, 0, 0))
                .build();
        Atom rocket2 = new Atom.AtomCreator()
                .setName("Rocket")
                .setWeight(1)
                .setPosition(new ThreeVector(50, 20, 0))
                .setSpeed(new ThreeVector(45, 0, 0))
                .build();
        rocket2.setMarkedByGod(true);
        rocket2.setRocket(true);
//        Rocket rocket = new Rocket.RocketCreator()
//                .setName("Rocket")
//                .setWeight(1)
//                .setPosition(new ThreeVector(50, 20, 0))
//                .setSpeed(new ThreeVector(20, 0, 0))
//                .build();
        rocket2.setCentreAtom(planet);
        rocket2.setMaxAcceleration(30);
        rocket2.setMaxSpeed(100);
        default6.add(planet);
        default6.add(rocket2);
//        saveState("empty", empty);
//        saveState("default_1", default1);
//        saveState("default_2", default2);
//        saveState("default_3", default3);
//        saveState("default_4", default4);

//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutput out = null;
//        byte[] yourBytes = new byte[0];
//        try {
//            out = new ObjectOutputStream(bos);
//            out.writeObject(savedStates);
//            yourBytes = bos.toByteArray();
//            if (out != null) {
//                out.close();
//            }
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
//        ObjectInput in = null;
//        try {
//            in = new ObjectInputStream(bis);
//            Object o = in.readObject();
//            bis.close();
//            if (in != null) {
//                in.close();
//            }
//            Map<String, List<Atom>>  actual = (Map<String, List<Atom>>) o;
//            System.out.println("actual = " + actual);
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream("states.out");
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(savedStates);
//            oos.flush();
//            oos.close();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            if (new File(STATE_FILE_NAME).exists() && RESTORE_STATES_WHEN_START_UP) {
                FileInputStream fis = new FileInputStream(STATE_FILE_NAME);
                ObjectInputStream oin = new ObjectInputStream(fis);
                HashMap<String, List<Atom>> map = (HashMap<String, List<Atom>>) oin.readObject();
                Set<Map.Entry<String, List<Atom>>> set = map.entrySet();
                Iterator<Map.Entry<String, List<Atom>>> iterator = set.iterator();
                System.out.println("Restore states processing. . .");
                while (iterator.hasNext()) {
                    Map.Entry<String, List<Atom>> mentry = iterator.next();
                    System.out.print("key: " + mentry.getKey() + " & Value: ");
                    System.out.println(mentry.getValue());
                    if (!savedStates.containsKey(mentry.getKey())) {
                        saveState(mentry.getKey(), mentry.getValue());
                    }
                }
            } else {
                System.err.println("[WARN] file '" + STATE_FILE_NAME + "' doesn't exist.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!savedStates.containsKey("empty")) {
            saveState("empty", empty);
        }
        if (!savedStates.containsKey("default_1")) {
            saveState("default_1", default1);
        }
        if (!savedStates.containsKey("default_2")) {
            saveState("default_2", default2);
        }
        if (!savedStates.containsKey("default_3")) {
            saveState("default_3", default3);
        }
        if (!savedStates.containsKey("default_4")) {
            saveState("default_4", default4);
        }
        if (!savedStates.containsKey("default_5")) {
            saveState("default_5", default5);
        }
        if (!savedStates.containsKey("default_6")) {
            saveState("default_6", default6);
        }
    }

    public double getActualMomentDuration() {
        return actualMomentDuration;
    }

    @Override
    public boolean areObjectsAvailable() {
        return objectsAvailable;
    }

    @Override
    public void setObjectsAvailable(boolean objectsAvailable) {
        this.objectsAvailable = objectsAvailable;
    }

}
