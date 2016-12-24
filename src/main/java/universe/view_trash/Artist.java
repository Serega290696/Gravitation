package universe.view_trash;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;
import universe.view_trash.enums.ColorEnum;
import universe.view_trash.enums.DrawFigure;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class Artist {
  //    public static double xshift = 0;
//    public static double yshift = 0;
  private final Visualizator visualizator;
  public final static double DEFAULT_XSHIFT = 0;
  public final static double DEFAULT_YSHIFT = 0;
//    private static final double Pi = 180f;

  private static boolean antiAlias = true;
  static Font awtFont1 = new Font("Times New Roman", Font.BOLD, 24);
  static Font awtFont2 = new Font("Times New Roman", Font.BOLD, 30);
  static Font dsCrystal = new Font("DS Crystal", Font.BOLD, 76);
  private static TrueTypeFont font1_1 = new TrueTypeFont(awtFont1, antiAlias);
  private static TrueTypeFont font1_2 = new TrueTypeFont(awtFont2, antiAlias);
  private static TrueTypeFont font2 = new TrueTypeFont(awtFont2, antiAlias);
  private static TrueTypeFont font3 = new TrueTypeFont(dsCrystal, antiAlias);

  private static final String CONTENT_PATH = "content/";
  private static final String TEXTURE_PATH = CONTENT_PATH + "images/";
  private static final String SOUND_PATH = CONTENT_PATH + "music/";
  private static final String FONTS_PATH = CONTENT_PATH + "fonts/";

  public Artist(Visualizator visualizator) {
    this.visualizator = visualizator;
  }

  public static Texture fon1;
  public static Texture fon3;
  private static Audio fonSound1;
//    public static long curTime;

  public void draw(DrawFigure figure, double x, double y, double sx, double sy, double rotate, ColorEnum color, double opacity) {

    glDisable(GL_TEXTURE_2D);
    double a = 10f / visualizator.FPS;
//        if(Game.getInstance().player.position.x < 10) {
//            Game.getInstance().setXshift(-a);
//        }
//        if(Game.getInstance().player.position.x > 90) {
//            Game.getInstance().setXshift(a);
//        }
//        if(Game.getInstance().player.position.y < 10*PrimeCause.ratio) {
//            Game.getInstance().setYshift(-a);
//        }
//        if(Game.getInstance().player.position.y > 90*PrimeCause.ratio) {
//            Game.getInstance().setYshift(a);
//        }
    x -= visualizator.getShift().x;
    y -= visualizator.getShift().y;
    x *= visualizator.em;
    y *= visualizator.em;

    sx *= visualizator.em;
    sy *= visualizator.em;
    switch (figure) {
      case RECT:
        rect(x, y, sx, sy, rotate, color, opacity);
        break;
      case TRIANGLE:
        triangle(x, y, sx, sy, rotate, color, opacity);
        break;
      case CIRCLE:
        circle(x, y, sx, sy, color, opacity);
        break;
      case FON:
        break;
      default:
        break;
    }
  }


  public void draw(DrawFigure figure, double x, double y, double sx, double sy) {
    draw(figure, x, y, sx, sy, 0, ColorEnum.BLACK, 1);
  }

  public void rect(double x, double y, double sx, double sy, double rotate, double opacity) {
    rect(x, y, sx, sy, rotate, 0);
  }

  public void chooseColor(ColorEnum color, double opacity) {
    switch (color) {
      case WHITE:
        glColor4d(0.8f, 0.8f, 0.8f, opacity);
        break;
      case RED:
        glColor4d(1.0f, 0.0f, 0.0f, opacity);
        glColor4d(0.733f, 0.223f, 0.168f, opacity);
        break;
      case GREEN:
        glColor4d(0.478f, 0.737f, 0.345f, opacity);
        break;
      case BLUE:
        glColor4d(0.247f, 0.494f, 1.0f, opacity);
        break;
      case BLACK:
        glColor4d(0f, 0.0f, 0.0f, opacity);
        break;
    }
  }

  public void rect(double x, double y, double sx, double sy, double rotate, ColorEnum color, double opacity) {
    glPushMatrix();
    {
      chooseColor(color, opacity);
      glTranslated(x, y, 0);
      glRotated((float) (-rotate), 0, 0, 1);
      glEnable(GL_POLYGON_SMOOTH);
      glBegin(GL_QUADS);
      {
        glVertex2d(-sx / 2, -sy / 2);
        glVertex2d(-sx / 2, sy / 2);
        glVertex2d(sx / 2, sy / 2);
        glVertex2d(sx / 2, -sy / 2);
      }
      glEnd();
    }
    glPopMatrix();
  }

  public void circle(double x, double y, double sx, double sy, ColorEnum color, double opacity) {
    glPushMatrix();
    {
      chooseColor(color, opacity);
      glTranslated(x, y, 0);
      if (sx < 200) {
        glEnable(GL_POINT_SMOOTH);
        glPointSize((float) sx);
        glBegin(GL_POINTS);
        glVertex2d(0, 0); // ������ �����
        glEnd();
      } else {
        glBegin(GL_POLYGON);
        {
          int amountPoints = (int) (20 * sx);
          for (int i = 0; i <= amountPoints; i++) {
            glVertex2d(sx / 2 * Math.cos((float) i / amountPoints * 2 * Math.PI), sy / 2 * Math.sin((float) i / amountPoints * 2 * Math.PI));
          }
        }
        glEnd();
      }
    }
    glPopMatrix();
  }

  public void triangle(double x, double y, double sx, double sy, double rotate, ColorEnum color, double opacity) {
    glPushMatrix();
    {

      double side = sx;
      chooseColor(color, opacity);
      glTranslated(x, y, 0);
      glRotated(-rotate, 0, 0, 1);
      glEnable(GL_POLYGON_SMOOTH);
      glBegin(GL_TRIANGLES);
      {
        glVertex2d(-sx / 2, -sy / 2);
        glVertex2d(-sx / 2, sy / 2);
        glVertex2d((float) ((Math.pow(3, 0.5f) - 1) / 2 * sx), 0);
      }
      glEnd();
    }
    glPopMatrix();
  }

  public void init() {
//        try {
//            fon1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(TEXTURE_PATH + "fon/fon1.png"));
////            fon3 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(TEXTURE_PATH + "fon/fon2.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        initMusic();
  }
}
