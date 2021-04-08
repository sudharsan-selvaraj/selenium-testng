package com.testninja.selenium.tests.github;

import com.testninja.selenium.framework.pageobject.annotations.PageObject;
import com.testninja.selenium.framework.testrunner.annotations.TestClass;
import com.testninja.selenium.framework.wrappers.BaseTest;
import com.testninja.selenium.pom.github.pages.GithubHomePage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@TestClass(
        testCaseId = "GITHUB_SEARCH_TC01",
        description = "Test github repository search functionality"
)
public class GithubRepositorySearch extends BaseTest {

    @PageObject
    public GithubHomePage homePage;

    @BeforeClass
    public void openGithub() {
        driver.get(parameters.getBaseUrl());
    }

    @Test
    public void test() {
        try {
            String searchQuery = "selenium-alexa-bot";
            homePage.searchRepository(searchQuery);
            report.fail("Test failure");
        } catch (Throwable t) {
            handleException(t);
        }
    }

}
