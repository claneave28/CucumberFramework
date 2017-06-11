package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.serviceTesting;

/**
 * Created by carl.laneave on 3/8/17.
 */
public class webservice_steps {
    serviceTesting webservices = new serviceTesting();

    @Given("^the user grabs the order numbers$")
    public void the_user_grabs_the_order_numbers() throws Exception {
        webservices.URL1();
    }
    @When("^xyz happens$")
    public void xyz_happens() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^everything is good$")
    public void everything_is_good() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }


}
