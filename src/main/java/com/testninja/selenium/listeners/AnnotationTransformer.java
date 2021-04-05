package com.testninja.selenium.listeners;

import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.framework.testrunner.annotations.TestClass;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationTransformer implements IAnnotationTransformer, IMethodInterceptor, IInvokedMethodListener {

    static ApplicationParameters applicationParameters;

    public static void setApplicationParameters(ApplicationParameters applicationParameters) {
        AnnotationTransformer.applicationParameters = applicationParameters;
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod != null) {
            Class<?> klass = testMethod.getDeclaringClass();
            if (klass.isAnnotationPresent(TestClass.class)) {
                TestClass testClassAnnotation = klass.getAnnotation(TestClass.class);
//                if (
//                        !testClassAnnotation.enabled()
//                        || !Arrays.asList(testClassAnnotation.product()).contains(applicationParameters.getProduct())
//                        || !Arrays.asList(testClassAnnotation.deploymentType()).contains(applicationParameters.getDeploymentType())
//                ) {
//                    annotation.setEnabled(false);
//                }
//                annotation.setGroups(testClassAnnotation.tags());
//
//                if(testClassAnnotation.retryTests()) {
//                    annotation.setRetryAnalyzer(RetryAnalyzer.class);
//                }
            }
        }
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> methodsToRun = new ArrayList<>();
        for(IMethodInstance method: methods) {
            Class<?> klass = method.getInstance().getClass();
            if (klass.isAnnotationPresent(TestClass.class)) {
                TestClass testClassAnnotation = klass.getAnnotation(TestClass.class);
                if (testClassAnnotation.enabled()) {
                    if(checkGroup(context.getIncludedGroups(), testClassAnnotation.tags())) {
                        methodsToRun.add(method);
                    }
                }
            }
        }

        return methodsToRun;
    }

    private boolean checkGroup(String[] includedGroups, String[] testTags) {
        if(includedGroups.length == 0) {
            return true;
        }
        List<String> groupList = Arrays.asList(includedGroups);
        for(String tag: testTags) {
            if(groupList.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    /* TODO: For dependent tests, retry mechanism needs to be handled manually when parent methods is passed after retry*/
//
//    @Override
//    public void afterInvocation(IInvokedMethod method, ITestResult result, ITestContext context) {
//        IRetryAnalyzer retry = result.getMethod().getRetryAnalyzer();
//        if(retry!=null) {
//            context.getFailedTests().removeResult(result.getMethod());
//            context.getSkippedTests().removeResult(result.getMethod());
//        }
//    }

}