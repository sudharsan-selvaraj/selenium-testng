package com.testninja.selenium.pom.wrappers;

import com.github.webdriverextensions.WebDriverExtensionFieldDecorator;
import com.testninja.selenium.ScriptHelper;
import com.testninja.selenium.framework.pageobject.BaseWebComponentFieldDecorator;
import com.testninja.selenium.framework.pageobject.PageObjectFactory;
import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.framework.report.SeleniumReport;
import com.testninja.selenium.utils.AssertionUtils;
import com.testninja.selenium.utils.Interactions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.Arrays;

public class BasePage {

    public Interactions interactions;
    protected SeleniumReport report;
    protected WebDriver driver;
    protected ApplicationParameters parameters;
    protected PageDetails pageDetails;
    public ScriptHelper scriptHelper;

    public BasePage(ScriptHelper scriptHelper, PageDetails pageDetails) {
        this.scriptHelper = scriptHelper;
        this.report = scriptHelper.getReport();
        this.driver = scriptHelper.getDriver();
        this.parameters = scriptHelper.getParameters();
        this.interactions = scriptHelper.getInteractions();
        this.pageDetails = pageDetails;

        PageFactory.initElements(new BaseWebComponentFieldDecorator(scriptHelper), this);
        PageObjectFactory.init(this, Arrays.asList(new Class[]{ScriptHelper.class}), Arrays.asList(new Object[]{scriptHelper}));
    }

    public void open(String url) {
        try {
            interactions.open(url);
            report.info("Navigated to " + url);
        } catch (ScriptTimeoutException e) {
            report.warning("Page took long time to load..");
        } catch (Exception e) {
            report.exception(e);
        }
    }

    public BasePage open() {
        open(getUrl());
        return this;
    }

    public String getUrl() {
        return parameters.getBaseUrl() + pageDetails.getRouteUrl();
    }

    /**
     * Verifies whether currently displayed page(URL) contains pageUrl(@param)
     *
     * @param page
     * @param pageUrl
     * @param displayStatus
     */
    private void verifyPageIsDisplayed(String page, String pageUrl, boolean displayStatus) {
        String msg = displayStatus ? " is displayed." : " is not displayed.";
        String currentUrl = driver.getCurrentUrl();
        pageUrl = parameters.getBaseUrl() + pageUrl;
        try {
            Assert.assertEquals(currentUrl.equals(pageUrl), displayStatus);
            report.pass(page + " page (" + pageUrl + ")" + msg);
        } catch (Throwable t) {
            msg = displayStatus ? " is not displayed." : " is displayed.";
            report.exception(page + " page " + msg + " Which is not expected. " +
                    "Actual: " + currentUrl +
                    "Expected: " + pageUrl, t);
        }
    }

    public void verifyElementIsDisplayed(By locator, String elementDesc, Boolean displayStatus) {
        try {
            WebElement element = this.driver.findElement(locator);
            verifyElementIsDisplayed(element, elementDesc, displayStatus);
        } catch (NoSuchElementException e) {
            String msg = displayStatus ? " is not displayed" : " is displayed";
            if (!displayStatus) {
                report.pass("\"" + elementDesc + "\"" + msg);
            } else {
                report.exception("\"" + elementDesc + "\"" + msg, e);
            }
        }
    }

    public void verifyElementIsDisplayed(WebElement element, String elementDesc, Boolean displayStatus) {
        String msg = displayStatus ? " is displayed" : " is not displayed";
        if (AssertionUtils.isDisplayed(element) == displayStatus) {
            report.pass("\"" + elementDesc + "\"" + msg);
        } else {
            msg = displayStatus ? " is not displayed" : " is displayed";
            report.fail("\"" + elementDesc + "\"" + msg);
        }
    }

}
