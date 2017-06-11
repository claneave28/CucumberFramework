package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.RefID;

/**
 * Created by carl.laneave on 2/14/17.
 */
public class RefID_Steps {
    RefID refId = new RefID();

    @Given("^user enters \"([^\"]*)\" and \"([^\"]*)\" for endeca URL$")
    public void user_enters_for_endeca_URL(String url, String page) throws Exception {
       refId.loadURL(url, page);
    }

    @When("^user lands they should pull data for ProductSAPId and write to \"([^\"]*)\"$")
    public void user_lands_they_should_pull_data_for_ProductSAPId_and_write_to(String id) throws Exception {
        refId.webList(id);
    }

    @Then("^continue onto next page and repeat pulling data for ProductSAPId$")
    public void continue_onto_next_page_and_repeat_pulling_data_for_ProductSAPId() throws Exception {
        //refId.pagenationIteration();
    }

}
