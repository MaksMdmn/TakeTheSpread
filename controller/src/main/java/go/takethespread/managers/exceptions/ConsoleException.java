package go.takethespread.managers.exceptions;

public class ConsoleException extends Exception {

    public ConsoleException(String msg, Exception e){
        super(msg,e);
    }

    public ConsoleException(String msg){
        super(msg);
    }

    public ConsoleException(){
    }

}
