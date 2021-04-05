package com.testninja.selenium.framework.testrunner;

import com.testninja.selenium.framework.report.TestCaseDetails;
import com.testninja.selenium.framework.testrunner.annotations.TestClass;

public interface ITestRunnerListener {

    default void onStart(TestCaseDetails testCaseDetails) { }

    default void onTestSkipped(TestClass testClass) {
    }

    default void onTestMethodStarted(TestClass testClass) {
    }

    default void onTestStepSkipped(TestStepDetails testClass) {
    }

    default void onTestStepStarted(TestStepDetails testStep) {
    }

    default void onTestStepCompleted(TestStepDetails testStep) {
    }

    default void onException(Throwable t) {

    }
}
