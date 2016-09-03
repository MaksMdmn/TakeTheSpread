package go.takethespread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import go.takethespread.fsa.TradeSystemInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class TEST_JSON {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonObj = null;

        try {
            double[] priceData = new double[4];
            priceData[0] = 77.7d;
            priceData[1] = 12.62d;
            priceData[2] = 3.5d;
            priceData[3] = 6.42d;
            jsonObj = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(priceData);
            System.out.println(jsonObj);
            printEmptyLine();
            try {
                double[] newPriceData = mapper.readValue(jsonObj, double[].class);
                System.out.println(newPriceData[3]);
                printEmptyLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(settingsToJson(mapper));
            printEmptyLine();
            System.out.println(orderstoJson(mapper));
            printEmptyLine();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static String settingsToJson(ObjectMapper mapper) throws JsonProcessingException {
        LinkedHashMap<String, Object> settingsMap = new LinkedHashMap();
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        settingsMap.put("HOST", info.host);
        settingsMap.put("PORT", info.port);
        settingsMap.put("NEAR FUTURES", info.instrument_n);
        settingsMap.put("FAR FUTURES", info.instrument_f);
        settingsMap.put("ACCOUNT", info.account);
        settingsMap.put("COMMISSION_VALUE, usd", info.commis.getAmount());
        settingsMap.put("DEFAULT SPREAD", info.default_spread.getAmount());
        settingsMap.put("DEFAULT SPREAD USING", info.default_spread_using);
        settingsMap.put("ENTER SIGNAL DEV", info.entering_dev.getAmount());
        settingsMap.put("MAX SIZE, n", info.max_size);
        settingsMap.put("TIME IN POS, sec", info.inPos_time_sec);
        settingsMap.put("LIMIT ORDERS USING", info.limit_using);
        settingsMap.put("MAX LOSS NUMBERS", info.max_loss_n);
        settingsMap.put("MIN SPREAD CALC TIME, sec", info.min_spreadCalc_n);
        settingsMap.put("SPREAD CALC TIME, sec", info.spreadCalc_time_sec);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(settingsMap);
    }

    private static String orderstoJson(ObjectMapper mapper) throws JsonProcessingException {
        Order o1 = new Order();
        Order o2 = new Order();
        Order o3 = new Order();

        o1.setDate(new Date());
        o1.setDeal(Order.Deal.Buy);
        o1.setFilled(3);
        o1.setOrdId("123421321");
        o1.setInstrument("CL 09-16");
        o1.setPrice(Money.dollars(56.2));
        o1.setSize(10);
        o1.setPriceFilled(Money.dollars(56.2));
        o1.setState(Order.State.Filled);
        o1.setType(Order.Type.Market);

        o2.setDate(new Date());
        o2.setDeal(Order.Deal.Buy);
        o2.setFilled(3);
        o2.setOrdId("71321");
        o2.setInstrument("CL 09-16");
        o2.setPrice(Money.dollars(6.2));
        o2.setSize(3);
        o2.setPriceFilled(Money.dollars(5.2));
        o2.setState(Order.State.Filled);
        o2.setType(Order.Type.Market);

        o3.setDate(new Date());
        o3.setDeal(Order.Deal.Buy);
        o3.setFilled(3);
        o3.setOrdId("178321");
        o3.setInstrument("CL 08-16");
        o3.setPrice(Money.dollars(5.2));
        o3.setSize(10);
        o3.setPriceFilled(Money.dollars(6.2));
        o3.setState(Order.State.Accepted);
        o3.setType(Order.Type.Market);


        List<Order> orders = new ArrayList<Order>() {{
            add(o1);
            add(o2);
            add(o3);
        }};

        List<Order> filteredOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getState() == Order.State.Filled) { // only full filled order FOR NOW!
                filteredOrders.add(o);
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredOrders);
    }


    private static void printEmptyLine(){
        System.out.println("------------------------------");
    }
}
