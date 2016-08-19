package go.takethespread.managers;

public class StatusManager implements StatusListener {

    private  boolean isLogin = false;
    private  boolean isRun = false;
    private  boolean isSettingsActual = false;

    private static StatusManager instance;

    private StatusManager(){
    }

    public static StatusManager getInstance() {
        if (instance == null) {
            instance = new StatusManager();
        }
        return instance;
    }


    @Override
    public void loginStatusChanged() {

    }

    @Override
    public void runStatusChanged() {

    }

    @Override
    public void settingsStatucChanged() {

    }

    public synchronized boolean isLogin() {
        return isLogin;
    }

    public synchronized boolean isRun() {
        return isRun;
    }

    public synchronized boolean isSettingsActual() {
        return isSettingsActual;
    }
}
