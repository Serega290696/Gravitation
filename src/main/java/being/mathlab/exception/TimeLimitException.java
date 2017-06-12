package being.mathlab.exception;

public class TimeLimitException extends Throwable {
    public TimeLimitException() {
        this("Computing duration is too long");
    }

    public TimeLimitException(String message) {
        super(message);
    }
}
