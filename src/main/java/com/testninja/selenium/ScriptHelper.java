package com.testninja.selenium;

import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.framework.report.SeleniumReport;
import org.openqa.selenium.WebDriver;

/**
 * Acts as a holder for commonly used objects across tests and page object classes
 * Instance of ScriptHelper will be passed as constructor arguments for page object classes;
 */

public class ScriptHelper {

    private ApplicationParameters parameters;
    private WebDriver driver;
    private SeleniumReport report;

    public ScriptHelper(WebDriver driver,
                        SeleniumReport report,
                        ApplicationParameters parameters) {
        this.driver = driver;
        this.report= report;
        this.parameters = parameters;
    }

    public ApplicationParameters getParameters() {
        return parameters;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public SeleniumReport getReport() {
        return report;
    }
}
