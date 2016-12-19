package universe;


import universe.mathematics.ThreeVector;
import universe.view_trash.enums.ColorEnum;
import universe.view_trash.enums.DrawFigure;

import java.util.ArrayList;
import java.util.List;

public class Atom {
  private final String name;
  private double weight;
  private ThreeVector size;
  private ThreeVector position;
  private ThreeVector speed;
  private ThreeVector acceleration;
  private ThreeVector power;
  private boolean markedByGod;
  private List<Event> bindEvents;
  private boolean destroyed = false;
  private DrawFigure figure = DrawFigure.CIRCLE;
  private ColorEnum color;
  private double rotation = 0;
  private double opacity = 1;
  private boolean hasGuardianAngel;

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
    speed = speed.plus(acceleration.multiply(UniverseConfigurations.MOMENT_SIZE));
    position = position.plus(speed.multiply(UniverseConfigurations.MOMENT_SIZE));
    System.out.println(this);
  }

  @Override
  public String toString() {
    return "Atom " + (name == null ? super.toString() : name) + "(" +
        "position=" + position +
        ", speed=" + speed +
        ", power=" + power +
        ", " + weight + ", size=" + size + ")";
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
