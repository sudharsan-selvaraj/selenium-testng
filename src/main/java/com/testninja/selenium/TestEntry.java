package com.testninja.selenium;

import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.testninja.selenium.framework.SuiteTransformer;
import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.listeners.TestExecutionListener;
import com.testninja.selenium.framework.Browser;
import com.testninja.selenium.framework.CliParser;
import com.testninja.selenium.framework.RunManager;
import com.testninja.selenium.framework.TestNgConfiguration;
import com.testninja.selenium.framework.report.IReportType;
import com.testninja.selenium.framework.report.SeleniumReport;
import com.testninja.selenium.listeners.AnnotationTransformer;
import com.testninja.selenium.listeners.RetryAnalyzer;
import com.testninja.selenium.reporters.console.ConsoleReporter;
import com.testninja.selenium.reporters.extent.ExtentReportManager;
import com.testninja.selenium.reporters.extent.ExtentReportSettings;
import com.testninja.selenium.reporters.reportportal.ReportPortalListener;
import com.testninja.selenium.reporters.reportportal.ReportPortalParams;
import com.testninja.selenium.utils.FileUtils;
import org.testng.IExecutionListener;

import java.util.*;
import java.util.logging.Level;

/**
 * Main class that servers as the entry point of the framework.
 */

public class TestEntry implements IExecutionListener {

    private ApplicationParameters applicationParameters;

    public static void main(String[] args) {
        new TestEntry().run(args);
    }

    public void run(String[] cliArguments) {
        applicationParameters = new ApplicationParameters();
        /* Parse command line arguments and assign its respective values to ApplicationParameters class instance */
        CliParser.parseArgs(applicationParameters, cliArguments, new String[]{});

        TestNgConfiguration testNgConfiguration = new TestNgConfiguration(
                applicationParameters.getBrowsers(),
                applicationParameters.getParallelMode(),
                applicationParameters.getThreadCount(),
                applicationParameters.getGroups(),
                applicationParameters.getModules(),
                applicationParameters.getModulesFolderPath()
        );

        /* run manager is responsible for driving the test  */
        RunManager runManager = new RunManager(testNgConfiguration);
        runManager.setSuiteTransformListener(new SuiteTransformer(applicationParameters))
                .addExecutionListener(new TestExecutionListener(applicationParameters))
                .addExecutionListener(this);

        /* Once all necessary step are completed, initialize the reporters that will be used in the test*/
        initializeReporters(applicationParameters);

        /*Add parameters to annotation transformer to disable test classes based on product*/
        AnnotationTransformer.setApplicationParameters(applicationParameters);

        /* Creates suite xml file based on CLI arguments and triggers the TestNg test programmatically */
        int exitCode = runManager.run();
        System.out.println("Selenium Process exited with code " + ConsoleReporter.getExitCode());
        System.exit(ConsoleReporter.getExitCode());
    }

    private void initializeReporters(ApplicationParameters applicationParameters) {
        SeleniumReport.addReporter(getExtentReporter(applicationParameters)); //HTML reported
        //SeleniumReport.addReporter(getReportPortalListener(applicationParameters)); //Dashboard reporter
        ConsoleReporter.setApplicationParameter(applicationParameters);
        SeleniumReport.addReporter(new ConsoleReporter()); //Real time execution log in console
    }

    private IReportType getExtentReporter(ApplicationParameters applicationParameters) {

        Map<String, String> systemInfo = new HashMap<>();
        systemInfo.put("browser", Arrays.toString(applicationParameters.getBrowsers().toArray(new Browser[]{})));
        //systemInfo.put("branch", applicationParameters.getBranch());

        ExtentReportSettings extentReportSettings = new ExtentReportSettings()
                .setClearOutputPath(true)
                .setConsolidateReport(true)
                .setOutputPath(FileUtils.getAbsoluteFolderPath("result"))
                .setSystemInfo(systemInfo);

        return new ExtentReportManager(extentReportSettings);
    }

    private ReportPortalListener getReportPortalListener(ApplicationParameters parameters) {
       // Mode reportPortalLaunchMode = applicationParameters.getBranch() != null && applicationParameters.getBranch().equalsIgnoreCase("develop") ? Mode.DEFAULT : Mode.DEBUG;
        ReportPortalParams params = new ReportPortalParams("applicationParameters.getBranch()", Mode.DEBUG, applicationParameters.getReportPortalScreenShot());

        Set<ItemAttributesRQ> attributes = new HashSet<>();
        if (parameters.getGroups() != null) {
            for (String group : parameters.getGroups()) {
                attributes.add(new ItemAttributesRQ(group));
            }
        }

        for (Browser browser : parameters.getBrowsers()) {
            attributes.add(new ItemAttributesRQ("device", browser.name()));
        }

        //attributes.add(new ItemAttributesRQ("product", applicationParameters.getProduct()));
//        System.out.println(applicationParameters.getReportPortalScreenShot());
        params.setAttributes(attributes);
        return new ReportPortalListener(params);
    }

    @Override
    public void onExecutionStart() {
        try {
            /* To suppress all selenium related log warning from printing to the console */
            java.util.logging.Logger.getLogger("org.openqa").setLevel(Level.OFF);
            RetryAnalyzer.setReRun(Globals.getParameters().getReRunTest());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
