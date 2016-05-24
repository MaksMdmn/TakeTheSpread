package go.takethespread.managers.socket;


public class NTTcpMessage {

    private NTTcpCommand command;
    private long id;
    private String param;
    protected static final String ntToken = ":-:";

    protected NTTcpMessage(NTTcpCommand command, String param) {
        this.command = command;
        this.id = NTTcpManager.getMessageId();
        this.param = param;

//        System.out.println("msg:" + " cmd:" + command + " id:" + id + " param:" + param);
    }

    protected NTTcpCommand getCommand() {
        return command;
    }

    protected long getId() {
        return id;
    }

    protected String getParam() {
        return param;
    }

    protected String prepareToSending() {
        return String.valueOf(id) +
                ntToken +
                command.name() +
                ntToken +
                param;
    }

    protected enum NTTcpCommand {
        GJ, //off
        ORDS,//all orders
        BYID,//order by id
        POS,//positiong
        BPOW,//buying power
        CSHV,//cash value
        RPNL,//PnL
        BMRT,
        BLMT,
        SMRT,
        SLMT, //buy\sell limir or market order
        CNAL,
        CNID,
        CHOR,
        FLLD,
        BDAK //bid ask data
    }

}
