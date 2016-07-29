package go.takethespread.fsa;

import go.takethespread.Money;

import java.io.*;
import java.util.Properties;

public final class TradeSystemInfo {
    private Properties actualProperties;
    public String account;
    public String host;
    public int port;
    public String instrument_n;
    public String instrument_f;
    public Money entering_dev;
    public int favorable_size;
    public boolean limit_use;
    public int max_loss_numbers;
    public Money commis_per_one_contract;
    public int spreadCalc_time_sec;
    public int inPos_time_sec;
    public boolean default_spread_use;
    public int min_spreadCalc_period;
    public Money default_spread;
//    public final Date trade_session_time;
//    public final Date exception_session_time;

    public void updateProp(){
        actualProperties = initProp("possibleSettings.properties");
        account = actualProperties.getProperty("account");
        host = actualProperties.getProperty("host");
        port = Integer.valueOf(actualProperties.getProperty("port"));
        instrument_n = actualProperties.getProperty("instrument_n");
        instrument_f = actualProperties.getProperty("instrument_f");
        max_loss_numbers = Integer.valueOf(actualProperties.getProperty("max_loss_numbers"));
        commis_per_one_contract = Money.dollars(Double.valueOf(actualProperties.getProperty("commis_per_contr_dol")));
        favorable_size = Integer.valueOf(actualProperties.getProperty("favorable_size"));
        entering_dev = Money.dollars(Double.valueOf(actualProperties.getProperty("enter_pos_dev")));
        default_spread = Money.dollars(Double.valueOf(actualProperties.getProperty("default_spread")));
        spreadCalc_time_sec = Integer.valueOf(actualProperties.getProperty("spreadCalc_time_sec"));
        min_spreadCalc_period = Integer.valueOf(actualProperties.getProperty("min_spreadCalc_period"));
        inPos_time_sec = Integer.valueOf(actualProperties.getProperty("inPos_time_sec"));
        default_spread_use = Boolean.valueOf(actualProperties.getProperty("default_spread_use"));
        limit_use = Boolean.valueOf(actualProperties.getProperty("limit_use"));
    }

    public boolean isPropNull() {
        return actualProperties == null;
    }

    public boolean isPropExists(String propName) {
        return actualProperties.contains(propName);
    }

    private Properties initProp(String fileName) {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Settings-example file was unable to find");
            }
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }
}
