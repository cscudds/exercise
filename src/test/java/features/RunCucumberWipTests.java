package features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(tags={"@wip"}, format = {"pretty", "html:target/cucumber"}, glue={"features"})
public class RunCucumberWipTests {
}
