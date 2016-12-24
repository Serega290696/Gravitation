package universe.view_trash;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import universe.Universe;

public class UniverseControlPanel extends Application {
  private static Universe universe;

  @Override
  public void start(Stage primaryStage) throws Exception {
    System.out.println("JavaFx is started !");
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/universe_control_panel.fxml"));
    Parent root = fxmlLoader.load();
    primaryStage.setTitle("Universe control panel");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    UniverseControlPanelController controller = (UniverseControlPanelController) fxmlLoader.getController();
    controller.setUniverse(universe);
//    controller.setStage(primaryStage);
  }

  public static void setUniverse(Universe universe) {
    UniverseControlPanel.universe = universe;
  }

  public static void launchControlPanel() {
    launch();
  }
}
