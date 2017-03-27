package being.mathematics;

public class MathUtilsTest {
    @org.junit.Test
    public void round() throws Exception {
        double x = 0.123456789098765432222222;
        double round = MathUtils.round(x, 10);
        System.out.println("round = " + round);
    }

    @org.junit.Test
    public void round1() throws Exception {
        double x = Math.sin(Math.PI / 2);
        double y = Math.asin(x);
        System.out.println("x = " + x);
        System.out.println("y = " + Math.PI / 2);
        System.out.println("y = " + y);
    }

    @org.junit.Test
    public void round2() throws Exception {
        double x = 10;
        double y = -10;
        double asin = Math.asin(y / (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))));
        asin = 180 / (Math.PI) * asin;
        System.out.println("asin = " + asin);
//        System.out.println("y = " + Math.PI / 2);
//        System.out.println("y = " + y);
    }

}