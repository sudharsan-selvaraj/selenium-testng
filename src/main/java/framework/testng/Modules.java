package framework.testng;

public enum Modules {
    jobs("jobs"),
    datasets("datasets"),
    users("users");

    public final String module;

    private Modules(String label) {
        this.module = label;
    }
}
