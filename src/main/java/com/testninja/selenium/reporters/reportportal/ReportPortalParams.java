package com.testninja.selenium.reporters.reportportal;

import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.Mode;

import java.util.Set;

public class ReportPortalParams {

    private String branchName;
    private Set<ItemAttributesRQ> attributes;
    private Mode mode;
    private Boolean attachScreenShot = true;

    public ReportPortalParams(String branchName, Mode mode, Boolean attachScreenShot) {
        this.branchName = branchName;
        this.mode = mode;
        this.attachScreenShot = attachScreenShot;
    }

    public Boolean getAttachScreenShot() {
        return attachScreenShot;
    }

    public void setAttachScreenShot(Boolean attachScreenShot) {
        this.attachScreenShot = attachScreenShot;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Set<ItemAttributesRQ> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<ItemAttributesRQ> attributes) {
        this.attributes = attributes;
    }
}
