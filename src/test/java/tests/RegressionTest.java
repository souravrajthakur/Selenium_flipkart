package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductListingPage;
import pages.SearchResultsPage;

import static utils.WebBrowserUtils.openFlipkartAndClosePopup;

public class RegressionTest extends BaseTest {

    @Test
    public void tc01_validLogin() {
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
    public void tc02_inValidLogin() {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        homePage.clickOnLogin();
        LoginPage loginPage = new LoginPage();
        loginPage.enterEmailOrMobile("dokago7765@naqulu.com");
        loginPage.clickRequestOtp();
        loginPage.verifyUnregisteredMessageVisible();
    }

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
    public void tc05_verifySearchFilters() {
        openFlipkartAndClosePopup();
        HomePage homePage = new HomePage();
        SearchResultsPage results = homePage.search("Laptop");
        results.applyBrandFilter();
        results.verifyBrandFiltered("HP");
        results.applyPriceFilter(50000, 75000);
        results.verifyPriceRange(50000, 75000);
    }

    @Test
    public void tc06_verifyProductCardsOnPLP() {
        openFlipkartAndClosePopup("https://www.flipkart.com/search?q=iphone");
        ProductListingPage plp = new ProductListingPage();
        plp.verifyProductCards();
    }

    @Test
    public void tc07_verifySortByPriceLowToHigh() {
        openFlipkartAndClosePopup("https://www.flipkart.com/search?q=laptop");
        ProductListingPage plp = new ProductListingPage();
        plp.sortByPriceLowToHigh();
        plp.verifyProductsSortedLowToHigh();
    }

    @Test
    public void tc08_verifyPaginationNextPageLoadsCorrectly() {
        openFlipkartAndClosePopup("https://www.flipkart.com/search?q=mobile");
        ProductListingPage plp = new ProductListingPage();
        int initialProductCount = plp.getProductCount();
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
}
