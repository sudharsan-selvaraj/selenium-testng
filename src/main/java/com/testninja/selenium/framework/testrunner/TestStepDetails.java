package com.testninja.selenium.framework.testrunner;
import java.lang.reflect.Method;

public class TestStepDetails {

    private Method testMethod;
    private boolean enabled;
    private String description;

    public TestStepDetails(Method testMethod,
                           boolean enabled,
                           String description) {
        this.testMethod = testMethod;
        this.enabled = enabled;
        this.description = description;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }
}
