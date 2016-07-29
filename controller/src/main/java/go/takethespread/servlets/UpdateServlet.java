package go.takethespread.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import go.takethespread.JSONSettingsData;
import go.takethespread.Order;
import go.takethespread.fsa.Side;
import go.takethespread.fsa.Term;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UpdateServlet extends HttpServlet {
    private static final String REQ = "req";
    private static final String PRICE_REQ = "prices";
    private static final String SET_REQ = "settings";
    private static final String ORD_REQ = "orders";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ExternalManager externalManager = NTTcpExternalManagerImpl.getInstance();
//        if (externalManager.isConnOkay()) {
//            InfoManager manager = InfoManager.getInstance();
        ObjectMapper mapper = new ObjectMapper();
//            String frontRequest = req.getParameter(REQ);
        String jsonObj = null;

        jsonObj = settingsToJson(null, mapper);

//            switch (frontRequest) {
//                case PRICE_REQ:
//                    jsonObj = priceDataToJson(manager, mapper);
//                    break;
//                case SET_REQ:
//                    jsonObj = settingsToJson(manager, mapper);
//                    break;
//                case ORD_REQ:
//                    jsonObj = orderstoJson(manager, mapper);
//                    break;
//                default:
//                    break;
//            }

        System.out.println("[" + jsonObj + "]");
        resp.setContentType("application/json");
        resp.getWriter().print("[" + jsonObj + "]");
        resp.getWriter().flush();

//        }
    }

    private String priceDataToJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
        double[] priceData = new double[4];
        priceData[0] = manager.getPrice(Term.NEAR, Side.BID).getAmount();
        priceData[1] = manager.getPrice(Term.NEAR, Side.ASK).getAmount();
        priceData[2] = manager.getPrice(Term.FAR, Side.BID).getAmount();
        priceData[3] = manager.getPrice(Term.FAR, Side.ASK).getAmount();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(priceData);
    }

    private String settingsToJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
        LinkedHashMap<String, Object> settingsMap = new LinkedHashMap();
//        TradeSystemInfo info = manager.getProps();
        TradeSystemInfo info = new TradeSystemInfo();
        info.updateProp();
//        settingsMap.put("HOST", info.host);
//        settingsMap.put("PORT", info.port);
//        settingsMap.put("NEAR FUTURES", info.instrument_n);
//        settingsMap.put("FAR FUTURES", info.instrument_f);
//        settingsMap.put("ACCOUNT", info.account);
//        settingsMap.put("COMMISSION_VALUE, usd", info.commis_per_one_contract.getAmount());
//        settingsMap.put("DEFAULT SPREAD", info.default_spread.getAmount());
//        settingsMap.put("DEFAULT SPREAD USING", info.default_spread_use);
//        settingsMap.put("ENTER SIGNAL DEV", info.entering_dev.getAmount());
//        settingsMap.put("MAX SIZE, n", info.favorable_size);
//        settingsMap.put("TIME IN POS, sec", info.inPos_time_sec);
//        settingsMap.put("LIMIT ORDERS USING", info.limit_use);
//        settingsMap.put("MAX LOSS NUMBERS", info.max_loss_numbers);
//        settingsMap.put("MIN SPREAD CALC TIME, sec", info.min_spreadCalc_period);
//        settingsMap.put("SPREAD CALC TIME, sec", info.spreadCalc_time_sec);
        JSONSettingsData test = new JSONSettingsData();
        test.setName("HOST");
        test.setValue(info.host);
        return mapper.writeValueAsString(test);
    }

    private String orderstoJson(InfoManager manager, ObjectMapper mapper) throws JsonProcessingException {
        List<Order> orders = manager.getAllOrders();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getState() == Order.State.Filled) { // only full filled order FOR NOW!
                filteredOrders.add(o);
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredOrders);
    }
}
