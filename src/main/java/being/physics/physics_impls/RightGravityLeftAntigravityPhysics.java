package being.physics.physics_impls;

import being.elements.Atom;
import being.mathematics.ThreeVector;
import being.physics.PhysicsConfigurations;

import java.util.Collection;

/**
 * Created by Serega on 06.06.2017.
 */
public class RightGravityLeftAntigravityPhysics extends NewtonPhysics {


    public void gravity(Collection<Atom> allAtoms, Collection<Atom> updatedAtoms) {

        //todo too slow. Vector optimization
        for (Atom updatedAtom : updatedAtoms) {
            ThreeVector power = new ThreeVector(0, 0, 0);
            for (Atom otherAtom : allAtoms) {
                if (updatedAtom != otherAtom) {
                    ThreeVector distance = otherAtom.getPosition().minus(updatedAtom.getPosition());
                    double minDistance = otherAtom.getSize().x / 2 + updatedAtom.getSize().x / 2;
                    double distanceModule = distance.module() >= minDistance
                            ?
                            distance.module()
                            :
                            minDistance;
                    ThreeVector plus = distance.normalize().multiply(
                            PhysicsConfigurations.NewtonPhysicsConfigurations.G /
                                    updatedAtom.getWeight() /
                                    otherAtom.getWeight() /
                                    Math.pow(distanceModule, 2)
                    );
                    if (distanceModule == 0) {
                        System.err.println("Black hole was created - distance is zero!");
                        System.exit(666);
                    }
                    if (PhysicsConfigurations.NewtonPhysicsConfigurations.DEBUG_LOG_LEVEL) {
                        System.out.println("Gravity = " + plus);
                    }
                    if (distance.x >= 0) { //right
                        power.plusAndUpdate(plus);
                    } else {//left
                        power.plusAndUpdate(plus.multiply(-1));
                    }
                }
            }

            updatedAtom.update(power);
        }
    }

}
