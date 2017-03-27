package being.view_trash;

import being.God;
import being.elements.Atom;
import being.mathematics.MathUtils;
import being.mathematics.ThreeVector;
import being.physics.NewtonPhysics;
import being.physics.PhysicsConfigurations;
import being.universe.AbstractUniverse;
import being.universe.Universe42;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UniverseControlPanelController {

    @FXML
    private TableView<Atom> objectsTable;
    private boolean universeAreStopped = false;
    private AbstractUniverse<NewtonPhysics, Atom> universe;
    private Set<Atom> atoms;
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
    private Label traceLength;
    @FXML
    private CheckBox onlyMarkedCheckbox;
    @FXML
    private CheckBox gravityCheckbox;
    @FXML
    private CheckBox electromagnetismCheckbox;
    @FXML
    private CheckBox showVectorsCheckbox;
    @FXML
    private CheckBox drawOnlyCirclesCheckbox;
    @FXML
    private Button backButton;
    @FXML
    private Slider traceSizeSlider;
    @FXML
    private Slider speedSlider;
    @FXML
    private TableView<org.apache.commons.lang3.tuple.Pair> parametersTable;
    @FXML
    private MenuItem emptyState;
    @FXML
    private MenuItem defaultState;
    @FXML
    private MenuButton restoreStateMenu;
    @FXML
    private Button playOrStopButton;
    @FXML
    private TextField timeField;
    @FXML
    private TextField gField;

    @FXML
    public void upd() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.G = Double.valueOf(gField.getText());
        PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION = Double.valueOf(timeField.getText());
    }

    public void init() {
        System.out.println("UniverseControlPanelController.init");
        objectsTable.setEditable(true);
//        parametersTable.setEditable(true);
        initTables();
        update();
        refresh();
        gravityCheckbox.setSelected(PhysicsConfigurations.NewtonPhysicsConfigurations.IS_GRAVITY_ENABLE);
        electromagnetismCheckbox.setSelected(PhysicsConfigurations.NewtonPhysicsConfigurations.IS_ELECTROMAGNETISM_ENABLE);
//        emptyState.setOnAction(e -> restoreState("empty"));
//        defaultState.setOnAction(e -> restoreState("default"));
//        MenuItem defaultState2 = new MenuItem("Default 2");
//        defaultState2.setOnAction(e -> restoreState("default_2"));
//        restoreStateMenu.getItems().add(defaultState2);
//        MenuItem defaultState3 = new MenuItem("Default 3");
//        defaultState3.setOnAction(e -> restoreState("default_3"));
//        restoreStateMenu.getItems().add(defaultState3);
//        MenuItem defaultState4 = new MenuItem("Default 4");
//        defaultState4.setOnAction(e -> restoreState("default_4"));
//        restoreStateMenu.getItems().add(defaultState4);
        Set<String> set = universe.getSavedStates().keySet();
        ArrayList<String> list = new ArrayList<>(set);
        Collections.sort(list);
        for (String title : list) {
            String beautyTitle = title.replace('_', ' ').trim();
            beautyTitle = String.valueOf(beautyTitle.charAt(0)).toUpperCase() + beautyTitle.substring(1);
            MenuItem menuItem = new MenuItem(beautyTitle);
            menuItem.setOnAction(e -> restoreState(title));
            restoreStateMenu.getItems().add(menuItem);
        }
    }

    //    @FXML
    public void restoreState(String stateTitle) {
        System.out.println("UniverseControlPanelController.restoreState: " + stateTitle);
        God.ONE.pause();
        God.ONE.restoreState(stateTitle);
    }

    @FXML
    public void refresh() {
        ObservableList<Atom> items = objectsTable.getItems();
        Set<Atom> objects = universe.getObjects();
//        if (objects.containsAll(items) && items.size() != 0 && items.size() != objects.size()) {// todo dangerous checking
//            objectsTable.refresh();
//        } else {
        atoms = universe.getObjects();
//        atoms.addAll(objects.stream()
//                .filter(o -> !showOnlyMarkedAtoms || o.isMarkedByGod())
//                .collect(Collectors.toList()));
        final ObservableList<Atom> usersObservableList =
                FXCollections.observableArrayList(atoms);
        objectsTable.getItems().clear();
        objectsTable.setItems(usersObservableList);
//        }
    }

    @FXML
    public void saveState() {
        God.ONE.pause();
        final String title = "State-" + (restoreStateMenu.getItems().size() - 1);
        God.ONE.saveState(title);
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(e -> restoreState(title));
        restoreStateMenu.getItems().add(menuItem);
    }

    @FXML
    public void drawOnlyCircles() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.DRAW_SIMPLE_SHAPES = drawOnlyCirclesCheckbox.isSelected();
    }

    @FXML
    public void initTables() {
        if (universe != null) {
            timeField.setText(String.valueOf(PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION));
            gField.setText(String.valueOf(PhysicsConfigurations.NewtonPhysicsConfigurations.G));
            objectsTable.getSelectionModel().setSelectionMode(
                    SelectionMode.MULTIPLE
            );
            objectsTable.setEditable(true);
            TableColumn<Atom, String> name = new TableColumn<>("Name");
            name.setEditable(true);
            name.setMinWidth(COLUMN_WIDTH);
            name.<Atom, String>setCellValueFactory(
                    new PropertyValueFactory<>("name"));

            TableColumn<Atom, Double> weight = new TableColumn<>("Weight");
            weight.setMinWidth(COLUMN_WIDTH);
            weight.setCellValueFactory(p ->
                    new ReadOnlyObjectWrapper<Double>(MathUtils.round(p.getValue().getWeight())));
            weight.<Atom, Double>setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            weight.setOnEditCommit(
                    event -> {
                        event.getTableView().getItems().get(
                                event.getTablePosition().getRow()).setWeight(event.getNewValue());
                    }
            );

            TableColumn<Atom, String> angel = new TableColumn<>("Angel");
            angel.setMinWidth(COLUMN_WIDTH / 1.5);
            angel.<Atom, String>setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                    param.getValue().getAngelGuardian() == null ? "" :
                            param.getValue().getAngelGuardian().getName()));
            TableColumn<Atom, Double> position = createColumnThreeVector("position");
            TableColumn<Atom, Double> speed = createColumnThreeVector("speed");
