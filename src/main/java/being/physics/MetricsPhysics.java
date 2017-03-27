package being.physics;

import being.elements.Angel;

import java.util.List;

public abstract class MetricsPhysics implements Physics {
    int dimensionality;

    public int getDimensionality() {
        return dimensionality;
    }

    public abstract void loadAngles(List<Angel> angels);
}
