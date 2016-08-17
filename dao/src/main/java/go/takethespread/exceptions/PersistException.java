package go.takethespread.exceptions;

public class PersistException extends Exception {
    public PersistException() {
        super();
    }

    public PersistException(String msg) {
        super(msg);
    }

    public PersistException(String msg, Exception e) {
        super(msg, e);
    }
}
