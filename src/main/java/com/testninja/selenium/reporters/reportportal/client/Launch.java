package com.testninja.selenium.reporters.reportportal.client;

import com.epam.ta.reportportal.ws.model.launch.StartLaunchRS;

public class Launch {

    private StartLaunchRS launchResponse;

    public Launch(StartLaunchRS launchResponse) {
        this.launchResponse = launchResponse;
    }

    public String getLaunchGuid() {
        return launchResponse.getId();
    }
}
