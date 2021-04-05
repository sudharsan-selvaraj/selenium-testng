package com.testninja.selenium.framework.testrunner;

import com.testninja.selenium.framework.report.TestCaseDetails;
import com.testninja.selenium.framework.testrunner.annotations.TestClass;
import com.testninja.selenium.framework.testrunner.annotations.TestStep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestClassInfoProvider {

    private Object testClassObject;
    private TestCaseDetails testCaseDetails;

    private TestClassInfoProvider(Object testClassObject) {
        this.testClassObject = testClassObject;
        this.testCaseDetails = obtainTestCaseDetailsFromClass();
    }

    public static TestClassInfoProvider newInstance(Object testClassObject) {
        return new TestClassInfoProvider(testClassObject);
    }

    public TestCaseDetails getTestCaseDetails() {
        return this.testCaseDetails;
    }

    private TestCaseDetails obtainTestCaseDetailsFromClass() {
        TestClass testClassInfo = testClassObject.getClass().getAnnotation(TestClass.class);
        if (testClassInfo == null) {
            return new TestCaseDetails("", "");
        }
        TestCaseDetails testCaseDetails = new TestCaseDetails(testClassInfo.testCaseId(), testClassInfo.description());
        for(String tag:testClassInfo.tags()) {
            testCaseDetails.addTag(tag);
        }
        return testCaseDetails;
    }
}
