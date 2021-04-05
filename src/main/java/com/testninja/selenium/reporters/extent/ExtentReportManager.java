package com.testninja.selenium.reporters.extent;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.testninja.selenium.framework.report.*;
import com.testninja.selenium.utils.FileUtils;
import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ExtentReportManager is responsible for generating HTML reports for each test exection.
 * all the log messages from the tests will be captured in the HTML file.
 */

public class ExtentReportManager implements IExecutionListener, IReportType, ISuiteListener {

    private static ExtentReportSettings extentReportSettings;
    private static Map<String, ExtentReports> suiteReporter = new HashMap<>();
    private static Map<String, ExtentTest> testNode = new HashMap<>();
    private static ExtentReports consolidatedReport;

    /* TestNg requires default constructor for dynamic invocation  */
    public ExtentReportManager() {
    }

    public ExtentReportManager(ExtentReportSettings extentReportSettings) {
        setExtentReportSettings(extentReportSettings);
    }

    private void setExtentReportSettings(ExtentReportSettings extentReportSettings) {
        ExtentReportManager.extentReportSettings = extentReportSettings;
    }

    /**
     * Invoked before the TestNg execution begins.
     * All clean up and initialization steps are carried out.
     */
    @Override
    public synchronized void onExecutionStart() {
        String outputPath = extentReportSettings.getOutputPath();
        if (isListenerRegistered()) {
            outputPath = extentReportSettings.getExtentReportListener().getBaseOutputFolder();
        }
        FileUtils.createDirectoryIfNotExists(outputPath, extentReportSettings.getClearOutputPath());
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @BeforeClass TestNg hook
     *
     * @param reportContext
     */
    @Override
    public synchronized void testClassStarted(ReportContext reportContext) {
        createNewTest(reportContext.getTestClassDetails());
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @AfterClass TestNg hook
     *
     * @param reportContext
     */
    @Override
    public synchronized void testClassEnded(ReportContext reportContext) {
        TestClassDetails testClassDetails = reportContext.getTestClassDetails();
        ExtentTest test = getExtentTest(testClassDetails);
        test.assignCategory(testClassDetails.getTestCaseInfo().getTags().toArray(new String[]{}));
        getExtentReports(testClassDetails).flush();
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @BeforeMethod TestNg hook
     * This also take cares of handling the method re-running scenario on any failure.
     *
     * @param reportContext
     */
    @Override
    public synchronized void onTestMethodStarted(ReportContext reportContext) {

        /*
         * if the test method is already available, then the method is invoked by retry analyser due to failure.
         */
        if (isTestAlreadyPresent(reportContext.getTestMethodDetails())) {
            ExtentTest test = getExtentTest(reportContext);

            /* Reset the previous failed execution results of the current test method */
            test.getModel().getLogContext().getAll().forEach(log -> {
                if (log.getStatus().equals(Status.FAIL)) {
                    log.setStatus(Status.WARNING);
                }
            });
            /* default status will be passed */
            test.getModel().setStatus(Status.PASS);
            addTestLog(test, MarkupHelper.createLabel("Re running the test", ExtentColor.RED).getMarkup(), null, LogStatus.INFO);
        } else {
            /* If te method is executed for the first time, create a new TestNode to the parent Test*/
            ExtentTest parentNode = getExtentTest(reportContext.getTestClassDetails());
            ExtentTest childNode = createChildTestMethodNode(parentNode, reportContext);
            testNode.put(reportContext.getTestMethodDetails().getId(), childNode);
        }
    }

    @Override
    public synchronized void onTestMethodRerunned(ReportContext reportContext) {
    }

    @Override
    public synchronized void onTestMethodEnded(ReportContext reportContext) {
    }

    /**
     * attaches log messages with the screenshot to the current execution context.
     *
     * @param reportContext
     * @param logDetails
     * @param ScreenshotPath
     * @param status
     */

    @Override
    public synchronized void addLog(ReportContext reportContext, LogDetails logDetails, String ScreenshotPath, LogStatus status) {
        addTestLog(reportContext, logDetails, ScreenshotPath, status);
    }

    /**
     * attaches log messages to the current execution context.
     *
     * @param reportContext
     * @param logDetails
     * @param status
     */
    @Override
    public synchronized void addLog(ReportContext reportContext, LogDetails logDetails, LogStatus status) {
        addTestLog(reportContext, logDetails, null, status);
    }


    /**
     * Invoked by TestNg before running the tests for the give Suite
     *
     * @param suite
     */
    @Override
    public synchronized void onStart(ISuite suite) {
        initializeSuiteLevelReport(suite);
    }


    private Boolean isListenerRegistered() {
        return extentReportSettings.getExtentReportListener() != null;
    }

    private IExtentReportListener getListener() {
        return extentReportSettings.getExtentReportListener();
    }

    /**
     * Creates a Base extent report HTML file for the given suite.
     * Later all child tests are created from the above parent report.
     *
     * @param suite
     */
    private void initializeSuiteLevelReport(ISuite suite) {
        if (extentReportSettings.getConsolidateReport()) {
            if (consolidatedReport == null) {
                ExtentSparkReporter spark = new ExtentSparkReporter(extentReportSettings.getOutputPath());
                spark.config().setTheme(Theme.STANDARD);
                consolidatedReport = new ExtentReports();
                initializeParentReport(consolidatedReport);
                consolidatedReport.attachReporter(spark);
            }
            suiteReporter.put(suite.getName(), consolidatedReport);
        } else {
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(getHTMLfileOutputPath(suite));
            ExtentReports suiteLevelReport = new ExtentReports();
            initializeParentReport(suiteLevelReport);
            suiteLevelReport.attachReporter(htmlReporter);
            suiteReporter.put(suite.getName(), suiteLevelReport);
        }
    }

    private void initializeParentReport(ExtentReports report) {
        if (extentReportSettings.getSystemInfo() != null) {
            extentReportSettings.getSystemInfo().forEach((k, v) -> report.setSystemInfo(k, v));
        }
    }

    private String getHTMLfileOutputPath(ISuite suite) {
        if (isListenerRegistered()) {
            return getListener().getSuiteHTMlPath(suite);
        }
        return extentReportSettings.getOutputPath() + File.separator + suite.getName() + ".html";
    }

    private String retrieveUniqueTestId(TestClassDetails testClassDetails) {
        return (String) testClassDetails.getId();
    }

    private void createNewTest(TestClassDetails testClassDetails) {
        ExtentReports suiteReport = getExtentReports(testClassDetails);
        ExtentTest test = suiteReport.createTest(testClassDetails.getClassName(), testClassDetails.getTestCaseInfo().getTestDescription());
        test.assignDevice(testClassDetails.getTestCaseInfo().getDevice());
        testNode.put(retrieveUniqueTestId(testClassDetails), test);
    }

    private ExtentReports getExtentReports(TestClassDetails testClassDetails) {
        return suiteReporter.get(testClassDetails.getSuite().getName());
    }

    private ExtentTest getExtentTest(TestClassDetails testClassDetails) {
        return testNode.get(retrieveUniqueTestId(testClassDetails));
    }

    private ExtentTest getExtentTest(TestMethodDetails testMethodDetails) {
        return testNode.get(testMethodDetails.getId());
    }

    private ExtentTest getExtentTest(ReportContext context) {
        if (context.isTestClass()) {
            return getExtentTest(context.getTestClassDetails());
        } else {
            return getExtentTest(context.getTestMethodDetails());
        }
    }

    private Status getStatus(LogStatus status) {
        return Status.valueOf(status.name());
    }

    /**
     * Method to format the log description.
     *
     * @param logDetails
     * @return
     */
    private String formatDescription(LogDetails logDetails) {
        Object description = logDetails.getLogDescription();
        if (description instanceof Throwable) {
            StringBuilder formattedLog = new StringBuilder();

            Throwable t = (Throwable) description;
            formattedLog.append(formatException(t));

            while (t.getCause() != null) {
                formattedLog.append(formatException(t.getCause()));
                t = t.getCause();
            }
            return formattedLog.toString();
        }
        return (String) description;
    }

    private void addTestLog(ReportContext reportContext, LogDetails logDetails, String ScreenshotPath, LogStatus status) {
        ExtentTest test;
        if (reportContext.isTestClass()) {
            test = getExtentTest(reportContext.getTestClassDetails());
        } else {
            test = getExtentTest(reportContext.getTestMethodDetails());
        }

//        MediaEntityModelProvider screenShot = null;
//        if (ScreenshotPath != null) {
//            try {
//                String base64EncodedValue = FileUtils.convertImageToBase64(ScreenshotPath);
//                screenShot = MediaEntityBuilder.createScreenCaptureFromBase64String(base64EncodedValue).build();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (logDetails.getLogDescription() instanceof Throwable) {
//            test.log(getStatus(status), (Throwable) logDetails.getLogDescription(), screenShot);
//        } else {
//            test.log(getStatus(status), (String) logDetails.getLogDescription(), screenShot);
//        }

        addTestLog(test, formatDescription(logDetails), ScreenshotPath, status);
    }

    private void addTestLog(ExtentTest test, String description, String ScreenshotPath, LogStatus status) {
        if (ScreenshotPath != null) {
            try {
                String base64EncodedValue = FileUtils.convertImageToBase64(ScreenshotPath);
                MediaEntityModelProvider screenShot = MediaEntityBuilder.createScreenCaptureFromBase64String(base64EncodedValue).build();
                test.log(getStatus(status), description, screenShot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            test.log(getStatus(status), description);
        }
    }

    private Boolean isTestAlreadyPresent(TestMethodDetails testMethodDetails) {
        return testNode.containsKey(testMethodDetails.getId());
    }

    private String formatException(Throwable t) {
        try {
            StringBuilder description = new StringBuilder();
            description.append("<div> <b>" + t.getClass().getCanonicalName() + ": </b>");
            description.append(t.getMessage());
            for (StackTraceElement stack : t.getStackTrace()) {
                description.append("<br><span>\tat " + stack.toString() + "</span>");
            }
            description.append("</div>");
            return description.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private ExtentTest createChildTestMethodNode(ExtentTest parentTest, ReportContext context) {
        String testTitle = context.getTestMethodDetails().getDescription().equals("") ? context.getTestMethodDetails().getName() : context.getTestMethodDetails().getDescription();
        return parentTest.createNode(testTitle);
    }
}
