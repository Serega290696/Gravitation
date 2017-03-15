package being.universe;

import being.exceptions.UniverseCreationException;

public class UniverseFactory {
    public static AbstractUniverse create(UniverseType type) throws UniverseCreationException {
        switch (type) {
            case UNIVERSE_42:
                return new Universe42();
        }
        throw new UniverseCreationException(type);
    }
}
