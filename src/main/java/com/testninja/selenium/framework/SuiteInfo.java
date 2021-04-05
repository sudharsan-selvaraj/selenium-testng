package com.testninja.selenium.framework;

import org.testng.xml.XmlSuite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a TestNg suite used by the framework
 * to generate suite xml file
 */

public class SuiteInfo implements Serializable {

    private String name;
    private Browser browser;
    private XmlSuite.ParallelMode parallelMode;
    private int threadCount;
    private Map<String, String> parameters;
    private List<String> listeners;
    private List<String> modules;

    public SuiteInfo(String suiteName) {
        name = suiteName;
        parallelMode = XmlSuite.ParallelMode.NONE;
        threadCount = 0;
        parameters = new HashMap<>();
        listeners = new ArrayList<>();
        modules = new ArrayList<>();
    }

    public SuiteInfo setBrowser(Browser browser) {
        this.browser = browser;
        return this;
    }

    public SuiteInfo setParallelMode(XmlSuite.ParallelMode parallelMode) {
        this.parallelMode = parallelMode;
        return this;
    }

    public SuiteInfo setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public SuiteInfo setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public SuiteInfo setListeners(List<String> listeners) {
        this.listeners = listeners;
        return this;
    }

    public SuiteInfo setModules(List<String> modules) {
        this.modules = modules;
        return this;
    }

    public SuiteInfo addParameter(String parameterName, String parameterValue) {
        this.parameters.put(parameterName, parameterValue);
        return this;
    }

    public SuiteInfo addListener(String listenerClass) {
        if(listenerClass.equals("")) {
            return this;
        }
        this.listeners.add(listenerClass);
        return this;
    }

    public String getName() {
        return name;
    }

    public Browser getBrowser() {
        return browser;
    }

    public List<String> getModules() {
        return modules;
    }

    public List<String> getListeners() {
        return listeners;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public XmlSuite.ParallelMode getParallelMode() {
        return parallelMode;
    }
}
