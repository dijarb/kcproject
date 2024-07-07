package pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage extends PageObject {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = ThucydidesWebDriverSupport.getDriver();
        initializeElements();
        ((JavascriptExecutor) driver).executeScript("window.focus();");
    }

    private void initializeElements() {
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Default wait time of 10 seconds
    }

    protected WebElement waitForElementToBeVisible(WebElement element) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                return wait.until(ExpectedConditions.visibilityOf(element));
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        throw new RuntimeException("Element is not stable after 2 attempts");
    }

    protected List<WebElement> waitForElementsToBeVisible(List<WebElement> elements) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        throw new RuntimeException("Elements are not stable after 2 attempts");
    }

    protected WebElement waitForElementToBeClickable(WebElement element) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(element));
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        throw new RuntimeException("Element is not stable after 2 attempts");
    }

    protected void scrollUp() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -document.body.scrollHeight)");
        waitABit(1000);
    }
}