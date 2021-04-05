package com.testninja.selenium.reporters.extent;

import org.testng.ISuite;

/**
 * To override the file path to save the reports in run time.
 */
public interface IExtentReportListener {

    String getBaseOutputFolder();

    String getSuiteHTMlPath(ISuite suite);
}
