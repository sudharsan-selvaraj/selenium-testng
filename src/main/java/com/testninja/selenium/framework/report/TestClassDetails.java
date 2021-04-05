package com.testninja.selenium.framework.report;
import org.testng.xml.XmlSuite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestClassDetails {

    public enum TestStatus {
        PASSED, FAILED, RERAN
    }

    private String id = UUID.randomUUID().toString();
    private String className;
    private String xmlTestName;
    private String packageName;
    private TestCaseDetails testCaseInfo;
    private XmlSuite suiteDetails;
    private Map<Object, Object> attributes = new HashMap<>();
    private Boolean isReran = false;
    private TestStatus status = TestStatus.PASSED;
    private Map<String, TestStatus> methodExecutionStatus = new HashMap<>();

    public String getId() {
        return id;
    }

    public Boolean getReran() {
        return isReran;
    }

    public void setReran(Boolean reran) {
        isReran = reran;
        status = TestStatus.PASSED;
    }

    public String getXmlTestName() {
        return xmlTestName;
    }

    public void setXmlTestName(String xmlTestName) {
        this.xmlTestName = xmlTestName;
    }

    public TestStatus getStatus() {
        return this.status;
    }

    public TestStatus getMethodStatus(String methodId) {
        return methodExecutionStatus.get(methodId);
    }

    public void setMethodStatus(String methodName, TestStatus status) {
        if (methodExecutionStatus.containsKey(methodName)) {
            methodExecutionStatus.replace(methodName, status);
        } else {
            methodExecutionStatus.put(methodName, status);
        }
        setTestStatus();
    }

    public void setTestStatus(TestStatus status) {
        this.status = status;
    }

    public void setTestStatus() {
        if (!methodExecutionStatus.isEmpty()) {
            for (Map.Entry<String, TestStatus> methodStatus : methodExecutionStatus.entrySet()) {
                if (methodStatus.getValue().equals(TestStatus.FAILED)) {
                    setTestStatus(methodStatus.getValue());
                    return;
                }
            }
            setTestStatus(TestStatus.PASSED);
        }
    }

    public TestCaseDetails getTestCaseInfo() {
        return testCaseInfo;
    }

    public void setTestCaseInfo(TestCaseDetails testCaseInfo) {
        this.testCaseInfo = testCaseInfo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCanonicalClassName() {
        return this.packageName + "." + this.className;
    }

    public XmlSuite getSuite() {
        return suiteDetails;
    }

    public void setSuite(XmlSuite suiteDetails) {
        this.suiteDetails = suiteDetails;
    }


    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Object, Object> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Object key, Object value) {
        if (this.attributes.containsKey(key)) {
            this.attributes.replace(key, value);
        } else {
            this.attributes.put(key, value);
        }
    }

    public Object getAttribute(Object key) {
        return this.attributes.get(key);
    }

}
