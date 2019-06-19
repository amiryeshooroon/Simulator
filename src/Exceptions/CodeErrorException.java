package Exceptions;

public class CodeErrorException extends Exception {
    public CodeErrorException() { super(); }
    public CodeErrorException(String message) { super(message); }
    public CodeErrorException(String message, Throwable cause) { super(message, cause); }
    public CodeErrorException(Throwable cause) { super(cause); }
}
