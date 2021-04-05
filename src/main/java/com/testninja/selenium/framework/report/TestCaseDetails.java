package com.testninja.selenium.framework.report;

import java.util.ArrayList;
import java.util.List;

public class TestCaseDetails {

    private String testCaseId;
    private String testDescription;
    private List<String> tags =new ArrayList<>();
    private String device;

    public TestCaseDetails(String testCaseId, String testDescription) {
        this.testCaseId = testCaseId;
        this.testDescription = testDescription;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public List<String> getTags() {
        return tags;
    }

    public TestCaseDetails addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDevice() {
        return device;
    }

    public TestCaseDetails setDevice(String device) {
        this.device = device;
        return this;
    }
}
