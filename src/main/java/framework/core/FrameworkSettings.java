package framework.core;

import com.beust.jcommander.Parameter;
import framework.testng.Modules;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.List;

public class FrameworkSettings {

    private static volatile FrameworkSettings instance = null;
    private static final String DEFAULT_SETTINGS_FILE = System.getProperty("user.dir") + File.separator+ "FrameworkSettings.properties";

    private FrameworkSettings(){}

    public static synchronized FrameworkSettings getInstance() {
        if(instance == null) {
            instance = new FrameworkSettings();
        }
        return instance;
    }

    public String getDefaultPropertiesPath() {
        return DEFAULT_SETTINGS_FILE;
    }

    /* command line arguments */

    @Parameter(names={"--defaultParametersFile"})
    private String defaultParametersFile;

    @Parameter(names={"--modulesPath"})
    private String modulesPath;

    @Parameter(names={"--threadCount"})
    private Integer threadCount;

    @Parameter(names={"--browsers"}, description="Comma separated values for all the browsers in which tests need to be executed")
    private List<BrowserType> browsers;

    @Parameter(names={"--modules"}, description="Comma separated values for all the modules that need to be executed")
    private List<Modules> modules;

    @Parameter(names={"--parallelMode"}, description="Admin username for the application")
    private XmlSuite.ParallelMode parallelMode;


    /* Access methods */

    public String getDefaultParametersFile() {
        return defaultParametersFile;
    }

    public String getModulesPath() {
        return modulesPath;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public List<BrowserType> getBrowsers() {
        return browsers;
    }

    public XmlSuite.ParallelMode getParallelMode() {
        return parallelMode;
    }

    public List<Modules> getModules() {
        return modules;
    }

}