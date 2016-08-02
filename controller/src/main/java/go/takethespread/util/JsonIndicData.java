package go.takethespread.util;

public class JsonIndicData {
    private int pos_n;
    private int pos_f;
    private double spot_n;
    private double spot_f;
    private double calcSpr;
    private double curSpr;
    private double cash;
    private double buyPw;
    private int deals;
    private int deals_prf;
    private int deals_ls;
    private double commis;
    private double pnl;

    public int getPos_n() {
        return pos_n;
    }

    public void setPos_n(int pos_n) {
        this.pos_n = pos_n;
    }

    public int getPos_f() {
        return pos_f;
    }

    public void setPos_f(int pos_f) {
        this.pos_f = pos_f;
    }

    public double getSpot_n() {
        return spot_n;
    }

    public void setSpot_n(double spot_n) {
        this.spot_n = spot_n;
    }

    public double getSpot_f() {
        return spot_f;
    }

    public void setSpot_f(double spot_f) {
        this.spot_f = spot_f;
    }

    public double getCalcSpr() {
        return calcSpr;
    }

    public void setCalcSpr(double calcSpr) {
        this.calcSpr = calcSpr;
    }

    public double getCurSpr() {
        return curSpr;
    }

    public void setCurSpr(double curSpr) {
        this.curSpr = curSpr;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getBuyPw() {
        return buyPw;
    }

    public void setBuyPw(double buyPw) {
        this.buyPw = buyPw;
    }

    public int getDeals() {
        return deals;
    }

    public void setDeals(int deals) {
        this.deals = deals;
    }

    public int getDeals_prf() {
        return deals_prf;
    }

    public void setDeals_prf(int deals_prf) {
        this.deals_prf = deals_prf;
    }

    public int getDeals_ls() {
        return deals_ls;
    }

    public void setDeals_ls(int deals_ls) {
        this.deals_ls = deals_ls;
    }

    public double getCommis() {
        return commis;
    }

    public void setCommis(double commis) {
        this.commis = commis;
    }

    public double getPnl() {
        return pnl;
    }

    public void setPnl(double pnl) {
        this.pnl = pnl;
    }
}
