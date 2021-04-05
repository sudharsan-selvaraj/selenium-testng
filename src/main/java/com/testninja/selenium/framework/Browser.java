package com.testninja.selenium.framework;

public enum Browser {
    CHROME("chrome"),
    SAFARI("safari"),
    EDGE("edge");
    
    public final String label;

    private Browser(String label) {
        this.label = label;
    }
}
