package Utilities.AutoPilot.Exceptions;

public class NotConnectedToServerException extends Exception {
    public NotConnectedToServerException() { super(); }
    public NotConnectedToServerException(String message) { super(message); }
    public NotConnectedToServerException(String message, Throwable cause) { super(message, cause); }
    public NotConnectedToServerException(Throwable cause) { super(cause); }
}
