package com.testninja.selenium.framework.pageobject;

import com.github.webdriverextensions.WebComponent;
import com.testninja.selenium.ScriptHelper;
import com.testninja.selenium.framework.report.SeleniumReport;
import com.testninja.selenium.utils.Interactions;
import org.openqa.selenium.WebDriver;

public class BaseWebComponent extends WebComponent {

    protected ScriptHelper scriptHelper;
    protected Interactions interactions;
    protected SeleniumReport report;

    public BaseWebComponent() {
        super();
    }

    public BaseWebComponent(ScriptHelper scriptHelper) {
        super();
        setScriptHelper(scriptHelper);
    }

    public void setScriptHelper(ScriptHelper scriptHelper) {
        this.scriptHelper = scriptHelper;
        this.interactions = scriptHelper.getInteractions();
        this.report = scriptHelper.getReport();
    }

    public WebDriver getDriver() {
        return scriptHelper.getDriver();
    }
}
