package features.stepdefinitions;

import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import features.support.Application;
import features.support.ClientState;
import features.support.Timer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProcessRequestSteps {
    @Inject
    Timer timer;

    @Inject
    ClientState state;

    @Inject
    Application app;

    @When("^I POST (\\d+) processes$")
    public void I_POST_processes(int count) throws Throwable {
        timer.start();
        while (count-- > 0) {
            state.executeMethod(app.process());
        }
    }

    @Then("^I get a valid response for each one$")
    public void I_get_a_valid_response_for_each_one() throws Throwable {
        assertThat(state.getLastResponseStatus() > 199, is(true));
        assertThat(state.getLastResponseStatus() < 300, is(true));
    }

    @Then("^it takes less than (\\d+) second$")
    public void it_takes_less_than_second(int interval) throws Throwable {
        assertThat(timer.stop() < interval, is(true));
    }
}
