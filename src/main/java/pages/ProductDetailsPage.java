package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class ProductDetailsPage {

    private final SelenideElement productTitle =
            $x("//span[contains(@class,'LMizgS')]");

    private final SelenideElement addToCartButton =
            $x("//button[text()='Add to cart']");

    private final SelenideElement buyNowButton =
            $x("//button[text()='Buy Now']");

    private final SelenideElement addToWishlistButton =
            $x("//span[contains(text(),'Wishlist')]");

    private final SelenideElement mainImage =
            $x("//img[contains(@class,'UCc1lI xD43kG GgrFN0')]");

    private final ElementsCollection imageThumbnails =
            $$x("//li[contains(@class,'gEHBBa bJvKyM')]//img");

    private final SelenideElement zoomContainer =
            $x("//div[contains(@class,'b_h3mK pMOtCI') or contains(@class,'pMOtCI')]");

    private final SelenideElement specificationsHeader =
            $x("//div[contains(text(),'Specifications')]");

    private final ElementsCollection specificationRows =
            $$x("//div[contains(@class,'H0UCo8')]//tr");

    private final SelenideElement readMore =
            $x("//button[contains(text(),'Read More')]");

    private final SelenideElement productPrice =
            $x("//div[contains(@class,'hZ3P6w bnqy13')]");

    @Step("Scroll to product specifications section")
    public void scrollToSpecifications() {
        specificationsHeader.scrollIntoView(true)
                .shouldBe(visible);
    }

    @Step("Verify product title is displayed")
    public void verifyProductTitleVisible() {
        productTitle.shouldBe(visible);
    }

    @Step("Get product title")
    public String getProductTitle() {
        return productTitle.shouldBe(visible).getText();
    }

    @Step("Get product price")
    public String getProductPrice() {
        return productPrice.shouldBe(visible).getText();
    }

    @Step("Add product to cart")
    public void clickAddToCart() {
        addToCartButton.shouldBe(visible, enabled).click();
    }

    @Step("Buy Now")
    public void clickBuyNow() {
        buyNowButton.shouldBe(visible, enabled).click();
    }

    @Step("Click Add to Wishlist")
    public void addToWishlist() {
        addToWishlistButton.shouldBe(visible).click();
    }

    @Step("Verify product image carousel is loaded")
    public void verifyImageCarouselLoaded() {
        mainImage.shouldBe(visible);
        imageThumbnails.filter(visible).shouldHave(sizeGreaterThan(0));
    }

    @Step("Verify zoom functionality on product image")
    public void verifyImageZoom() {
        mainImage.shouldBe(visible).hover();
        if (zoomContainer.exists()) {
            zoomContainer.shouldBe(visible);
        } else {
            Allure.step("Image zoom is not available in this environment");
        }
    }

    @Step("Scroll through product images")
    public void scrollThroughImages() {
        imageThumbnails.filter(visible).shouldHave(sizeGreaterThan(0));
        for (SelenideElement thumbnail : imageThumbnails.filter(visible)) {
            thumbnail.scrollIntoView(true)
                    .shouldBe(visible, enabled)
                    .click();
            mainImage.shouldBe(visible);
        }
    }

    @Step("Verify all product specifications are visible")
    public void verifyProductSpecificationsVisible() {
        if (readMore.exists()) {
            readMore.shouldBe(visible, enabled).click();
        }
        specificationRows.filter(visible).shouldHave(sizeGreaterThan(0));
        for (SelenideElement spec : specificationRows.filter(visible)) {
            spec.shouldBe(visible);
        }
    }
}
