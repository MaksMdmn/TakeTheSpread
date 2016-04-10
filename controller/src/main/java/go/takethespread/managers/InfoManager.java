package go.takethespread.managers;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InfoManager {

    private static InfoManager instance;
    private Properties properties;

    private InfoManager(){
        propertiesInit();
    }

    public static InfoManager getInstance() {
        if (instance == null) {
            instance = new InfoManager();
        }
        return instance;
    }

    public Properties getActualProperties(){
        return properties;
    }

    private void propertiesInit() {
        properties = new Properties();
        String fileName = "possibleSettings.properties";
        try (InputStream input = getInstance().getClass().getClassLoader().getResourceAsStream(fileName);) {
            if (input == null) {
                throw new RuntimeException("Settings-example file was unable to find");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
