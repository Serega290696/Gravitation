package universe;

import universe.mathematics.ThreeVector;

public class Event extends Atom {
  private double time;

  public Event(Atom atom, double time) {
    this(atom.getWeight(), atom.getSize(), atom.getPosition(), atom.getSpeed(), atom.isMarkedByGod(), time);
  }

  public Event(double weight, ThreeVector size, ThreeVector position, ThreeVector speed, boolean markedByGod, double time) {
    super(weight, size, position, speed, markedByGod);
    this.time = time;
  }

  public double getTime() {
    return time;
  }

  public Atom getAtom() {
    return new Atom(name, weight, size, position, speed, markedByGod);
  }
}
