package universe;

public class Universe {
  private final static God god = God.ONE;
  private Physics physics;
  private Space space;

  public Universe(NewtonPhysics physics, int dimensionality) {
    andGodSay(physics, dimensionality);
  }

  private void andGodSay(NewtonPhysics physics, int dimensionality) {
    this.physics = physics;
    this.space = new Space(physics, dimensionality);
  }

  public Space getSpace() {
    return space;
  }

  public void bigBang() {
    space.bigBang();
  }

  public void live() {
    space.live();
  }
}
