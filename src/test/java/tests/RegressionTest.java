package tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import base.BaseTest;
import io.qameta.allure.Allure;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductListingPage;
import pages.SearchResultsPage;

public class RegressionTest extends BaseTest {
    private static final String FLIPKART_HOME_URL = "https://www.flipkart.com";
    private static final By FLIPKART_POPUP_CLOSE_BUTTON = By.xpath("//span[text()='\u2715']");
    private static final Duration POPUP_WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final long POPUP_POLL_INTERVAL_MS = 500;

    private void openFlipkartAndClosePopup() {
        openFlipkartAndClosePopup(FLIPKART_HOME_URL);
    }

    private void openFlipkartAndClosePopup(String url) {
        open(url);
        closeFlipkartPopup();
    }

    private void closeFlipkartPopup() {
        WebDriver driver = getWebDriver();
        long endTime = System.currentTimeMillis() + POPUP_WAIT_TIMEOUT.toMillis();

        while (System.currentTimeMillis() < endTime) {
            driver.switchTo().defaultContent();
            if (clickPopupCloseButtonIfVisible(driver)) {
                return;
            }

            List<WebElement> frames = driver.findElements(By.tagName("iframe"));
            for (WebElement frame : frames) {
                driver.switchTo().defaultContent();
                try {
                    driver.switchTo().frame(frame);
                    if (clickPopupCloseButtonIfVisible(driver)) {
                        driver.switchTo().defaultContent();
                        return;
                    }
                } catch (NoSuchFrameException | StaleElementReferenceException ignored) {
                    Allure.step("Popup iframe changed before it could be checked, continuing");
                }
            }

            sleep(POPUP_POLL_INTERVAL_MS);
        }

        driver.switchTo().defaultContent();
        Allure.step("Flipkart popup close button not visible, continuing test execution");
    }

