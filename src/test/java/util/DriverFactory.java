package util;


import cucumber.api.Scenario;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.OperaDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by carl.laneave on 5/20/2016.
 * WebDriver Factory
 * Based on browser.properties for config
 * Currently required to modify device detail to reflect actual devices
 * Will setup with virtual devices for CI testing.  The property file will require browser = android or ios modifications
 * Prior to running through Jenkins.
 * test
 */

public class DriverFactory {

    public static WebDriver driver;
    public static WebDriverWait waitVar = null;
    public static int waitTime = 30;
    public static String propertyFileName = null;
    public static String browser="";
    public static String URL1 = new PropertyReader().readProperty("URL");

    public static void getDriverInstance() throws Exception
    {
        System.out.println("Before Class");
        browser = new PropertyReader().readProperty("browser");
        createNewDriverInstance(browser);

    }

    //mobile.properties has browser=[value].  Function pulls to declare IOS or Android
    public static void createNewDriverInstance(final String browserID) throws Exception {

        if (browserID == null){
            returnFireFoxDriver();
            propertyFileName = "FireFox";
        }
        if (browserID.equalsIgnoreCase("firefox")) {
            returnFireFoxDriver();
            propertyFileName = "FireFox";
        }
        if (browserID.equalsIgnoreCase("chrome")) {
            returnChromeDriver();
            propertyFileName = "Chrome";
        }
        if (browserID.equalsIgnoreCase("IE")) {
            returnIEDriver();
            propertyFileName = "IE";
        }
        if (browserID.equalsIgnoreCase("Edge")) {
            returnEdgeDriver();
            propertyFileName = "Edge";
        }
        if (browserID.equalsIgnoreCase("GhostDriver")) {
            returnGhostDriver();
            propertyFileName = "GhostDriver";
        }
        if (browserID.equalsIgnoreCase("Opera")) {
            returnOperaDriver();
            propertyFileName = "Opera";
        }
        if (browserID.equalsIgnoreCase("Grid")){
            returnGridDriver();
            propertyFileName= "Grid";
        }

        waitVar = new WebDriverWait(driver, waitTime);
        driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

    }

    public static void returnGridDriver() throws MalformedURLException {

DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setBrowserName("firefox");
        capability.setPlatform(Platform.LINUX);
        driver = new RemoteWebDriver(new URL("http://10.253.139.107:4444/wd/hub"), capability);
        driver.get(URL1);
    }

    public static void returnFireFoxDriver() {

        //File pathBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        //FirefoxBinary Binary = new FirefoxBinary(pathBinary);
       // FirefoxProfile firefoxPro = new FirefoxProfile();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        //driver = new FirefoxDriver();
        waitVar = new WebDriverWait(driver, 10);
        driver.get(URL1);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        System.out.println("FireFox started");

    }
    public static void returnChromeDriver() {
        //ChromeDriverManager.setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        //Path needs to reflect where you are storing your chromedriver
        String exePath ="/usr/local/share/chromedriver";
        //String exePath = "driver/chromedriver";
        System.setProperty("webdriver.chrome.driver", exePath);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        waitVar = new WebDriverWait(driver, 90);
        driver.get(URL1);
        System.out.println("Chrome started");

    }
    public static void returnGhostDriver() {
        driver = new PhantomJSDriver();
        waitVar = new WebDriverWait(driver, 90);
        driver.get(URL1);
        System.out.println("PhantomJS/GhostDriver Started");
    }
    public static void returnIEDriver() {
        InternetExplorerDriverManager.setup();
        driver = new InternetExplorerDriver();
        waitVar = new WebDriverWait(driver, 30);
        driver.get(URL1);
        System.out.println("IE started");
    }

    public static void returnEdgeDriver() {
        InternetExplorerDriverManager.setup();
        driver = new InternetExplorerDriver();
        waitVar = new WebDriverWait(driver, 90);
        driver.get(URL1);
        System.out.println("IE started..Edge not configured yet");
    }

    public static void returnOperaDriver() {
        OperaDriverManager.setup();
        driver = new OperaDriver();
        waitVar = new WebDriverWait(driver, 90);
        driver.get(URL1);
        System.out.println("Opera Started");
    }


    public void destroyDriver() throws Exception{
        System.out.println("Driver Quit...");
        driver.quit();
    }

    //Called on any Scenario fails.
    public void screenShot(Scenario scenario) {
        final byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }

}
