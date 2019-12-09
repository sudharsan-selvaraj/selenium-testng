import framework.core.ILaunchListener;
import framework.testng.SuiteInfo;

public class LaunchListener implements ILaunchListener {
    @Override
    public SuiteInfo transformSuite(SuiteInfo suite) {
        FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
        suite.addParameter("browserName", suite.getName());
        suite.addParameter("baseUrl", frameworkParameters.getBaseUrl());
        suite.addParameter("adminUsername", frameworkParameters.getUserName());
        suite.addParameter("adminPassword", frameworkParameters.getPassword());

        suite.addListener("");

        return suite;
    }
}
