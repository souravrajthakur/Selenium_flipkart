package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class ProductListingPage {

    private final ElementsCollection productCards =
            $$x("//div[@data-id]");

    private final String productNameXpath = ".//div[contains(@class,'RG5Slk')]";
    private final String productPriceXpath = ".//div[contains(@class,'hZ3P6w DeU9vF')]";
    private final String productRatingXpath = ".//div[contains(@class,'MKiFS6')]";
    private final String productImageXpath = ".//img";

    private final SelenideElement sortLowToHigh =
            $x("//div[text()='Price -- Low to High']");

    private final ElementsCollection productPrices =
            $$x("//div[@data-id]//div[contains(@class,'hZ3P6w DeU9vF')]");

    private final SelenideElement nextPageButton =
            $x("//a[.//span[normalize-space()='Next']]");

    @Step("Verify product cards contain name, price, rating and image")
    public void verifyProductCards() {
        productCards.filter(visible).shouldHave(sizeGreaterThan(0));
        for (SelenideElement card : productCards) {
            card.scrollIntoView(true);
            card.$x(productNameXpath)
                    .shouldBe(visible)
                    .shouldNotBe(empty);
            card.$x(productPriceXpath)
                    .shouldBe(visible)
                    .shouldNotBe(empty);
            SelenideElement rating = card.$x(productRatingXpath);
            if (rating.exists()) {
                rating.shouldBe(visible);
            }
            card.$x(productImageXpath)
                    .shouldBe(visible)
                    .shouldHave(attributeMatching("src", ".+"));
        }
    }

    @Step("Select sort option: Price Low to High")
    public void sortByPriceLowToHigh() {
        sortLowToHigh.shouldBe(visible).click();
        productPrices.filter(visible).shouldHave(sizeGreaterThan(0));
    }

    @Step("Verify products are sorted by price from low to high")
    public void verifyProductsSortedLowToHigh() {
        List<Integer> actualPrices = new ArrayList<>();

        for (SelenideElement price : productPrices.filter(visible)) {
            String amount = price.getText().replaceAll("[^0-9]", "");
            actualPrices.add(Integer.parseInt(amount));
        }

        List<Integer> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);

        assert actualPrices.equals(sortedPrices)
                : "Products are NOT sorted Low to High\nActual: "
                + actualPrices + "\nExpected: " + sortedPrices;
    }

    @Step("Scroll to pagination section")
    public void scrollToPagination() {
        nextPageButton.shouldBe(visible).scrollIntoView("{block: 'center'}");
    }

    @Step("Click on Next page button")
    public void clickNextPage() {
        nextPageButton.shouldBe(visible, enabled)
                .scrollIntoView("{block: 'center'}")
                .click();
    }

    @Step("Verify next page products are loaded")
    public void verifyNextPageLoaded(int previousCount) {
        productCards.shouldHave(sizeGreaterThanOrEqual(previousCount));
        productCards.first().shouldBe(visible);
    }

    @Step("Get current product count")
    public int getProductCount() {
        ElementsCollection visibleCards = productCards.filter(visible);
        visibleCards.shouldHave(sizeGreaterThan(0));
        return visibleCards.size();
    }
}
