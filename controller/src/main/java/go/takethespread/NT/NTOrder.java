package go.takethespread.NT;


public class NTOrder {

    private final int orderId;
    private final String command;
    private final String account;
    private final String instrument;
    private final String action;
    private final int quantity;
    private final String orderType;
    private final double limitPrice;
    private final double stopPrice;
    private final String timeInForce;
    private final String oco;
    private final String strategy;
    private final String strategyId;

    private NTOrder(Builder builder) {
        this.orderId = builder.orderId;
        this.account = builder.account;
        this.command = builder.command;
        this.instrument = builder.instrument;
        this.action = builder.action;
        this.quantity = builder.quantity;
        this.orderType = builder.orderType;
        this.limitPrice = builder.limitPrice;
        this.stopPrice = builder.stopPrice;
        this.timeInForce = builder.timeInForce;
        this.oco = builder.oco;
        this.strategy = builder.strategy;
        this.strategyId = builder.strategyId;
    }

    public static class Builder {
        private final int orderId;
        private final String command;
        private String account = "";
        private String action = "";
        private String instrument = "";
        private int quantity = 0;
        private String orderType = "";
        private double limitPrice = 0;
        private double stopPrice = 0;
        private String timeInForce = "";
        private String oco = "";
        private String strategy = "";
        private String strategyId = "";

        public Builder(int orderId, NTCommand NTCommand) {
            if (orderId < 0) {
//                this.orderId = Integer.valueOf(INSTANCE.NewOrderId());
                this.orderId = 0;
            } else {
                this.orderId = orderId;
            }
            this.command = NTCommand.toString();
        }

        public Builder account(String val) {
            account = val;
            return this;
        }

        public Builder action(NTAction val) {
            action = val.toString();
            return this;
        }

        public Builder instrument(String val) {
            instrument = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder orderType(NTOrderType val) {
            orderType = val.toString();
            return this;
        }

        public Builder limitPrice(double val) {
            limitPrice = val;
            return this;
        }

        public Builder stopPrice(double val) {
            stopPrice = val;
            return this;
        }

        public Builder timeInForce(NTtif val) {
            timeInForce = val.toString();
            return this;
        }

        public Builder oco(String val) {
            oco = val;
            return this;
        }

        public Builder strategy(String val) {
            strategy = val;
            return this;
        }

        public Builder strategyId(String val) {
            strategyId = val;
            return this;
        }

        public NTOrder build() {
            return new NTOrder(this);
        }
    }

    public boolean doOrder() {
        String orderIdTemp;
        if (orderId == -13) {
            orderIdTemp = "";
        } else {
            orderIdTemp = String.valueOf(orderId);
        }
//        return INSTANCE.Command(command, account, instrument, action,
//                        quantity, orderType, limitPrice, stopPrice, timeInForce, oco, orderIdTemp,
//                        strategy, strategyId) == 0;

        return true;
    }

    public int remainingContracts(int orderId){
        String orderIdTemp = String.valueOf(orderId);
//        return this.quantity - INSTANCE.Filled(orderIdTemp);
        return 0;
    }

}


