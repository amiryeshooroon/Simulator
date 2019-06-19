package Utilities.AutoPilot.Exceptions;

public class ServerAlreadyAliveException extends Exception {
    public ServerAlreadyAliveException() { super(); }
    public ServerAlreadyAliveException(String message) { super(message); }
    public ServerAlreadyAliveException(String message, Throwable cause) { super(message, cause); }
    public ServerAlreadyAliveException(Throwable cause) { super(cause); }
}
