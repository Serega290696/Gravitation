package universe;

public class Universe {
  private final static God god = God.ONE;
  private Physics physics;
  private Spacetime spacetime;
  private boolean stopped;

  public Universe(NewtonPhysics physics, int dimensionality) {
    andGodSay(physics, dimensionality);
  }

  private void andGodSay(NewtonPhysics physics, int dimensionality) {
    this.physics = physics;
    this.spacetime = new Spacetime(physics, dimensionality);
  }

  public Spacetime getSpacetime() {
    return spacetime;
  }

  public void bigBang() {
    spacetime.bigBang();
  }

  public void live() {
    spacetime.live();
  }

  public boolean isStopped() {
    return stopped;
  }

  public void resume() {
    spacetime.resume();
    stopped = false;
  }

  public void stop() {
    spacetime.stop();
    stopped = true;
  }
}
