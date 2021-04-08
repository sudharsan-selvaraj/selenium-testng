package com.testninja.selenium.framework.pageobject;

import com.github.webdriverextensions.WebDriverExtensionFieldDecorator;
import com.testninja.selenium.ScriptHelper;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Field;

public class BaseWebComponentFieldDecorator implements FieldDecorator {

    private ScriptHelper scriptHelper;
    private FieldDecorator fieldDecorator;

    public BaseWebComponentFieldDecorator(ScriptHelper scriptHelper) {
        this.scriptHelper = scriptHelper;
        this.fieldDecorator = new WebDriverExtensionFieldDecorator(scriptHelper.getDriver());
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        Object proxyWebElement = fieldDecorator.decorate(loader, field);

        if(proxyWebElement instanceof BaseWebComponent) {
            ((BaseWebComponent)proxyWebElement).setScriptHelper(scriptHelper);
        }
        return proxyWebElement;
    }
}
