package universe.mathematics;

public class MathUtils {
  public static double round(double number, int order) {
    if(order > 19) {
      try {
        throw new Exception("capacity is more then 10^19.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ((double) Math.round(number * Math.pow(10, order))) / Math.pow(10, order);
  }

  public static double round(double number) {
    return round(number, 2);
  }
}
