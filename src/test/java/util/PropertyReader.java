package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by carl.laneave on 5/20/2016.
 * I/O Property File Reader
 * Required to read property file for username/pw and Browser Type
 */
public class PropertyReader {
    Properties properties = new Properties();
    InputStream inputStream = null;
    public PropertyReader() {
        loadProperties();
    }
    private void loadProperties() {
        try {
            inputStream = new FileInputStream("src/browser.properties"); //Requires modification currently to choose OS
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readProperty(String key) {
        return properties.getProperty(key);
    }
}
