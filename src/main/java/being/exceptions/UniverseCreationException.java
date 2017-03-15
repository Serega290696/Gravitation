package being.exceptions;

import being.universe.UniverseType;

public class UniverseCreationException extends Throwable {
    public UniverseCreationException() {
        System.err.println("Error during being creation");
    }

    public UniverseCreationException(UniverseType type) {
        this(type, "");
    }

    public UniverseCreationException(UniverseType type, String msg) {
        System.err.println("Error during being type '" + type + "' creation. " + msg);
    }
}
