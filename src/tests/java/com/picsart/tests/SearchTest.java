package com.picsart.tests;

import com.picsart.pages.SearchPage;
import com.picsart.utils.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class SearchTest {
    private WebDriver driver;
    private SearchPage searchPage;

    @Parameters({"width", "height"})
    @BeforeMethod
    public void setup(int width, int height) {
        System.out.println(width);
        driver = DriverFactory.getDriver(width, height);
        searchPage = new SearchPage(driver);
    }

    @Test(description = "Test Picsart Search filters and asset interactions")
    public void testSearchFiltersAndAssets() {
        Allure.step("Open Picsart search page");
        searchPage.open();

        searchPage.switchToMainIframe();

        Allure.step("Toggle filter");
        searchPage.toggleFilter();

        Allure.step("Verify Filters disappear");
        searchPage.verifyFiltersDisappear();

        Allure.step("Toggle filter to make the filter appear");
        searchPage.toggleFilter();

        Allure.step("Select Personal License");
        searchPage.clickLicenseOption("Personal");
        Allure.step("Verify no PLUS assets are displayed");
        searchPage.verifyNoPlusAssetsAreDisplayed();

        Allure.step("Hover over asset and verify Like, Save, and Try Now buttons are displayed");
        searchPage.hoverOverAssetAndVerifyButtonsAreDisplayed();


        Allure.step("Click Like button and verify Sign-in popup");
        searchPage.clickLikeAndVerifySignInPopup();

        Allure.step("Close the Sign-in popup");
        searchPage.closePopup();
        searchPage.switchToMainIframe();

        Allure.step("Remove Personal License filter");
        searchPage.clickLicenseOption("Personal");

        Allure.step(" Hover over a PLUS asset and verify only Try Now button appears");
        searchPage.hoverOverPlusAssetAndVerifyTryNowOnlyIsDisplayed();

        Allure.step("Click on Try Now button and verify editing screen opens");
        searchPage.verifyEditingScreen();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
