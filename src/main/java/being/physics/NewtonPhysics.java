package being.physics;

import being.elements.Angel;
import being.elements.Atom;
import being.mathematics.MathUtils;
import being.mathematics.ThreeVector;
import being.view.enums.ColorEnum;

import java.util.*;

import static being.physics.PhysicsConfigurations.NewtonPhysicsConfigurations.ELASTICITY_FACTOR;

public class NewtonPhysics extends MetricsPhysics {
    private List<Angel> angels;
    private final Object angelsLock = new Object();
    private final Map<Angel, LifeCycleStatus> lifeCycleStatus = new HashMap<>();

    public NewtonPhysics() {
        dimensionality = 3;
    }

    public void gravity(Collection<Atom> allAtoms, Collection<Atom> updatedAtoms) {

        //todo too slow. Vector optimization
        for (Atom updatedAtom : updatedAtoms) {
            ThreeVector power = new ThreeVector(0, 0, 0);
            for (Atom otherAtom : allAtoms) {
                if (updatedAtom != otherAtom) {
                    ThreeVector distance = otherAtom.getPosition().minus(updatedAtom.getPosition());
                    double minDistance = otherAtom.getSize().x / 2 + updatedAtom.getSize().x / 2;
                    double distanceModule = distance.module() >= minDistance
                            ?
                            distance.module()
                            :
                            minDistance;
                    ThreeVector plus = distance.normalize().multiply(
                            PhysicsConfigurations.NewtonPhysicsConfigurations.G *
                                    updatedAtom.getWeight() *
                                    otherAtom.getWeight() /
                                    Math.pow(distanceModule, 2)
                    );
//                    System.out.println("1");
                    if (distanceModule == 0) {
                        System.err.println("Black hole was created - distance is zero!");
                        System.exit(666);
                    }
                    power.plusAndUpdate(plus);
                }
            }
            updatedAtom.update(power);
        }
    }

