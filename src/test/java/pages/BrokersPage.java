package pages;

import net.serenitybdd.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.navigation.Pages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@At(urls = ".*/en/broker*")
public class BrokersPage extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrokersPage.class);

    @FindBy(css = "div.MuiGrid-item .MuiCardContent-root a h6")
    List<WebElement> brokerNames;

    @FindBy(id = "broker-keyword")
    WebElement searchBar;

    @FindBy(xpath = "//button[.='Clear']")
    WebElement clearButton;

    public boolean isDisplayed() {
        return wait.until(ExpectedConditions.titleIs(Pages.BROKER.getName()));
    }

    public List<String> getBrokerNames() {
        loadAllBrokers(brokerNames);
        scrollUp();
        return waitForElementsToBeVisible(brokerNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void loadAllBrokers(List<WebElement> brokerNames) {
        int previousSize = 0;
        int currentSize = brokerNames.size();
        int scrollAmount = 300;

        while (currentSize > previousSize) {
            previousSize = currentSize;
            for (int i = 0; i < 8; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", scrollAmount);
                waitABit(500);
            }
            List<WebElement> newBrokerNames = driver.findElements(By.cssSelector("div.MuiGrid-item .MuiCardContent-root a h6"));
            for (WebElement element : newBrokerNames) {
                if (!brokerNames.contains(element)) {
                    brokerNames.add(element);
                }
            }
            currentSize = brokerNames.size();
        }
    }

    public void searchAndVerifyBrokerDetails(String brokerName) {
        searchBroker(brokerName);
        WebElement broker = waitForElementToBeVisible(getBroker());
        verifyBrokerDetails(broker, brokerName);
    }

    public void searchBroker(String brokerName) {
        waitForElementToBeClickable(clearButton).click();
        wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.cssSelector("div.MuiGrid-item"));
            return elements.size() > 1;
        });
        try {
            waitForElementToBeVisible(searchBar).sendKeys(brokerName + Keys.ENTER);
        }catch (StaleElementReferenceException e){
            waitForElementToBeVisible(driver.findElement(By.id("broker-keyword"))).sendKeys(brokerName + Keys.ENTER);
        }
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("div.MuiGrid-item"), 1));
        List<WebElement> displayedBrokers = driver.findElements(By.cssSelector("div.MuiGrid-item"));
        assertEquals("More than one broker displayed", 1, displayedBrokers.size());
    }

    private WebElement getBroker() {
        return wait.until(driver -> driver.findElement(By.cssSelector("div.MuiGrid-item")));
    }

    @Step("Verify broker details for {0}")
    public void verifyBrokerDetails(WebElement broker, String brokerName) {
        LOGGER.info("Verifying data for broker {}", brokerName);

        String actualName = waitForElementToBeVisible(broker.findElement(By.cssSelector(".MuiCardContent-root a h6"))).getText();
        assertEquals("Broker name mismatch", brokerName, actualName);

        WebElement address = waitForElementToBeVisible(broker.findElement(By.cssSelector(".MuiCardContent-root span")));
        assertTrue(address.isDisplayed());

        WebElement nrOfProperties = waitForElementToBeVisible(broker.findElement(By.cssSelector(".MuiCardContent-root a:nth-of-type(2)")));
        assertTrue(nrOfProperties.isDisplayed());

        waitForElementToBeClickable(broker.findElement(By.tagName("button"))).click();

        List<WebElement> phoneNumbers = waitForElementsToBeVisible(broker.findElements(By.cssSelector(".MuiStack-root:nth-of-type(2) a")));
        phoneNumbers.forEach(element -> assertTrue(element.isDisplayed()));
    }
}
