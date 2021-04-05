package com.testninja.selenium.reporters.console;

import java.util.*;

public class ExecutionTimeTracker {

    Map<String, Entity> suites = new HashMap<>();

    public void suiteStarted(String name) {
        Entity suite = new Entity();
        suites.put(name, suite);
        suite.start();
    }

    public String suiteEnded(String name) {
        suites.get(name).end();
        return suites.get(name).getExecutionTime();
    }

    public void testStarted(String suiteName, String testName) {
        Entity test = new Entity();
        test.start();
        suites.get(suiteName).addChild(testName, test);
    }

    public String testEnded(String suiteName, String name) {
        suites.get(suiteName).getChild(name).end();
        return suites.get(suiteName).getChild(name).getExecutionTime();
    }

    public void testClassStarted(String suiteName, String testName, String testClassIdentifier) {
        Entity testClass = new Entity();
        testClass.start();
        suites.get(suiteName).getChild(testName).addChild(testClassIdentifier, testClass);
    }

    public String testClassEnded(String suiteName, String testName, String testClassIdentifier) {
        suites.get(suiteName).getChild(testName).getChild(testClassIdentifier).end();
        return suites.get(suiteName).getChild(testName).getChild(testClassIdentifier).getExecutionTime();
    }

}


interface EntityRunTime {
    public void start();

    public void end();

    public String getExecutionTime();

    public void addChild(String childIdentifier, Entity entity);

    public Entity getChild(String childIdentifier);
}

class Entity implements EntityRunTime {

    Map<String, Entity> childEntities = new HashMap<>();

    private Date startDate;
    private Date endDate;

    @Override
    public void start() {
        startDate = new Date();
    }

    @Override
    public void end() {
        endDate = new Date();
    }

    @Override
    public void addChild(String childIdentifier, Entity entity) {
        this.childEntities.put(childIdentifier, entity);
    }

    @Override
    public Entity getChild(String childIdentifier) {
        return this.childEntities.get(childIdentifier);
    }

    @Override
    public String getExecutionTime() {
        long second = 1000l;
        long minute = 60l * second;
        long hour = 60l * minute;
        long diff = endDate.getTime() - startDate.getTime();

        StringBuilder time = new StringBuilder();
        time.append(String.format("%02d", diff / hour));
        time.append("h ");

        time.append(String.format("%02d", (diff % hour) / minute));
        time.append("m ");

        time.append(String.format("%02d", (diff % minute) / second));
        time.append("s");

        return time.toString();
    }
}