    public void electromagnetism(Collection<Atom> allAtoms, Collection<Atom> updatedAtoms) throws Exception {
        for (Atom atom : updatedAtoms) {
            for (Atom otherAtom : allAtoms) {
                if (atom != otherAtom && intersect(atom, otherAtom)
                        && atom.getSerialNumber() > otherAtom.getSerialNumber()) {
                    Atom heavy = atom.getWeight() >= otherAtom.getWeight() ? atom : otherAtom;
                    Atom easy = atom.getWeight() >= otherAtom.getWeight() ? otherAtom : atom;
                    if (heavy.getSpeed().module() == 0) {
                        heavy.setSpeed(new ThreeVector(0.00000001, 0, 0));
                    }
                    if (easy.getSpeed().module() == 0) {
                        easy.setSpeed(new ThreeVector(0.00000001, 0, 0));
                    }
                    heavy.setColor(ColorEnum.BLUE);
                    easy.setColor(ColorEnum.GREEN);
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.DEBUG_LOG_LEVEL) {
                        System.out.println("---- NewtonPhysics.electromagnetism: " + heavy + ", " + easy);
                    }
                    ThreeVector positionDifferenceVector = heavy.getPosition().minus(easy.getPosition()).multiply(-1);
                    double positionDifferenceVectorAngle = positionDifferenceVector.angle();
                    double speedAngle = heavy.getSpeed().angle();
                    double angle = (positionDifferenceVectorAngle - speedAngle) % (2 * Math.PI);
                    ThreeVector v1 = heavy.getSpeed().projectOn(positionDifferenceVector);
                    ThreeVector v01 = heavy.getSpeed().projectRestOn(positionDifferenceVector);
                    ThreeVector v2 = easy.getSpeed().projectOn(positionDifferenceVector.multiply(-1));
                    ThreeVector v02 = easy.getSpeed().projectRestOn(positionDifferenceVector.multiply(-1));
                    ThreeVector pulse1 = v1.multiply(heavy.getWeight());
                    ThreeVector pulse2 = v2.multiply(easy.getWeight());
                    ThreeVector pulseSum = pulse1
                            .plus(pulse2);
                    ThreeVector pulseSumNorma = pulseSum.normalize();
                    double pulseSumModule = pulseSum.module();
                    int direction1 = Math.abs(v1.angle() - positionDifferenceVectorAngle) < Math.PI / 2
                            ?
                            1 : -1;
                    int direction2 = Math.abs(v2.angle() - positionDifferenceVectorAngle) < Math.PI / 2
                            ?
                            1 : -1;
                    double newSpeed1Module = -v1.module() * direction1
                            + 2
                            * (v1.module() * heavy.getWeight() * direction1 + v2.module() * easy.getWeight() * direction2)
                            / (heavy.getWeight() + easy.getWeight());
                    double newSpeed2Module = -v2.module() * direction2
                            + 2
                            * (v1.module() * heavy.getWeight() * direction1 + v2.module() * easy.getWeight() * direction2)
                            / (heavy.getWeight() + easy.getWeight());
//                    (heavy.getWeight() * v1.module()
//                            + easy.getWeight() * v2.module())
//                            / (2 * heavy.getWeight());
//                    double newSpeed2Module = (heavy.getWeight() * v1.module()
//                            + easy.getWeight() * v2.module())
//                            / (2 * easy.getWeight());
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.DEBUG_LOG_LEVEL) {
                        System.out.println("angle = " + angle);
                        System.out.println("v01 = " + v01);
                        System.out.println("v1 = " + v1);
                        System.out.println("v02 = " + v02);
                        System.out.println("v2 = " + v2);
                        System.out.println("newSpeed1Module = " + newSpeed1Module);
                        System.out.println("newSpeed2Module = " + newSpeed2Module);
                        System.out.println("pulseSumNorma = " + pulseSumNorma);
                        System.out.println("pulseSumModule = " + pulseSumModule);
                        System.out.println("v1.angle() = " + v1.angle());
                        System.out.println("v2.angle() = " + v2.angle());
                    }
                    double newAngle1 = positionDifferenceVectorAngle;
                    double newAngle2 = positionDifferenceVectorAngle;
                    if(newSpeed1Module < 0) {
                        newAngle1 = positionDifferenceVectorAngle + Math.PI;
                    }
                    if(newSpeed2Module < 0) {
                        newAngle2 = positionDifferenceVectorAngle + Math.PI;
                    }

                    ThreeVector n1 = new ThreeVector(newAngle1 , newSpeed1Module * ELASTICITY_FACTOR);
                    ThreeVector n2 = new ThreeVector(newAngle2, newSpeed2Module * ELASTICITY_FACTOR);
                    ThreeVector n12 = v01.plus(n1);
                    ThreeVector n22 = v02.plus(n2);

                    heavy.setSpeed(n12);
                    easy.setSpeed(n22);
//                    if (Math.abs(pulse1.module() - pulse2.module()) <= 10) { // almost equal
//                        ThreeVector n1 = new ThreeVector(newAngle1 , newSpeed1Module * ELASTICITY_FACTOR);
//                        ThreeVector n2 = new ThreeVector(newAngle2, newSpeed2Module * ELASTICITY_FACTOR);
//                        ThreeVector n12 = v01.plus(n1);
//                        ThreeVector n22 = v02.plus(n2);
//
//                        heavy.setSpeed(n12);
//                        easy.setSpeed(n22);
//                    } else {
//                        ThreeVector n1 = new ThreeVector(positionDifferenceVectorAngle, newSpeed1Module * ELASTICITY_FACTOR);
//                        ThreeVector n2 = new ThreeVector(positionDifferenceVectorAngle, newSpeed2Module * ELASTICITY_FACTOR);
//                        ThreeVector n12 = v01.plus(n1);
//                        ThreeVector n22 = v02.plus(n2);
//
//                        heavy.setSpeed(n12);
//                        easy.setSpeed(n22);
//                    }

//                    double normalDistance = (heavy.getSize().x / 2 + easy.getSize().x / 2);
//                    double distanceModule = heavy.getPosition().minus(easy.getPosition())
//                            .multiply(-1)
//                            .module();
//                    double distancePenalty = normalDistance -
//                            distanceModule;
//                    System.out.println("distanceModule = " + distanceModule);
//                    System.out.println("normalDistance = " + normalDistance);
//                    System.out.println("distancePenalty = " + distancePenalty);
//                    if (distancePenalty > 0) {
//                        heavy.setPosition(heavy.getPosition().plus(
//                                new ThreeVector(positionDifferenceVectorAngle + Math.PI,
//                                        distancePenalty / 1)));
//                        easy.setPosition(easy.getPosition().plus(
//                                new ThreeVector(positionDifferenceVectorAngle,
//                                        distancePenalty / 1)));
//                    }
//                    System.out.println("!");
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.DEBUG_LOG_LEVEL) {
                        System.out.println("heavy.getSpeed() = " + heavy.getSpeed());
                        System.out.println("easy.getSpeed() = " + easy.getSpeed());
                    }
//                    System.exit(123);
                } else {
                    atom.setColor(ColorEnum.WHITE);
                    otherAtom.setColor(ColorEnum.WHITE);
                }
            }
        }
    }

    public void push(Collection<Atom> allAtoms, Collection<Atom> updatedAtoms) throws Exception {
        for (Atom atom : updatedAtoms) {
            for (Atom otherAtom : allAtoms) {
                if (atom != otherAtom && intersect(atom, otherAtom)
//                        && atom.getSerialNumber() > otherAtom.getSerialNumber()
                        ) {
                    Atom heavy = atom.getWeight() >= otherAtom.getWeight() ? atom : otherAtom;
                    Atom easy = atom.getWeight() >= otherAtom.getWeight() ? otherAtom : atom;
                    if (heavy.getSpeed().module() == 0) {
                        heavy.setSpeed(new ThreeVector(0.00000001, 0, 0));
                    }
                    if (easy.getSpeed().module() == 0) {
                        easy.setSpeed(new ThreeVector(0.00000001, 0, 0));
                    }
                    double normalDistance = (heavy.getSize().x / 2 + easy.getSize().x / 2);
                    ThreeVector positionDifferenceVector = heavy.getPosition().minus(easy.getPosition()).multiply(-1);
                    double positionDifferenceVectorAngle = positionDifferenceVector.angle();
                    double distanceModule = positionDifferenceVector
                            .module();
                    double distancePenalty = normalDistance -
                            distanceModule;
//                    System.out.println("distanceModule = " + distanceModule);
//                    System.out.println("normalDistance = " + normalDistance);
//                    System.out.println("distancePenalty = " + distancePenalty);
                    if (distancePenalty > 0) {
                        heavy.setPosition(heavy.getPosition().plus(
                                new ThreeVector(positionDifferenceVectorAngle + Math.PI,
                                        distancePenalty / 2)));
                        easy.setPosition(easy.getPosition().plus(
                                new ThreeVector(positionDifferenceVectorAngle,
                                        distancePenalty / 2)));
                    }
                }
            }
        }
    }

    public void nextInstant() {
//        long l = System.currentTimeMillis();
//        System.out.println("NewtonPhysics.nextInstant: ");
//        ExecutorService service = Executors.newFixedThreadPool(angels.size());
//        for (Angel a : lifeCycleStatus.keySet()) {
//            while (!lifeCycleStatus.get(a)) ;
//            lifeCycleStatus.put(a, false);
//        }
//todo : if multi-angles env. then synchronize angels here
        for (Angel a : lifeCycleStatus.keySet()) {
            while (lifeCycleStatus.get(a) == LifeCycleStatus.WORK) ;
        }
        for (Angel a : lifeCycleStatus.keySet()) {
            lifeCycleStatus.put(a, LifeCycleStatus.PREPARED);
        }
//        synchronized (angelsLock) {
//            System.out.println("Time: " + (System.currentTimeMillis() - l));
//            lifeCycleStatus = false;
//            angelsLock.notifyAll();
//        }
//        System.out.println("---------------" + l);
    }

    public Object getLock() {
        return angelsLock;
    }

    @Override
    public Set basalObjectsGeneration() {
        return new HashSet<>();
    }

    @Override
    public boolean isPrepared(Angel angel) {
        return lifeCycleStatus.get(angel) == LifeCycleStatus.PREPARED;
    }

    @Override
    public void startWork(Angel angel) {
        lifeCycleStatus.put(angel, LifeCycleStatus.WORK);
    }

    @Override
    public void finishWork(Angel angel) {
        lifeCycleStatus.put(angel, LifeCycleStatus.AWAIT);
    }

    public boolean intersect(Atom atom, Atom otherAtom) throws Exception {
        switch (atom.getFigure()) {
            case CIRCLE:
            case STAR:
                switch (otherAtom.getFigure()) {
                    case STAR:
                    case CIRCLE:
                        return atom.getPosition().minus(otherAtom.getPosition()).module() <
                                MathUtils.max(atom.getSize().x, atom.getSize().y) / 2 + MathUtils.max(otherAtom.getSize().x, otherAtom.getSize().y) / 2;
                }
        }
        throw new Exception("Figure type doesn't supported");
    }

    public static boolean intersect(Atom atom, double x, double y) throws Exception {
        switch (atom.getFigure()) {
            case CIRCLE:
            case STAR:
                return atom.getPosition().minus(new ThreeVector(x, y, 0)).module() <
                        MathUtils.max(atom.getSize().x, atom.getSize().y) / 2 + MathUtils.max(0.01, 0.01) / 2;
        }
        throw new Exception("Figure type doesn't supported");
    }

    @Override
    public void loadAngles(List<Angel> angels) {
        this.angels = angels;
        this.angels.forEach(a -> lifeCycleStatus.put(a, LifeCycleStatus.PREPARED));
    }
}