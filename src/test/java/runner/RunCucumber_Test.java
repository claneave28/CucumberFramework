package runner; /**
 * Created by carl.laneave on 5/19/2016.
 * Last Updated 5/31/16 - Carl  Laneave
 * -Created Initial Runner - 5/19/2016 CL
 * -Added Tag Annotation Capabilities - 5/21/2016 CL
 * -Added Extent 1.0 Reporting plugin - 5/24/2016 CL
 * -Upgraded to Extent 1.1 Reporting plugin due to reporting issues - 5/27/2016 CL
 * -Added a BeforeClass for Report setup - 5/27/2016 CL
 * -Modified extent-config.xml to reflect FisherSci as the app being tested in the report Generation - 5/31/2016 CL
 *
 * ****Description*****
 * Junit TestRunner that is required to run Cucumber Suite of tests
 * Uses Extent Reports to show result sets > Outputted to output/Run<num> using Cucumber json reports
 * ********************
 */

import com.cucumber.listener.ExtentCucumberFormatter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.File;



@RunWith(Cucumber.class)

@CucumberOptions(
        plugin = "com.cucumber.listener.ExtentCucumberFormatter",
        monochrome = true,
        tags = "@ProductTitle",  //Used to define Scripts to be ran(@) and Exclude (@~)
        features = "src/features/",  //Location of Feature Files
        format = { "pretty","html: cucumber-html-reports",
                "json: cucumber-html-reports/cucumber.json" },  //Report Generation with test results
        //dryRun = true,
        glue = "steps" )  //Location of all Step Declaration

public class RunCucumber_Test extends AbstractTestNGCucumberTests {
    @BeforeClass
    public static void setup() {
        // Initiates the extent report and generates the output in the output/Run_/report.html file by default.
        ExtentCucumberFormatter.initiateExtentCucumberFormatter();
        ExtentCucumberFormatter.loadConfig(new File("src/test/resources/extent-config.xml"));
    }
}





