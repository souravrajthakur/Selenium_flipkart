package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import com.codeborne.selenide.WebDriverRunner;

public class WebBrowserUtils {
    public static final String FLIPKART_HOME_URL = "https://www.flipkart.com";
    private static final By FLIPKART_POPUP_CLOSE_BUTTON = By.xpath("//span[text()='\u2715']");
    private static final Duration POPUP_WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final long POPUP_POLL_INTERVAL_MS = 500;

    public static void openFlipkartAndClosePopup() {
        openFlipkartAndClosePopup(FLIPKART_HOME_URL);
    }

    public static void openFlipkartAndClosePopup(String url) {
        open(url);
        closeFlipkartPopup();
    }

    public static void closeFlipkartPopup() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        try {
            new WebDriverWait(driver, POPUP_WAIT_TIMEOUT)
                    .pollingEvery(Duration.ofMillis(POPUP_POLL_INTERVAL_MS))
                    .until(WebBrowserUtils::closePopupInPageOrFrame);
        } catch (TimeoutException e) {
            Allure.step("Flipkart popup close button not visible, continuing test execution");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    private static boolean closePopupInPageOrFrame(WebDriver driver) {
        driver.switchTo().defaultContent();
        if (clickPopupCloseButtonIfVisible(driver)) {
            return true;
        }

        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        for (WebElement frame : frames) {
            driver.switchTo().defaultContent();
            try {
                driver.switchTo().frame(frame);
                if (clickPopupCloseButtonIfVisible(driver)) {
                    return true;
                }
            } catch (NoSuchFrameException | StaleElementReferenceException ignored) {
                Allure.step("Popup iframe changed before it could be checked, continuing");
            }
        }

        return false;
    }

    private static boolean clickPopupCloseButtonIfVisible(WebDriver driver) {
        try {
            for (WebElement closeButton : driver.findElements(FLIPKART_POPUP_CLOSE_BUTTON)) {
                if (closeButton.isDisplayed() && closeButton.isEnabled()) {
                    closeButton.click();
                    Allure.step("Flipkart popup closed");
                    return true;
                }
            }
        } catch (WebDriverException e) {
            return false;
        }

        return false;
    }

    public static void switchToLastTab() {
        var windowHandles = WebDriverRunner.getWebDriver().getWindowHandles();
        var lastWindow = windowHandles.toArray()[windowHandles.size() - 1].toString();
        WebDriverRunner.getWebDriver().switchTo().window(lastWindow);
    }

    public static void switchToFirstTab() {
        switchTo().window(0);
    }

}
