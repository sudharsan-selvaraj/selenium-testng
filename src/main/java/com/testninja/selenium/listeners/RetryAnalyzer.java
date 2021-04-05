package com.testninja.selenium.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static Boolean enableTestReRun;
    int counter = 0;
    int retryLimit = 2;

    @Override
    public boolean retry(ITestResult result) {
        if(!enableTestReRun) {
            return false;
        }
        if (counter < retryLimit) {
            counter++;
            return true;
        }
        return false;
    }

    public static void setReRun(Boolean status) {
        enableTestReRun = status;
    }
}