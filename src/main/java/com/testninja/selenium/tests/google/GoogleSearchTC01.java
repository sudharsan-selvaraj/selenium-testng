package com.testninja.selenium.tests.google;

import com.testninja.selenium.framework.pageobject.annotations.PageObject;
import com.testninja.selenium.framework.testrunner.annotations.TestClass;
import com.testninja.selenium.framework.wrappers.BaseTest;
import com.testninja.selenium.pom.google.pages.GoogleSearchResultPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@TestClass(
        testCaseId = "GOOGLE_SEARCH_TC01",
        description = "Test google search results page"
)
public class GoogleSearchTC01 extends BaseTest {

    @PageObject
    public GoogleSearchResultPage googleSearchResultPage;

    @BeforeClass
    public void openGoogle() {
        driver.get(parameters.getBaseUrl());
    }

    @Test
    public void test() {
        try {
            String searchQuery = "Sudharsan Selvaraj";
            googleSearchResultPage.enterSearchQuery(searchQuery);
            assertEquals(searchQuery, googleSearchResultPage.getSearchQuery());
            report.pass("Search query is correctly displayed as "+ searchQuery);
        } catch (Throwable t) {
            handleException(t);
        }
    }

}
