package go.takethespread.managers;

import go.takethespread.NT.NTOrder;

import java.util.*;

public class InfoManager {

    private static InfoManager instance;
    private NTPlatformManager platformManager;
    private List<NTOrder> orders = new ArrayList<>();
    private Set<NTOrder>checkedOrders = new HashSet<>();
    private Map<String, Integer> positionsMap = new HashMap<>();


    public static InfoManager getInstance() {
        if (instance == null) {
            instance = new InfoManager();
        }
        return instance;
    }

    //check from program side
    public int checkPosition(String instrument){
        return positionsMap.get(instrument);
    }

    protected void addOrder(NTOrder order){
        orders.add(order);
    }

    private void positionsCalc(){
        for (NTOrder order: orders){
            String instrument = order.getInstrument();
            int filled = platformManager.getFilledOfOrder(order.getId());



        }
    }
}
