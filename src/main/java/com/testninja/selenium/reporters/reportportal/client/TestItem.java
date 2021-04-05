package com.testninja.selenium.reporters.reportportal.client;

import com.epam.ta.reportportal.ws.model.item.ItemCreatedRS;

public class TestItem {

    private ItemCreatedRS itemCreatedRS;
    private Launch launch;

    public TestItem(Launch launch, ItemCreatedRS itemReponse) {
        this.itemCreatedRS = itemReponse;
        this.launch = launch;
    }

    public TestItem(TestItem testItem, ItemCreatedRS itemReponse) {
        this.itemCreatedRS = itemReponse;
        this.launch = testItem.launch;
    }

    public String getUuid() {
        return itemCreatedRS.getId();
    }

    public String getLaunchUUid() {
        return this.launch.getLaunchGuid();
    }

}
