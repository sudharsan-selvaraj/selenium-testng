package com.testninja.selenium.framework;

import com.testninja.selenium.framework.Browser;
import com.testninja.selenium.framework.SuiteInfo;
import com.testninja.selenium.framework.SuiteTransformListener;
import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.reporters.console.ConsoleReporter;
import com.testninja.selenium.reporters.extent.ExtentReportManager;
import com.testninja.selenium.listeners.AnnotationTransformer;
import com.testninja.selenium.reporters.reportportal.ReportPortalListener;

/**
 * Based on the command line arguments, suite xml files will be generated dynamically.
 * Before creating the xml files, transformSuite callback will be called with SuiteInfo object
 * which represents the suite xml file. The SuiteInfo can be further customised based on the
 * execution requirements.
 */
public class SuiteTransformer implements SuiteTransformListener {

    private ApplicationParameters parameters;

    public SuiteTransformer(ApplicationParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public SuiteInfo transformSuite(SuiteInfo suite) {

        /* Below statements will add these parameters to generated suite.xml files */
        suite.addParameter("browserName", suite.getBrowser().name());
        suite.addParameter("baseUrl", this.parameters.getBaseUrl());
        suite.addParameter("seleniumAddress", this.parameters.getSeleniumAddress());

        if (this.parameters.getChromeDriverPath() != null) {
            System.setProperty("webdriver.chrome.driver", this.parameters.getChromeDriverPath());
            System.setProperty("webdriver.chrome.silentOutput", "true");
        }

        if (this.parameters.getEdgeDriverPath() != null) {
            System.setProperty("webdriver.edge.driver", this.parameters.getEdgeDriverPath());
        }

        suite.addListener(ExtentReportManager.class.getCanonicalName());
        suite.addListener(AnnotationTransformer.class.getCanonicalName());
        //suite.addListener(ReportPortalListener.class.getCanonicalName());
        suite.addListener(ConsoleReporter.class.getCanonicalName());

        return suite;
    }
}
