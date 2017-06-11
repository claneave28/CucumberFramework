package steps;

/**
 * Created by carl.laneave on 5/19/2016.
 * Tied to Login.feature and basic login features
 *
 */

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.Login;
import util.env;

import java.io.IOException;
import java.net.MalformedURLException;

public class Login_Steps {

    Login login = new Login();
    env env = new env();

    public Login_Steps() throws MalformedURLException {
    }

    //Excel Steps
    @When("^I am on the amps mainscreen$")
    public void i_am_on_the_amps_mainscreen()  {
        System.out.println("Im loading");
    }
    //Excel Steps
    @Then("^I input username and passwords with excel row\"([^\"]*)\" dataset$")
    public void i_input_username_and_passwords_with_excel_row_dataset(int rownum) throws IOException {

    }

    @When("^I goto the mainscreen for excel$")
    public void i_goto_the_mainscreen_for_excel() {
      System.out.println("This is the first step");
    }

    @Then("^I input the column \"([^\"]*)\" that I am searching$")
    public void i_input_the_column_that_I_am_searching(String columnHeader) throws Exception{


    }



    @Given("^user navigates to the AMPS login screen$")
    public void user_navigates_to_the_AMPS_login_screen(){

        login.Login_Validate();

    }

    @When("^user logs in with correct credentials$")
    public void user_logs_in_with_correct_credentials() throws Throwable {
    login.Login();
    }

    @Then("^login should be successful$")
    public void login_should_be_successful() {
        login.Login_Success();
    }

   @When("^user logs in using Username as \"([^\"]*)\" and Password \"([^\"]*)\"$")
    public void user_logs_in_using_Username_as_and_Password(String username, String password) {
        login.LoginProcess(username,password);
    }

    @Then("^error message should display$")
    public void error_message_should_display() {
        login.Login_Fail_BadCred();
    }

    @Then("^error message should display for missing username after username is deleted$")
    public void error_message_should_display_for_missing_username_after_username_is_deleted() {
        login.Login_Fail_Missing_Username();
    }

    @Then("^error message should display for missing username after password is deleted$")
    public void error_message_should_display_for_missing_username_after_password_is_deleted() {
        login.Login_Fail_Missing_Password();
    }

    @Given("^this is a demo step$")
    public void this_is_a_demo_step() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I run this demo step$")
    public void i_run_this_demo_step() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^you should see code snippets$")
    public void you_should_see_code_snippets() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }


}
