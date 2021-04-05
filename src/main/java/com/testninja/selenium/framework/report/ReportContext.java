package com.testninja.selenium.framework.report;

public class ReportContext {

    private Boolean isTestClass;
    private TestClassDetails testClassDetails;
    private TestMethodDetails testMethodDetails;

    public Boolean isTestClass() {
        return isTestClass;
    }

    public void setIsTestClass(Boolean isTestClass) {
        this.isTestClass = isTestClass;
    }

    public TestClassDetails getTestClassDetails() {
        return testClassDetails;
    }

    public void setTestClassDetails(TestClassDetails testClassDetails) {
        this.testClassDetails = testClassDetails;
    }

    public TestMethodDetails getTestMethodDetails() {
        return testMethodDetails;
    }

    public void setTestMethodDetails(TestMethodDetails testMethodDetails) {
        this.testMethodDetails = testMethodDetails;
    }
}
