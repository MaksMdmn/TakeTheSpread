package go.takethespread.NjTr;


public class NjTrOrder {

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

    private NjTrOrder(Builder builder) {
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

        public Builder(int orderId, NjTrCommand njTrCommand) {
            if (orderId < 0) {
                this.orderId = Integer.valueOf(NjTrAPI.INSTANCE.NewOrderId());
            } else {
                this.orderId = orderId;
            }
            this.command = njTrCommand.toString();
        }

        public Builder account(String val) {
            account = val;
            return this;
        }

        public Builder action(NjTrAction val) {
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

        public Builder orderType(NjTrOrderType val) {
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

        public Builder timeInForce(NjTrTIF val) {
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

        public NjTrOrder build() {
            return new NjTrOrder(this);
        }
    }

    public boolean doOrder() {
        String orderIdTemp;
        if (orderId == -13) {
            orderIdTemp = "";
        } else {
            orderIdTemp = String.valueOf(orderId);
        }
        return NjTrAPI.INSTANCE.Command(command, account, instrument, action,
                        quantity, orderType, limitPrice, stopPrice, timeInForce, oco, orderIdTemp,
                        strategy, strategyId) == 0;
    }

    public int remainingContracts(int orderId){
        String orderIdTemp = String.valueOf(orderId);
        return this.quantity - NjTrAPI.INSTANCE.Filled(orderIdTemp);
    }

}


