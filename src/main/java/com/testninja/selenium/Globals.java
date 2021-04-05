package com.testninja.selenium;

import com.testninja.selenium.framework.parameters.ApplicationParameters;

public class Globals {

    private static ApplicationParameters parameters;

    public static ApplicationParameters getParameters() {
        return parameters;
    }

    public static void setParameters(ApplicationParameters parameters) {
        Globals.parameters = parameters;
    }
}