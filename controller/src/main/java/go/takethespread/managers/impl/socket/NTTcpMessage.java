package go.takethespread.managers.impl.socket;



public class NTTcpMessage {

    private NTTcpCommand command;
    private long id;
    private String param;
    protected static final String ntToken = ":-:";

    public NTTcpMessage(NTTcpCommand command, String param) {
        this.command = command;
        this.id = NTTcpManager.getMessageId();
        this.param = param;
    }

    public NTTcpCommand getCommand() {
        return command;
    }

    public long getId() {
        return id;
    }

    public String getParam() {
        return param;
    }

    public String prepareToSending(){
        return String.valueOf(id) +
                ntToken +
                command.name() +
                ntToken +
                param;
    }

    protected enum NTTcpCommand{
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
        SLMT //buy\sell limir or market order
    }

}