//            TableColumn size = createColumnThreeVector("size");
            TableColumn<Atom, Double> size = new TableColumn<>("Size (sx)");
            size.setMinWidth(COLUMN_WIDTH);
            size.setCellValueFactory(p ->
                    new ReadOnlyObjectWrapper<Double>(MathUtils.round(p.getValue().getSize().x)));
            size.<Atom, Double>setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            size.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            size.setOnEditCommit(
                    event -> {
                        event.getTableView().getItems().get(
                                event.getTablePosition().getRow()).setSize(
                                new ThreeVector(event.getNewValue(), event.getNewValue(), event.getNewValue()));
                    }
            );
            objectsTable.getColumns().
                    addAll(
                            name,
                            position, speed, size,
                            weight
                            , angel
                    );

            TableColumn<org.apache.commons.lang3.tuple.Pair, String> parameterTitle = new TableColumn<>("Title");
            parameterTitle.setMinWidth(COLUMN_WIDTH);
            parameterTitle.<org.apache.commons.lang3.tuple.Pair, String>setCellValueFactory(
                    new PropertyValueFactory<Pair, String>("left"));
            TableColumn<org.apache.commons.lang3.tuple.Pair, String> parameterValue = new TableColumn<>("Value");
            parameterValue.setMinWidth(COLUMN_WIDTH);
            parameterValue.<org.apache.commons.lang3.tuple.Pair, String>setCellValueFactory(
                    new PropertyValueFactory<Pair, String>("right"));
