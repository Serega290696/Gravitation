package being.elements;


import being.God;
import being.physics.PhysicsConfigurations;
import being.mathematics.ThreeVector;
import being.view.enums.ColorEnum;
import being.view.enums.DrawFigureType;

import java.io.Serializable;
import java.util.*;

public class Atom implements Cloneable, Serializable {
    protected final String name;
    protected final long serialNumber;
    protected static long serialNumberCounter = 0;
    protected List<Event> bindEvents;

    protected double weight;
    protected ThreeVector size;
    protected ThreeVector position;
    protected ThreeVector speed;
    protected ThreeVector acceleration;
    protected ThreeVector power;
    protected boolean markedByGod;
    protected boolean destroyed = false;
    protected DrawFigureType figure = DrawFigureType.STAR;
    protected ColorEnum color;
    protected double rotation = 0;
    protected double opacity = 0.9;
    protected boolean hasGuardianAngel;
    protected ThreeVector previousPosition;
    protected long relativelyTime;
    protected transient Angel angelGuardian;
    private boolean hidden;

    private boolean isRocket = false;
    private boolean enableEngine = false;
    private double maxAcceleration;
    private double maxSpeed;
    private Atom centreAtom;
    private double fuelConsumption = 0;

    public Atom(double weight, ThreeVector size, ThreeVector position, ThreeVector speed, boolean markedByGod) {
        this(null, weight, size, position, speed, markedByGod);
    }

    public Atom(String name, double weight, ThreeVector size, ThreeVector position, ThreeVector speed, boolean markedByGod) {
        serialNumber = ++serialNumberCounter;
        this.name = name;
        this.weight = weight;
        this.size = size;
        this.position = position;
        this.speed = speed;
        this.markedByGod = markedByGod;
        color = markedByGod ? ColorEnum.RED : ColorEnum.WHITE;
        bindEvents = Collections.synchronizedList(new ArrayList<Event>());
        rotation = 0;
        opacity = 1;
        acceleration = new ThreeVector(0, 0, 0);
        power = new ThreeVector(0, 0, 0);
        previousPosition = new ThreeVector(0, 0, 0);
        isRocket = false;
    }

    public void update(ThreeVector power) {
        this.power = power;
        acceleration = power.divide(weight);
        if (isRocket) {
//            System.out.println("acc_1 = " + acceleration.module());
//            System.out.println("centreAtom = " + centreAtom);
//            System.out.println("centreAtom.getPosition() = " + centreAtom.getPosition());
            ThreeVector distance;
            double stableOrbitSpeed;
            if (enableEngine) {
                distance = centreAtom.getPosition().minus(this.getPosition());
                stableOrbitSpeed = Math.sqrt(power.module() * distance.module() / weight);
                if (speed.module() < stableOrbitSpeed) {
                    double angle;
                    ThreeVector p = new ThreeVector(distance.angle() + Math.PI / 2, 1);
//                if (speed.projectOn(p).module() > 10) {
//                    System.out.println("p = " + p.angle());
                    angle = speed.projectOn(p).angle();
//                } else {
//                    angle = distance.angle() - Math.PI / 2;
//                }
//                System.out.println("angle = " + angle);
                    double tractionAcceleration = Math.min(
                            maxAcceleration,
                            10000000
//                        Math.abs(speed.module() - stableOrbitSpeed) / PhysicsConfigurations.MOMENT_DURATION
                    );
                    fuelConsumption += tractionAcceleration * PhysicsConfigurations.MOMENT_DURATION;
//                System.out.println("fuelConsumption = " + fuelConsumption);
                    acceleration.plusAndUpdate(new ThreeVector(angle,
                            tractionAcceleration));
//                acceleration.plusAndUpdate(new ThreeVector(angle,
//                        newSpeed));
//                new ThreeVector(distance.angle()+Math.PI/2, stableOrbitSpeed);
//                System.out.println("acc_2 = " + acceleration.module());
                }
            }

        }
        speed = speed.plus(acceleration.multiply(PhysicsConfigurations.MOMENT_DURATION));
        if (isRocket) {
            rotation = speed.angle();
        }
        previousPosition = position.clone();
//        previousPosition = position.plus(speed.plus(power.divide(weight)
//                .multiply(PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION))
//                .multiply(PhysicsConfigurations.MOMENT_DURATION));
//        previousPosition = position.clone();
    }

    public void move() {
        position = position.plus(speed.multiply(PhysicsConfigurations.MOMENT_DURATION));
        rotation += 1;
//    System.out.println(this);
        int bindEventsSize = bindEvents.size();
        if (!markedByGod && bindEventsSize > PhysicsConfigurations.NewtonPhysicsConfigurations.MAX_EVENTS_AMOUNT_PER_ATOM
                && !isRocket) {
            synchronized (bindEvents) {
                Iterator<Event> iterator = bindEvents.iterator();
                for (int i = 0; iterator.hasNext(); i++) {
                    iterator.next();
                    if (i < bindEventsSize / 4 && i % 2 == 0) {
                        iterator.remove();
                    }
                }
//                bindEvents.removeAll(bindEvents.subList(0, PhysicsConfigurations.NewtonPhysicsConfigurations.MAX_EVENTS_AMOUNT_PER_ATOM / 4));
            }
        }
    }

