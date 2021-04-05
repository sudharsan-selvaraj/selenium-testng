package com.testninja.selenium.framework.report;

public interface IReportType {

    void testClassStarted(ReportContext reportContext);

    void testClassEnded(ReportContext reportContext);

    void onTestMethodStarted(ReportContext reportContext);

    void onTestMethodEnded(ReportContext reportContext);

    void onTestMethodRerunned(ReportContext reportContext);

    void addLog(ReportContext reportContext,LogDetails logDetails, String ScreenshotPath, LogStatus status);

    void addLog(ReportContext reportContext,LogDetails logDetails, LogStatus status);

}
