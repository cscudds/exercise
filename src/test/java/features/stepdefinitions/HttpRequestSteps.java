package features.stepdefinitions;

import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import features.support.Application;
import features.support.ClientState;
import org.apache.commons.io.IOUtils;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

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

    @Then("^I see JSON like \"([^\"]*)\"$")
    public void I_see_JSON_like(String filename) throws Throwable {
        JSONAssert.assertEquals(expectedJSON(filename), state.getLastResponse(), false);
    }

    private String expectedJSON(String filename) {
        InputStream in = this.getClass().getResourceAsStream(String.format("/json/%s", filename));
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(in, writer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Can't read JSON file " + filename);
        }
        return writer.toString();
    }
}
