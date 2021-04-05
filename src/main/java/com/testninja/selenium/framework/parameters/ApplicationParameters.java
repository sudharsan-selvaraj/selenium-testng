package com.testninja.selenium.framework.parameters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;

import com.testninja.selenium.TestNgGroups;
import org.openqa.selenium.Dimension;

import java.io.File;

/**
 * Class representation of CLI arguments required by the application under test.
 */

public class ApplicationParameters extends FrameworkParameters {

    @Parameter(names = {"--baseUrl"}, description = "Url of the application")
    private String baseUrl = "http://localhost:8000/datai";

    @Parameter(names = {"--screenSize"}, description = "Screen size in width x height format", converter = ApplicationParameters.ScreenSizeConverter.class)
    private Dimension screenSize = new Dimension(1440, 1050);

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    static class ScreenSizeConverter implements IStringConverter<Dimension> {
        @Override
        public Dimension convert(String value) {
            String[] dimensions = value.split("x");
            if (dimensions.length == 2) {
                return new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
            }
            return null;
        }
    }
}
