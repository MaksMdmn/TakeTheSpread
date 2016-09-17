package go.takethespread.managers;


import go.takethespread.GenericDao;
import go.takethespread.MarketData;
import go.takethespread.exceptions.PersistException;
import go.takethespread.fsa.TradeBlotter;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MarketDataCollector {
    private static MarketDataCollector instance;
    private static int ENOUGH_DATA_TIME_TO_PUSH = 1500;

    private List<MarketData> mdList;
    private PostgresDaoFactoryImpl factory;
    private GenericDao<MarketData, Integer> mdDao;

    private MarketDataCollector() {
        mdList = new LinkedList<>();
        factory = new PostgresDaoFactoryImpl();
    }

    public static MarketDataCollector getInstance() {
        if (instance == null) {
            instance = new MarketDataCollector();
        }
        return instance;
    }

    public void collectMarketData(TradeBlotter blotter) {
        MarketData md = new MarketData();
        md.setDate(new Date());
        md.setBid_n(blotter.getBid_n());
        md.setBidSize_n(blotter.getBidVol_n());
        md.setAsk_n(blotter.getAsk_n());
        md.setAskSize_n(blotter.getAskVol_n());
        md.setBid_f(blotter.getBid_f());
        md.setBidSize_f(blotter.getBidVol_f());
        md.setAsk_f(blotter.getAsk_f());
        md.setAskSize_f(blotter.getAskVol_f());
        mdList.add(md);
    }

    public boolean isItTimeToPushToDb() {
        return mdList.size() >= ENOUGH_DATA_TIME_TO_PUSH;
    }

    public void pushToDataBase() {
        if (mdList == null) {
            return;
        }

        if (mdList.isEmpty()) {
            return;
        }

        try {
            mdDao = factory.getDao(factory.getContext(), MarketData.class);
            for (MarketData m : mdList) {
                mdDao.persist(m);
            }
            mdList.clear();
        } catch (PersistException e) {
            e.printStackTrace();
        }

    }

    public void TESTcollectMarketData( MarketData md) {
        mdList.add(md);
    }

}
