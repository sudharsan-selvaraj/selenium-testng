package framework.core;

public enum BrowserType {
    chrome("chrome"),
    safari("safari"),
    edge("edge");

    public final String browser;

    private BrowserType(String label) {
        this.browser = label;
    }
}