    public void fixEvent() {
        relativelyTime += PhysicsConfigurations.MOMENT_DURATION * 1000;
//        System.out.println("Atom.fixEvent: ");
        synchronized (bindEvents) {
            bindEvents.add(new Event(this.clone(), relativelyTime));
        }
//        System.out.println(bindEvents.get(bindEvents.size() - 1).getAtom().hasGuardianAngel());
    }

    public void resetTrace() {
        synchronized (bindEvents) {
            bindEvents.clear();
        }
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Atom " + (name == null ? super.toString() : name) + "(" +
                "position=" + position +
                ", speed=" + speed +
                ", power=" + power +
                ", " + weight + ", size=" + size + ")";
    }

    public void backForward() {
//        System.out.println("Atom.backForward");
//        System.out.println("bindEvents = ");
//        for (Event e : bindEvents) {
////            System.out.println(e.getAtom().hasGuardianAngel());
//            System.out.println(e.getAtom().getPosition());
//        }

        synchronized (bindEvents) {
            Atom atom = bindEvents.get(bindEvents.size() - 1).getAtom();
            this.weight = atom.weight;
            this.size = atom.size;
            this.position = atom.position;
            this.speed = atom.speed;
            this.acceleration = atom.acceleration;
            this.power = atom.power;
            this.markedByGod = atom.markedByGod;
            if (this.bindEvents.size() > 2) {
                synchronized (bindEvents) {
                    this.bindEvents.remove(this.bindEvents.size() - 1);
                }
            }
//        for (Event e : bindEvents) {
////            System.out.println(e.getAtom().hasGuardianAngel());
//            System.out.println(e.getAtom().getPosition());
//        }
            this.destroyed = atom.destroyed;
            this.figure = atom.figure;
            this.color = atom.color;
            this.rotation = atom.rotation;
            this.opacity = atom.opacity;
            this.hasGuardianAngel = atom.hasGuardianAngel;
            this.previousPosition = atom.previousPosition;
            this.relativelyTime = atom.relativelyTime;
            this.angelGuardian = atom.angelGuardian;
            this.hidden = atom.hidden;
            this.centreAtom = atom.centreAtom;
            this.maxSpeed = atom.maxSpeed;
            this.maxAcceleration = atom.maxAcceleration;
            this.fuelConsumption = atom.fuelConsumption;
        }
//        System.out.println("hasGuardianAngel = " + hasGuardianAngel);
    }

    @Override
    public Atom clone() {
        Atom clone = new Atom(name, weight, size.clone(), position.clone(), speed.clone(), markedByGod);
        clone.weight = this.weight;
        clone.size = this.size;
        clone.position = this.position;
        clone.speed = this.speed;
        clone.acceleration = this.acceleration;
        clone.power = this.power;
        clone.markedByGod = this.markedByGod;
        clone.bindEvents = this.bindEvents;
        clone.destroyed = this.destroyed;
        clone.figure = this.figure;
        clone.color = this.color;
        clone.rotation = this.rotation;
        clone.opacity = this.opacity;
        clone.hasGuardianAngel = this.hasGuardianAngel;
        clone.previousPosition = this.previousPosition;
        clone.relativelyTime = this.relativelyTime;
        clone.angelGuardian = this.angelGuardian;
        clone.hidden = this.hidden;
        clone.isRocket = this.isRocket;
        clone.centreAtom = this.centreAtom;
        clone.maxAcceleration = this.maxAcceleration;
        clone.maxSpeed = this.maxSpeed;
        clone.fuelConsumption = this.fuelConsumption;
//        System.out.println("clone.hasGuardianAngel = " + clone.hasGuardianAngel);
        return clone;
    }

    protected ThreeVector getPower() {
        return power;
    }

    public List<Event> getBindEvents() {
        return bindEvents;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public ThreeVector getPreviousPosition() {
        return previousPosition;
    }

    protected long getRelativelyTime() {
        return relativelyTime;
    }

    public DrawFigureType getFigure() {
        return figure;
    }

    public double getRotation() {
        return rotation;
    }

    public ColorEnum getColor() {
        return color;
    }

    public double getOpacity() {
        return opacity;
    }

    public boolean hasGuardianAngel() {
        return hasGuardianAngel;
    }

    public void setHasGuardianAngel(boolean hasGuardianAngel) {
        this.hasGuardianAngel = hasGuardianAngel;
    }

    public boolean isHidden() {
        return hidden;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isRocket() {
        return isRocket;
    }

    public void setRocket(boolean rocket) {
        isRocket = rocket;
//        figure = DrawFigureType.RECT;
    }

    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Atom getCentreAtom() {
        return centreAtom;
    }

    public void setCentreAtom(Atom centreAtom) {
        this.centreAtom = centreAtom;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public void returnOnPreviousState() {
        synchronized (bindEvents) {
            Atom lastState = bindEvents.get(bindEvents.size() - 1).getAtom();
            this.weight = lastState.getWeight();
            this.size = lastState.getSize();
            this.position = lastState.getPosition();
            this.speed = lastState.getSpeed();
            this.markedByGod = lastState.isMarkedByGod();
            this.centreAtom = lastState.centreAtom;
            this.maxSpeed = lastState.maxSpeed;
            this.maxAcceleration = lastState.maxAcceleration;
            this.fuelConsumption = lastState.fuelConsumption;
        }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setSize(ThreeVector size) {
        this.size = size;
    }

    public void setAcceleration(ThreeVector acceleration) {
        this.acceleration = acceleration;
    }

    public void setMarkedByGod(boolean markedByGod) {
        this.markedByGod = markedByGod;
        color = ColorEnum.RED;
    }

    public ThreeVector getSize() {
        return size;
    }

    public ThreeVector getPosition() {
        return position;
    }

    public ThreeVector getSpeed() {
        return speed;
    }

    public void setSpeed(ThreeVector speed) {
        this.speed = speed;
    }

    public ThreeVector getAcceleration() {
        return acceleration;
    }

    public boolean isMarkedByGod() {
        return markedByGod;
    }

    public String getName() {
        return name;
    }

    public Angel getAngelGuardian() {
        return angelGuardian;
    }

    public void setAngelGuardian(Angel angelGuardian) {
        this.angelGuardian = angelGuardian;
    }

    public void setPosition(ThreeVector position) {
        this.position = position;
    }

    public boolean inVisibilityZone(ThreeVector shift) {
        double x = position.x;
        double y = position.y;
        double sx = size.x;
        double sy = size.y;
        double highLimitY = shift.y + 100 * God.ONE.MIND.DEFAULT_EM / God.ONE.MIND.em;
        double lowLimitY = shift.y;
        double highLimitX = shift.x + 100 * God.ONE.MIND.DEFAULT_EM / God.ONE.MIND.em;
        double lowLimitX = shift.x;
        double left = x - sx / 2;
        double right = x + sx / 2;
        double top = y + sy / 2;
        double bottom = y - sy / 2;
//        if (markedByGod) {
//            System.out.println(
//                    (left < highLimitX
//                    && right > lowLimitX)
//                    && (bottom < highLimitY
//                    && top > lowLimitY)
//            );
//        }
        return (left < highLimitX
                && right > lowLimitX)
                && (bottom < highLimitY
                && top > lowLimitY)
                ;
    }

    public static class AtomCreator {
        private double weight;
        private ThreeVector size;
        private ThreeVector position;
        private ThreeVector speed;
        private boolean markedByGod;
        private String name;

        public AtomCreator setSpeed(ThreeVector speed) {
            this.speed = speed;
            return this;
        }

        public AtomCreator setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public AtomCreator setSize(ThreeVector size) {
            this.size = size;
            return this;
        }

        public AtomCreator setPosition(ThreeVector position) {
            this.position = position;
            return this;
        }

        public AtomCreator setMarkedByGod(boolean markedByGod) {
            this.markedByGod = markedByGod;
            return this;
        }

        public AtomCreator setName(String name) {
            this.name = name;
            return this;
        }

        public AtomCreator generateSize(double radius) {
            if (size == null) {
//                double radius = Math.pow(3d / 4 * weight / Math.PI, 1d / 3);
                double volume = Math.pow(radius, 3) * 4 * Math.PI / 3;//Math.pow(3d / 4 * weight / Math.PI, 1d / 3);
                weight = volume * 1;
                size = new ThreeVector(radius * 2, radius * 2, radius * 2);
            }
            return this;
        }

        public Atom build() {
            if (size == null) {
                if (weight == 0) {
                    weight = 1;
                }
                double radius = Math.pow(3d / 4 * weight / Math.PI, 1d / 3);
                size = new ThreeVector(radius * 2, radius * 2, radius * 2);
            }
            if (position == null) {
                position = new ThreeVector(0, 0, 0);
            }
            if (speed == null) {
                speed = new ThreeVector(0, 0, 0);
            }
            if (name == null) {
                name = "Atom-r" + (int) (Math.random() * 1000);
            }
            return new Atom(name, weight, size, position, speed, markedByGod);
        }

        public AtomCreator generateName(Set<Atom> atoms) {
            int maxNumber = 1;
            for (Object o : atoms) {
                if (o instanceof Atom) {
                    Atom a = (Atom) o;
                    String name = a.getName();
                    int curNumber = -1;
                    for (int i = name.length() - 1; i >= 0; i--) {
                        try {
                            Integer.parseInt(name.substring(i));
                        } catch (NumberFormatException e) {
                            break;
                        }
                        curNumber = Math.abs(Integer.parseInt(name.substring(i)));
                    }

                    if (curNumber > maxNumber) {
                        maxNumber = curNumber;
                    }
                }
            }
            name = "Atom-" + (maxNumber + 1);
            return this;
        }
    }

}
