package com.testninja.selenium.framework.report;

import com.testninja.selenium.utils.WebDriverUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SeleniumReport {

    private static List<IReportType> reporters = new ArrayList<>();
    public static ReportSettings reportSettings = new ReportSettings();
    private ReportContext reportContext;
    public WebDriver driver;

    public SeleniumReport(WebDriver driver, ReportContext reportContext) {
        this.driver = driver;
        this.reportContext = reportContext;
    }

    public void setReportContext(ReportContext reportContext) {
        this.reportContext = reportContext;
    }

    public static void addReporter(IReportType reporter) {
        reporters.add(reporter);
    }

    public static void setReportSettings(ReportSettings reportSettings) {
        SeleniumReport.reportSettings = reportSettings;
    }

    public void startTest() {
        reporters.forEach(reporter -> reporter.testClassStarted(reportContext));
    }

    public void endTest() {
        reportContext.getTestClassDetails().getTestCaseInfo().addTag("completed");
        reporters.forEach(reporter -> reporter.testClassEnded(reportContext));
    }

    public void testMethodStart() {
        String methodId = reportContext.getTestMethodDetails().getId();
        if(reportContext.getTestClassDetails().getMethodStatus(methodId) == null) {
            reportContext.getTestClassDetails().setMethodStatus(methodId, TestClassDetails.TestStatus.PASSED);
        }
        reporters.forEach(reporter -> reporter.onTestMethodStarted(reportContext));
    }

    public void testMethodEnded() {
        String methodId = reportContext.getTestMethodDetails().getId();
        TestClassDetails.TestStatus status = reportContext.getTestClassDetails().getMethodStatus(methodId);
        if(status.equals(TestClassDetails.TestStatus.RERAN)) {
            reportContext.getTestClassDetails().setMethodStatus(methodId, TestClassDetails.TestStatus.PASSED);
        }
        reporters.forEach(reporter -> reporter.onTestMethodEnded(reportContext));
    }

    public void rerunTestMethod() {
        reportContext.getTestClassDetails().setMethodStatus(reportContext.getTestMethodDetails().getId(), TestClassDetails.TestStatus.RERAN);
        reporters.forEach(reporter -> reporter.onTestMethodRerunned(reportContext));
    }

    public void pass(Object description) {
        log(description, LogStatus.PASS);
    }

    public void fail(Object description) {
        log(description, LogStatus.FAIL);
    }

    public void fatal(Object description) {
        log(description, LogStatus.FATAL);
    }

    public void error(Object description) {
        log(description, LogStatus.ERROR);
    }

    public void warning(Object description) {
        log(description, LogStatus.WARNING);
    }

    public void info(Object description) {
        log(description, LogStatus.INFO);
    }

    public void exception(Throwable t) {
       exception(null, t);
    }

    public void exception(String description, Throwable t) {
        if(description == null) {
            description = "";
        } else {
            description+= "\n";
        }
        Assert.fail(description, t);
    }

    public void log(Object description, LogStatus status) {
        LogDetails logDetails = logTracer(description);
        addLogToReporters(logDetails, status, takeScreenshot(status));
        if (status.equals(LogStatus.FAIL) ||
                status.equals(LogStatus.ERROR) ||
                status.equals(LogStatus.WARNING)) {
            if (!reportContext.isTestClass()) {
                reportContext.getTestClassDetails().setMethodStatus(reportContext.getTestMethodDetails().getId(), TestClassDetails.TestStatus.FAILED);
            } else {
                reportContext.getTestClassDetails().setTestStatus(TestClassDetails.TestStatus.FAILED);
            }
            //Assert.fail(description.toString());
        }
    }

    private void addLogToReporters(LogDetails description, LogStatus status, String screenshotPath) {
        reporters.forEach(reporter -> {
            if (screenshotPath == null) {
                reporter.addLog(reportContext, description, status);
            } else {
                reporter.addLog(reportContext, description, screenshotPath, status);
            }
        });
    }

    private String getScreenshotPath() {
        return reportSettings.getScreenShotFolderPath() + File.separator + UUID.randomUUID() + ".jpeg";
    }

    private Boolean isScreenShotRequired(LogStatus status) {
        return (reportSettings.getScreenShotOnFailedTests() && status.equals(LogStatus.FAIL)) ||
                (reportSettings.getScreenShotOnPassedTests() && status.equals(LogStatus.PASS));
    }

    private String takeScreenshot(LogStatus status) {
        if (isScreenShotRequired(status)) {
            try {
                return WebDriverUtils.takeScreenshot(driver, getScreenshotPath()).getPath();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private LogDetails logTracer(Object description) {
        int stackTraceIndex = 3;
        if (Thread.currentThread().getStackTrace()[stackTraceIndex].toString().contains(this.getClass().getName())) {
            stackTraceIndex++;
        }
        return new LogDetails(description, Thread.currentThread().getStackTrace()[stackTraceIndex]);
    }
}
