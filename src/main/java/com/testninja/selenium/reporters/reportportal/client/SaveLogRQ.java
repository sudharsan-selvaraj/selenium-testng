package com.testninja.selenium.reporters.reportportal.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonInclude(Include.NON_NULL)
public class SaveLogRQ {

    @JsonProperty("uuid")
    private String uuid;

    @JsonAlias({ "itemUuid", "item_id" })
    private String item_id;

    @JsonProperty(value = "launchUuid")
    @ApiModelProperty(required = true)
    private String launchUuid;

    @NotNull
    @JsonProperty(value = "time", required = true)
    @ApiModelProperty(required = true)
    private Date time;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "level")
    @ApiModelProperty(allowableValues = "error, warn, info, debug, trace, fatal, unknown")
    private String level;

    @JsonProperty(value = "file")
    private File file;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getLogTime() {
        return time;
    }

    public void setLogTime(Date logTime) {
        this.time = logTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemUuid() {
        return item_id;
    }

    public void setItemUuid(String itemUuid) {
        this.item_id = itemUuid;
    }

    public String getLaunchUuid() {
        return launchUuid;
    }

    public void setLaunchUuid(String launchUuid) {
        this.launchUuid = launchUuid;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @JsonInclude(Include.NON_NULL)
    public static class File {

        @JsonProperty(value = "name")
        private String name;

        private java.io.File file;

        public java.io.File getFile() {
            return file;
        }

        public void setFile(java.io.File file) {
            this.file = file;
            this.name = file.getName();
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SaveLogRQ{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", itemUuid='").append(item_id).append('\'');
        sb.append(", launchUuid='").append(launchUuid).append('\'');
        sb.append(", logTime=").append(time);
        sb.append(", message='").append(message).append('\'');
        sb.append(", level='").append(level).append('\'');
        sb.append(", file=").append(file);
        sb.append('}');
        return sb.toString();
    }
}
