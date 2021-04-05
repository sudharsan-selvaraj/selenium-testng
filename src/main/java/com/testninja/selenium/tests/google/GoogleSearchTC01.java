package com.testninja.selenium.tests.google;

import com.testninja.selenium.framework.testrunner.annotations.TestClass;
import com.testninja.selenium.framework.wrappers.BaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@TestClass(
        testCaseId = "GOOGLE_SEARCH_TC01",
        description = "Test google search results page"
)
public class GoogleSearchTC01 extends BaseTest {

    @Test
    public void test() {
        driver.get(parameters.getBaseUrl());
        driver.findElement(By.id("sdfsdfsdf")).click();
        report.fail("Successfully executed the tests");
    }

}
