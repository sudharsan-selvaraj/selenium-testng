package com.testninja.selenium.framework.report;

public enum LogStatus {
    PASS("pass"),
    FAIL("fail"),
    FATAL("fatal"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info"),
    SKIP("skip"),
    UNKNOWN("unknown");

    private String label;
    private LogStatus(String label) {
        this.label = label;
    }
}
