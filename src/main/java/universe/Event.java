package universe;

import universe.mathematics.ThreeVector;

public class Event extends Atom {
  private double time;

  public Event(double weight, ThreeVector size, ThreeVector position, ThreeVector speed, boolean markedByGod, double time) {
    super(weight, size, position, speed, markedByGod);
    this.time  = time;
  }
}
