package pages;

/**
 * Created by carl.laneave on 6/10/2016.
 * Last Updated 6/12/16 - Carl Laneave
 * -Created Initial Objects - 6/10/16 cl
 * -Created Initial Methods - 6/10/16 cl
 * -Added Error Validation - 6/12/16 cl
 *
 * Page Object Repo and Abstraction Layer for AMPS Screen Login.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import util.DriverFactory;
import util.PropertyReader;
import util.env;

import static org.junit.Assert.assertEquals;

public class Login extends DriverFactory {

    //Login
    By btn_logon = By.id("submit_button"); //Logon Button
    By txtbox_username = By.name("j_username"); //UserName Textbox
    By txtbox_password = By.name("j_password"); //Password Textbox
    By txt_error_message = By.className("errorblock"); //Bad username/password combo

    static Logger log = LogManager.getLogger();
    env hook = new env();

    public void Login_Validate(){
        assertEquals("AMPS Login" , driver.getTitle());
    }

    public void Login() {
        String usrname = new PropertyReader().readProperty("UserName");
        String pwd = new PropertyReader().readProperty("Password");
        LoginProcess(usrname, pwd);
    }

    public void LoginProcess(String usrname, String pwd) {


        if (driver.findElement(btn_logon).isDisplayed()) {
            //Send UserName
            driver.findElement(txtbox_username).clear();
            driver.findElement(txtbox_username).sendKeys(usrname);
            //Send Password
            driver.findElement(txtbox_password).clear();
            driver.findElement(txtbox_password).sendKeys(pwd);
            //Click Logon
            hook.clickAttempt(btn_logon);
            //driver.findElement(btn_logon).clickMDL-3150();
        } else {
            log.info("No Login Button Present");
            Assert.fail("No Login Present");
        }
    }

    public void Login_Success(){
        assertEquals("AMPS - Thermo Fisher Scientific" , driver.getTitle());
    }

    public void Login_Fail_BadCred(){
       String badcred = driver.findElement(txt_error_message).getText();
        assertEquals("Your login attempt was not successful, try again.\n" +
                "Cause: Bad credentials" , badcred);
    }

    public void Login_Fail_Missing_Username(){
        driver.findElement(btn_logon).click();
        String missingUsername = driver.findElement(txt_error_message).getText();
        assertEquals("Your login attempt was not successful, try again.\n" +
                "Cause: Empty Username" , missingUsername);
    }

    public void Login_Fail_Missing_Password(){

        driver.findElement(txtbox_username).sendKeys("asdf");
        driver.findElement(btn_logon).click();
        String missingPassword = driver.findElement(txt_error_message).getText();
        assertEquals("Your login attempt was not successful, try again.\n" +
                "Cause: Empty Password" , missingPassword);
    }

    public void GoogleTest(){
        String title = driver.getTitle();
        if(title.equalsIgnoreCase("Google")){
            log.info("Success!");
        }
        else{
            Assert.fail("Incorrect Header");
        }
    }

    public void GoogleSearch(){
        driver.findElement(By.xpath("//*[@id='hplogo']/a/img")).click();
    }

    public void GoogleSearchTest(){
        String title = driver.getCurrentUrl();
        log.info(title);
        if(title.equalsIgnoreCase("https://www.google.com/?gws_rd=ssl#q=Jagdish+Chandra+Bose&oi=ddle&hl=en")){
            log.info("Success!");
        }
        else{
            Assert.fail("Incorrect load");
        }
    }
}