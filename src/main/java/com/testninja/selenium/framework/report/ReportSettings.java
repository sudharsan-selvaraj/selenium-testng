package com.testninja.selenium.framework.report;

import com.testninja.selenium.utils.FileUtils;

public class ReportSettings {

    private static final String SCREENSHOTS_FOLDER_NAME = "result/screen_shots";
    private Boolean screenShotOnFailedTests = true;
    private Boolean screenShotOnPassedTests = false;
    private String screenShotFolderPath = _getScreenshotsPath();

    public String getScreenShotFolderPath() {
        return screenShotFolderPath;
    }

    public void setScreenShotFolderPath(String screenShotFolderPath) {
        this.screenShotFolderPath = screenShotFolderPath;
    }

    public Boolean getScreenShotOnPassedTests() {
        return screenShotOnPassedTests;
    }

    public void setScreenShotOnPassedTests(Boolean screenShotOnPassedTests) {
        this.screenShotOnPassedTests = screenShotOnPassedTests;
    }

    public Boolean getScreenShotOnFailedTests() {
        return this.screenShotOnFailedTests;
    }

    public void setScreenShotOnFailedTests(Boolean screenShotOnFailedTests) {
        this.screenShotOnFailedTests = screenShotOnFailedTests;
    }

    private String _getScreenshotsPath() {
        return FileUtils.getAbsoluteFolderPath(SCREENSHOTS_FOLDER_NAME);
    }

}
