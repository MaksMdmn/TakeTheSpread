package go.takethespread;

import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.sql.Connection;
import java.util.Date;

public class SettingsDaoTest {
    private static Setting account = new Setting();
    private static Setting host = new Setting();
    private static Setting port = new Setting();
    private static Setting instrument_n = new Setting();
    private static Setting instrument_f = new Setting();
    private static Setting max_loss_n = new Setting();
    private static Setting commis = new Setting();
    private static Setting max_size = new Setting();
    private static Setting entering_dev = new Setting();
    private static Setting default_spread = new Setting();
    private static Setting spread_ticks_ago_n = new Setting();
    private static Setting inPos_time_sec = new Setting();
    private static Setting default_spread_using = new Setting();

    public static void main(String[] args) {
        try {
            DaoFactory<Connection> daoFactory = new PostgresDaoFactoryImpl();
            GenericDao<Setting, Integer> settingDao = daoFactory.getDao(daoFactory.getContext(), Setting.class);
            setMySettings();
            persistMySettings(settingDao);

        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    private static void setMySettings() {
        account.setName(Settings.ACCOUNT);
        account.setLastUpdate(new Date());
        host.setName(Settings.HOST);
        host.setLastUpdate(new Date());
        port.setName(Settings.PORT);
        port.setLastUpdate(new Date());
        instrument_n.setName(Settings.INSTRUMENT_N);
        instrument_n.setLastUpdate(new Date());
        instrument_f.setName(Settings.INSTRUMENT_F);
        instrument_f.setLastUpdate(new Date());
        max_loss_n.setName(Settings.MAX_LOSS_N);
        max_loss_n.setLastUpdate(new Date());
        commis.setName(Settings.COMMISSION);
        commis.setLastUpdate(new Date());
        max_size.setName(Settings.MAX_SIZE);
        max_size.setLastUpdate(new Date());
        entering_dev.setName(Settings.ENTER_DEVIATION);
        entering_dev.setLastUpdate(new Date());
        default_spread.setName(Settings.DEFAULT_SPREAD);
        default_spread.setLastUpdate(new Date());
        spread_ticks_ago_n.setName(Settings.SPREAD_TICKS_AGO_N);
        spread_ticks_ago_n.setLastUpdate(new Date());
        inPos_time_sec.setName(Settings.TIME_IN_POS_SEC);
        inPos_time_sec.setLastUpdate(new Date());
        default_spread_using.setName(Settings.DEFAULT_SPREAD_USING);
        default_spread_using.setLastUpdate(new Date());


        account.setValue("lobstatest");
        host.setValue("localhost");
        port.setValue("8085");
        instrument_n.setValue("CL 10-16");
        instrument_f.setValue("CL 11-16");
        max_loss_n.setValue("2");
        commis.setValue("4.87");
        max_size.setValue("50");
        entering_dev.setValue("0.04");
        default_spread.setValue("0.65");
        spread_ticks_ago_n.setValue("20");
        inPos_time_sec.setValue("10");
        default_spread_using.setValue("true");
    }

    private static void persistMySettings(GenericDao<Setting, Integer> settingDao) throws PersistException {
        settingDao.persist(account);
        settingDao.persist(host);
        settingDao.persist(port);
        settingDao.persist(instrument_n);
        settingDao.persist(instrument_f);
        settingDao.persist(max_loss_n);
        settingDao.persist(commis);
        settingDao.persist(max_size);
        settingDao.persist(entering_dev);
        settingDao.persist(default_spread);
        settingDao.persist(spread_ticks_ago_n);
        settingDao.persist(inPos_time_sec);
        settingDao.persist(default_spread_using);
    }


}