//            pairTitle.<org.apache.commons.lang3.tuple.Pair, String>setCellValueFactory(
//                    new PropertyValueFactory<Pair, String>("left"));
            parametersTable.getColumns().addAll(parameterTitle, parameterValue);
        }
    }

    @FXML
    public void addAtom() {
        Atom atom = new Atom.AtomCreator()
                .generateName(universe.getObjects())
                .setPosition(new ThreeVector(
                        God.ONE.MIND.getShift().x + 50 * God.ONE.MIND.DEFAULT_EM / God.ONE.MIND.em,
                        God.ONE.MIND.getShift().y + 50 * God.ONE.MIND.DEFAULT_EM / God.ONE.MIND.em,
                        0))
                .build();

        System.out.println("atom = " + atom);
        universe.addObjects(atom);
    }

    @FXML
    public void editAtom() {

    }

    @FXML
    public void focusAtom() {
        Atom selectedAtom = objectsTable.getSelectionModel().getSelectedItem();
        if (universe.getFocusedAtom() != selectedAtom) {
            universe.setFocusedAtom(selectedAtom);
        } else {
            universe.setFocusedAtom(null);
        }
    }

    @FXML
    public void deleteAtom() {
        Atom selected = objectsTable.getSelectionModel().getSelectedItem();
//        .remove(selected);
        universe.deleteObject(selected);
    }

    public void update() {
        if (!God.ONE.isGodsWrath() && universe != null) {
            ObservableList<Atom> items = objectsTable.getItems();
            Set<Atom> objects = universe.getObjects();
            objectsTable.refresh();
//            refresh();
//            if (!forcedToRefresh && objects.containsAll(items) && items.size() != 0 && items.size() != objects.size()) {// todo dangerous checking
//                objectsTable.refresh();
//            } else {
//                atoms.clear();
//                atoms.addAll(objects.stream()
//                        .filter(o -> o instanceof Atom)
//                        .filter(o -> !showOnlyMarkedAtoms || ((Atom) o).isMarkedByGod())
//                        .map(o -> (Atom) o)
//                        .collect(Collectors.toList()));
//                final ObservableList<Atom> usersObservableList =
//                        FXCollections.observableArrayList(atoms);
//                objectsTable.getItems().clear();
//                objectsTable.setItems(usersObservableList);
//            }
            int updatedTraceSize = (int) (3 * Math.pow(2.1, traceSizeSlider.getValue() / 10) - 3);
            God.setMaxShownEventsPerAtom(updatedTraceSize);
            PhysicsConfigurations.NewtonPhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION =
                    PhysicsConfigurations.NewtonPhysicsConfigurations.DEFAULT_DISPLAYING_MOMENT_MIN_DURATION *
                            (1 * Math.pow(1.8, (100 - speedSlider.getValue() - 50) / 5));
//                            speedSlider.getValue();

            List<Pair> parameters = new ArrayList<>();
            Pair<String, String> p1 = Pair.of("Universe state", universe.getStage().name());
            Pair<String, String> p2 = Pair.of("Scale factor (em)", String.valueOf(God.ONE.MIND.em));
            Pair<String, String> p3 = Pair.of("Shift", String.valueOf(God.ONE.MIND.getShift()));
            Pair<String, String> p4 = Pair.of("G-const", String.valueOf(PhysicsConfigurations.NewtonPhysicsConfigurations.G));
            Pair<String, String> p5 = Pair.of("Moment duration (m.d.)", String.valueOf(MathUtils.round(PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION, 6)));
            Pair<String, String> p6 = Pair.of("Displaying m.d.", String.valueOf(MathUtils.round(PhysicsConfigurations.NewtonPhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION, 6)));
            parameters.add(p1);
            parameters.add(p2);
            parameters.add(p3);
            parameters.add(p4);
            parameters.add(p5);
            parameters.add(p6);
            if (universe instanceof Universe42) {
                Pair<String, String> p7 = Pair.of("Actual m.d.",
                        String.valueOf(MathUtils.round(
                                ((Universe42) universe).getActualMomentDuration() / 1000, 6)));
                parameters.add(p7);
            }
            ObservableList<Pair> pairs = FXCollections.observableArrayList(parameters);
            parametersTable.getItems().clear();
            parametersTable.setItems(pairs);
        }
    }

