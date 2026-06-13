package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utils.WebBrowserUtils;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class SearchResultsPage {

    private final SelenideElement smartSuggestion =
            $x("//span[contains(text(),'Show results for')]//a");

    private final SelenideElement brandDropdown =
            $x("//div[text()='Brand']");

    private final SelenideElement brandResultExpand =
            $x("(//div[@class='GN2Hca rQQNAD'])[1]");

    private final SelenideElement brandFilterHP =
            $x("//div[text()='HP']/preceding-sibling::div");

    private final SelenideElement minPriceDropdown =
            $x("//select[@class='hbnjE2']");

    private final SelenideElement maxPriceDropdown =
            $x("(//select[@class='hbnjE2'])[2]");

    private final ElementsCollection productTitles =
            $$x("//div[@class='RG5Slk']");

    private final ElementsCollection productPrices =
            $$x("//div[@class='hZ3P6w DeU9vF']");

    private final ElementsCollection productCards =
            $$x("//div[@data-id]");

    private final By outOfStockLabel =
            By.xpath(".//*[contains(text(),'Out of Stock') or contains(text(),'Currently unavailable')]");

    @Step("Verify results contain the mentioned string")
    public void verifyResultsContain(String keyword) {
        $$(".RG5Slk").filterBy(text(keyword)).first().shouldBe(visible);
    }

    @Step("Verify results contain smart suggestions")
    public void verifySmartSuggestionAlt() {
        smartSuggestion.shouldBe(visible);
    }

    @Step("Apply brand filter: HP")
    public void applyBrandFilter() {
        brandDropdown.shouldBe(visible).click();
        brandResultExpand.shouldBe(visible).click();
        brandFilterHP.shouldBe(visible, Duration.ofSeconds(5)).click();
        waitForVisibleProducts();
    }

    @Step("Apply price filter from {min} to {max}")
    public void applyPriceFilter(int min, int max) {
        selectPriceOption(minPriceDropdown, min);
        selectPriceOption(maxPriceDropdown, max);
        waitForVisiblePrices();
    }

    @Step("Verify products belong to selected brand")
    public void verifyBrandFiltered(String brand) {
        productTitles.shouldBe(sizeGreaterThan(0));
        for (SelenideElement title : productTitles) {
            title.shouldHave(text(brand));
        }
    }

    @Step("Verify products are within selected price range")
    public void verifyPriceRange(int min, int max) {
        waitForVisiblePrices();
        for (String priceText : productPrices.texts()) {
            int value = parsePrice(priceText);
            org.testng.Assert.assertTrue(
                    value >= min && value <= max,
                    "Price out of range: " + value
            );
        }
    }

    @Step("Verify out-of-stock items are marked on PLP")
    public void verifyOutOfStockItemsVisible() {
        waitForVisibleProducts();
        boolean foundOutOfStock = false;
        for (SelenideElement card : productCards) {
            if (card.$(outOfStockLabel).exists()) {
                card.$(outOfStockLabel).shouldBe(visible);
                foundOutOfStock = true;
                break;
            }
        }
        assert foundOutOfStock : "No out-of-stock product found on PLP";
    }

    @Step("Open first product from search results")
    public ProductDetailsPage openFirstProduct() {
        $x("(//div[@data-id])[1]").shouldBe(visible).click();
        WebBrowserUtils.switchToLastTab();
        return new ProductDetailsPage();
    }

    private void waitForVisibleProducts() {
        productCards.filter(visible).shouldHave(sizeGreaterThan(0));
    }

    private void waitForVisiblePrices() {
        productPrices.filter(visible).shouldHave(sizeGreaterThan(0));
    }

    private int parsePrice(String priceText) {
        return Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
    }

    private void selectPriceOption(SelenideElement dropdown, int price) {
        dropdown.shouldBe(visible, enabled)
                .selectOptionContainingText(String.valueOf(price));
    }
}
