package steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DriverFactory;

import java.net.MalformedURLException;

/**
 * Created by carl.laneave on 6/10/2016.
 * Initializes Environment, Breaks down driver and takes screenshots of failed scenarios
 * Before/After run after each scenario
 * Utilizes browser.properties to define driver
 */
public class init {
    DriverFactory df = new DriverFactory();
    static Logger log = LogManager.getLogger();

    @Before
    public void setUp() throws Throwable {
        try {
            df.getDriverInstance();
            System.out.println("Driver Init...");
        } catch (MalformedURLException e) {
            log.error("Could not initiate driver: "+e);
            e.printStackTrace();
        }

    }

    @After
    //Needs Cleaned Up
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                df.screenShot(scenario);
                log.info("Scenario Failed:  Screenshot Taken!");
            }
        } finally {
            try {
                df.destroyDriver();
            } catch (Exception e) {
                log.error("Error on teardown: "+e);
                e.printStackTrace();
            }
        }
    }
}

