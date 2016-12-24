package universe;

import universe.view_trash.Visualizator;

public class PrimeCause {

  public static void main(String[] args) {
    while (!God.ONE.isOblivion()) {
      new PrimeCause();
    }
  }

  private PrimeCause() { // private модификатор изящно намекает, что первопричина не может быть создана кем то, ведь у нее нет истоков...... .-.
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
