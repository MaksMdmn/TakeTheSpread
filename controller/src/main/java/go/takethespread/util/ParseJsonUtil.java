package go.takethespread.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import go.takethespread.*;
import go.takethespread.exceptions.PersistException;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.impl.MarketDataDaoImpl;
import go.takethespread.impl.OrderDaoImpl;
import go.takethespread.impl.PostgresDaoFactoryImpl;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.StatusManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import java.sql.Connection;
import java.util.*;

public final class ParseJsonUtil {

    private static InfoManager manager = InfoManager.getInstance();
    private static ObjectMapper mapper = new ObjectMapper();
    private static StatusManager statusManager = StatusManager.getInstance();
    private static DaoFactory<Connection> daoFactory = new PostgresDaoFactoryImpl();

    private ParseJsonUtil() {
    }

    public static synchronized boolean checkConn() {
        return NTTcpExternalManagerImpl.getInstance().isConnOkay();
//        return true;
    }

    public static synchronized boolean checkDataChannel() {
        return manager.isBlotterActive();
    }

    public static String settingsToJson() throws JsonProcessingException {
        String answer = "";
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        Map<Settings, String> settingsMap = info.getSettingsMap();
        if (info.fullSettingsVerification()) {
            JsonSettingsData tempObj;
            List<JsonSettingsData> jsonList = new ArrayList<>();
            for (Map.Entry<Settings, String> pair : settingsMap.entrySet()) {
                tempObj = new JsonSettingsData();
                tempObj.setName(pair.getKey().name());
                tempObj.setValue(pair.getValue());
                jsonList.add(tempObj);
            }

            answer = mapper.writeValueAsString(jsonList);
        }

        return answer;
    }

    public static String ordersToJson() throws JsonProcessingException {
        String answer = "";
        if (statusManager.isOrdersInfoUpdated()) {
            List<JsonOrderData> jsonOrders = new ArrayList<>();
            try {
                OrderDaoImpl dao = new OrderDaoImpl(daoFactory.getContext());
                for (Order o : dao.readTodayFilledOrdersDesc()) {
                    jsonOrders.add(new JsonOrderData(o));
                }

            } catch (PersistException e) {
                e.printStackTrace();
            }

            answer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonOrders);
            statusManager.restoreOrdersInfoStatus();
        }

        return answer;
    }

    public static String indicatorsToJson() throws JsonProcessingException {
        JsonIndicData indicData = new JsonIndicData();
        indicData.setPos_n(manager.getPosition(Term.NEAR));
        indicData.setPos_f(manager.getPosition(Term.FAR));
        indicData.setSpot_n(manager.getPrice(Term.NEAR, Side.BID).getAmount()); //HARDCORE: IN THIS CASE NEAREST ALWAYS LOWER THEN FAR
        indicData.setSpot_f(manager.getPrice(Term.FAR, Side.ASK).getAmount());
        indicData.setCalcSpr(manager.getCurrentSpread().getAmount());
        indicData.setCurSpr(manager.getBestSpread().getAmount());
        indicData.setCash(manager.getCash().getAmount());
        indicData.setBuyPw(manager.getBuyingPwr().getAmount());
        indicData.setDeals(0);
        indicData.setDeals_prf(0);
        indicData.setDeals_ls(0);
        indicData.setCommis(0d);
        indicData.setPnl(manager.getPnL().getAmount());

//        indicData.setPos_n((int) (Math.random() * 10));
//        indicData.setPos_f((int) (Math.random() * 10));
//        indicData.setSpot_n(Money.dollars(0d).getAmount()); //HARDCORE: IN THIS CASE NEAREST ALWAYS LOWER THEN FAR
//        indicData.setCalcSpr(Money.dollars(0d).getAmount());
//        indicData.setCurSpr(Money.dollars(0d).getAmount());
//        indicData.setCash(Money.dollars(0d).getAmount());
//        indicData.setBuyPw(Money.dollars(0d).getAmount());
//        indicData.setDeals((int) (Math.random() * 10));
//        indicData.setDeals_prf((int) (Math.random() * 10));
//        indicData.setDeals_ls((int) (Math.random() * 10));
//        indicData.setCommis(0d);
//        indicData.setPnl(Money.dollars(0d).getAmount());

        return "[" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(indicData) + "]";
    }

    public static String priceDataToJson(boolean appendHistorical) throws JsonProcessingException {
        String answer = null;
        if (appendHistorical) {
            try {
                MarketDataDaoImpl mdDao = new MarketDataDaoImpl(daoFactory.getContext());
                answer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mdDao.readLastActualMarketData(100)); //HARDCORE
            } catch (PersistException e) {
                e.printStackTrace();
            }
        } else {
            double[] priceData = new double[4];
            priceData[0] = manager.getPrice(Term.NEAR, Side.BID).getAmount();
            priceData[1] = manager.getPrice(Term.NEAR, Side.ASK).getAmount();
            if (statusManager.isTransactionHappened()) {
                double[] transactionPrices = statusManager.getLastTransactionPricesAndRestore();
                priceData[2] = transactionPrices[0];
                priceData[3] = transactionPrices[1];
            } else {
                priceData[2] = 0d;
                priceData[3] = 0d;
            }

//            priceData[0] = 12d;
//            priceData[1] = 15d;
//            priceData[2] = 1d;
//            priceData[3] = 32d;

            answer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(priceData);
        }

        return answer;
    }
}

