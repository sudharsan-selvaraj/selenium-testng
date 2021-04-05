package com.testninja.selenium.framework;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class DriverProvider {

    public static WebDriver getDriver(Browser browser, String seleniumAddress) throws MalformedURLException {

        /* To suppress all selenium related log warning from printing to the console */
        java.util.logging.Logger.getLogger("org.openqa").setLevel(Level.OFF);

        DesiredCapabilities capabilities = getCapabilities(browser);
        if(seleniumAddress == null || seleniumAddress.equals("")) {
            return getDriver(browser, capabilities);
        }
        WebDriver driver =  new RemoteWebDriver(new URL(seleniumAddress), capabilities);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        return driver;
    }


    private static WebDriver getDriver(Browser browser, Capabilities capabilities) {
        switch (browser.name()) {
            default:
                ChromeOptions options = new ChromeOptions();
                options.setHeadless(true);
                options.addArguments("--log-level=OFF","--silent");
                options.setCapability(ChromeOptions.CAPABILITY, capabilities);
                return new ChromeDriver(options);
        }
    }

    private static DesiredCapabilities getCapabilities(Browser browser) {
        DesiredCapabilities capabilities;
        switch (browser) {
            case EDGE:  return new DesiredCapabilities(BrowserType.EDGE, "", Platform.ANY);
            default:
            capabilities = new DesiredCapabilities(BrowserType.CHROME, "", Platform.ANY);
            ChromeOptions options =new ChromeOptions();
            options.setCapability("w3c", false);
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);

            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return capabilities;
        }
    }

}
