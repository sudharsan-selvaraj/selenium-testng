package com.testninja.selenium.pom.github.pages;

import com.testninja.selenium.ScriptHelper;
import com.testninja.selenium.framework.pageobject.annotations.Page;
import com.testninja.selenium.pom.github.components.SearchInput;
import com.testninja.selenium.pom.wrappers.BasePage;
import com.testninja.selenium.pom.wrappers.PageDetails;
import org.openqa.selenium.support.FindBy;

@Page
public class GithubHomePage extends BasePage {

    @FindBy(name = "q")
    private SearchInput searchInput;

    public GithubHomePage(ScriptHelper scriptHelper) {
        super(scriptHelper, new PageDetails(""));
    }

    public void searchRepository(String repositoryName) {
        try {
            searchInput.search(repositoryName);
            interactions.pressEnter(searchInput);
            report.info("Entered "+ repositoryName + " in search input");
        } catch (Exception e) {
            report.exception("Error while searching for repository  " + repositoryName, e);
        }
    }
}
