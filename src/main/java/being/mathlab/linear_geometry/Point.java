package being.mathlab.linear_geometry;

import java.util.Arrays;

public class Point {

  private double[] coords;

  public Point(double... coords) {
    this.coords = coords;
  }

  public double[] getCoords() {
    return coords;
  }

  public Point plus(Point point) {
    double coords[] = new double[this.coords.length];
    for (int i = 0; i < coords.length; i++) {
      coords[i] = this.coords[i] + point.getCoords()[i];
    }
    return new Point(coords);
  }

  public Point divide(int n) {
    double coords[] = new double[this.coords.length];
    for (int i = 0; i < coords.length; i++) {
      coords[i] = this.coords[i] / n;
    }
    return new Point(coords);
  }

  public Point multiple(double n) {
    double coords[] = new double[this.coords.length];
    for (int i = 0; i < coords.length; i++) {
      coords[i] = this.coords[i] * n;
    }
    return new Point(coords);
  }

  public Point minus(Point point) {
    double coords[] = new double[this.coords.length];
    for (int i = 0; i < coords.length; i++) {
      coords[i] = this.coords[i] - point.getCoords()[i];
    }
    return new Point(coords);
  }

  @Override
  public String toString() {
    return "Point{" +
        "coords=" + Arrays.toString(coords) +
        '}';
  }
}
