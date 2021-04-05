package com.testninja.selenium.framework.testrunner;

public class InvalidTestClassException extends Exception {
    public InvalidTestClassException(Object obj) {
        super("Class "+ obj.getClass().getName()+ " is not annotaded with @TestClass");
    }
}