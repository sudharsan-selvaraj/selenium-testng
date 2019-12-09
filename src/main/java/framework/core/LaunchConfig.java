package framework.core;

public class LaunchConfig {

    private FrameworkSettings frameworkSettings;
    private ILaunchListener launchListener;

    public LaunchConfig(FrameworkSettings frameworkSettings, ILaunchListener launchListener) {
        this.frameworkSettings = frameworkSettings;
        this.launchListener = launchListener;
    }

    public FrameworkSettings getFrameworkSettings() {
        return frameworkSettings;
    }

    public ILaunchListener getLaunchListener() {
        return launchListener;
    }
}
