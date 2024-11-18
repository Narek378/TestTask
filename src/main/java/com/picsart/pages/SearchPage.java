package com.picsart.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.List;


import static org.testng.Assert.assertTrue;

public class SearchPage {
    private WebDriver driver;

    private WebDriverWait wait;

    private By acceptCookiesButton = By.cssSelector("button[id='onetrust-accept-btn-handler']");
    private By mainIframe = By.cssSelector("iframe[data-testid='com.picsart.social.search']");

    private By searchResultsText = By.xpath("//*[contains(text(),'unique photos from our library')]");
    private By filterButton = By.cssSelector("[data-testid='search-header-filter']");

    private By filterPanel = By.cssSelector("[data-testid='search-filter-root']");

    private String licenseOptionTemplate = "input[data-testid='checkbox-item-check'][aria-label='licenses-%s-checkbox']";

    private By plusAssetIndicator = By.cssSelector("[data-testid='premium-icon-root']");
    private By assetItem = By.cssSelector("[data-testid='search-card-root']");

    private String saveButton = "save_button_item%s";
    private String likeButton = "like_button_item%s";
    private String tryNowButton = "try_now_button_item%s";
    private By signInPopup = By.cssSelector("[data-testid='registration-modal-container']");

    private By closePopupButton = By.cssSelector("[data-testid='modal-close-icon']");

    private By canvasContainer = By.cssSelector("[data-test='canvas-container']");
    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    public void open() {
        driver.get("https://picsart.com/search/images/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(acceptCookiesButton)).click();
    }

    public void switchToMainIframe() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainIframe));
        driver.switchTo().frame(driver.findElement(mainIframe));
    }

    @Step("Toggle filter to open/close")
    public void toggleFilter() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(filterButton)).click();
    }

    @Step("Verify that the filters disappear")
    public void verifyFiltersDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(filterPanel));
    }


    @Step("Click on License filter option: {option}")
    public void clickLicenseOption(String option) {
        By checkboxLocator = By.cssSelector(String.format(licenseOptionTemplate, option));
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        WebElement searchResults = driver.findElement(searchResultsText);
        String textContent = searchResults.getText();
        checkbox.click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(searchResultsText, textContent)));
    }
    @Step("Click on License filter option: {option}")
    public void clickLicenseOptionMobile(String option) {
        By checkboxLocator = By.cssSelector(String.format(licenseOptionTemplate, option));
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
    }
    @Step("Verify there are no PLUS assets displayed")
    public void verifyNoPlusAssetsAreDisplayed() {
        List<WebElement> assetElements = driver.findElements(assetItem);
        for (WebElement asset : assetElements) {
            List<WebElement> crownIcons = asset.findElements(plusAssetIndicator);
            assertTrue(crownIcons.isEmpty(), "Found a PLUS asset with a crown icon: " + asset.getAttribute("id"));
        }
    }

    @Step("Hover over an asset and verify Like, Save, and Try Now buttons are displayed")
    public void hoverOverAssetAndVerifyButtonsAreDisplayed() {
        List<WebElement> assetElements = driver.findElements(assetItem);
        int index = 0;
        WebElement asset = assetElements.get(index);
        Actions actions = new Actions(driver);
        actions.moveToElement(asset).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(likeButton, index))));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(saveButton, index))));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(tryNowButton, index))));
    }

    public void verifyButtonsAreDisplayedMobile() {
        List<WebElement> assetElements = driver.findElements(assetItem);
        int index=0;
        for (WebElement assetItem : assetElements) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(likeButton, index))));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(saveButton, index))));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(tryNowButton, index))));
            index ++;
        }

    }

    @Step("Click Like button and verify Sign-in popup appears")
    public void clickLikeAndVerifySignInPopup() {
        driver.findElement((By.id(String.format(likeButton, 0)))).click();
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.visibilityOfElementLocated(signInPopup));

    }

    @Step("Close Sign-in popup")
    public void closePopup() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(closePopupButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(signInPopup));

    }

    @Step("Hover over a PLUS asset and verify only Try Now button appears")
    public void hoverOverPlusAssetAndVerifyTryNowOnlyIsDisplayed() {
        List<WebElement> assetElements = driver.findElements(assetItem);
        String targetAutomationValue = "search-item-premium";


        WebElement matchingAsset = assetElements.stream()
                .filter(asset -> targetAutomationValue.equals(asset.getAttribute("data-automation")))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No asset found with data-automation value: " + targetAutomationValue));


        int index = assetElements.indexOf(matchingAsset);
        Actions actions = new Actions(driver);
        actions.moveToElement(matchingAsset).perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(String.format(tryNowButton, index))));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(String.format(likeButton, index))));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(String.format(saveButton, index))));

        driver.findElement(By.id(String.format(tryNowButton, index))).click();
    }

    @Step("Verify editing screen opens")
    public void verifyEditingScreen() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.urlContains("editor"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(canvasContainer));
    }
}
