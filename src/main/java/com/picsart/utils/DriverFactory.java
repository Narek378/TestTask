package com.picsart.utils;

import io.appium.java_client.AppiumDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();
    private static final String LAMBDATEST_URL = "https://hub.lambdatest.com/wd/hub";

    public static WebDriver getDriver(int width, int height) {
        if (driver.get() == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=" + width + "," + height);
            driver.set(new ChromeDriver(options));
        }
        return driver.get();
    }

    public static AppiumDriver getMobileDriver(String platform, String browser, boolean isCloud,String deviceType) throws MalformedURLException {
        if (mobileDriver.get() == null) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (isCloud) {
                // LambdaTest Capabilities
                capabilities.setCapability("user", "");
                capabilities.setCapability("accessKey", "");
                capabilities.setCapability("browserName", browser);
                capabilities.setCapability("platformName", platform);
                capabilities.setCapability("realMobile", "false");
                capabilities.setCapability("project", "Picsart Mobile Automation");
                capabilities.setCapability("build", "Build 1");

                // Optional Device Type
                if (deviceType != null && !deviceType.isEmpty()) {
                    capabilities.setCapability("deviceName", deviceType);
                } else {
                    // Default Device
                    capabilities.setCapability("deviceName", platform.equalsIgnoreCase("Android") ? "Samsung Galaxy S24" : "iPhone 14 Pro");
                }

                mobileDriver.set(new AppiumDriver(new URL(LAMBDATEST_URL), capabilities));
            } else {
                // Local Configuration
                capabilities.setCapability("platformName", platform);
                capabilities.setCapability("browserName", browser);
                capabilities.setCapability("deviceName", platform.equalsIgnoreCase("Android") ? "Android Emulator" : "iPhone Simulator");
                mobileDriver.set(new AppiumDriver(new URL("http://localhost:4723/wd/hub"), capabilities));
            }
        }
        return mobileDriver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }

        if (mobileDriver.get() != null) {
            mobileDriver.get().quit();
            mobileDriver.remove();
        }
    }
}
