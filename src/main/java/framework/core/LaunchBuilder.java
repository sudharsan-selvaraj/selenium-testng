package framework.core;

import java.io.IOException;

public class LaunchBuilder {

    private Object frameWorkParameters;
    private ILaunchListener launchListener;
    private static FrameworkSettings frameWorkSettings = FrameworkSettings.getInstance();
    private String[] cliArguments = new String[]{};

    public LaunchBuilder setFrameWorkParameters(Object frameWorkParameters) {
        this.frameWorkParameters = frameWorkParameters;
        return this;
    }

    public LaunchBuilder setCliArguments(String[] args) {
        this.cliArguments = args;
        return this;
    }

    public LaunchBuilder setLaunchListener(ILaunchListener launchListener) {
        this.launchListener = launchListener;
        return this;
    }

    public Launcher build() {
        try {
            initializeFrameworkSettings();
            return new Launcher(getLaunchConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeFrameworkSettings() throws IOException {
        initializeCliArgs(frameWorkSettings, cliArguments, frameWorkSettings.getDefaultPropertiesPath());
        initializeCliArgs(frameWorkParameters, cliArguments, frameWorkSettings.getDefaultParametersFile());
    }

    private LaunchConfig getLaunchConfig() {
        return new LaunchConfig(frameWorkSettings, launchListener);
    }

    private void initializeCliArgs(Object obj, String[] cliArguments, String defaultArguments) throws IOException {
        if (obj != null) {
            CliParser.parseArgs(obj, cliArguments, defaultArguments);
        }
    }
}
