package step_definitions;

import io.cucumber.java.en.*;
import net.thucydides.model.environment.*;
import net.thucydides.model.util.*;
import pages.*;

import java.util.*;

import static net.serenitybdd.core.Serenity.getDriver;
import static org.junit.Assert.assertTrue;

public class BrokersStepDefinition {

    private final transient BrokersPage brokersPage = new BrokersPage();
    private final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    private final String baseUrl = environmentVariables.getProperty("serenity.base.url");
    private List<String> brokerNames;

    @Given("I am on the Yavlena brokers page")
    public void userIsAtBrokersPage(){
        getDriver().get(baseUrl);
        assertTrue(brokersPage.isDisplayed());
    }

    @When("I get the names of all brokers")
    public void getNamesOfBrokers(){
        brokerNames = brokersPage.getBrokerNames();
        System.out.println(brokerNames);
        System.out.println(brokerNames.size());
    }

    @Then("I search and verify details for each broker")
    public void searchAndVerifyBrokerDetails(){
        for (String brokerName : brokerNames) {
            brokersPage.searchAndVerifyBrokerDetails(brokerName);
        }
    }
}
