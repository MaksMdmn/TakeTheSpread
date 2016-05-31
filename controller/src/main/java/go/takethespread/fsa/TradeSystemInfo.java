package go.takethespread.fsa;

import go.takethespread.Money;

import java.io.*;
import java.util.Properties;

public final class TradeSystemInfo {
    private Properties actualProperties;
    public final String account;
    public final String host;
    public final int port;
    public final String instrument_n;
    public final String instrument_f;
    public Money entering_spread;
    public Money leaving_spread;
    public final Money entering_dev;
    public final Money leaving_dev;
    public int favorable_size;
    public boolean limit_use;
    public Money max_loss;
    public int max_loss_numbers;
    public final Money commis_per_one_contract;
//    public final Date trade_session_time;
//    public final Date exception_session_time;

    public TradeSystemInfo() {
        actualProperties = initProp("possibleSettings.properties");
        account = actualProperties.getProperty("account");
        host = actualProperties.getProperty("host");
        port = Integer.valueOf(actualProperties.getProperty("port"));
        instrument_n = actualProperties.getProperty("instrument_n");
        instrument_f = actualProperties.getProperty("instrument_f");
        entering_spread = Money.dollars(Double.valueOf(actualProperties.getProperty("entering_spread")));
        leaving_spread = Money.dollars(Double.valueOf(actualProperties.getProperty("leaving_spread")));
        entering_dev = Money.dollars(Double.valueOf(actualProperties.getProperty("entering_dev")));
        leaving_dev = Money.dollars(Double.valueOf(actualProperties.getProperty("leaving_dev")));
        favorable_size = Integer.valueOf(actualProperties.getProperty("favorable_size"));
        limit_use = Boolean.valueOf(actualProperties.getProperty("limit_use"));
        max_loss = Money.dollars(Double.valueOf(actualProperties.getProperty("max_loss")));
        max_loss_numbers = Integer.valueOf(actualProperties.getProperty("max_loss_numbers"));
        commis_per_one_contract = Money.dollars(Double.valueOf(actualProperties.getProperty("commis_per_contract")));
    }

    public boolean isPropNull() {
        return actualProperties == null;
    }

    public boolean isPropExists(String propName) {
        return actualProperties.contains(propName);
    }

    public void testUpdateEnterSpread(double val) {
        try (FileOutputStream out = new FileOutputStream("possibleSettings.properties")) {
            actualProperties.setProperty("entering_spread", String.valueOf(val));
            actualProperties.store(out, null);
            entering_spread = Money.dollars(Double.valueOf(actualProperties.getProperty("entering_spread")));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
