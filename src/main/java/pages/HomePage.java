package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.page;

public class HomePage {
    private static final Duration PAGE_WAIT = Duration.ofSeconds(30);
    private static final Duration SHORT_WAIT = Duration.ofSeconds(10);

    private final SelenideElement login =
            $x("//span[text()='Login']");

    private final SelenideElement loginPopupCloseButton =
            $x("//span[text()='\u2715']");

    private final SelenideElement homePage =
            $x("//a[@title='Flipkart']");

    private final SelenideElement searchBar =
            $x("//input[@name='q']");

    private final SelenideElement flipkartMinutes =
            $x("//span[text()='Minutes']");

    private final SelenideElement mobilesAndTablets =
            $x("//span[text()='Mobiles & Tablets']");

    private final SelenideElement fashion =
            $x("//span[text()='Fashion']");

    private final SelenideElement electronics =
            $x("//span[text()='Electronics']");

    private final SelenideElement tvsAndAppliances =
            $x("//span[text()='TVs & Appliances']");

    private final SelenideElement homeAndFurniture =
            $x("//span[text()='Home & Furniture']");

    private final SelenideElement flightBookings =
            $x("//span[text()='Flight Bookings']");

    private final SelenideElement beautyFood =
            $x("//span[text()='Beauty, Food..']");

    private final SelenideElement grocery =
            $x("//span[text()='Grocery']");

    private final SelenideElement flipkartMinutesHeading =
            $x("//h1[text()='Flipkart Minutes']");

    private final SelenideElement mobilesAndTabletsHeading =
            $x("//h1[text()='Mobile Phones']");

    private final SelenideElement electronicsAll =
            $x("//a[text()='All']");

    private final SelenideElement electronicsHeading =
            $x("//h1[text()='Audio & Video']");

    @Step("Search product: {query}")
    public SearchResultsPage search(String query) {
        searchBar.shouldBe(visible, PAGE_WAIT).setValue(query).pressEnter();
        return page(SearchResultsPage.class);
    }

    @Step("Click on login")
    public void clickOnLogin() {
        login.shouldBe(visible, PAGE_WAIT).click();
    }

    @Step("Click on Flipkart home logo")
    public void clickOnLoginHomePage() {
        homePage.shouldBe(visible, PAGE_WAIT).click();
    }

    @Step("Click on login popup close button")
    public void closeLoginPopup() {
        loginPopupCloseButton.shouldBe(visible, PAGE_WAIT).click();
    }

    @Step("Verify Flipkart minutes on HomePage")
    public void verifyFlipkartMinutesVisible() {
        verifyVisibleAndClickable(flipkartMinutes);
    }

    @Step("Verify Flipkart minutes Landing")
    public void verifyFlipkartMinutesLanding() {
        flipkartMinutes.shouldBe(visible, PAGE_WAIT).click();
        flipkartMinutesHeading.shouldBe(visible, SHORT_WAIT);
    }

    @Step("Verify Mobiles & Tablets on HomePage")
    public void verifyMobilesAndTabletsVisible() {
        verifyVisibleAndClickable(mobilesAndTablets);
    }

    @Step("Hover on Mobiles & Tablets")
    public void hoverOnMobilesAndTablets() {
        mobilesAndTablets.shouldBe(visible, PAGE_WAIT).hover();
    }

    @Step("Verify Mobiles & Tablets Landing")
    public void verifyMobilesAndTabletsLanding() {
        mobilesAndTablets.shouldBe(visible, PAGE_WAIT).click();
        mobilesAndTabletsHeading.shouldBe(visible, SHORT_WAIT);
    }

    @Step("Verify Fashion on HomePage")
    public void verifyFashionVisible() {
        verifyVisibleAndClickable(fashion);
    }

    @Step("Verify Electronics on HomePage")
    public void verifyElectronicsVisible() {
        verifyVisibleAndClickable(electronics);
    }

    @Step("Hover on Electronics")
    public void hoverOnElectronics() {
        electronics.shouldBe(visible, PAGE_WAIT).hover();
    }

    @Step("Verify Electronics Landing")
    public void verifyElectronicsLanding() {
        electronicsAll.shouldBe(visible, PAGE_WAIT).click();
        electronicsHeading.shouldBe(visible, SHORT_WAIT);
    }

    @Step("Verify TVs & Appliances on HomePage")
    public void verifyTvsAndAppliancesVisible() {
        verifyVisibleAndClickable(tvsAndAppliances);
    }

    @Step("Verify Home & Furniture on HomePage")
    public void verifyHomeAndFurnitureVisible() {
        verifyVisibleAndClickable(homeAndFurniture);
    }

    @Step("Verify Flight Bookings on HomePage")
    public void verifyFlightBookingsVisible() {
        verifyVisibleAndClickable(flightBookings);
    }

    @Step("Verify Beauty, Food.. on HomePage")
    public void verifyBeautyFoodVisible() {
        verifyVisibleAndClickable(beautyFood);
    }

    @Step("Verify Grocery on HomePage")
    public void verifyGroceryVisible() {
        verifyVisibleAndClickable(grocery);
    }

    @Step("Open first product from search results")
    public ProductDetailsPage openFirstProduct() {
        $x("(//div[@data-id])[1]").shouldBe(visible).click();
        return new ProductDetailsPage();
    }

    private void verifyVisibleAndClickable(SelenideElement element) {
        element.shouldBe(visible, PAGE_WAIT);
        element.shouldBe(clickable);
    }
}
