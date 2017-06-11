package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.Login;

/**
 * Created by carl.laneave on 11/30/16.
 */
public class Parallel_Steps {
    Login login = new Login();

    @Given("^user navigates to the Google Screen$")
    public void user_navigates_to_the_Google_Screen()  {
     login.GoogleTest();
    }

    @When("^the main image is clicked$")
    public void the_main_image_is_clicked() {
       login.GoogleSearch();
    }

    @Then("^search is completed$")
    public void search_is_completed() {
     login.GoogleSearchTest();
    }

}
