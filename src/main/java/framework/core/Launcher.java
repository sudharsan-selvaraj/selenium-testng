package framework.core;

import framework.testng.SuiteBuilder;
import framework.testng.SuiteInfo;
import framework.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Launcher {

    private LaunchConfig launchConfig;
    private FrameworkSettings frameworkSettings;

    private List<SuiteInfo> suites;
    private List<String> suitesToBeExecuted;

    public Launcher(LaunchConfig config) {
        this.launchConfig = config;
        this.frameworkSettings = config.getFrameworkSettings();

        suitesToBeExecuted = new ArrayList<>();
    }

    public void launch() {
        initializeTestSuites();
        createSuiteXmls();
    }

    public void initializeTestSuites() {
        suites = getSuites(frameworkSettings.getBrowsers());
    }

    private List<SuiteInfo> getSuites(List<BrowserType> browsers) {
        List<SuiteInfo> suites = new ArrayList<>();
        for(BrowserType browser : browsers) {
            SuiteInfo suite = new SuiteInfo(browser.name());
            suite.setParallelMode(frameworkSettings.getParallelMode());
            suite.setThreadCount(frameworkSettings.getThreadCount());
            suite.setModules(frameworkSettings.getModules());
            suites.add(suite);
        }
        return suites;
    }

    private void createSuiteXmls() {
        String suitesFileFolderPath = initSuiteXmlFolder();
        SuiteBuilder suiteBuilder = new SuiteBuilder(frameworkSettings.getModulesPath());

        if(this.suites.size() > 0) {
            for(SuiteInfo suite: suites) {
                suite = transformSuiteInfo(suite);
                if(suite == null) {
                    System.err.println("Suite value is null..");
                } else {
                    String suiteOutputPath = suitesFileFolderPath+ File.separator+ suite.getName() + ".xml";
                    suiteBuilder.generateTestNgSuiteFile(suite, suiteOutputPath);
                    suitesToBeExecuted.add(suiteOutputPath);
                }
            }
        } else {
            System.err.println("No suites Provided..");
        }
    }


    private String initSuiteXmlFolder() {
        File folderPath = new File(System.getProperty("user.dir") + File.separator + "suite-xmls");
        if(!folderPath.exists()) {
            folderPath.mkdir();
        } else {
            FileUtils.clearDirectory(folderPath);
        }
        return folderPath.getPath();
    }

    private SuiteInfo transformSuiteInfo(SuiteInfo suiteInfo) {
        if(launchConfig.getLaunchListener() == null) {
            return suiteInfo;
        }
        return launchConfig.getLaunchListener().transformSuite(suiteInfo);
    }
}
