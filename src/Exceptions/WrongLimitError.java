package Exceptions;

public class WrongLimitError extends  Exception {
    public WrongLimitError() { super(); }
    public WrongLimitError(String message) { super(message); }
    public WrongLimitError(String message, Throwable cause) { super(message, cause); }
    public WrongLimitError(Throwable cause) { super(cause); }
}
