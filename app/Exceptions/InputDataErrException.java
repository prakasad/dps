package Exceptions;

public class InputDataErrException extends Exception {
    private static final long serialVersionUID = 1L;

    public InputDataErrException(String message) {
        super(message);
    }
}
