package go.takethespread.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import go.takethespread.*;
import go.takethespread.fsa.Side;
import go.takethespread.fsa.Term;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.InfoManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class UpdateServlet extends HttpServlet {
    private static final String REQ = "whichTable";
    private static final String PRICE_REQ = "prices";
    private static final String SET_REQ = "settings";
    private static final String INDIC_REQ = "indicators";
    private static final String ORD_REQ = "orders";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ExternalManager externalManager = NTTcpExternalManagerImpl.getInstance();
//        if (externalManager.isConnOkay()) {
//            InfoManager manager = InfoManager.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        String frontRequest = req.getParameter(REQ);
        String jsonObj = null;


        switch (frontRequest) {
            case PRICE_REQ:
                jsonObj = priceDataToJson(null, mapper);
                break;
            case SET_REQ:
                jsonObj = settingsToJson(null, mapper);
                break;
            case ORD_REQ:
                jsonObj = ordersToJson(null, mapper);
                break;
            case INDIC_REQ:
                jsonObj = "[" + indicatorsToJson(null, mapper) + "]";
                break;
            default:
                break;
        }

//        }
        System.out.println(jsonObj);
        resp.setContentType("application/json");
        System.out.println("param: " + req.getParameter("whichTable"));
        resp.getWriter().print(jsonObj);
        resp.getWriter().flush();

    }

    private String priceDataToJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
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

    private int getRandomInt(int minimum, int maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }

    private String settingsToJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
        LinkedHashMap<String, Object> settingsMap = new LinkedHashMap();
//        TradeSystemInfo info = manager.getProps();
        TradeSystemInfo info = new TradeSystemInfo();
        info.updateProp();
        settingsMap.put("HOST", info.host);
        settingsMap.put("PORT", info.port);
        settingsMap.put("NEAR FUTURES", info.instrument_n);
        settingsMap.put("FAR FUTURES", info.instrument_f);
        settingsMap.put("ACCOUNT", info.account);
        settingsMap.put("COMMISSION_VALUE, usd", info.commis_per_one_contract.getAmount());
        settingsMap.put("DEFAULT SPREAD", info.default_spread.getAmount());
        settingsMap.put("DEFAULT SPREAD USING", info.default_spread_use);
        settingsMap.put("ENTER SIGNAL DEV", info.entering_dev.getAmount());
        settingsMap.put("MAX SIZE, n", info.favorable_size);
        settingsMap.put("TIME IN POS, sec", info.inPos_time_sec);
        settingsMap.put("LIMIT ORDERS USING", info.limit_use);
        settingsMap.put("MAX LOSS NUMBERS", info.max_loss_numbers);
        settingsMap.put("MIN SPREAD CALC TIME, sec", info.min_spreadCalc_period);
        settingsMap.put("SPREAD CALC TIME, sec", info.spreadCalc_time_sec);

        JSONSettingsData tempObj;
        List<JSONSettingsData> jsonList = new ArrayList<>();
        for (Map.Entry<String, Object> pair : settingsMap.entrySet()) {
            tempObj = new JSONSettingsData();
            tempObj.setName(pair.getKey());
            tempObj.setValue(pair.getValue());
            jsonList.add(tempObj);
        }

        return mapper.writeValueAsString(jsonList);
    }

    private String ordersToJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
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

        List<JSONOrderData> filteredOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getState() == Order.State.Filled) { // only full filled order FOR NOW!
                filteredOrders.add(new JSONOrderData(o));
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredOrders);
    }

    private String indicatorsToJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
        //test only
        JSONIndicData indicData = new JSONIndicData();
        indicData.setBuyPw(43123212d);
        indicData.setCalcSpr(32.5);
        indicData.setCurSpr(33.66);
        indicData.setCash(12111d);
        indicData.setCommis(94);
        indicData.setPnl(-321d);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(indicData);
    }
}
