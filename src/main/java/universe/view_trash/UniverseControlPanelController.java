package universe.view_trash;

import javafx.scene.control.TableView;
import universe.God;
import universe.Universe;

public class UniverseControlPanelController {

  public TableView atomsTable;
  private Universe universe;

  public void stopOrResume() {
    System.out.println("UniverseControlPanelController.stopOrResume");
    if (universe.isStopped()) {
      System.out.println("RESUME");
      universe.resume();
    } else {
      System.out.println("STOP");
      universe.stop();
    }
  }

  public void restart() {
    God.ONE.visualizatingFinishNotify();
  }

  public void setUniverse(Universe universe) {
    this.universe = universe;
  }
}
