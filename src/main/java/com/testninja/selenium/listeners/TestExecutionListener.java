package com.testninja.selenium.listeners;

import com.testninja.selenium.Globals;
import com.testninja.selenium.framework.parameters.ApplicationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IExecutionListener;

public class TestExecutionListener implements IExecutionListener {

    Logger logger = LoggerFactory.getLogger(TestExecutionListener.class);

    private ApplicationParameters applicationParameters;

    public TestExecutionListener(ApplicationParameters parameters) {
        this.applicationParameters = parameters;
    }

    @Override
    public void onExecutionStart() {
        /* Global class holds all the shared resources required by the test */
        Globals.setParameters(applicationParameters);

    }

    @Override
    public void onExecutionFinish() {

    }

}
