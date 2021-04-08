package com.testninja.selenium.framework.wrappers;

import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.Globals;
import com.testninja.selenium.ScriptHelper;
import com.testninja.selenium.framework.Browser;
import com.testninja.selenium.framework.DriverProvider;
import com.testninja.selenium.framework.pageobject.PageObjectFactory;
import com.testninja.selenium.framework.report.ReportContext;
import com.testninja.selenium.framework.report.SeleniumReport;
import com.testninja.selenium.framework.report.TestCaseDetails;
import com.testninja.selenium.framework.report.TestMethodDetails;
import com.testninja.selenium.framework.testrunner.TestClassInfoProvider;
import com.testninja.selenium.utils.Interactions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BaseTest extends TestManager {

    protected ApplicationParameters parameters;
    protected WebDriver driver;

    protected SeleniumReport report;
    protected Interactions interactions;
    protected ScriptHelper scriptHelper;

    protected TestClassInfoProvider testClassInfoProvider;
    protected ReportContext reportContext;
    protected Map<String, TestMethodDetails> testMethodDetailsList = new HashMap<>();

    public Boolean skipHooks = false;
    Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeClass(alwaysRun = true)
    @Parameters({"browserName", "seleniumAddress"})
    protected void setup(String browserName, String seleniumAddress) {
        try {
            parameters = Globals.getParameters();
            driver = DriverProvider.getDriver(Browser.valueOf(browserName), seleniumAddress);
            driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
            driver.manage().window().setSize(parameters.getScreenSize());
            testClassInfoProvider = TestClassInfoProvider.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = {"setup"})
    protected void initializeReport(XmlTest context) {
        this.testClassDetails = getTestClassInfoDetails(context);
        report = new SeleniumReport(driver, getReportContext());
        interactions = new Interactions(driver);
        parameters = Globals.getParameters();
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = {"setup", "initializeReport"})
    protected void initializeScriptHelper(XmlTest context) {
        try {
            scriptHelper = new ScriptHelper(driver, report, parameters, interactions);
            report.startTest();
            PageObjectFactory.init(this, Arrays.asList(ScriptHelper.class), Arrays.asList(scriptHelper));
        } catch (Throwable e) {
            handleException(e);
            e.printStackTrace();
        }
    }

    @AfterClass(alwaysRun = true)
    protected void tearDownDriver() {
        try {
            List<String> logs = filterBrowserLog();
            if (!logs.isEmpty()) {
                report.fail("Console error detected : \n" + Arrays.toString(logs.toArray()));
            }
            driver.quit();
        } catch (Exception e) {
            handleException(e);
        } finally {
            if (!skipHooks) {
                report.endTest();
            }
        }
    }

    /* Reporting Events */

    @BeforeMethod(alwaysRun = true)
    protected void beforeMethod(Method result) {
        if (skipHooks) {
            return;
        }
        reportContext.setIsTestClass(false);
        reportContext.setTestMethodDetails(getTestMethodDetails(result));
        if (checkTestReran(result)) {
            report.rerunTestMethod();
        }
        report.testMethodStart();
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod(ITestResult result) {
        if (skipHooks) {
            return;
        }
        if (result.getThrowable() != null) {
            report.fail(result.getThrowable());
        }
        report.testMethodEnded();
        reportContext.setIsTestClass(true);
    }


    private TestMethodDetails getTestMethodDetails(Method method) {
        if (testMethodDetailsList.get(method.getName()) == null) {
            TestMethodDetails testMethodDetails = new TestMethodDetails();
            testMethodDetails.setName(method.getName());
            testMethodDetails.setId(UUID.randomUUID().toString());
            testMethodDetailsList.put(method.getName(), testMethodDetails);
            Test annotation = method.getAnnotation(Test.class);
            if (annotation != null) {
                testMethodDetails.setDescription(annotation.description());
            }
            return testMethodDetails;
        } else {
            return testMethodDetailsList.get(method.getName());
        }
    }

    protected void handleException(Throwable t) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        try {
            Class<?> klass = Class.forName(className);
            Method method = klass.getMethod(methodName);
            /**
             *  If there are any exception in BeforeClass annotated method, testng will abort the execution
             * and the reporters will be unaware of this exception. Below method will identify any failures in
             * BeforeClass annotated methods and delegates the information to the reporters.
             */
            if (method.isAnnotationPresent(BeforeClass.class)) {
                skipHooks = true;
                report.fail(t);
                report.endTest();
                report.exception("Error in before class", t);
            } else if (method.isAnnotationPresent(AfterClass.class)) {
                report.exception("Error in after class", t);
            } else {
                report.exception(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ReportContext getReportContext() {
        if (reportContext == null) {
            reportContext = new ReportContext();
            reportContext.setIsTestClass(true);
            reportContext.setTestClassDetails(testClassDetails);
        }
        return reportContext;
    }

    @Override
    protected TestCaseDetails getTestCaseDetails() {
        return testClassInfoProvider.getTestCaseDetails();
    }

    List<String> logsList = new ArrayList<String>() {
        {
            add("Cannot read property \'summaryInstance\' of undefined");
            add("Failed to load template");
            add("Failed to load resource:");
            add("Cannot read property \'$touched\' of undefined");
            add("Cannot read property \'length\' of undefined\\n    at t.e.ifGroupHasValidUser");
            add("Cannot read property \'remove\' of null");
            add("Cannot read property \'getBoundingClientRect\'");
            add("Cannot read property \'postMessage\' of undefined");
            add("this._request.$cancelRequest is not a function");
            add("Cannot read property \'datatype\' of undefined");
            add("Cannot read property \'code\' of undefined");
            add("unable to find bean reference frameworkOverrides while initialising e");
            add("Cannot read property \'find\' of undefined");
            add("Access to XMLHttpRequest at");
            add("0 Error: attribute d: Expected number");
            add("NaN");
        }
    };

    public List<String> getBrowserLog() {
        List<LogEntry> logEntries = driver.manage().logs().get("browser").getAll();
        List<String> browserLogs = new ArrayList<String>();
        for (LogEntry logEntry : logEntries) {
            if(!logEntry.getLevel().getName().equals("WARNING") && !logEntry.getLevel().getName().equals("INFO")){
                browserLogs.add(logEntry.getMessage());
            }
        }
        return browserLogs;
    }

    public List<String> filterBrowserLog() {
        return getBrowserLog()
                .stream()
                .filter((browserLog) -> {
                    for (String log : logsList) {
                        if (browserLog.contains(log)) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
