package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

public class BaseTest {
    @BeforeMethod
    public void setUp() {
        //DriverFactory.setupDriver();
        Configuration.timeout = 15000;
        Configuration.pageLoadTimeout = 120000;
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (isHeadless()) {
            options.addArguments("--headless=new");
        }
        Configuration.browserCapabilities = new DesiredCapabilities();
        Configuration.browserCapabilities.setCapability (ChromeOptions.CAPABILITY, options);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
    }

    private boolean isHeadless() {
        return "true".equalsIgnoreCase(System.getProperty("selenide.headless"))
                || "true".equalsIgnoreCase(System.getenv("CI"));
    }

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
