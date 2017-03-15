package being.view_trash;

import being.God;
import being.elements.Atom;
import being.mathematics.MathUtils;
import being.physics.PhysicsConfigurations;
import being.universe.AbstractUniverse;
import being.universe.UniverseStage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UniverseControlPanelController {

    @FXML
    private TableView<Atom> objectsTable;
    private boolean universeAreStopped = false;
    private AbstractUniverse universe;
    private List<Atom> atoms = new ArrayList<>();
    private int COLUMN_WIDTH = 70;
    private static final double rewindChangingPercent = 50.0;
    private boolean showOnlyMarkedAtoms = false;
    @FXML
    private Label actualMomentSize;
    @FXML
    private Label momentSize;
    @FXML
    private Label shift;
    @FXML
    private CheckBox onlyMarkedCheckbox;
    @FXML
    private Button backButton;

    public void init() {
        System.out.println("UniverseControlPanelController.init");
        initObjectsTable();
        updateObjectsTable();
    }

    @FXML
    public void initObjectsTable() {
        if (universe != null) {
            TableColumn<Atom, String> name = new TableColumn<>("Name");
            name.setMinWidth(COLUMN_WIDTH);
            name.<Atom, String>setCellValueFactory(
                    new PropertyValueFactory<>("name"));
            TableColumn<Atom, String> angel = new TableColumn<>("Angel");
            angel.setMinWidth(COLUMN_WIDTH);
            angel.<Atom, Double>setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getAngelGuardian().getName()));
            TableColumn position = createColumnThreeVector("position");
            TableColumn speed = createColumnThreeVector("speed");
            TableColumn size = createColumnThreeVector("size");
            objectsTable.getColumns().
                    addAll(
                            name, position, speed, size, angel);
        }
    }

    public void updateObjectsTable(boolean forcedToRefresh) {
        if (!God.ONE.isGodsWrath() && universe != null) {
            ObservableList<Atom> items = objectsTable.getItems();
            Set<Object> objects = universe.getObjects();

            if (!forcedToRefresh && objects.containsAll(items) && items.size() != 0 && items.size() != objects.size()) {// todo dangerous checking
                objectsTable.refresh();
//                System.out.println("Atoms in table (" + items.size() + "): ");
//                System.out.println("\t\t" + items.get(0));
//                System.out.println("\t\t" + items.get(0));
            } else {
                atoms.clear();
                atoms.addAll(objects.stream()
                        .filter(o -> o instanceof Atom)
                        .filter(o -> !showOnlyMarkedAtoms || ((Atom) o).isMarkedByGod())
                        .map(o -> (Atom) o)
                        .collect(Collectors.toList()));
                final ObservableList<Atom> usersObservableList =
                        FXCollections.observableArrayList(atoms);
                objectsTable.getItems().clear();
                objectsTable.setItems(usersObservableList);
//                System.out.println("Atoms in table (" + atoms.size() + "): ");
//                System.out.println("\t\t" + atoms.get(0));
//                System.out.println("\t\t" + atoms.get(0));
            }
            momentSize.setText("Moment size: " + PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_SIZE);
            actualMomentSize.setText("Actual moment size: " + PhysicsConfigurations.NewtonPhysicsConfigurations.ACTUAL_MOMENT_MIN_SIZE);
            shift.setText("Shift: " + God.ONE.MIND.getShift());
        }
    }
    @FXML
    public void updateObjectsTable() {
        updateObjectsTable(false);
    }


    private TableColumn createColumnThreeVector(String columnTitle) {
        TableColumn<Atom, Double> position = new TableColumn<>(columnTitle);
        TableColumn<Atom, Double> posX = new TableColumn<>("x");
        posX.setMinWidth(COLUMN_WIDTH * 1);
        posX.setMaxWidth(COLUMN_WIDTH * 1);
        posX.<Atom, Double>setCellValueFactory(param -> {
                    switch (columnTitle) {
                        case "position":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getPosition().x));
                        case "speed":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSpeed().x));
                        case "acceleration":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getAcceleration().x));
                        case "size":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSize().x));
                        default:
                            return null;
                    }
                }
        );
        TableColumn<Atom, Double> posY = new TableColumn<>("y");
        posY.setMinWidth(COLUMN_WIDTH * 0.5);
        posY.setMaxWidth(COLUMN_WIDTH * 0.7);
        posY.<Atom, Double>setCellValueFactory(param -> {
                    switch (columnTitle) {
                        case "position":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getPosition().y));
                        case "speed":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSpeed().y));
                        case "acceleration":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getAcceleration().y));
                        case "size":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSize().y));
                        default:
                            return null;
                    }
                }
        );
        TableColumn<Atom, Double> posZ = new TableColumn<>("z");
        posZ.setMinWidth(COLUMN_WIDTH * 0.5);
        posZ.setMaxWidth(COLUMN_WIDTH * 0.7);
        posZ.<Atom, Double>setCellValueFactory(param -> {
                    switch (columnTitle) {
                        case "position":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getPosition().z));
                        case "speed":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSpeed().z));
                        case "acceleration":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getAcceleration().z));
                        case "size":
                            return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSize().z));
                        default:
                            return null;
                    }
                }
        );
        position.getColumns().addAll(posX, posY, posZ);
        return position;
    }

    private TableColumn<Atom, String> createColumn(String title) {
        return createColumn(title, title.substring(0, 1).toLowerCase() + title.substring(1), 1);
    }

    private TableColumn<Atom, String> createColumn(String title, String fieldName, double widthFactor) {
        TableColumn<Atom, String> column = new TableColumn<Atom, String>(title);
        column.setMinWidth(COLUMN_WIDTH * widthFactor);
        column.<Atom, String>setCellValueFactory(
                new PropertyValueFactory<>(fieldName));
        return column;
    }

//    public void stopOrResume() {
//        if (universe.getStage() == UniverseStage.ALIVE) {
//            System.out.println("STOP");
//            universe.pause();
//        } else {
//            System.out.println("RESUME");
//            universe.resume();
//        }
//    }

    public void restart() {
        //todo
        // this method throw complicated and ridiculous exception related with multithreading
        God.ONE.eatAppleOfKnowledge();
    }

    public void onlyMarkedCheckbox() {
        System.out.println("UniverseControlPanelController.onlyMarkedCheckbox: " +  onlyMarkedCheckbox.isSelected());
        showOnlyMarkedAtoms = onlyMarkedCheckbox.isSelected();
        updateObjectsTable(true);
    }

    public void rewind() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.ACTUAL_MOMENT_MIN_SIZE *=
                1 + rewindChangingPercent / 100;
    }

    public void backFrame() {
        universe.backFrame();
        //todo
    }

    public void playOrStop() {
        if (God.ONE.isPause()) {
            God.ONE.play();
        } else {
            God.ONE.pause();
        }
        //todo
    }

    public void nextFrame() {
        universe.nextFrame();
        //todo
    }

    public void flashForward() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.ACTUAL_MOMENT_MIN_SIZE *=
                1 - rewindChangingPercent / 100;
        //todo
    }


    public void setUniverse(AbstractUniverse universe) {
        this.universe = universe;
    }
}
