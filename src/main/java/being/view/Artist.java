package being.view;

import being.God;
import being.mathematics.ThreeVector;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;
import being.view.enums.ColorEnum;
import being.view.enums.DrawFigureType;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

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
    private final God.GodMind godMind;
    public final static double DEFAULT_XSHIFT = 0;
    public final static double DEFAULT_YSHIFT = 0;
//    private static final double Pi = 180f;

    private static boolean antiAlias = true;

    private static final String CONTENT_PATH = "content/";
    private static final String TEXTURE_PATH = CONTENT_PATH + "images/";
    private static final String SOUND_PATH = CONTENT_PATH + "music/";
    private static final String FONTS_PATH = CONTENT_PATH + "fonts/";
    private Font timesNewRomanFont = new Font("Times New Roman", Font.BOLD, 24);
    private Font dsCrystal = new Font("DS Crystal", Font.BOLD, 24);
    //    private Font customFont1;
//    private Font customFont2;
//    private Font customFont3;
//    private TrueTypeFont[] fonts;
//    private TrueTypeFont font;
//    private TrueTypeFont font2;
//    private TrueTypeFont font3;
//    private TrueTypeFont font4;
//    private TrueTypeFont font5;
    private TrueTypeFont chosenFont;
    private Font[] fontsArray = new Font[God.ONE.MIND.FONT_TYPE_AMOUNT];

    public Artist(God.GodMind godMind) {
        this.godMind = godMind;
    }

    public static Texture fon1;
    public static Texture fon3;
    private static Audio fonSound1;
