package com.testninja.selenium.framework.report;

public class TestMethodDetails {
    private String name;
    private String description;
    private String id;
    private Boolean reran = false;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getReran() {
        return reran;
    }

    public void setReran(Boolean reran) {
        this.reran = reran;
    }
}
