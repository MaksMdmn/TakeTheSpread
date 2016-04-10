package go.takethespread;


import com.sun.jna.Library;

public interface NTdll extends Library {

    double BuyingPower(String account);

    double CashValue(String account);

    int Connected(int showMessage);

    int TearDown();

    //    Optional function to set the host and port number.
//    By default, host is set to "localhost" and port is set to 36973. The default port number can be
//    set via the General tab under Options. If you change these default values, this function
//    must be called before any other function. A return value of 0 indicates success and -1 indicates an error.
    int SetUp(String host, int port);

//    Function for submitting, cancelling and changing orders, positions and strategies.
//    Refer to the Commands and Valid Parameters section for detailed information.
//    A return value of 0 indicates success and -1 indicates an error.
//    The Log tab will list context sensitive error information.

    int Command(String command, String account, String instrument, String action,
                int quantity, String orderType, double limitPrice, double stopPrice,
                String timeInForce, String oco, String orderId, String strategy, String strategyId);


//    The parameter confirm indicates if an order confirmation message will appear.
//    This toggles the global option that can be set manually in the NinjaTrader Control Center by selecting
//    the Tools menu and the menu item Options, then checking the "Confirm order placement" checkbox.
//    A value of 1 sets this option to true, any other value sets this option to false.

    int ConfirmOrders(int confirm);


//    Gets the number of contracts/shares filled for the orderId.

    int Filled(String orderId);


//    Gets the most recent price for the specified instrument and data type.
//    0 = last, 1 = bid, 2 = ask. You must first call the SubscribeMarketData() function
//    prior to calling this function.

    double MarketData(String instrument, int type);


    //    Gets the market position for an instrument/account combination.
//    Returns 0 for flat, negative value for short positive value for long.
    int MarketPosition(String instrument, String account);


    //    Gets a new unique order ID value.
    String NewOrderId();


    //    Gets a string of order ID's of all orders of an account separated by '|'.
//   *If a user defined order ID was not originally provided, the internal token ID value
//    is used since it is guaranteed to be unique.
    String Orders(String account);


    //    Gets the order state (see definitions) for the orderId. Returns an empty string if the order ID value provided does
//    not return an order.
    String OrderStatus(String orderId);


    //    Gets the realized profit and loss of an account.
    double RealizedPnL(String account);


    //    Starts a market data stream for the specific instrument. Call the MarketData() function to retrieve prices.
//    Make sure you call the UnSubscribeMarketData() function to close the data stream.
//    A return value of 0 indicates success and -1 indicates an error.
    int SubscribeMarketData(String instrument);


    //    Stops a market data stream for the specific instrument.
//    A return value of 0 indicates success and -1 indicates an error.
    int UnsubscribeMarketData(String instrument);

}


