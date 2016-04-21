package go.takethespread.exceptions;

public class DbException extends Exception {
    public DbException() {
        super();
    }

    public DbException(String msg) {
        super(msg);
    }

    public DbException(String msg, Exception e) {
        super(msg, e);
    }
}
