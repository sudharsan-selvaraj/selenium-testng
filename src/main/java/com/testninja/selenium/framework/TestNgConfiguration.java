package com.testninja.selenium.framework;

import org.testng.xml.XmlSuite;

import java.util.List;

/**
 * Class representation of the TestNg configurations
 * required by the framework to initiate the test
 */

public class TestNgConfiguration {

    private List<Browser> browsers;
    private XmlSuite.ParallelMode parallelMode;
    private Integer threadCount;
    private List<String> groups;
    private List<String> modules;
    private String modulesFolderPath;

    public TestNgConfiguration(List<Browser> browsers,
                               XmlSuite.ParallelMode parallelMode,
                               Integer threadCount,
                               List<String> groups,
                               List<String> modules,
                               String modulesFolderPath) {

        this.browsers = browsers;
        this.parallelMode = parallelMode;
        this.threadCount = threadCount;
        this.groups = groups;
        this.modules = modules;
        this.modulesFolderPath = modulesFolderPath;
    }

    public XmlSuite.ParallelMode getParallelMode() {
        return parallelMode;
    }

    public String getModulesFolderPath() {
        return modulesFolderPath;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getModules() {
        return modules;
    }

    public List<Browser> getBrowsers() {
        return browsers;
    }
}
