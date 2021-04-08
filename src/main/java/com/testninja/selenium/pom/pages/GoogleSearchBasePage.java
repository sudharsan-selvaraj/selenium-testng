package com.testninja.selenium.pom.pages;

import com.testninja.selenium.ScriptHelper;
import com.testninja.selenium.pom.wrappers.BasePage;
import com.testninja.selenium.pom.wrappers.PageDetails;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleSearchBasePage extends BasePage {

    @FindBy(name= "q")
    public WebElement searchInput;

    public GoogleSearchBasePage(ScriptHelper scriptHelper) {
        super(scriptHelper, new PageDetails(""));
    }

    public void enterSearchQuery(String query) {
        try {
            interactions.clearAndType(searchInput, query);
            interactions.pressEnter(searchInput);
            report.info("Entered search string as "+ query);
        } catch (Exception e) {
            report.exception("unable to enter search query", e);
        }
    }

    public String getSearchQuery() {
        return interactions.getAttribute(searchInput, "value");
    }
}