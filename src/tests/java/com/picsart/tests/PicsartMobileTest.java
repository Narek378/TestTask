package com.picsart.tests;

import com.picsart.pages.SearchPage;
import com.picsart.utils.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.*;

import java.net.MalformedURLException;

@Epic("Picsart Mobile Automation")
@Feature("Filter and Asset Interactions on Mobile")
public class PicsartMobileTest {
    private AppiumDriver mobileDriver;
    private SearchPage searchPage;
    @Parameters({"platform", "browser", "isCloud","deviceType"})
    @BeforeMethod
    public void setup(String platform, String browser, boolean isCloud,@Optional("") String deviceType) throws MalformedURLException {
        mobileDriver = DriverFactory.getMobileDriver(platform, browser, isCloud,deviceType);
        searchPage = new SearchPage(mobileDriver);
    }

    @Test(description = "Automate Picsart Search Filters and Asset Interactions on Mobile")
    public void testPicsartSearch() {
        Allure.step("Open Picsart search page");
        searchPage.open();

        searchPage.switchToMainIframe();

        Allure.step("Toggle filter");
        searchPage.toggleFilter();


        Allure.step("Select Personal License");
        searchPage.clickLicenseOptionMobile("Personal");

        Allure.step("Toggle filter");
        searchPage.toggleFilter();
        searchPage.verifyFiltersDisappear();

        Allure.step("Verify no PLUS assets are displayed");
        searchPage.verifyNoPlusAssetsAreDisplayed();
        searchPage.verifyButtonsAreDisplayedMobile();
    }


    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
