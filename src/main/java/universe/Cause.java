package universe;

import universe.view_trash.Visualizator;

public class Cause {

  public static void main(String[] args) {
    final NewtonPhysics physics = new NewtonPhysics();
    final int dimensionality = 3;
    final Universe universe = new Universe(physics, dimensionality);
    new Thread(() -> {
      Visualizator visualizator = new Visualizator(universe);
      visualizator.visualize();
    }).start();
    universe.bigBang();
    universe.live();
  }
}
