package com.testninja.selenium.reporters.reportportal;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.utils.properties.PropertiesLoader;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.testninja.selenium.framework.report.*;
import com.testninja.selenium.reporters.reportportal.client.Launch;
import com.testninja.selenium.reporters.reportportal.client.LaunchManager;
import com.testninja.selenium.reporters.reportportal.client.ReportPortalService;
import com.testninja.selenium.reporters.reportportal.client.ReportPortalServiceBuilder;
import com.testninja.selenium.reporters.reportportal.client.TestItem;
import com.testninja.selenium.reporters.reportportal.client.SaveLogRQ;
import java.net.InetAddress;
import org.testng.*;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Listerner class used to log the test execution metrics to report portal server.
 *
 * @Referece: https://reportportal.io/
 */

public class ReportPortalListener implements ITestListener, IExecutionListener, ISuiteListener, IReportType {

    private static Launch reportPortalLaunch;
    private static LaunchManager launchManager;
    private static Boolean isLaunchSuccessful = true;
    private static ReportPortalParams params;
    private static Map<String, TestItem> suiteItems = new HashMap<>();
    private static Map<String, TestItem> testItems = new HashMap<>();
    private static Map<String, TestItem> testClassItems = new HashMap<>();
    private static Map<String, TestItem> testMethodItems = new HashMap<>();
    private static HashMap<String, Boolean> suiteStatus = new HashMap<>();
    private static ListenerParameters reportPortalProperties;

    static {
        reportPortalProperties = new ListenerParameters(PropertiesLoader.load());
        ReportPortalService service = new ReportPortalServiceBuilder().
                withParameter(new ListenerParameters(PropertiesLoader.load()))
                .build();
        launchManager = new LaunchManager(service);
    }

    private static Lock loggingLock = new ReentrantLock();

    /* Default constructor is required while registering it in TestNg listener */
    public ReportPortalListener() {
    }

