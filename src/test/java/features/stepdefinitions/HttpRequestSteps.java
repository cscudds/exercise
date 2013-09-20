package features.stepdefinitions;

import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import features.support.Application;
import features.support.ClientState;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpRequestSteps {
    @Inject
    ClientState state;

    @Inject
    Application app;

    @When("^I GET \"([^\"]*)\"$")
    public void I_GET(String url) throws Throwable {
        state.executeMethod(app.getEndpoint(url));
    }

    @Then("^I see \"([^\"]*)\"$")
    public void I_see(String expectedResponse) throws Throwable {
        assertThat(state.getLastResponse(), containsString(expectedResponse));
    }

    @Then("^it has status (\\d+)$")
    public void it_has_status(int expectedStatus) throws Throwable {
        assertThat(state.getLastResponseStatus(), is(expectedStatus));
    }
}
