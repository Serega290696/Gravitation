package being.view;

import being.universe.AbstractUniverse;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UniverseControlPanel extends Application {
    private static AbstractUniverse universe;
    private static UniverseControlPanelController controller;
    private static final double ATOMS_UPDATE_PERIOD_SECONDS = 0.1;
    private static boolean panelLaunched = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/universe_control_panel.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Universe42");
        primaryStage.setX(850);
        primaryStage.setY(20);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        panelLaunched = true;
        controller = (UniverseControlPanelController) fxmlLoader.getController();
        controller.setUniverse(universe);
        controller.init();
        Timeline tableSchedulingUpdater = new Timeline(new KeyFrame(Duration.seconds(ATOMS_UPDATE_PERIOD_SECONDS), event -> {
            controller.update();
        }));
        tableSchedulingUpdater.setCycleCount(Timeline.INDEFINITE);
        tableSchedulingUpdater.play();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DIGIT1) {
                controller.rewind();
            } else if (e.getCode() == KeyCode.DIGIT2) {
                controller.backFrame();
            } else if (e.getCode() == KeyCode.DIGIT3) {
                controller.playOrStop();
            } else if (e.getCode() == KeyCode.DIGIT4) {
                controller.nextFrame();
            } else if (e.getCode() == KeyCode.DIGIT5) {
                controller.flashForward();
            }else if (e.getCode() == KeyCode.R) {
                controller.restart();
            }
        });
//    controller.setStage(primaryStage);
    }

    public static void setUniverse(AbstractUniverse universe) {
        UniverseControlPanel.universe = universe;
//        controller.setUniverse(universe);
//        controller.init();
    }

    public static void launchControlPanel() {
        if (!panelLaunched) {
            launch();
        } else {
            updateUniverse();
        }
    }

    private static void updateUniverse() {
        controller.setUniverse(universe);
//        controller.init();
    }
}
