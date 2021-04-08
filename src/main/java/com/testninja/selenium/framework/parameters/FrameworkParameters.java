package com.testninja.selenium.framework.parameters;

import com.beust.jcommander.Parameter;
import org.testng.xml.XmlSuite;
import com.testninja.selenium.framework.Browser;

import java.util.Arrays;
import java.util.List;

/**
 * Class representation of CLI arguments required by the framework.
 */

public class FrameworkParameters {

    @Parameter(names = {"--browsers"}, description = "Comma separated values of all the browsers in which tests need to be executed")
    private List<Browser> browsers = Arrays.asList(Browser.CHROME);

    @Parameter(names = {"--threadCount"})
    private Integer threadCount = 10;

    @Parameter(names = {"--angularApp"})
    private Boolean isAngularApp = false;

    @Parameter(names = {"--parallelMode"}, description = "Parallel execution")
    private XmlSuite.ParallelMode parallelMode = XmlSuite.ParallelMode.CLASSES;

    @Parameter(names = {"--modules"}, description = "Comma separated values of all the modules that need to be executed")
    private List<String> modules = Arrays.asList("google_search");

    @Parameter(names = {"--groups"}, description = "Comma separated values of all the groups that need to be executed")
    protected List<String> groups = null;

    @Parameter(names = {"--seleniumAddress"}, description = "Selenium grid address for chrome driver")
    private String seleniumAddress = null;

    @Parameter(names = {"--chromeDriverPath"}, description = "Selenium grid address for chrome driver")
    private String chromeDriverPath = null;

    @Parameter(names = {"--edgeDriverPath"}, description = "Selenium grid address for edge driver")
    private String edgeDriverPath = null;

    @Parameter(names = {"--reRunTest"}, description = "Re-run the failed test")
    private Boolean reRunTest = true;

    @Parameter(names = {"--reportPortalScreenShot"}, description = "Flag denoting to attach screenshot for log in report portal")
    private Boolean reportPortalScreenShot = true;

    private String moduleXMLFolderPath = "modules-xml";

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }

    public String getEdgeDriverPath() {
        return edgeDriverPath;
    }

    public String getSeleniumAddress() {
        return seleniumAddress;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public List<Browser> getBrowsers() {
        return browsers;
    }

    public XmlSuite.ParallelMode getParallelMode() {
        return parallelMode;
    }

    public List<String> getModules() {
        return modules;
    }

    public String getModulesFolderPath() {
        return moduleXMLFolderPath;
    }

    public List<String> getGroups() {
        return groups;
    }

    public Boolean getReRunTest() {
        return reRunTest;
    }

    public Boolean getIsAngularApp() {
        return isAngularApp;
    }

    public Boolean getReportPortalScreenShot() {
        return reportPortalScreenShot;
    }
}
