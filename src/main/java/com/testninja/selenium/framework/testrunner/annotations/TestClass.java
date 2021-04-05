package com.testninja.selenium.framework.testrunner.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestClass {

    String description() default "";

    String testCaseId() default "";

    String[] tags() default {};

    String[] product() default {};

    String[] deploymentType() default {};

    boolean enabled() default true;

    String skippedReason() default "";

    boolean retryTests() default true;
}
