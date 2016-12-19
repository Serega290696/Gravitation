package universe.mathematics;

public class MathUtilsTest {
  @org.junit.Test
  public void round() throws Exception {
    double x = 0.123456789098765432222222;
    double round = MathUtils.round(x, 10);
    System.out.println("round = " + round);
  }

  @org.junit.Test
  public void round1() throws Exception {

  }

}