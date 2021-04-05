package com.testninja.selenium.reporters.reportportal.client.models;

import com.epam.ta.reportportal.ws.model.attribute.ItemAttributeResource;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.epam.ta.reportportal.ws.model.statistics.StatisticsResource;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;

public class LaunchResponse {

    private Long id;

    private String uuid;

    private String name;

    private Long number;

    private String description;

    private String status;

    @JsonProperty(value = "statistics")
    @Valid
    private StatisticsResource statisticsResource;

    private Set<ItemAttributeResource> attributes;

    private Mode mode;

    @JsonProperty(value = "analysing")
    private Set<String> analyzers = new LinkedHashSet<>();

    private double approximateDuration;

    private boolean hasRetries;

    private boolean rerun;

    public double getApproximateDuration() {
        return approximateDuration;
    }

    public void setApproximateDuration(double approximateDuration) {
        this.approximateDuration = approximateDuration;
    }

    public Long getLaunchId() {
        return id;
    }

    public void setLaunchId(Long launchId) {
        this.id = launchId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StatisticsResource getStatisticsResource() {
        return statisticsResource;
    }

    public void setStatisticsResource(StatisticsResource statisticsResource) {
        this.statisticsResource = statisticsResource;
    }

    public Set<ItemAttributeResource> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<ItemAttributeResource> attributes) {
        this.attributes = attributes;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Set<String> getAnalyzers() {
        return analyzers;
    }

    public void setAnalyzers(Set<String> analyzers) {
        this.analyzers = analyzers;
    }

    public boolean isHasRetries() {
        return hasRetries;
    }

    public boolean getHasRetries() {
        return hasRetries;
    }

    public void setHasRetries(boolean hasRetries) {
        this.hasRetries = hasRetries;
    }

    public boolean isRerun() {
        return rerun;
    }

    public void setRerun(boolean rerun) {
        this.rerun = rerun;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LaunchResource{");
        sb.append("id=").append(id);
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", number=").append(number);
        sb.append(", description='").append(description).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", statisticsResource=").append(statisticsResource);
        sb.append(", attributes=").append(attributes);
        sb.append(", mode=").append(mode);
        sb.append(", analyzers=").append(analyzers);
        sb.append(", approximateDuration=").append(approximateDuration);
        sb.append(", hasRetries=").append(hasRetries);
        sb.append(", rerun=").append(rerun);
        sb.append('}');
        return sb.toString();
    }
}

