package being.elements;

import being.physics.Physics;
import being.universe.AbstractUniverse;

import java.util.Collection;

/**
 * Created by Serega on 22.05.2017.
 */
public class AngelsFactory {
    public static Angel getInstance(Class<? extends Angel> angelClass, String name, Physics physics, AbstractUniverse universe, Collection<Atom> atoms) {
        if (angelClass == Angel.class) {
            return new Angel("Anael", physics, universe, atoms);
        } else if (angelClass == OneMomentAngel.class) {
            return new OneMomentAngel("Anael", physics, universe, atoms);
        }
        return null;
    }
}
