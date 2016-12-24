package universe.view_trash;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import universe.Atom;
import universe.God;
import universe.Universe;
import universe.UniverseConfigurations;
import universe.mathematics.ThreeVector;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

public class Visualizator {
  private final String gameTitle;
  public final int DELAY;
  public final int FPS;
  public final float ratio;
  private final int D_WIDTH;
  private final int D_HEIGHT;
  public final float DEFAULT_EM;
  private final Universe universe;
  private final Artist artist;

  private List<Atom> atoms;
  public float em;
  private ThreeVector shift;
  public double speedFactor;

  public Visualizator(Universe universe) {
    this.universe = universe;
    atoms = universe.getSpacetime().getAtoms();
    shift = new ThreeVector(0, 0, 0);
    gameTitle = "Game";
    DELAY = (int) (UniverseConfigurations.MOMENT_SIZE * 1000);
    FPS = (int) (1000 / DELAY);
    ratio = 1.0f;
    D_WIDTH = 800;
    D_HEIGHT = 800;
    DEFAULT_EM = D_WIDTH / 100;
    em = DEFAULT_EM;
    speedFactor = 1.5;
    initDisplay();
    initGL();
    artist = new Artist(this);
  }


  public void visualize() {
    Thread controlPanelThread = new Thread(() -> {
      UniverseControlPanel.setUniverse(universe);
      UniverseControlPanel.launchControlPanel();
    });
    controlPanelThread.setDaemon(true);
    controlPanelThread.start();
    artist.init();
    visualizationLoop();
    cleanUp();
  }


  private void visualizationLoop() {
    while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//      getInput();
//      if (!.isPaused()) {
      render();
//      }
      Display.update();
      Display.sync(FPS);
      if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
//        restartGame();
      }
    }
    God.ONE.visualizatingFinishNotify();
    God.ONE.oblivion();
  }

  private void initDisplay() {

    try {
      Display.setDisplayMode(new DisplayMode(D_WIDTH, D_HEIGHT));
      Display.create();
      Display.setVSyncEnabled(true);
      Display.setTitle(gameTitle);

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

    for (Atom atom : atoms) {
      artist.draw(atom.getFigure(), atom.getPosition().x, atom.getPosition().y, atom.getSize().x, atom.getSize().y,
          atom.getRotation(), atom.getColor(), atom.getOpacity());
    }
//    Draw.draw(figure, position.x, position.y, size.x, size.y, rotation, color, opacity);

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