//    @FXML
//    public void update() {
//        update(false);
//    }


    private TableColumn<Atom, Double> createColumnThreeVector(String columnTitle) {
        TableColumn<Atom, Double> bigColumn = new TableColumn<>(columnTitle);
        TableColumn<Atom, Double> subColumnX = new TableColumn<>("x");
        subColumnX.setMinWidth(COLUMN_WIDTH * 0.5);
        subColumnX.setMaxWidth(COLUMN_WIDTH * 0.7);
        subColumnX.<Atom, Double>setCellValueFactory(param -> {
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
        TableColumn<Atom, Double> subColumnY = new TableColumn<>("y");
        subColumnY.setMinWidth(COLUMN_WIDTH * 0.5);
        subColumnY.setMaxWidth(COLUMN_WIDTH * 0.7);
        subColumnY.<Atom, Double>setCellValueFactory(param -> {
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
        TableColumn<Atom, Double> subColumnZ = new TableColumn<>("z");
        subColumnZ.setMinWidth(COLUMN_WIDTH * 0.5);
        subColumnZ.setMaxWidth(COLUMN_WIDTH * 0.7);
        subColumnZ.<Atom, Double>setCellValueFactory(param -> {
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

//        subColumnX.setCellValueFactory(
//                new PropertyValueFactory<Atom, Double>(columnTitle));
        subColumnX.<Atom, Double>setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        subColumnY.<Atom, Double>setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        subColumnZ.<Atom, Double>setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
//        subColumnX.setOnEditCommit(
//                event -> {
//                    Atom atom = event.getTableView().getItems().get(
//                            event.getTablePosition().getRow());
//                    atom.setPosition(
//                            new ThreeVector(event.getNewValue(), atom.getPosition().y, atom.getPosition().z));
//                }
//        );
        switch (columnTitle) {
            case "position":
                subColumnX.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setPosition(
                                    new ThreeVector(event.getNewValue(), atom.getPosition().y, atom.getPosition().z));
                        }
                );
                subColumnY.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setPosition(
                                    new ThreeVector(atom.getPosition().x, event.getNewValue(), atom.getPosition().z));
                        }
                );
                subColumnZ.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setPosition(
                                    new ThreeVector(atom.getPosition().x, atom.getPosition().y, event.getNewValue()));
                        }
                );
                break;
            case "speed":
                subColumnX.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setSpeed(
                                    new ThreeVector(event.getNewValue(), atom.getSpeed().y, atom.getSpeed().z));
                        }
                );
                subColumnY.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setSpeed(
                                    new ThreeVector(atom.getSpeed().x, event.getNewValue(), atom.getSpeed().z));
                        }
                );
                subColumnZ.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setSpeed(
                                    new ThreeVector(atom.getSpeed().x, atom.getSpeed().y, event.getNewValue()));
                        }
                );
                break;
            case "acceleration":
                subColumnX.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setAcceleration(
                                    new ThreeVector(event.getNewValue(), atom.getAcceleration().y, atom.getAcceleration().z));
                        }
                );
                subColumnY.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setAcceleration(
                                    new ThreeVector(atom.getAcceleration().x, event.getNewValue(), atom.getAcceleration().z));
                        }
                );
                subColumnZ.setOnEditCommit(
                        event -> {
                            Atom atom = event.getTableView().getItems().get(
                                    event.getTablePosition().getRow());
                            atom.setAcceleration(
                                    new ThreeVector(atom.getAcceleration().x, atom.getAcceleration().y, event.getNewValue()));
                        }
                );
                break;
            case "size":
                EventHandler<TableColumn.CellEditEvent<Atom, Double>> handler = event -> {
                    Atom atom = event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    atom.setSize(
                            new ThreeVector(event.getNewValue(), event.getNewValue(), event.getNewValue()));
                };
                subColumnX.setOnEditCommit(handler);
                subColumnY.setOnEditCommit(handler);
                subColumnZ.setOnEditCommit(handler);
                break;
            default:
                return null;
        }
        bigColumn.getColumns().addAll(subColumnX, subColumnY, subColumnZ);
        if (columnTitle.equals("speed") || columnTitle.equals("acceleration")) {
            TableColumn<Atom, Double> module = new TableColumn<>("module");
            module.setMinWidth(COLUMN_WIDTH * 0.5);
            module.setMaxWidth(COLUMN_WIDTH * 0.7);
            module.<Atom, Double>setCellValueFactory(param -> {
                        switch (columnTitle) {
                            case "speed":
                                return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getSpeed().module()));
                            case "acceleration":
                                return new ReadOnlyObjectWrapper<>(MathUtils.round(param.getValue().getAcceleration().module()));
                            default:
                                return null;
                        }
                    }
            );
            bigColumn.getColumns().add(module);
        }
        return bigColumn;
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

    public void onlyMarkedCheckboxAction() {
        showOnlyMarkedAtoms = onlyMarkedCheckbox.isSelected();
        God.ONE.MIND.setShowOnlyMarkedAtoms(showOnlyMarkedAtoms);
        refresh();
//        update();
    }

    public void gravityCheckboxAction() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.IS_GRAVITY_ENABLE = gravityCheckbox.isSelected();
    }

    public void electromagnetismCheckboxAction() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.IS_ELECTROMAGNETISM_ENABLE = electromagnetismCheckbox.isSelected();
    }

    public void rewind() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION *=
                1 + rewindChangingPercent / 100;
    }

    public void backFrame() {
        universe.backFrame();
    }

    public void playOrStop() {
        System.out.println("UniverseControlPanelController.playOrStop: isPaused=" + God.ONE.isPause());
        if (God.ONE.isPause()) {
            playOrStopButton.setText("| |");
            God.ONE.play();
        } else {
            playOrStopButton.setText(">");
            God.ONE.pause();
        }
    }

    public void nextFrame() {
        universe.nextFrame();
    }

    public void flashForward() {
        PhysicsConfigurations.NewtonPhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION *=
                1 - rewindChangingPercent / 100;
    }

    @FXML
    public void showVectorsCheckboxAction() {
        God.ONE.setShowAccelerationVector(showVectorsCheckbox.isSelected());
        God.ONE.setShowSpeedVector(showVectorsCheckbox.isSelected());
    }

    @FXML
    public void changeCursor() {
        God.ONE.MIND.CURSOR_TYPE++;
        God.ONE.MIND.FONT_TYPE++;
        God.ONE.MIND.FONT_TYPE %= God.ONE.MIND.FONT_TYPE_AMOUNT;
        God.ONE.MIND.CURSOR_TYPE %= God.ONE.MIND.CURSOR_TYPES_AMOUNT;
        God.ONE.MIND.CURSOR_TYPE++;
    }

//    @FXML
//    public void clearAllAtoms() {
//        God.ONE.clearAllAtoms();
//    }

    public void setUniverse(AbstractUniverse universe) {
        this.universe = universe;
    }
}
