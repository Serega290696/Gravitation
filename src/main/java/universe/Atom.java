package universe;


import universe.mathematics.ThreeVector;
import universe.view_trash.enums.ColorEnum;
import universe.view_trash.enums.DrawFigure;

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
    bindEvents.add(new Event(this.clone(), relativelyTime));
    this.power = power;
    acceleration = power.divide(weight);
    speed = speed.plus(acceleration.multiply(UniverseConfigurations.MOMENT_SIZE));
    previousPosition = position.clone();
    position = position.plus(speed.multiply(UniverseConfigurations.MOMENT_SIZE));
    System.out.println(this);
    relativelyTime += UniverseConfigurations.MOMENT_SIZE * 1000;
    if(!markedByGod && bindEvents.size() > 3) {
      bindEvents.remove(0);
    }
  }

  @Override
  public String toString() {
    return "Atom " + (name == null ? super.toString() : name) + "(" +
        "position=" + position +
        ", speed=" + speed +
        ", power=" + power +
        ", " + weight + ", size=" + size + ")";
  }

  @Override
  protected Atom clone() {
    return new Atom(name, weight, size.clone(), position.clone(), speed.clone(), markedByGod);
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
    Atom lastState = bindEvents.get(bindEvents.size()-1).getAtom();
    this.weight = lastState.getWeight();
    this.size = lastState.getSize();
    this.position = lastState.getPosition();
    this.speed = lastState.getSpeed();
    this.markedByGod = lastState.isMarkedByGod();
  }

  static class AtomCreator {
    private double weight;
    private ThreeVector size;
    private ThreeVector position;
    private ThreeVector speed;
    private boolean markedByGod;
    private String name;

    AtomCreator setSpeed(ThreeVector speed) {
      this.speed = speed;
      return this;
    }

    AtomCreator setWeight(double weight) {
      this.weight = weight;
      return this;
    }

    AtomCreator setSize(ThreeVector size) {
      this.size = size;
      return this;
    }

    AtomCreator setPosition(ThreeVector position) {
      this.position = position;
      return this;
    }

    AtomCreator setMarkedByGod(boolean markedByGod) {
      this.markedByGod = markedByGod;
      return this;
    }

    AtomCreator setName(String name) {
      this.name = name;
      return this;
    }

    Atom build() {
      if (size == null) {
        double radius = Math.pow(3d / 4 * weight / Math.PI, 1d / 3);
        size = new ThreeVector(radius, radius, radius);
      }
      return new Atom(name, weight, size, position, speed, markedByGod);
    }
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
}