//    public static long curTime;

    public void draw(DrawFigureType figure, double x, double y, double sx, double sy, double rotate, ColorEnum color, double opacity) {

        glDisable(GL_TEXTURE_2D);
        double a = 10f / godMind.FPS;
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
//        x -= godMind.getShift().x;
//        y -= godMind.getShift().y;
//        x *= godMind.em;
//        y *= godMind.em;

//        sx *= godMind.em;
//        sy *= godMind.em;
        x = coordinateToPixels(x, godMind.getShift().x);
        y = coordinateToPixels(y, godMind.getShift().y);
        sx = sizeToPixels(sx);
        sy = sizeToPixels(sy);
        switch (figure) {
            case RECT:
                rect(x, y, sx, sy, rotate, color, opacity);
                break;
            case TRIANGLE:
                triangle(x, y, sx, sy, rotate, color, opacity);
                break;
            case CIRCLE:
                circle(x, y, sx, sy, rotate, color, opacity);
                break;
            case STAR:
                star(x, y, sx, sy, rotate, color, opacity);
                break;
            case FON:
                break;
            default:
                break;
        }
    }

    private double coordinateToPixels(double coord, double shift) {
        coord -= shift;
        coord *= godMind.em;
        return coord;
    }

    private double sizeToPixels(double size) {
        size *= godMind.em;
        return size;
    }


    public void draw(DrawFigureType figure, double x, double y, double sx, double sy) {
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

    public void circle(double x, double y, double sx, double sy, double rotation, ColorEnum color, double opacity) {
        glPushMatrix();
        {
            chooseColor(color, opacity);
            glTranslated(x, y, 0);
            if (sx < 200) {
                if (sx == 0) {
                    sx = 1;
                }
                if (sy == 0) {
                    sy = 1;
                }
                glEnable(GL_POINT_SMOOTH);
                glPointSize((float) sx);
                glBegin(GL_POINTS);
                glVertex2d(0, 0); // ������ �����
                glEnd();
            } else {
                glBegin(GL_POLYGON);
                {
                    int amountPoints = (int) (0.1 * sx);
                    for (int i = 0; i <= amountPoints; i++) {
                        glVertex2d(sx / 2 * Math.cos((float) i / amountPoints * 2 * Math.PI), sy / 2 * Math.sin((float) i / amountPoints * 2 * Math.PI));
                    }
                }
                glEnd();
            }
        }
        glPopMatrix();
    }

    public void star(double x, double y, double sx, double sy, double rotation, ColorEnum color, double opacity) {
        glPushMatrix();
        {
            chooseColor(color, opacity);
            glTranslated(x, y, 0);
            glRotated(-rotation, 0, 0, 1);
            if (sx < 8) {
                if (sx == 0) {
                    sx = 1;
                }
                glEnable(GL_POINT_SMOOTH);
                glPointSize((float) sx);
                glBegin(GL_POINTS);
                glVertex2d(0, 0);
                glEnd();
            } else {
                glBegin(GL_POLYGON);
                {
                    int amountPoints = 18;
//                int amountPoints = (int) (0.1 * sx);
                    for (int i = 0; i <= amountPoints; i++) {
                        double factor = 1;
                        if (i % 2 == 0) {
                            factor = (0.5 + 0.0 * Math.random());
                        }
                        glVertex2d(factor * sx / 2 * Math.cos((float) i / amountPoints * 2 * Math.PI), factor * sy / 2 * Math.sin((float) i / amountPoints * 2 * Math.PI));
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

    private void beginComplexFigure(double x, double y, double rotate, ColorEnum color, double opacity) {
        glPushMatrix();
        chooseColor(color, opacity);
        glTranslated(x, y, 0);
        glRotated(-rotate, 0, 0, 1);
    }

    private void commitComplexFigure() {
        glPopMatrix();
    }


    public void init() {
        loadFonts();
//        try {
//            fon1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(TEXTURE_PATH + "fon/fon1.png"));
////            fon3 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(TEXTURE_PATH + "fon/fon2.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        initMusic();
    }

    public void writeText(String text, double x, double y, ColorEnum color) {
        x = coordinateToPixels(x, godMind.getShift().x);
        y = coordinateToPixels(y, godMind.getShift().y);
        org.newdawn.slick.Color slickColor;
        switch (color) {
            case BLACK:
                slickColor = Color.black;
                break;
            case BLUE:
                slickColor = Color.blue;
                break;
            case RED:
                slickColor = Color.red;
                break;
            case WHITE:
                slickColor = Color.white;
                break;
            case GREEN:
                slickColor = new org.newdawn.slick.Color(0.1f, 0.6f, 0.1f, 1.0f);
                break;
            default:
                slickColor = org.newdawn.slick.Color.green;
        }
//        chooseColor(color, 1);
        chosenFont = new TrueTypeFont(fontsArray[God.ONE.MIND.FONT_TYPE], false);
//        switch (fontType) {
//            case 1:
//                chosenFont = new TrueTypeFont(timesNewRomanFont, false);
//                break;
//            case 2:
//                chosenFont = new TrueTypeFont(dsCrystal, false);
//                break;
//            case 3:
//                chosenFont = new TrueTypeFont(customFont1, false);
//                break;
//            case 4:
//                chosenFont = new TrueTypeFont(customFont2, false);
//                break;
//            case 5:
//                chosenFont = new TrueTypeFont(customFont3, false);
//                break;
//            default:
//                chosenFont = new TrueTypeFont(timesNewRomanFont, false);
//        }
//        font = new TrueTypeFont(timesNewRomanFont, false);
//        font2 = new TrueTypeFont(dsCrystal, false);
//        font3 = new TrueTypeFont(customFont1, false);
//        font4 = new TrueTypeFont(customFont2, false);
//        font5 = new TrueTypeFont(customFont3, false);
        if (chosenFont != null) {
            chosenFont.drawString((float) (x - (chosenFont.getWidth(text) / 2)),
                    (float) (y - chosenFont.getHeight(text) / 2),
                    text,
                    slickColor);
        }
    }

    public void loadFonts() {
        fontsArray[0] = timesNewRomanFont.deriveFont(24f);
        fontsArray[1] = dsCrystal.deriveFont(24f);
        int fontCounter = 2;
        String fontFileName1 = "src\\main\\resources\\fonts\\neuro.ttf";
        String fontFileName2 = "src\\main\\resources\\fonts\\crom_v1.ttf";
        String fontFileName3 = "src\\main\\resources\\fonts\\hemi_head.ttf";
        String[] fontsFileName = new String[]{fontFileName1, fontFileName2, fontFileName3};
        for (String fileName : fontsFileName) {
            if (fontCounter >= God.ONE.MIND.FONT_TYPE_AMOUNT) {
                break;
            }
            File file1 = new File(fileName);
            if (file1.exists() && !file1.isDirectory()) {
                InputStream inputStream = ResourceLoader.getResourceAsStream(fileName);
                fontsArray[fontCounter] = null;
                try {
                    fontsArray[fontCounter] = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                    fontsArray[fontCounter] = fontsArray[fontCounter].deriveFont(24f); // set font size
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }
            }
            fontCounter++;

        }
    }

    public void drawCursor(int cursorType, float cursorWidth, float cursorHeight, float em, float DEFAULT_EM, ThreeVector shift) {

        switch (cursorType) {
            case 1:
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x + cursorWidth / em / 3,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y + cursorHeight / em / 3,
                        cursorHeight / em, cursorWidth / em, 115, ColorEnum.BLUE, 1);
                break;
            case 2:
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                        cursorHeight / em, cursorWidth / em, 0, ColorEnum.BLUE, 1);
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                        cursorHeight / em, cursorWidth / em, 90, ColorEnum.BLUE, 1);
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                        cursorHeight / em, cursorWidth / em, 180, ColorEnum.BLUE, 1);
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y,
                        cursorHeight / em, cursorWidth / em, -90, ColorEnum.BLUE, 1);
                break;
            case 3:
                double animationCycleMilliseconds = 1000;
                double cursorAnimationPhase = animationCycleMilliseconds - System.currentTimeMillis() % animationCycleMilliseconds;
                double maxCursorShift = 3;
                double cursorShiftX = 1.2 + (maxCursorShift - 1.2) * Math.abs(1 - cursorAnimationPhase / animationCycleMilliseconds * 2);
                double cursorShiftY = 1.6 + (maxCursorShift - 1.4) * Math.abs(1 - cursorAnimationPhase / animationCycleMilliseconds * 2);
                double trWidth = cursorWidth / em / 3;
                double trHeight = cursorHeight / em / 3;
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x - cursorWidth / em / cursorShiftX,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y + 0,
                        trHeight, trWidth, 0, ColorEnum.BLUE, 1);
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x - 0,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y + cursorHeight / em / cursorShiftY,
                        trHeight, trWidth, 90, ColorEnum.BLUE, 1);
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x + cursorWidth / em / cursorShiftX,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y + 0,
                        trHeight, trWidth, 180, ColorEnum.BLUE, 1);
                draw(DrawFigureType.TRIANGLE,
                        Mouse.getX() / em + shift.x - 0,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.y - cursorHeight / em / cursorShiftY,
                        trHeight, trWidth, -90, ColorEnum.BLUE, 1);
                break;
        }
    }

    public ComplexFigure initComplexFigure(double x, double y, double rotate, ColorEnum color, double opacity) {
        return new ComplexFigure(x, y, rotate, color, opacity);
    }

    class SimpleFigure {
        private DrawFigureType figureType;
        private double x;
        private double y;
        private double sx;
        private double sy;
        private double rotation;
        private ColorEnum color;
        private double opacity;

        public SimpleFigure(DrawFigureType figureType, double x, double y, double sx, double sy,
                            double rotation, ColorEnum color, double opacity) {
            this.figureType = figureType;
            this.x = x;
            this.y = y;
            this.sx = sx;
            this.sy = sy;
            this.rotation = rotation;
            this.color = color;
            this.opacity = opacity;
        }

        public void drawAndCommit() {
            draw(figureType, x, y, sx, sy, rotation, color, opacity);
        }

        public void drawComplexFigure() {
            switch (figureType) {
                case TRIANGLE:
                    complexTriangle(x, y, sx, sy, rotation, color, opacity);
                    break;
            }
        }

        private void complexTriangle(double x, double y, double sx, double sy, double rotate, ColorEnum color, double opacity) {
            glEnable(GL_POLYGON_SMOOTH);
            glBegin(GL_TRIANGLES);
            {
                glRotated(-rotate, 0, 0, 1);
                glVertex2d(x + -sx / 2, y + -sy / 2);
                glVertex2d(x + -sx / 2, y + sy / 2);
                glVertex2d(x + (float) ((Math.pow(3, 0.5f) - 1) / 2 * sx), y + 0);
            }
            glEnd();
        }
    }

    class ComplexFigure {
        private double x;
        private double y;
        private double rotation;
        private ColorEnum color;
        private double opacity;

        private Set<SimpleFigure> figures = new HashSet<>();

        public ComplexFigure(double x, double y, double rotation, ColorEnum color, double opacity) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.color = color;
            this.opacity = opacity;
        }

        public void addFigure(SimpleFigure figure) {
            figures.add(figure);
        }

        public void draw() {
            beginComplexFigure(x, y, rotation, color, opacity);
            figures.forEach(SimpleFigure::drawComplexFigure);
            commitComplexFigure();
            figures.clear();
        }


    }
}
