package being;

import being.elements.Atom;
import being.elements.Event;
import being.exceptions.UniverseCreationException;
import being.mathematics.ThreeVector;
import being.physics.physics_impls.NewtonPhysics;
import being.physics.PhysicsConfigurations;
import being.universe.*;
import being.view.Artist;
import being.view.UniverseControlPanel;
import being.view.enums.ColorEnum;
import being.view.enums.DrawFigureType;
import being.view.enums.ActionType;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public enum God {
    ONE;

    public final GodMind MIND;
    private final Set<AbstractUniverse> universes;

//    public boolean pause = false;

    private boolean godsWrath;
    private boolean oblivion;

    private static final boolean SHOW_CONTROL_PANEL = true;
    private static final boolean SHOW_VISUALISATION = true;

    private static int maxShownEventsPerAtom = PhysicsConfigurations.NewtonPhysicsConfigurations.MAX_EVENTS_AMOUNT_PER_ATOM / 1;
    private boolean showSpeedVector = true;
    private boolean showAccelerationVector = true;
    private boolean rocketMode = false;

    God() {
        godsWrath = false;
        oblivion = false;

        universes = new HashSet<>();
        MIND = new GodMind();
    }

    public boolean isGodsWrath() {
        return godsWrath;
    }

    public void eatAppleOfKnowledge() {
        System.out.println("WRATH");
//        godsWrath = true;
        universes.forEach(u -> u.setRestart(true));
//        universes.forEach(AbstractUniverse::bigBang);
//        universes.clear();
    }

    //Say "Let there be light"
    public AbstractUniverse sayLetThereBeLight(UniverseType type) throws UniverseCreationException {
        godsWrath = false;
        AbstractUniverse universe = UniverseFactory.create(type);
        universes.add(universe);
        return universe;
    }

    //предать забвению
    public void causeToForget() {
        oblivion = true;
    }

    public boolean isOblivion() {
        return oblivion;
    }

    public void resumeAllUniverse() {
        universes.forEach(AbstractUniverse::resume);
    }

    public void stopAllUniverse() {
        universes.forEach(AbstractUniverse::pause);
    }

    public boolean isPause() {
        return universes.iterator().next().isPaused();
    }

    public void play() {
        universes.forEach(AbstractUniverse::resume);
    }

    public void pause() {
        universes.forEach(AbstractUniverse::pause);
    }

    public static int getMaxShownEventsPerAtom() {
        return maxShownEventsPerAtom;
    }

    public static void setMaxShownEventsPerAtom(int maxShownEventsPerAtom) {
        God.maxShownEventsPerAtom = maxShownEventsPerAtom;
    }

    public boolean isShowSpeedVector() {
        return showSpeedVector;
    }

    public void setShowSpeedVector(boolean showSpeedVector) {
        this.showSpeedVector = showSpeedVector;
    }

    public boolean isShowAccelerationVector() {
        return showAccelerationVector;
    }

    public void setShowAccelerationVector(boolean showAccelerationVector) {
        this.showAccelerationVector = showAccelerationVector;
    }

    public void clearAllAtoms() {
        universes.forEach(AbstractUniverse::clearAllAtoms);
    }

    public void restoreState(String stateTitle) {
        universes.forEach(u -> u.restoreState(stateTitle));
    }

    public void saveState(String stateTitle) {
        universes.forEach(u -> u.saveState(stateTitle));
    }

    public boolean getRocketMode() {
        return rocketMode;
    }

    public void setRocketMode(boolean rocketMode) {
        this.rocketMode = rocketMode;
    }

    public class GodMind {
        private static final double ZOOM_CHANGING_PER_SECOND = 10;
        private static final float CURSOR_HEIGHT = 30;
        private static final float CURSOR_WIDTH = 20;
        public int FONT_TYPE = 1;
        public int FONT_TYPE_AMOUNT = 5;
        public int CURSOR_TYPE = 3;
        public int CURSOR_TYPES_AMOUNT = 3;
        private ThreeVector lastMousePosition = new ThreeVector(Mouse.getX(), Mouse.getY(), 0);

        private AbstractUniverse<NewtonPhysics, Atom> chosenUniverse;

        private final String title;
        public final int DELAY;
        public final int FPS;
        public final float ratio;
        private final int D_WIDTH;
        private final int D_HEIGHT;
        public final float DEFAULT_EM;
        private Artist artist;

        private List<Atom> objects;
        public float em;
        private ThreeVector shift;
        public double speedFactor;
        private boolean simulationPanelIsShown = false;
        private boolean controlPanelIsShown = false;
        private boolean simulationPanelIsLoaded = false;
        private boolean controlPanelIsLoaded = false;

        private int leftMouseDownCounter = 0;
        private int rightMouseDownCounter = 0;
        private boolean mouseGrabbed = false; //todo
        private boolean showOnlyMarkedAtoms;
        private List<KeyboardEvent> keyboardEvents = new ArrayList<>();
        private double zoomDecreasingSpeed = 0;
        private Set<Atom> objectsSet;
        private boolean drawNewAtom = true;

        private GodMind() {
            if (universes.size() > 0) {
                updateChosenUniverse();
                Set<Atom> oo = chosenUniverse.getObjects();
                this.objects.addAll(oo.stream().collect(Collectors.toList()));
            }
            shift = new ThreeVector(0, 0, 0);
            title = "God view";
            DELAY = (int) (0.03 * 1000);
            FPS = (int) (1000 / DELAY);
            ratio = 1.0f;
            D_WIDTH = 800;
            D_HEIGHT = 800;
            DEFAULT_EM = D_WIDTH / 100;
            em = DEFAULT_EM / 1;
//            em = DEFAULT_EM/ 1_000;
            speedFactor = 1.5;
        }

        //show control window
        public void bindToUserMind() {
            updateChosenUniverse();
            Thread controlPanelThread = null;

            if (SHOW_CONTROL_PANEL && !controlPanelIsShown) {
                controlPanelThread = new Thread(() -> {
                    UniverseControlPanel.setUniverse(chosenUniverse);
                    UniverseControlPanel.launchControlPanel();
                });
                controlPanelThread.setDaemon(false);
                controlPanelThread.start();
                controlPanelIsShown = true;
            }
            if (SHOW_VISUALISATION) {
                if (!simulationPanelIsLoaded) {
                    initDisplay();
                    initGL();
                    artist = new Artist(this);
                    artist.init();
                    simulationPanelIsLoaded = true;
                }
                if (!simulationPanelIsShown) {
                    visualizationLoop();
                    if (controlPanelThread != null && controlPanelThread.isAlive()) {
                        controlPanelThread.interrupt();
                    }
                    cleanUp();
                    simulationPanelIsLoaded = false;
                }
            }
        }

        private void updateChosenUniverse() {
            chosenUniverse = universes.stream().findFirst().get();
        }


        private void visualizationLoop() {
            createKeyboardEvent(() -> {
                if (chosenUniverse.isPaused()) chosenUniverse.resume();
                else chosenUniverse.pause();
            }, ActionType.CLICKED, Keyboard.KEY_P);
            createKeyboardEvent(() -> shift.y -= 3, ActionType.PRESSED, Keyboard.KEY_UP);
            createKeyboardEvent(() -> shift.y += 3, ActionType.PRESSED, Keyboard.KEY_DOWN);
            createKeyboardEvent(() -> shift.x -= 3, ActionType.PRESSED, Keyboard.KEY_LEFT);
            createKeyboardEvent(() -> shift.x += 3, ActionType.PRESSED, Keyboard.KEY_RIGHT);
            createKeyboardEvent(God.this::eatAppleOfKnowledge, ActionType.CLICKED, Keyboard.KEY_R);
            createKeyboardEvent(God.this::clearAllAtoms, ActionType.CLICKED, Keyboard.KEY_C);
            createKeyboardEvent(() -> {
                universes.forEach(u -> u.mouseClick(
                        Mouse.getX() / em + shift.x,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                        (double) 5,
                        0, true));
            }, ActionType.PRESSED_50_MILLISECONDS, Keyboard.KEY_A);
            createKeyboardEvent(() -> {
                universes.forEach(u -> u.mouseClick(
                        Mouse.getX() / em + shift.x,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                        (double) 20,
                        0, true));
            }, ActionType.PRESSED_50_MILLISECONDS, Keyboard.KEY_LSHIFT, Keyboard.KEY_A);
            objectsSet = chosenUniverse.getObjects();
            while (!God.ONE.isGodsWrath() && !Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                render();
                Display.update();
                Display.sync(FPS);
                updateKeyboardEventsType();
                listenKeyboardEvent();
                // Mouse clicks
                drawNewAtom = true;
                if (Mouse.isButtonDown(0)) {
//                    if (leftMouseDownCounter == 0) {
                    drawNewAtom = chosenUniverse.mousePush(
                            Mouse.getX() / em + shift.x,
                            100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                            leftMouseDownCounter,
                            0, false);
//                    }
                    leftMouseDownCounter++;
                } else if (leftMouseDownCounter > 0) {
                    universes.forEach(u -> u.mouseClick(
                            Mouse.getX() / em + shift.x,
                            100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                            leftMouseDownCounter,
                            0, false));
                    leftMouseDownCounter = 0;
                }
                if (Mouse.isButtonDown(1)) {
                    rightMouseDownCounter++;
                } else if (rightMouseDownCounter > 0) {
                    universes.forEach(u -> u.mouseClick(
                            Mouse.getX() / em + shift.x,
                            100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                            (double) rightMouseDownCounter,
                            1, false));
                    rightMouseDownCounter = 0;
                }
                // MOVING
                if (Mouse.isButtonDown(2)) {
                    shift.x += (lastMousePosition.x - Mouse.getX()) / em * 1;
                    shift.y -= (lastMousePosition.y - Mouse.getY()) / em * 1;
                }
                // ZOOM
                int dWheel = Mouse.getDWheel();
                if (dWheel > 0) {
                    if (zoomDecreasingSpeed < 1) {
                        zoomDecreasingSpeed += 10d / FPS;
                    }
                } else if (dWheel < 0) {
                    if (zoomDecreasingSpeed > -1) {
                        zoomDecreasingSpeed -= 10d / FPS;
                    }
                } else {
                    if (Math.abs(zoomDecreasingSpeed) <= 0.1) {
                        zoomDecreasingSpeed = 0;
                    } else {
                        zoomDecreasingSpeed -= 10d / FPS / 2 * Math.signum(zoomDecreasingSpeed);
                    }
                }
                if (zoomDecreasingSpeed < 0) {
                    float updatedEm = (float) (em / (1 + (ZOOM_CHANGING_PER_SECOND - 1) / FPS * (-zoomDecreasingSpeed)));
                    shift.x += (D_WIDTH / em - D_WIDTH / updatedEm) * (1 - (double) Mouse.getX() / D_WIDTH);
                    shift.y += (D_HEIGHT / em - D_HEIGHT / updatedEm) * ((double) Mouse.getY() / D_HEIGHT);
                    em = updatedEm;
                } else if (zoomDecreasingSpeed > 0) {
                    if (zoomDecreasingSpeed < 1) {
                        zoomDecreasingSpeed += 1d / FPS;
                    }
                    float updatedEm = (float) (em * (1 + (ZOOM_CHANGING_PER_SECOND - 1) / FPS * zoomDecreasingSpeed));
                    shift.x += (D_WIDTH / em - D_WIDTH / updatedEm) * ((double) Mouse.getX() / D_WIDTH);
                    shift.y += (D_HEIGHT / em - D_HEIGHT / updatedEm) * (1 - (double) Mouse.getY() / D_HEIGHT);
                    em = updatedEm;
                }
                universes.forEach(u -> u.setMousePosition(((double) Mouse.getX() / D_WIDTH / em * DEFAULT_EM * 100 + shift.x),
                        ((double) (D_HEIGHT - Mouse.getY()) / D_HEIGHT * 100 / em * DEFAULT_EM + shift.y)));
                Atom focusedAtom = chosenUniverse.getFocusedAtom();
                if (focusedAtom != null) {
                    shift.x = ((Atom) focusedAtom).getPosition().x - 50 / em * DEFAULT_EM;
                    shift.y = ((Atom) focusedAtom).getPosition().y - 50 / em * DEFAULT_EM;
                    shift.z = ((Atom) focusedAtom).getPosition().z - 50 / em * DEFAULT_EM;
                }
                lastMousePosition.x = Mouse.getX();
                lastMousePosition.y = Mouse.getY();
            }
            God.ONE.eatAppleOfKnowledge();
            God.ONE.causeToForget();
            simulationPanelIsLoaded = false;
            System.out.println("END_END");
        }

        private void updateKeyboardEventsType() {
            for (KeyboardEvent event : keyboardEvents) {
                boolean keysPressed = true;
                for (int key : event.getKeys()) {
//                    System.out.println("key = " + key);
                    if (!Keyboard.isKeyDown(key)) {
                        keysPressed = false;
                    }
                }
                if (Arrays.stream(event.getKeys()).noneMatch(k -> k == Keyboard.KEY_LSHIFT)
                        && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    keysPressed = false;
                }
                if (Arrays.stream(event.getKeys()).noneMatch(k -> k == Keyboard.KEY_LCONTROL)
                        && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    keysPressed = false;
                }
                if ((event.getCurrentActionType() & ActionType.NO_ACTION) > 0 && keysPressed) {
                    event.setCurrentActionType(ActionType.CLICKED);
                } else if ((event.getCurrentActionType() & ActionType.CLICKED) > 0 && keysPressed) {
                    event.setCurrentActionType(ActionType.PRESSED);
                }
                if ((event.getCurrentActionType() & ActionType.PRESSED) > 0 && !keysPressed) {
                    event.setCurrentActionType(ActionType.RELEASED);
                }
                if ((event.getCurrentActionType() & ActionType.PRESSED) > 0 && keysPressed
                        && System.currentTimeMillis() - event.getLastActionTime() >= 10) {
                    event.setCurrentActionType(ActionType.PRESSED_50_MILLISECONDS);
                }
                if ((event.getCurrentActionType() & ActionType.PRESSED) > 0 && keysPressed
                        && System.currentTimeMillis() - event.getLastActionTime() >= 200) {
                    event.setCurrentActionType(ActionType.PRESSED_200_MILLISECONDS);
                }
                if ((event.getCurrentActionType() & ActionType.PRESSED) > 0 && keysPressed
                        && System.currentTimeMillis() - event.getLastActionTime() >= 1000) {
                    event.setCurrentActionType(ActionType.PRESSED_ONE_SECOND);
                }
                if ((event.getCurrentActionType() & ActionType.PRESSED) > 0 && keysPressed
                        && System.currentTimeMillis() - event.getLastActionTime() >= 2000) {
                    event.setCurrentActionType(ActionType.PRESSED_TWO_SECONDS);
                }
                if ((event.getCurrentActionType() & ActionType.RELEASED) > 0 && keysPressed) {
                    event.setCurrentActionType(ActionType.CLICKED);
                }
                if ((event.getCurrentActionType() & ActionType.RELEASED) > 0 && !keysPressed) {
                    event.setCurrentActionType(ActionType.NO_ACTION);
                }
            }
        }

        private void createKeyboardEvent(Runnable r, int type, int... triggerKey) {
            KeyboardEvent event = new KeyboardEvent(r, type, triggerKey);
            keyboardEvents.add(event);
        }

        private void listenKeyboardEvent() {
            for (KeyboardEvent event : keyboardEvents) {
                if (Arrays.stream(event.getKeys()).allMatch(Keyboard::isKeyDown)
                        && event.getTriggerActionType() == event.getCurrentActionType()) {
                    event.run();
                }
            }
        }

        private void initDisplay() {

            try {
                Display.setLocation(50, 20);
                Display.setDisplayMode(new DisplayMode(D_WIDTH, D_HEIGHT));
                Display.create();
                Display.setVSyncEnabled(true);
                Display.setTitle(title);
                Keyboard.create();
                Mouse.create();
                Mouse.setGrabbed(mouseGrabbed);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }

        }

//  public static void mouseGrabbed(boolean mouseGrabbed) {
//    Mouse.setGrabbed(mouseGrabbed);
//  }

        private void initGL() {
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glClearDepth(1);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glViewport(0, 0, D_WIDTH, D_HEIGHT);
            glMatrixMode(GL_MODELVIEW);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            GL11.glOrtho(0, D_WIDTH, D_HEIGHT, 0, D_WIDTH, -D_WIDTH);
            glMatrixMode(GL_MODELVIEW);
            glClearColor(0.0f, 0.0f, 0.002f, 1);
        }

        private void render() {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            Set<Atom> renderedObjects = new HashSet<>();
//            objectsSet = chosenUniverse.getObjects();
//            while(chosenUniverse.getObjectProcessingStage() != ObjectProcessingStage.DISPLAYING);
//            while (!chosenUniverse.areObjectsAvailable()) ;
//            chosenUniverse.setObjectsAvailable(false);
//            renderedObjects.addAll(objectsSet);
//            chosenUniverse.setObjectProcessingStage(ObjectProcessingStage.DISTRIBUTION);
//            chosenUniverse.setObjectsAvailable(true);
            renderedObjects = objectsSet;
            if (renderedObjects.size() > 0) {
                for (Atom atom : renderedObjects) {
                    if (!atom.isHidden() && (!showOnlyMarkedAtoms || atom.isMarkedByGod())) {
                        synchronized (atom.getBindEvents()) {
                            Event event;
                            for (int i = atom.getBindEvents().size() - 1;
                                 i >= 0 ;//&& atom.getBindEvents().size() - maxShownEventsPerAtom < i;
                                 i--) {
                                event = atom.getBindEvents().get(i);
                                artist.draw(event.getFigure(), event.getPosition().x, event.getPosition().y,
                                        event.getSize().x, event.getSize().y,
                                        event.getRotation(), event.getColor(), event.getOpacity() / 2
                                );
                            }
                        }
                    }
                }
                for (Atom atom : renderedObjects) {
                    if (!atom.isHidden() && (!showOnlyMarkedAtoms || atom.isMarkedByGod())
                            && atom.inVisibilityZone(shift)) {
                        artist.draw(
                                PhysicsConfigurations.NewtonPhysicsConfigurations.DRAW_SIMPLE_SHAPES
                                        ? DrawFigureType.CIRCLE
                                        : atom.getFigure(),
                                atom.getPosition().x, atom.getPosition().y,
                                atom.getSize().x, atom.getSize().y,
                                atom.getRotation(), atom.getColor(), atom.getOpacity() / atom.getPosition().z);
                        if (isShowSpeedVector() && chosenUniverse.getStage() == UniverseStage.PAUSE) {
                            artist.writeText(
                                    atom.getName() + "(" + atom.getSerialNumber() + ")",
                                    atom.getPosition().x,
                                    atom.getPosition().y,
                                    ColorEnum.GREEN
                            );
                        }
                        double speedX = atom.getSpeed().x;
                        double speedY = atom.getSpeed().y;
                        double speed = atom.getSpeed().module();
                        double accelerationX = atom.getAcceleration().x;
                        double accelerationY = atom.getAcceleration().y;
                        double acceleration = atom.getAcceleration().module();
                        double angle = -Math.atan(speedY / speedX);
                        if (showSpeedVector) {
                            artist.draw(
                                    DrawFigureType.TRIANGLE,
                                    atom.getPosition().x -
                                            (Math.cos(angle + (Math.PI / 2 * (1 - Math.signum(-speedX))))
                                                    * atom.getSpeed().module() / 2),
                                    atom.getPosition().y +
                                            (Math.sin(angle + (Math.PI / 2 * (1 - Math.signum(-speedX))))
                                                    * atom.getSpeed().module() / 2),
                                    speed, atom.getSize().x / 1.5,
                                    (180 / (Math.PI) * angle + 180) + (90 - 90 * Math.signum(-speedX)),
                                    atom.getColor(), 0.3);
                        }
                        angle = -Math.atan(accelerationY / accelerationX);
                        if (showAccelerationVector) {
                            artist.draw(
                                    DrawFigureType.TRIANGLE,
                                    atom.getPosition().x -
                                            (Math.cos(angle + (Math.PI / 2 * (1 - Math.signum(-accelerationX))))
                                                    * acceleration / 2),
                                    atom.getPosition().y +
                                            (Math.sin(angle + (Math.PI / 2 * (1 - Math.signum(-accelerationX))))
                                                    * acceleration / 2),
                                    acceleration, atom.getSize().x / 5,
                                    (180 / (Math.PI) * angle + 180) + (90 - 90 * Math.signum(-accelerationX)),
                                    ColorEnum.BLUE, 0.3);
                        }
                    }
                }
            }

            if (rightMouseDownCounter == 0 && leftMouseDownCounter == 0) {
                artist.drawCursor(CURSOR_TYPE, CURSOR_WIDTH, CURSOR_HEIGHT, em, DEFAULT_EM, shift);
            } else if (drawNewAtom) {
                double cursorSize = Math.max(rightMouseDownCounter, leftMouseDownCounter);
                cursorSize = cursorSize == 0 ? 5 : cursorSize;
                artist.draw(DrawFigureType.CIRCLE, Mouse.getX() / em + shift.x, 100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
//                    0,
                        Math.pow(cursorSize / 10, 2d) * 2 / em * DEFAULT_EM,
//                        Math.pow(3d / 4d * Math.pow(cursorSize, 2d) / Math.PI, 1d / 3d) / em * DEFAULT_EM,
//                    0,
                        Math.pow(cursorSize / 10, 2d) * 2 / em * DEFAULT_EM,
                        0, ColorEnum.BLUE, 1);
            }
        }

        private void cleanUp() {
            Display.destroy();
            Keyboard.destroy();
            Mouse.destroy();
        }

        public ThreeVector getShift() {
            return shift;
        }

        public void setShift(ThreeVector shift) {
            this.shift = shift;
        }

        public void setShowOnlyMarkedAtoms(boolean showOnlyMarkedAtoms) {
            this.showOnlyMarkedAtoms = showOnlyMarkedAtoms;
        }
    }
}
