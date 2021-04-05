package com.testninja.selenium.reporters.console;

public class SkippedClassInfo {

    private String className;
    private String skippedReason;

    public SkippedClassInfo(String className, String skipedReason) {
        this.className = className;
        this.skippedReason = skipedReason;
    }

    public String getClassName() {
        return className;
    }

    public String getSkippedReason() {
        return skippedReason;
    }
}
