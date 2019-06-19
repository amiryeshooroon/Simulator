package Utilities.AutoPilot.Exceptions;

public class ArgumentErrorException extends Exception {
    public ArgumentErrorException() { super(); }
    public ArgumentErrorException(String message) { super(message); }
    public ArgumentErrorException(String message, Throwable cause) { super(message, cause); }
    public ArgumentErrorException(Throwable cause) { super(cause); }
}
