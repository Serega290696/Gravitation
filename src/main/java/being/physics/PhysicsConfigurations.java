package being.physics;

public class PhysicsConfigurations {
    public static double MOMENT_DURATION = 0.02;
    public static double DEFAULT_DISPLAYING_MOMENT_MIN_DURATION = 0.02;
    public static double DISPLAYING_MOMENT_MIN_DURATION = DEFAULT_DISPLAYING_MOMENT_MIN_DURATION;
    public static boolean DEBUG_LOG_LEVEL = false;

    public static class NewtonPhysicsConfigurations extends PhysicsConfigurations {
        public static final int MAX_EVENTS_AMOUNT_PER_ATOM = 5000;
        public final static double ELASTICITY_FACTOR = 0.5;
//        public static double G = 6.67d*Math.pow(10, 1);// G = 10^-11 - truth value. But I use 'km' instead 'm'
        public static double G = 9d*Math.pow(10, -17+7);// G = 10^-11 - truth value. But I use 'km' instead 'm'
//        then G must be equal 10^-17. But it so boring, objects move so slowly. Therefore I assign G=10^-10
        public static boolean IS_GRAVITY_ENABLE = true;
        public static boolean IS_ELECTROMAGNETISM_ENABLE = true;
        public static boolean DRAW_SIMPLE_SHAPES = false;
    }

}
