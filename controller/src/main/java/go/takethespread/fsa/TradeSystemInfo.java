package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Settings;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Properties;

public final class TradeSystemInfo {
    private static TradeSystemInfo instance;

    public String account;
    public String host;
    public int port;
    public String instrument_n;
    public String instrument_f;
    public Money entering_dev;
    public int max_size;
    public boolean limit_using;
    public int max_loss_n;
    public Money commis;
    public int spreadCalc_time_sec;
    public int inPos_time_sec;
    public boolean default_spread_using;
    public int min_spreadCalc_n;
    public Money default_spread;
    //    public final Date exception_session_time;
//    public final Date trade_session_time;

    private Properties actualProperties;
    private LinkedHashMap<Settings, String> settingsMap;

    private TradeSystemInfo() {

    }

    public static TradeSystemInfo getInstance() {
        if (instance == null) {
            instance = new TradeSystemInfo();
        } else {
            if (instance.settingsMap == null) {
                throw new RuntimeException("class cannot be used - please, initialize:  settingsMap=" + instance.settingsMap + " (use initProp, when you 1st init.");
            }
        }
        return instance;
    }

    public void initProp() {
        actualProperties = newProp("possibleSettings.properties");
        settingsMap = new LinkedHashMap();

        settingsMap.put(Settings.HOST, actualProperties.getProperty("host"));
        settingsMap.put(Settings.PORT, actualProperties.getProperty("port"));
        settingsMap.put(Settings.INSTRUMENT_N, actualProperties.getProperty("instrument_n"));
        settingsMap.put(Settings.INSTRUMENT_F, actualProperties.getProperty("instrument_f"));
        settingsMap.put(Settings.ACCOUNT, actualProperties.getProperty("account"));
        settingsMap.put(Settings.COMMISSION, actualProperties.getProperty("commis_per_contr_dol"));
        settingsMap.put(Settings.DEFAULT_SPREAD, actualProperties.getProperty("default_spread"));
        settingsMap.put(Settings.DEFAULT_SPREAD_USING, actualProperties.getProperty("default_spread_use"));
        settingsMap.put(Settings.ENTER_DEVIATION, actualProperties.getProperty("enter_pos_dev"));
        settingsMap.put(Settings.MAX_SIZE, actualProperties.getProperty("favorable_size"));
        settingsMap.put(Settings.TIME_IN_POS_SEC, actualProperties.getProperty("inPos_time_sec"));
        settingsMap.put(Settings.LIMIT_USING, actualProperties.getProperty("limit_use"));
        settingsMap.put(Settings.MAX_LOSS_N, actualProperties.getProperty("max_loss_numbers"));
        settingsMap.put(Settings.MIN_SPR_CALC_PERIOD_N, actualProperties.getProperty("min_spreadCalc_period"));
        settingsMap.put(Settings.SPR_CALC_TIME_SEC, actualProperties.getProperty("spreadCalc_time_sec"));

        updateLocalValues();
    }

    public synchronized String updateLocalValue(Settings setting, String value) {
        String oldVal = settingsMap.get(setting);
        String result = "";
        try {
            settingsMap.put(setting, value);
            updateLocalValues();
            result = value;
        } catch (IllegalArgumentException e) {
            settingsMap.put(setting, oldVal);
            updateLocalValues();
            System.out.println("try again madafuk, cause " + e.getMessage());
            result = oldVal;
        }

        return result;
    }

    public boolean isPropNull() {
        return actualProperties == null && settingsMap == null;
    }

    public boolean isPropExists(String propName) {
        return settingsMap.containsKey(propName);
    }

    public LinkedHashMap<Settings, String> getSettingsMap() {
        return new LinkedHashMap<>(settingsMap);
    }

    private Properties newProp(String fileName) {
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

    private void updateLocalValues() {
        account = settingsMap.get(Settings.ACCOUNT);
        host = settingsMap.get(Settings.HOST);
        port = Integer.valueOf(settingsMap.get(Settings.PORT));
        instrument_n = settingsMap.get(Settings.INSTRUMENT_N);
        instrument_f = settingsMap.get(Settings.INSTRUMENT_F);
        max_loss_n = Integer.valueOf(settingsMap.get(Settings.MAX_LOSS_N));
        commis = Money.dollars(Double.valueOf(settingsMap.get(Settings.COMMISSION)));
        max_size = Integer.valueOf(settingsMap.get(Settings.MAX_SIZE));
        entering_dev = Money.dollars(Double.valueOf(settingsMap.get(Settings.ENTER_DEVIATION)));
        default_spread = Money.dollars(Double.valueOf(settingsMap.get(Settings.DEFAULT_SPREAD)));
        spreadCalc_time_sec = Integer.valueOf(settingsMap.get(Settings.SPR_CALC_TIME_SEC));
        min_spreadCalc_n = Integer.valueOf(settingsMap.get(Settings.MIN_SPR_CALC_PERIOD_N));
        inPos_time_sec = Integer.valueOf(settingsMap.get(Settings.TIME_IN_POS_SEC));
        default_spread_using = Boolean.valueOf(settingsMap.get(Settings.DEFAULT_SPREAD_USING));
        limit_using = Boolean.valueOf(settingsMap.get(Settings.LIMIT_USING));
    }

}
