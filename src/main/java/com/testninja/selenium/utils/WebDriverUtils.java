package com.testninja.selenium.utils;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class WebDriverUtils {

    public static File takeScreenshot(WebDriver driver, String outputFilePath) throws IOException {
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(outputFilePath);
        FileUtils.copyFile(SrcFile, DestFile);
        return DestFile;
    }

    public static void navigate(WebDriver driver, String url) {
        new NgWebDriver((JavascriptExecutor) driver).waitForAngularRequestsToFinish();
        driver.get(url);
    }
}
