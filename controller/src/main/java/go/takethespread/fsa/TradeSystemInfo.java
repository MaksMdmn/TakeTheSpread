package go.takethespread.fsa;

import go.takethespread.*;
import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;
import go.takethespread.impl.SettingDaoImpl;

import java.sql.Connection;
import java.util.*;

public final class TradeSystemInfo {
    private static TradeSystemInfo instance;

    public String account;
    public String host;
    public int port;
    public String instrument_n;
    public String instrument_f;
    public Money entering_dev;
    public int max_size;
    public int max_loss_n;
    public Money commis;
    public int inPos_time_sec;
    public boolean default_spread_using;
    public int spread_ticks_ago;
    public Money default_spread;
    public int current_tactics;
    //    public final Date trade_session_time;
    //    public final Date exception_session_time;
    private Map<Settings, String> settingsMap = null;

    private TradeSystemInfo() {
        initProp();
    }

    public static TradeSystemInfo getInstance() {
        if (instance == null) {
            instance = new TradeSystemInfo();
        } else {
            if (instance.settingsMap == null) {
                throw new RuntimeException("class cannot be used - please, initialize:  settingsMap=" + instance.settingsMap + " (use initProp, when you 1st init).");
            }
        }
        return instance;
    }

    public synchronized String updateLocalValue(Settings setting, String value) {
        String oldVal = settingsMap.get(setting);
        String result = "";
        try {
            DaoFactory<Connection> daoFactory = new PostgresDaoFactoryImpl();
            SettingDaoImpl settingDao = new SettingDaoImpl(daoFactory.getContext());

            settingsMap.put(setting, value);
            result = value;
            updateVal();

            Setting tempSetting = settingDao.readSettingByName(setting.name());
            tempSetting.setLastUpdate(new Date());
            tempSetting.setValue(value);
            settingDao.update(tempSetting);

        } catch (IllegalArgumentException e) {
            settingsMap.put(setting, oldVal);
            System.out.println("try again, cause " + e.getMessage());
            result = oldVal;
            updateVal();
        } catch (PersistException e) {
            System.out.println("try again, cause " + e.getMessage());
            result = oldVal;
            updateVal();
        }
        return result;
    }

    public boolean isSettingExists(String settingName) {
        return settingsMap.containsKey(Settings.valueOf(settingName));
    }

    public Map<Settings, String> getSettingsMap() {
        return new TreeMap<Settings, String>(settingsMap);
    }

    public boolean fullSettingsVerification() {
        Settings[] settingsArr = Settings.values();
        List<Settings> settingsList = Arrays.asList(settingsArr);

        if (settingsMap.isEmpty() || settingsMap == null) {
            return false;
        }

        if (settingsArr.length != settingsMap.size()) {
            return false;
        }

        for (Map.Entry<Settings, String> pair : settingsMap.entrySet()) {
            if (!settingsList.contains(pair.getKey())) {
                return false;
            }
            if (pair.getValue() == null) {
                return false;
            }
        }

        return true;

    }

    private void initProp() {
        if (settingsMap == null) {
            PostgresDaoFactoryImpl daoFactory = new PostgresDaoFactoryImpl();
            settingsMap = new TreeMap<Settings, String>();
            try {
                GenericDao<Setting, Integer> stDao = daoFactory.getDao(daoFactory.getContext(), Setting.class);
                List<Setting> settings = stDao.readAll();

                for (Setting s : settings) {
                    settingsMap.put(s.getName(), s.getValue());
                }

            } catch (PersistException e) {
                e.printStackTrace();
            }

            updateVal();
        }
    }

    private void updateVal() {
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
        spread_ticks_ago = Integer.valueOf(settingsMap.get(Settings.SPREAD_TICKS_AGO_N));
        inPos_time_sec = Integer.valueOf(settingsMap.get(Settings.TIME_IN_POS_SEC));
        default_spread_using = Boolean.valueOf(settingsMap.get(Settings.DEFAULT_SPREAD_USING));
        current_tactics = Integer.valueOf(settingsMap.get(Settings.LOBSTA_TACTICS));
    }

}