    private boolean clickPopupCloseButtonIfVisible(WebDriver driver) {
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

    @Test
    public void tc01_validLogin()
    {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        homePage.clickOnLogin();
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmailOrMobile("souravraj9999@gmail.com");
        loginPage.clickRequestOtp();
        loginPage.verifyOtpScreenDisplayed();
        loginPage.verifyVerifyButtonVisible();
    }

    @Test
    public void tc02_inValidLogin()
    {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        homePage.clickOnLogin();
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmailOrMobile("dokago7765@naqulu.com");
        loginPage.clickRequestOtp();
        loginPage.verifyUnregisteredMessageVisible();
    }

    //@Test
    // public void tc03_verifyTopNavigationMenus() {
    //     openFlipkartAndClosePopup();
    //     HomePage homePage = new HomePage();
    //     homePage.verifyFlipkartMinutesVisible();
    //     homePage.verifyMobilesAndTabletsVisible();
    //     homePage.verifyFashionVisible();
    //     homePage.verifyElectronicsVisible();
    //     homePage.verifyTvsAndAppliancesVisible();
    //     homePage.verifyHomeAndFurnitureVisible();
    //     homePage.verifyFlightBookingsVisible();
    //     homePage.verifyBeautyFoodVisible();
    //     homePage.verifyGroceryVisible();
    // }

    // @Test
    // public void tc04_verifyProductCategoryNavigation() {
    //     openFlipkartAndClosePopup();
    //     HomePage homePage = new HomePage();
    //     homePage.verifyFlipkartMinutesVisible();
    //     homePage.verifyFlipkartMinutesLanding();
    //     back();
    //     try {
    //         homePage.closeLoginPopup();
    //         homePage.hoverOnMobilesAndTablets();
    //     }
    //     catch (NoSuchElementException | org.openqa.selenium.WebDriverException | com.codeborne.selenide.ex.ElementNotFound e)
    //     {
    //         Allure.step("Login popup not present, continuing test execution");
    //     }
    //     homePage.verifyMobilesAndTabletsVisible();
    //     homePage.verifyMobilesAndTabletsLanding();
    //     back();
    //     homePage.verifyElectronicsVisible();
    //     homePage.hoverOnElectronics();
    //     homePage.verifyElectronicsLanding();
    //     back();
    // }

    @Test
    public void tc03_verifySearchBarValid() {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        SearchResultsPage results = homePage.search("iPhone 17");
        results.verifyResultsContain("iPhone 17");
    }

    @Test
    public void tc04_verifySearchBarSmartSuggestions() {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        SearchResultsPage results = homePage.search("iPhon 17");
        results.verifySmartSuggestionAlt();
        results.verifyResultsContain("iPhone 17");
    }

    @Test
    public void tc05_verifySearchFilters() throws InterruptedException {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        SearchResultsPage results = homePage.search("Laptop");
        results.applyBrandFilter();
        results.verifyBrandFiltered("HP");
        results.applyPriceFilter("₹50000", "₹75000");
        results.verifyPriceRange(50000, 75000);
    }

    @Test
    public void tc0_verifyProductCardsOnPLP() throws InterruptedException {
        openFlipkartAndClosePopup("https://www.flipkart.com/search?q=iphone");
        ProductListingPage plp = new ProductListingPage();
        plp.verifyProductCards();
    }

    @Test
    public void tc07_verifySortByPriceLowToHigh() throws InterruptedException {
        openFlipkartAndClosePopup("https://www.flipkart.com/search?q=laptop");
        ProductListingPage plp = new ProductListingPage();
        plp.sortByPriceLowToHigh();
        plp.verifyProductsSortedLowToHigh();
    }

    @Test
    public void tc08_verifyPaginationNextPageLoadsCorrectly() throws InterruptedException {
        openFlipkartAndClosePopup("https://www.flipkart.com/search?q=mobile");
        ProductListingPage plp = new ProductListingPage();
        int initialProductCount = plp.getProductCount();
        Thread.sleep(3000);
        plp.scrollToPagination();
        plp.clickNextPage();
        plp.verifyNextPageLoaded(initialProductCount);
    }

    @Test
    public void tc09_verifyOutOfStockItemsOnPLP() {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        SearchResultsPage results = homePage.search("iphone");
        results.verifyOutOfStockItemsVisible();
    }

    // @Test
    // public void tc12_verifyProductImagesCarousel() throws InterruptedException {
    //     openFlipkartAndClosePopup();
    //     HomePage homePage = new HomePage();
    //     SearchResultsPage results = homePage.search("iphone");
    //     ProductDetailsPage pdp = results.openFirstProduct();
    //     pdp.verifyImageCarouselLoaded();
    //     pdp.scrollThroughImages();
    //     pdp.verifyImageZoom();
    // }

    // @Test
    // public void tc13_verifyProductSpecs() throws InterruptedException {
    //     openFlipkartAndClosePopup();
    //     HomePage homePage = new HomePage();
    //     SearchResultsPage results = homePage.search("iPhone 14");
    //     results.openFirstProduct();
    //     ProductDetailsPage pdp = new ProductDetailsPage();
    //     pdp.scrollToSpecifications();
    //     pdp.verifyProductSpecificationsVisible();
    // }

    // @Test()
    // public void tc14_addToCartFromPDPTest() {
    //     openFlipkartAndClosePopup();
    //     HomePage homePage = new HomePage();
    //     SearchResultsPage results = homePage.search("iPhone 17 Pro Max");
    //     results.openFirstProduct();
    //     ProductDetailsPage pdp = new ProductDetailsPage();
    //     String expectedTitle = pdp.getProductTitle();
    //     String expectedPrice = pdp.getProductPrice();
    //     pdp.clickAddToCart();
    //     CartPage cart = new CartPage();
    //     Assert.assertEquals(
    //             cart.getCartProductTitle(),
    //             expectedTitle,
    //             "Incorrect product added to cart"
    //     );
    //     Assert.assertTrue(
    //             cart.getCartProductPrice().contains(expectedPrice),
    //             "Price mismatch in cart"
    //     );
    //     cart.verifyPlaceOrderButton();
    //     cart.clickPlaceOrder();
    //     CheckoutPage checkout = new CheckoutPage();
    //     checkout.verifyCheckoutPage();
    // }

    // @Test()
    // public void tc15_buyNowTest() {
    //     openFlipkartAndClosePopup();
    //     HomePage homePage = new HomePage();
    //     SearchResultsPage results = homePage.search("iPhone 17 Pro Max");
    //     results.openFirstProduct();
    //     ProductDetailsPage pdp = new ProductDetailsPage();
    //     pdp.clickBuyNow();
    //     CheckoutPage checkout = new CheckoutPage();
    //     checkout.verifyCheckoutPage();
    // }
}
