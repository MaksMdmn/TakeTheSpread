package go.takethespread.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.fsa.Settings;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.InfoManager;

import java.util.*;

public final class ParseJsonUtil {

    private static InfoManager manager = InfoManager.getInstance();
    private static ObjectMapper mapper = new ObjectMapper();

    private ParseJsonUtil(){
    }

    public static boolean checkConn(){
//        return NTTcpExternalManagerImpl.getInstance().isConnOkay();
        return true;
    }

    public static String settingsToJson() throws JsonProcessingException {
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        info.initProp();
        LinkedHashMap<Settings, String> settingsMap = info.getSettingsMap();


        JsonSettingsData tempObj;
        List<JsonSettingsData> jsonList = new ArrayList<>();
        for (Map.Entry<Settings, String> pair : settingsMap.entrySet()) {
            tempObj = new JsonSettingsData();
            tempObj.setName(pair.getKey().name());
            tempObj.setValue(pair.getValue());
            jsonList.add(tempObj);
        }

        return mapper.writeValueAsString(jsonList);
    }

    public static String ordersToJson() throws JsonProcessingException {
        Order o1 = new Order();
        Order o2 = new Order();
        Order o3 = new Order();

        o1.setDate(new Date());
        o1.setDeal(Order.Deal.Buy);
        o1.setFilled(3);
        o1.setId("123421321");
        o1.setInstrument("CL 09-16");
        o1.setPrice(Money.dollars(56.2));
        o1.setSize(10);
        o1.setPriceFilled(Money.dollars(56.2));
        o1.setState(Order.State.Filled);
        o1.setType(Order.Type.Market);

        o2.setDate(new Date());
        o2.setDeal(Order.Deal.Buy);
        o2.setFilled(3);
        o2.setId("71321");
        o2.setInstrument("CL 09-16");
        o2.setPrice(Money.dollars(6.2));
        o2.setSize(3);
        o2.setPriceFilled(Money.dollars(5.2));
        o2.setState(Order.State.Filled);
        o2.setType(Order.Type.Market);

        o3.setDate(new Date());
        o3.setDeal(Order.Deal.Buy);
        o3.setFilled(3);
        o3.setId("178321");
        o3.setInstrument("CL 08-16");
        o3.setPrice(Money.dollars(5.2));
        o3.setSize(10);
        o3.setPriceFilled(Money.dollars(6.2));
        o3.setState(Order.State.Accepted);
        o3.setType(Order.Type.Market);

//        List<Order> orders = manager.getAllOrders();
        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);

        List<JsonOrderData> filteredOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getState() == Order.State.Filled) { // only full filled order FOR NOW!
                filteredOrders.add(new JsonOrderData(o));
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredOrders);
    }

    public static String indicatorsToJson() throws JsonProcessingException {
        //test only
        JsonIndicData indicData = new JsonIndicData();
        indicData.setBuyPw(43123212d);
        indicData.setCalcSpr(32.5);
        indicData.setCurSpr(33.66);
        indicData.setCash(12111d);
        indicData.setCommis(94);
        indicData.setPnl(-321d);

        return "[" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(indicData) + "]";
    }

    public static String priceDataToJson() throws JsonProcessingException {
        double[] priceData = new double[4];
//        priceData[0] = manager.getPrice(Term.NEAR, Side.BID).getAmount();
//        priceData[1] = manager.getPrice(Term.NEAR, Side.ASK).getAmount();
//        priceData[2] = manager.getPrice(Term.FAR, Side.BID).getAmount();
//        priceData[3] = manager.getPrice(Term.FAR, Side.ASK).getAmount();

        int zeroOne = getRandomInt(0, 1);
        priceData[0] = (double) getRandomInt(60, 80);
        priceData[1] = (double) getRandomInt(30, 50);
        priceData[2] = (double) getRandomInt(60, 80) * zeroOne;
        priceData[3] = (double) getRandomInt(30, 50) * zeroOne;

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(priceData);
    }

    private static int getRandomInt(int minimum, int maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }

}

