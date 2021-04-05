package com.testninja.selenium.framework.wrappers;

import com.testninja.selenium.framework.report.TestCaseDetails;
import com.testninja.selenium.framework.report.TestClassDetails;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

abstract public class TestManager {

    protected TestClassDetails testClassDetails;
    protected Map<String, Integer> methodInvocationCount = new HashMap<>();
    private static Map<String, Map<String, Integer>> suiteWiseInvocationCountMap = new HashMap<>();
    protected abstract TestCaseDetails getTestCaseDetails();

    protected synchronized TestClassDetails getTestClassInfoDetails(XmlTest context) {
        TestClassDetails info = new TestClassDetails();
        info.setSuite(context.getSuite());
        info.setClassName(this.getClass().getName() + " #" + incrementAndGetInvocationCount(context));
        info.setPackageName(this.getClass().getPackage().getName());
        info.setXmlTestName(context.getName());
        TestCaseDetails TCDetails = getTestCaseDetails();
        TCDetails.setDevice(context.getSuite().getName());
        info.setTestCaseInfo(TCDetails);
        return info;
    }

    protected boolean checkTestReran(Method method) {
        if (methodInvocationCount.containsKey(method.getName())) {
            methodInvocationCount.replace(method.getName(), methodInvocationCount.get(method.getName()) + 1);
            testClassDetails.setReran(true);
            testClassDetails.getTestCaseInfo().addTag("re-ran");
            return true;
        } else {
            methodInvocationCount.put(method.getName(), 1);
            return false;
        }
    }

    private int incrementAndGetInvocationCount(XmlTest test) {
        synchronized (suiteWiseInvocationCountMap) {
            String suiteName = test.getSuite().getName();
            String className = this.getClass().getName();

            if (!suiteWiseInvocationCountMap.containsKey(suiteName)) {
                Map<String, Integer> classInvocationCount = new HashMap<>();
                suiteWiseInvocationCountMap.put(suiteName, classInvocationCount);
            }

            if (suiteWiseInvocationCountMap.get(suiteName).containsKey(className)) {
                Integer count = suiteWiseInvocationCountMap.get(suiteName).get(className);
                suiteWiseInvocationCountMap.get(suiteName).replace(className, count + 1);
                return count + 1;
            } else {
                suiteWiseInvocationCountMap.get(suiteName).put(className, 1);
                return 1;
            }
        }
    }
}
