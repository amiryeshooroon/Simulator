package Exceptions;

public class CantConnectToServerException extends Exception{
    public CantConnectToServerException() { super(); }
    public CantConnectToServerException(String message) { super(message); }
    public CantConnectToServerException(String message, Throwable cause) { super(message, cause); }
    public CantConnectToServerException(Throwable cause) { super(cause); }
}
