package com.testninja.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebElementUtils {

    public static List<WebElement> textContainsFilter(List<WebElement> webElements, String textToBe) {
        return webElements.stream().filter(webElement -> webElement.getText()
                .contains(textToBe)).collect(Collectors.toList());
    }

    public static List<WebElement> textContainsIgnoreCaseFilter(List<WebElement> webElements, String textToBe) {
        return webElements.stream().filter(webElement -> webElement.getText().toUpperCase()
                .contains(textToBe.toUpperCase())).collect(Collectors.toList());
    }

    public static List<WebElement> textContainsFilter(List<WebElement> webElements, By childLocator,  String textToBe) {
        return webElements.stream().filter(webElement -> webElement.findElement(childLocator).getText()
                .contains(textToBe)).collect(Collectors.toList());
    }

    public static List<WebElement> textEqualsFilter(List<WebElement> webElements, String textToBe) {
        return webElements.stream().filter(webElement -> webElement.getText()
                .equalsIgnoreCase(textToBe)).collect(Collectors.toList());
    }

    public static  List<WebElement> displayFilter(List<WebElement> webElements) {
        return webElements.stream().filter(webElement -> AssertionUtils.isDisplayed(webElement)).
                collect(Collectors.toList());
    }

    public static  List<WebElement> containsClass(List<WebElement> webElements, String className) {
        return webElements.stream().filter(webElement -> AssertionUtils.hasClass(webElement, className)).
                collect(Collectors.toList());
    }

    public static  List<WebElement> notContainsClass(List<WebElement> webElements, String className) {
        return webElements.stream().filter(webElement -> !AssertionUtils.hasClass(webElement, className)).
                collect(Collectors.toList());
    }

    public static List<WebElement> filterByChildElement(WebElement parentElement, By childLocator) {
        try {
            return parentElement.findElements(childLocator);
        } catch (Exception e) {
            return new ArrayList<WebElement>();
        }
    }

    public static Boolean isChildElementDisplayed(WebElement parentElement, By childLocator) {
        try {
            return AssertionUtils.isDisplayed(parentElement.findElement(childLocator));
        } catch (Exception e) {
            return false;
        }
    }

    public static List<WebElement> filterByChildElement(List<WebElement> webElements, By childLocator) {
        return webElements.stream().filter(webElement -> isChildElementDisplayed(webElement, childLocator)).collect(Collectors.toList());
    }
}
