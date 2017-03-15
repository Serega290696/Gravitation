package being.physics;

public class PhysicsConfigurations {
    public static final double MOMENT_SIZE = 0.02;
    public static double ACTUAL_MOMENT_MIN_SIZE = 0.2;

    public class NewtonPhysicsConfigurations extends PhysicsConfigurations {
        public static final int MAX_EVENTS_AMOUNT_PER_ATOM = 50;
        public static final double G = 10;
    }

}
