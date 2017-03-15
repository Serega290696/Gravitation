package being.elements;

import being.mathematics.ThreeVector;

public class Event extends Atom {
    private double time;

    public Event(Atom atom, double time) {
        super(atom.weight, atom.size, atom.position, atom.speed, atom.markedByGod);
        this.time = time;
        this.weight = atom.weight;
        this.size = atom.size;
        this.position = atom.position;
        this.speed = atom.speed;
        this.acceleration = atom.acceleration;
        this.power = atom.power;
        this.markedByGod = atom.markedByGod;
//        this.bindEvents = atom.bindEvents;
        this.destroyed = atom.destroyed;
        this.figure = atom.figure;
        this.color = atom.color;
        this.rotation = atom.rotation;
        this.opacity = atom.opacity;
        this.hasGuardianAngel = atom.hasGuardianAngel;
        this.previousPosition = atom.previousPosition;
        this.relativelyTime = atom.relativelyTime;
        this.angelGuardian = atom.angelGuardian;
    }

    public double getTime() {
        return time;
    }

    public Atom getAtom() {
        Atom atom = new Atom(name, weight, size.clone(), position.clone(), speed.clone(), markedByGod);
        atom.weight = this.weight;
        atom.size = this.size;
        atom.position = this.position;
        atom.speed = this.speed;
        atom.acceleration = this.acceleration;
        atom.power = this.power;
        atom.markedByGod = this.markedByGod;
//        atom.bindEvents = this.bindEvents;
        atom.destroyed = this.destroyed;
        atom.figure = this.figure;
        atom.color = this.color;
        atom.rotation = this.rotation;
        atom.opacity = this.opacity;
        atom.hasGuardianAngel = this.hasGuardianAngel;
        atom.previousPosition = this.previousPosition;
        atom.relativelyTime = this.relativelyTime;
        atom.angelGuardian = this.angelGuardian;
        return atom;
    }
}
