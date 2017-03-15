package being.mathematics;

public class ThreeVector implements Cloneable {
  public double x;
  public double y;
  public double z;

  public ThreeVector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public ThreeVector divide(double number) {
    return new ThreeVector(x / number, y / number, z / number);
  }

  public ThreeVector multiply(double number) {
    return new ThreeVector(x * number, y * number, z * number);
  }

  public ThreeVector minus(ThreeVector subtrahend) {
    return new ThreeVector(x - subtrahend.x, y - subtrahend.y, z - subtrahend.z);
  }

  public ThreeVector plus(ThreeVector term) {
    return new ThreeVector(x + term.x, y + term.y, z + term.z);
  }

  public void plusAndUpdate(ThreeVector term) {
    x = x + term.x;
    y = y + term.y;
    z = z + term.z;
  }

  public double module() {
    return Math.pow(x * x + y * y + z * z, 0.5);
  }

  @Override
  public String toString() {
    return "Vector(" +
        MathUtils.round(x, 10) +
        ", " + MathUtils.round(y, 10) +
        ", " + MathUtils.round(z, 10) +
        ')';
  }

  @Override
  public ThreeVector clone() {
//    System.err.println("[WARN] in clone()");
    return new ThreeVector(x, y, z);
  }

  public ThreeVector normalize() {
    return new ThreeVector(x / this.module(), y / this.module(), z / this.module());
  }
}
