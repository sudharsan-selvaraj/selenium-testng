package com.testninja.selenium.framework;

import com.testninja.selenium.utils.FileUtils;
import org.testng.IExecutionListener;
import org.testng.TestNG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main entry point for running tests by dynamically invoking TestNG framework.
 */
public class RunManager {

    private TestNgConfiguration testNgParameters;
    private SuiteTransformListener suiteTransformListener;
    private List<IExecutionListener> executionListeners = new ArrayList<>();

    private List<SuiteInfo> suites;
    private List<String> suitesToBeExecuted = new ArrayList<>();

    public RunManager(TestNgConfiguration testNgParameters) {
        this.testNgParameters = testNgParameters;
    }

    public RunManager setSuiteTransformListener(SuiteTransformListener listener) {
        this.suiteTransformListener = listener;
        return this;
    }

    public RunManager addExecutionListener(IExecutionListener executionListener) {
        this.executionListeners.add(executionListener);
        return this;
    }

    public int run() {
        createSuiteXmls();
        runStarted();

        int exitStatus = startTest();

        runCompleted();
        return exitStatus > 0 ? 1 : exitStatus;
    }

    private void runStarted() {
        executionListeners.forEach(listener ->  listener.onExecutionStart() );
    }

    private void runCompleted() {
        executionListeners.forEach(listener -> {  listener.onExecutionFinish(); });
    }

    private int startTest() {
        TestNG testRunner = new TestNG();
        testRunner.setTestSuites(suitesToBeExecuted);
        testRunner.setSuiteThreadPoolSize(suitesToBeExecuted.size());
        if (testNgParameters.getGroups() != null) {
            testRunner.setGroups(String.join(",", testNgParameters.getGroups()));
        }
        testRunner.run();
        return testRunner.getStatus();
    }

    private String initSuiteXmlFolder(String folderPath) {
        FileUtils.createDirectoryIfNotExists(folderPath, true);
        return folderPath;
    }

    private List<SuiteInfo> getSuites(List<Browser> browsers) {
        List<SuiteInfo> suites = new ArrayList<>();
        for (Browser browser : browsers) {
            SuiteInfo suite = new SuiteInfo(browser.name());
            suite.setBrowser(browser);
            suite.setParallelMode(testNgParameters.getParallelMode());
            suite.setThreadCount(testNgParameters.getThreadCount());
            suite.setModules(testNgParameters.getModules());
            suites.add(suite);
        }
        return suites;
    }

    private void createSuiteXmls() {
        String suitesFileFolderPath = initSuiteXmlFolder(FileUtils.getAbsoluteFolderPath("suites"));
        SuiteBuilder suiteBuilder = new SuiteBuilder(testNgParameters.getModulesFolderPath());

        this.suites = getSuites(testNgParameters.getBrowsers());

        if (this.suites.size() > 0) {
            for (SuiteInfo suite : suites) {
                suite = transformSuiteInfo(suite);
                if (suite == null) {
                    System.err.println("Suite value is null...");
                } else {
                    String suiteOutputPath = suitesFileFolderPath + File.separator + suite.getName() + ".xml";
                    suiteBuilder.generateTestNgSuiteFile(suite, suiteOutputPath);
                    suitesToBeExecuted.add(suiteOutputPath);
                }
            }
        } else {
            System.err.println("No suites Provided...");
        }
    }

    private SuiteInfo transformSuiteInfo(SuiteInfo suiteInfo) {
        if (this.suiteTransformListener == null) {
            return suiteInfo;
        }
        return this.suiteTransformListener.transformSuite(suiteInfo);
    }

}
