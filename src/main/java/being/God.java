package being;

import being.physics.PhysicsConfigurations;
import being.universe.AbstractUniverse;
import being.universe.UniverseFactory;
import being.universe.UniverseType;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import being.elements.Atom;
import being.exceptions.UniverseCreationException;
import being.mathematics.ThreeVector;
import being.view_trash.Artist;
import being.view_trash.UniverseControlPanel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

public enum God {
    ONE;

    public final GodMind MIND;
    private final Set<AbstractUniverse> universes;

    public boolean pause = false;

    private boolean godsWrath;
    private boolean oblivion;

    private static final boolean SHOW_CONTROL_PANEL = true;
    private static final boolean SHOW_VISUALISATION = true;

    God() {
        System.out.println("God.God");
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
        godsWrath = true;
        universes.clear();
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
        return pause;
    }

    public void play() {
        universes.forEach(AbstractUniverse::resume);
        pause = false;
    }

    public void pause() {
        universes.forEach(AbstractUniverse::pause);
        pause = true;
    }

    public class GodMind {
        private AbstractUniverse chosenUniverse;

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


        private GodMind() {
            if (universes.size() > 0) {
                updateChosenUniverse();
                Set<Object> oo = chosenUniverse.getObjects();
                this.objects.addAll(oo.stream().filter(o -> o instanceof Atom)
                        .map(o -> (Atom) o).collect(Collectors.toList()));
            }
            shift = new ThreeVector(0, 0, 0);
            title = "God view";
            DELAY = (int) (PhysicsConfigurations.MOMENT_SIZE * 1000);
            FPS = (int) (1000 / DELAY);
            ratio = 1.0f;
            D_WIDTH = 800;
            D_HEIGHT = 800;
            DEFAULT_EM = D_WIDTH / 100;
            em = DEFAULT_EM;
            speedFactor = 1.5;
        }

        //show control window
        public void bindToUserMind() {
            updateChosenUniverse();
//            if (!controlPanelIsShown) {
            Thread controlPanelThread = null;

            if (SHOW_CONTROL_PANEL && !controlPanelIsShown) {
                controlPanelThread = new Thread(() -> {
                    UniverseControlPanel.setUniverse(chosenUniverse);
//                    if (!controlPanelIsShown) {
                    UniverseControlPanel.launchControlPanel();
//                    }
                });
                controlPanelThread.setDaemon(true);
                controlPanelThread.start();
                controlPanelIsShown = true;
            }
//            controlPanelIsShown = true;
//            }
            if (SHOW_VISUALISATION) {
                System.out.println("simulationPanelIsLoaded = " + simulationPanelIsLoaded);
                if (!simulationPanelIsLoaded) {
                    System.out.println("Init GL");
                    initDisplay();
                    initGL();
                    artist = new Artist(this);
                    artist.init();
                    simulationPanelIsLoaded = true;
                }
                System.out.println("in");
                if (!simulationPanelIsShown) {
                    System.out.println("Stat vis loop");
//            new Thread(() -> {
                    visualizationLoop();
                    if (controlPanelThread != null && controlPanelThread.isAlive()) {
                        controlPanelThread.interrupt();
                    }
                    System.out.println("Clean up");
                    cleanUp();
                    simulationPanelIsLoaded = false;
//            }).start();
                }
            }
        }

        private void updateChosenUniverse() {
            chosenUniverse = universes.stream().findFirst().get();
        }


        private void visualizationLoop() {
            while (!God.ONE.isGodsWrath() && !Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//      getInput();
//      if (!.isPaused()) {
                render();
//      }
                Display.update();
                Display.sync(FPS);
                if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
                    chosenUniverse.pause();
                    System.out.println("PAUSED");
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
                    chosenUniverse.resume();
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
                    eatAppleOfKnowledge();
                }
            }
            God.ONE.eatAppleOfKnowledge();
            God.ONE.causeToForget();
            simulationPanelIsLoaded = false;
            System.out.println("END_END");
        }

        private void initDisplay() {

            try {
                Display.setDisplayMode(new DisplayMode(D_WIDTH, D_HEIGHT));
                Display.create();
                Display.setVSyncEnabled(true);
                Display.setTitle(title);

                Keyboard.create();
                Mouse.create();
                Mouse.setGrabbed(false);
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
            Set<Object> objectsSet = chosenUniverse.getObjects();
            renderedObjects.addAll(objectsSet.stream().filter(o -> o instanceof Atom)
                    .map(o -> (Atom) o).collect(Collectors.toList()));
            if (renderedObjects != null && renderedObjects.size() > 0) {
                for (Atom atom : renderedObjects) {
                    artist.draw(atom.getFigure(), atom.getPosition().x, atom.getPosition().y, atom.getSize().x, atom.getSize().y,
                            atom.getRotation(), atom.getColor(), atom.getOpacity());
                }
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
    }
}