    public ReportPortalListener(ReportPortalParams params) {
        ReportPortalListener.params = params;
    }
    /**
     * Invoked before the TestNg execution begins.
     * Creates a new test launch for the given branch name for which the test is executed.
     */
    @Override
    public void onExecutionStart() {
        try {
            loggingLock.lock();
            String launchName = params.getBranchName() == null ? getComputerName() : params.getBranchName();
            Mode mode = params.getMode() == null ? Mode.DEBUG : params.getMode();
            StartLaunchRQ rq = new StartLaunchRQ();
            rq.setStartTime(new Date());
            rq.setName(launchName);
            rq.setMode(mode);
            if(params.getAttributes() != null) {
                params.getAttributes().add(new ItemAttributesRQ("branch", launchName));
                rq.setAttributes(params.getAttributes());
            }
            reportPortalLaunch = launchManager.startNewLaunch(rq);
            loggingLock.unlock();

            String launchUrl = String.format("%s/ui#%s/%s/all/%s",
                    reportPortalProperties.getBaseUrl(),
                    reportPortalProperties.getProjectName(),
                    (mode.equals(Mode.DEFAULT) ? "launches" : "userdebug"),
                    launchManager.getLaunchId(reportPortalLaunch.getLaunchGuid())
                    );
            System.out.println("REPORT PORTAL LINK: "+ launchUrl);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Invoked by TestNg before running the tests for the give Suite
     *
     * @param suite
     */
    @Override
    public void onStart(ISuite suite) {
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            StartTestItemRQ rq = new StartTestItemRQ();
            rq.setName(suite.getName());
            rq.setType("SUITE");
            rq.setStartTime(new Date());

            TestItem suiteItem = launchManager.startTestItem(reportPortalLaunch, rq);
            suiteItems.put(suite.getName(), suiteItem);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Invoked by TestNg before running the tests inside the <test></test> tags
     *
     * @param context
     */
    @Override
    public void onStart(ITestContext context) {
        if (context.getAllTestMethods().length == 0) {
            return;
        }
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            StartTestItemRQ rq = new StartTestItemRQ();
            rq.setName(context.getName());
            rq.setType("TEST");
            rq.setStartTime(new Date());

            TestItem suiteItem = suiteItems.get(context.getSuite().getName());
            TestItem testItem = launchManager.startTestItem(suiteItem, rq);
            testItems.put(context.getSuite().getName() + "_" + context.getName(), testItem);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @BeforeClass TestNg hook
     *
     * @param reportContext
     */
    @Override
    public void testClassStarted(ReportContext reportContext) {
        TestClassDetails testClassDetails = reportContext.getTestClassDetails();
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            StartTestItemRQ rq = new StartTestItemRQ();
            rq.setName(testClassDetails.getClassName());
            rq.setTestCaseId(testClassDetails.getTestCaseInfo().getTestCaseId());
            rq.setCodeRef(testClassDetails.getClassName());
            rq.setDescription(testClassDetails.getTestCaseInfo().getTestDescription());
            rq.setType("STEP");
            rq.setStartTime(new Date());

            Set<ItemAttributesRQ> tags = new HashSet<>();
            for (String tag : testClassDetails.getTestCaseInfo().getTags()) {
                tags.add(new ItemAttributesRQ(tag));
            }

            tags.add(new ItemAttributesRQ("device", testClassDetails.getTestCaseInfo().getDevice()));
            rq.setAttributes(tags);

            TestItem testItem = launchManager.startTestItem(testItems.get(testClassDetails.getSuite().getName() + "_" + testClassDetails.getXmlTestName()), rq);
            testClassItems.put(testClassDetails.getId(), testItem);
            loggingLock.unlock();

        } catch (Exception e) {
            handleException(e);
        }
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
    public void addLog(ReportContext reportContext, LogDetails logDetails, String ScreenshotPath, LogStatus status) {
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            SaveLogRQ rq = new SaveLogRQ();
            rq.setMessage(formatLogDescription(logDetails));
            rq.setLogTime(new Date());
            if (ScreenshotPath != null && ReportPortalListener.params.getAttachScreenShot() == true) {
//                System.out.println("Attaching Screenshot");
                SaveLogRQ.File rqFile = new SaveLogRQ.File();
                rqFile.setFile(new File(ScreenshotPath));
                rq.setFile(rqFile);
            }
            rq.setLevel(getLogLevel(status));
            TestItem testItem;
            if (reportContext.isTestClass()) {
                testItem = testClassItems.get(reportContext.getTestClassDetails().getId());
            } else {
                testItem = testMethodItems.get(reportContext.getTestMethodDetails().getId());
            }
            launchManager.log(testItem, rq);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * attaches log messages to the current execution context.
     *
     * @param reportContext
     * @param logDetails
     * @param status
     */
    @Override
    public void addLog(ReportContext reportContext, LogDetails logDetails, LogStatus status) {
        addLog(reportContext, logDetails, null, status);
    }

    @Override
    public void onTestMethodRerunned(ReportContext reportContext) {
        //onTestMethodStarted(reportContext);
        //onTestMethodEnded(reportContext);
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @BeforeMethod TestNg hook
     * This also take cares of handling the method re-running scenario on any failure.
     *
     * @param reportContext
     */
    @Override
    public void onTestMethodStarted(ReportContext reportContext) {
        TestClassDetails testClassDetails = reportContext.getTestClassDetails();
        TestMethodDetails testMethodDetails = reportContext.getTestMethodDetails();
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            StartTestItemRQ rq = new StartTestItemRQ();
            rq.setName(testMethodDetails.getDescription()+" ["+testMethodDetails.getName()+"]");
            rq.setDescription(testMethodDetails.getDescription());
            rq.setType("STEP");
            rq.setStartTime(new Date());
            rq.setHasStats(false);
            TestClassDetails.TestStatus status = testClassDetails.getMethodStatus(testMethodDetails.getId());
            /* if the method is already executed, then mark it as a re-runed test */
            if (status.equals(TestClassDetails.TestStatus.RERAN)) {
               // rq.setName(testMethodDetails.getDescription()+" ["+testMethodDetails.getName()+"] [RE-TRIED]");
                rq.setRetry(true);
            }
            Set<ItemAttributesRQ> tags = new HashSet<>();
            for (String tag : testClassDetails.getTestCaseInfo().getTags()) {
                tags.add(new ItemAttributesRQ(tag));
            }

            TestItem testItem = launchManager.startTestItem(testClassItems.get(testClassDetails.getId()), rq);
            testMethodItems.put(testMethodDetails.getId(), testItem);
            loggingLock.unlock();

        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @AfterMethod TestNg hook
     *
     * @param reportContext
     */
    @Override
    public void onTestMethodEnded(ReportContext reportContext) {
        TestClassDetails testClassDetails = reportContext.getTestClassDetails();
        TestMethodDetails testMethodDetails = reportContext.getTestMethodDetails();
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            FinishTestItemRQ rq = new FinishTestItemRQ();
            rq.setEndTime(new Date());
            rq.setStatus(getTestClassStatus(testClassDetails.getMethodStatus(testMethodDetails.getId()).name()));

            TestItem testItem = testMethodItems.get(testMethodDetails.getId());
            launchManager.finishTestItem(testItem, rq);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Invoked by SeleniumReport class from the test classes using @AfterClass TestNg hook
     *
     * @param reportContext
     */
    @Override
    public void testClassEnded(ReportContext reportContext) {
        TestClassDetails testClassDetails = reportContext.getTestClassDetails();
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            FinishTestItemRQ rq = new FinishTestItemRQ();
            rq.setEndTime(new Date());
            rq.setStatus(getTestClassStatus(testClassDetails.getStatus().name()));

            TestItem testItem = testClassItems.get(testClassDetails.getId());
            testClassItems.remove(testClassDetails.getId());
            launchManager.finishTestItem(testItem, rq);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Invoked by TestNg after running all tests inside the <test></test> tag.
     *
     * @param context
     */
    @Override
    public void onFinish(ITestContext context) {
        if (context.getAllTestMethods().length == 0) {
            return;
        }
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            FinishTestItemRQ rq = new FinishTestItemRQ();
            rq.setEndTime(new Date());
            rq.setStatus(getTestStatus(context));
            launchManager.finishTestItem(testItems.get(context.getSuite().getName() + "_" + context.getName()), rq);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }
    /**
     * Invoked by TestNg after running the tests in the give Suite
     *
     * @param suite
     */
    @Override
    public void onFinish(ISuite suite) {
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            FinishTestItemRQ rq = new FinishTestItemRQ();
            rq.setEndTime(new Date());
            rq.setStatus(getSuiteStatus(suite));

            suiteStatus.put(suite.getName(), !suite.getSuiteState().isFailed());
            launchManager.finishTestItem(suiteItems.get(suite.getName()), rq);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }


    /**
     * Invoked after the TestNg execution completes.
     * Stops the current test launch with the appropriate status.
     */
    @Override
    public void onExecutionFinish() {
        if (!isLaunchSuccessful) {
            return;
        }
        try {
            loggingLock.lock();
            FinishExecutionRQ rq = new FinishExecutionRQ();
            rq.setEndTime(new Date());
            rq.setStatus(getLaunchStatus());
            launchManager.stopLaunch(reportPortalLaunch, rq);
            loggingLock.unlock();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private String getLogLevel(LogStatus status) {
        switch (status) {
            case PASS:
                return "trace";
            case FAIL:
            case ERROR:
                return "error";
            case WARNING:
                return "warm";
            case INFO:
                return "info";
            default:
                return "unknown";
        }
    }

    private String getTestClassStatus(String status) {
        switch (status.toLowerCase()) {
            case "rerunned":
                return "RESETED";
            default:
                return status;
        }
    }

    private String getTestStatus(ITestContext context) {
        if (context.getFailedTests().size() > 0) {
            return "FAILED";
        } else {
            return "PASSED";
        }
    }

    private String getSuiteStatus(ISuite suite) {
        return suite.getSuiteState().isFailed() ? "FAILED" : "PASSED";
    }

    private String getLaunchStatus() {
        for (Map.Entry<String, Boolean> entry : suiteStatus.entrySet()) {
            if (!entry.getValue()) {
                return "FAILED";
            }
        }
        return "PASSED";
    }

    private String formatLogDescription(LogDetails logDetails) {
        Object description = logDetails.getLogDescription();
        StringBuilder formattedLog = new StringBuilder();
        formattedLog.append(logDetails.getCaller().toString() + "\n");
        if (description instanceof Throwable) {
            StringWriter stringWriter = new StringWriter();
            ((Throwable) description).printStackTrace(new PrintWriter(stringWriter));
            formattedLog.append(stringWriter.toString());
        } else {
            formattedLog.append((String) description);
        }
        return formattedLog.toString();
    }

    private void handleException(Throwable e) {
        loggingLock.unlock();
        isLaunchSuccessful = false;
        e.printStackTrace();
    }

    private String getComputerName()
    {
      try {
          return InetAddress.getLocalHost().getHostName();
      } catch (Exception e) {
          return "Unknown Computer";
      }
    }
}
