package com.testninja.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.awt.*;

public class AssertionUtils {

    public static Boolean hasClass(WebElement element, String expectedClass) {
        return element.getAttribute("class").contains(expectedClass);
    }

    public static Boolean isPresent(WebElement parentElement, By locator) {
        return parentElement.findElements(locator).size() > 0;
    }

    public static Boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static Boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static Boolean isCheckboxSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static String covertToHexCode(String RgbBorderColor) {
        String[] rgb = RgbBorderColor.replace("rgba(", "").replace(")", "").split(",");
        Color c = new Color(Integer.valueOf(rgb[0].trim()), Integer.valueOf(rgb[1].trim()), Integer.valueOf(rgb[2].trim()));
        return "#" + Integer.toHexString(c.getRGB() & 0xffffff);
    }

}
