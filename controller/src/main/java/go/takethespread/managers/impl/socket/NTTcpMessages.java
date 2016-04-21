package go.takethespread.managers.impl.socket;



public enum NTTcpMessages {
    ORD,//order by id
    ORDS,//all orders
    NEWID,//new order id (if it'll be need)
    POS,//positiong
    BPOW,//buying power
    CSHV,//cash value
    RPNL//PnL
}
