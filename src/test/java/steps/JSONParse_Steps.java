package steps;

import cucumber.api.java.en.When;
import pages.JSON_Actions;

/**
 * Created by carl.laneave on 9/26/16.
 */
public class JSONParse_Steps {
    JSON_Actions json = new JSON_Actions();

    @When("^a user pulls a service call from \"([^\"]*)\" and validates environment: \"([^\"]*)\" against environment: \"([^\"]*)\"$")
    public void a_user_pulls_a_service_call_from_and_validates_environment_against_environment(String url, String env1, String env2) throws Throwable {
        json.production_JSON(url,env1,env2);
    }

}
