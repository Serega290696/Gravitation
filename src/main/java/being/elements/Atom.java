package being.elements;


import being.physics.PhysicsConfigurations;
import being.mathematics.ThreeVector;
import being.view_trash.enums.ColorEnum;
import being.view_trash.enums.DrawFigure;

import java.util.ArrayList;
import java.util.List;

public class Atom implements Cloneable {
    protected final String name;
    protected double weight;
    protected ThreeVector size;
    protected ThreeVector position;
    protected ThreeVector speed;
    protected ThreeVector acceleration;
    protected ThreeVector power;
    protected boolean markedByGod;
    protected List<Event> bindEvents;
    protected boolean destroyed = false;
    protected DrawFigure figure = DrawFigure.CIRCLE;
    protected ColorEnum color;
    protected double rotation = 0;
    protected double opacity = 1;
    protected boolean hasGuardianAngel;
    protected ThreeVector previousPosition;
    protected long relativelyTime;
    protected Angel angelGuardian;

    public Atom(double weight, ThreeVector size, ThreeVector position, ThreeVector speed, boolean markedByGod) {
        this(null, weight, size, position, speed, markedByGod);
    }

    public Atom(String name, double weight, ThreeVector size, ThreeVector position, ThreeVector speed, boolean markedByGod) {
        this.name = name;
        this.weight = weight;
        this.size = size;
        this.position = position;
        this.speed = speed;
        this.markedByGod = markedByGod;
        color = markedByGod ? ColorEnum.RED : ColorEnum.WHITE;
        bindEvents = new ArrayList<Event>();
        rotation = 0;
        opacity = 1;
    }

    public void update(ThreeVector power) {
        this.power = power;
        acceleration = power.divide(weight);
        speed = speed.plus(acceleration.multiply(PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_SIZE));
        previousPosition = position.clone();
        position = position.plus(speed.multiply(PhysicsConfigurations.MOMENT_SIZE));
//    System.out.println(this);
        relativelyTime += PhysicsConfigurations.MOMENT_SIZE * 1000;
        if (!markedByGod && bindEvents.size() > PhysicsConfigurations.NewtonPhysicsConfigurations.MAX_EVENTS_AMOUNT_PER_ATOM) {
            bindEvents.remove(0);
        }
    }

    public void fixEvent() {
//        System.out.println("Atom.fixEvent: ");
        bindEvents.add(new Event(this.clone(), relativelyTime));
//        System.out.println(bindEvents.get(bindEvents.size() - 1).getAtom().hasGuardianAngel());
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
        Atom atom = bindEvents.get(bindEvents.size() - 1).getAtom();
        this.weight = atom.weight;
        this.size = atom.size;
        this.position = atom.position;
        this.speed = atom.speed;
        this.acceleration = atom.acceleration;
        this.power = atom.power;
        this.markedByGod = atom.markedByGod;
        if (this.bindEvents.size() - 2 > 0) {
            this.bindEvents = this.bindEvents.subList(0, this.bindEvents.size() - 1);
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
//        System.out.println("hasGuardianAngel = " + hasGuardianAngel);
    }

    @Override
    protected Atom clone() {
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

    public DrawFigure getFigure() {
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

    public void returnOnPreviousState() {
        Atom lastState = bindEvents.get(bindEvents.size() - 1).getAtom();
        this.weight = lastState.getWeight();
        this.size = lastState.getSize();
        this.position = lastState.getPosition();
        this.speed = lastState.getSpeed();
        this.markedByGod = lastState.isMarkedByGod();
    }


    public double getWeight() {
        return weight;
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

        public Atom build() {
            if (size == null) {
                double radius = Math.pow(3d / 4 * weight / Math.PI, 1d / 3);
                size = new ThreeVector(radius, radius, radius);
            }
            return new Atom(name, weight, size, position, speed, markedByGod);
        }
    }

}
