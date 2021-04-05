package com.testninja.selenium.framework.report;

public class LogDetails {

    Object logDescription;
    StackTraceElement caller;

    LogDetails(Object logDescription, StackTraceElement stackTraceElement) {
        this.logDescription = logDescription;
        this.caller = stackTraceElement;
    }

    public Object getLogDescription() {
        return logDescription;
    }

    public StackTraceElement getCaller() {
        return caller;
    }
}
