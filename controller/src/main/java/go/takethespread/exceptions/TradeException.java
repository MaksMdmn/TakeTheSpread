package go.takethespread.exceptions;

public class TradeException extends Exception{
    public TradeException(String msg, Exception e) {
        super(msg, e);
    }

    public TradeException(String msg) {
        super(msg);
    }

    public TradeException() {
    }
}
