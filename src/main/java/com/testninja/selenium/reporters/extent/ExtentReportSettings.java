package com.testninja.selenium.reporters.extent;

import java.util.Map;

/**
 * Settings required to customise Extent HTML Report
 *
 */
public class ExtentReportSettings {

    private String outputPath;
    private Boolean clearOutputPath = false;
    private Boolean consolidateReport = true;
    Map<String, String> systemInfo;

    public Map<String, String> getSystemInfo() {
        return systemInfo;
    }

    public ExtentReportSettings setSystemInfo(Map<String, String> systemInfo) {
        this.systemInfo = systemInfo;
        return this;
    }

    public Boolean getConsolidateReport() {
        return consolidateReport;
    }

    public ExtentReportSettings setConsolidateReport(Boolean consolidateReport) {
        this.consolidateReport = consolidateReport;
        return this;
    }

    private IExtentReportListener extentReportListener;

    public String getOutputPath() {
        return outputPath;
    }

    public ExtentReportSettings setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }

    public IExtentReportListener getExtentReportListener() {
        return extentReportListener;
    }

    public Boolean getClearOutputPath() {
        return clearOutputPath;
    }

    public ExtentReportSettings setClearOutputPath(Boolean clearOutputPath) {
        this.clearOutputPath = clearOutputPath;
        return this;
    }

    public void setExtentReportListener(IExtentReportListener extentReportListener) {
        this.extentReportListener = extentReportListener;
    }
}
