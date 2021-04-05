package com.testninja.selenium.reporters.console;

import com.testninja.selenium.framework.parameters.ApplicationParameters;
import com.testninja.selenium.framework.report.*;
import com.testninja.selenium.framework.testrunner.annotations.TestClass;
import com.testninja.selenium.utils.ConsoleTablePrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class ConsoleReporter implements IReportType, ISuiteListener, IExecutionListener, ITestListener {

    Logger logger = LoggerFactory.getLogger(ConsoleReporter.class);
    static Map<String, List<String>> testStacktrace = new HashMap<>();
    static Map<String, Boolean> isTestReruned = new HashMap<>();
    static List<SkippedClassInfo> skippedClassInfo = new ArrayList<>();
    static ApplicationParameters parameters;
    static ExecutionTimeTracker executionTimeTracker = new ExecutionTimeTracker();
    static Integer failedTestCount = 0;

    public static void setApplicationParameter(ApplicationParameters parameters) {
        ConsoleReporter.parameters = parameters;
    }

    @Override
    public void addLog(ReportContext reportContext, LogDetails logDetails, String screenShotPath, LogStatus status) {
        addLog(reportContext, logDetails, status);
    }

    @Override
    public void addLog(ReportContext reportContext, LogDetails logDetails, LogStatus status) {
        String testId = getTestId(reportContext);
        if (isTestReruned.containsKey(testId) && isTestReruned.get(testId) && isException(logDetails)) {
            String logMessage = "\n"+formatException(logDetails);
            addStackTraceToTest(testId, logMessage);
        }
    }

    @Override
    public void testClassEnded(ReportContext reportContext) {
        String testId = getTestId(reportContext);
        TestClassDetails testClassDetails = reportContext.getTestClassDetails();
        if (testClassDetails.getStatus().equals(TestClassDetails.TestStatus.FAILED) && !isTestReruned.containsKey(testId)) {
            return;
        }
        String endTime = executionTimeTracker.testClassEnded(reportContext.getTestClassDetails().getSuite().getName(), reportContext.getTestClassDetails().getXmlTestName(), reportContext.getTestClassDetails().getId());
        String logMessage = "[" + testClassDetails.getSuite().getName() + "] " + testClassDetails.getClassName() + " [" + testClassDetails.getStatus().toString() + "] ["+ endTime + "]" ;
        if(!testClassDetails.getStatus().equals(TestClassDetails.TestStatus.FAILED)){
            System.out.println(logMessage);
        } else {
            System.out.println(logMessage);
            failedTestCount++;
//            if(testStacktrace.get(testId) !=null) {
//                for(String stackTrace: testStacktrace.get(testId)) {
//                    logger.error(stackTrace+"\n");
//                }
//            }
        }
    }

    @Override
    public void onTestMethodRerunned(ReportContext reportContext) {
        isTestReruned.put(getTestId(reportContext), true);
    }

    private String getTestId(ReportContext reportContext) {
        return reportContext.getTestClassDetails().getId();
    }

    private boolean isException(LogDetails logDetails) {
        return (logDetails.getLogDescription() instanceof Throwable);
    }

    private String formatException(LogDetails logDetails) {
        StringWriter stringWriter = new StringWriter();
        ((Throwable) logDetails.getLogDescription()).printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private synchronized void addStackTraceToTest(String testId, String exceptionMessage) {
       try {
           if(testStacktrace.get(testId) == null) {
               List<String> stackTrace = new ArrayList<String>(Arrays.asList(exceptionMessage));
               testStacktrace.put(testId, stackTrace);
           } else {
               List<String> stackTrace = testStacktrace.get(testId);
               stackTrace.add(exceptionMessage);
           }
       } catch (Exception e) {
           logger.error("error adding log to console", e);
       }
    }

    @Override
    public void testClassStarted(ReportContext reportContext) {
        executionTimeTracker.testClassStarted(reportContext.getTestClassDetails().getSuite().getName(), reportContext.getTestClassDetails().getXmlTestName(), reportContext.getTestClassDetails().getId());
        isTestReruned.putIfAbsent(reportContext.getTestClassDetails().getId(), true);
        //System.out.println("Test [started]" + reportContext.getTestClassDetails().getClassName());
    }

    @Override
    public void onTestMethodStarted(ReportContext reportContext) {
        //not implemented
    }

    @Override
    public void onTestMethodEnded(ReportContext reportContext) {
        //not implemented
    }

    @Override
    public void onStart(ISuite suite) {
        executionTimeTracker.suiteStarted(suite.getName());
        for(XmlTest test:suite.getXmlSuite().getTests()) {
            for(XmlClass xmlClass: test.getClasses()) {
                try {
                    Class<?> klass = Class.forName(xmlClass.getName());
                    if(klass.isAnnotationPresent(TestClass.class)) {
                        TestClass testClassAnnotation = klass.getAnnotation(TestClass.class);
                        if (!testClassAnnotation.enabled()) {
                            skippedClassInfo.add(new SkippedClassInfo(xmlClass.getName(), testClassAnnotation.skippedReason()));
                        }
                    }
                } catch (Exception e ){
                    //ignore exception
                }
            }
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        executionTimeTracker.suiteEnded(suite.getName());
    }

    public void onExecutionStart() {
        System.out.println("\n****************** Execution Started *******************");
        System.out.println("BASE URL: "+parameters.getBaseUrl());
        //System.out.println("PRODUCT: "+parameters.getProduct());
        System.out.println("PARALLEL MODE: "+ parameters.getParallelMode());
        System.out.println("GROUPS : "+ (parameters.getGroups() == null ? "-" : parameters.getGroups()));
        System.out.println("THREAD COUNT : " + parameters.getThreadCount());
        //System.out.println("BRANCH : "+ parameters.getBranch());
    }

    @Override
    public void onExecutionFinish() {
        String[] headers = new String[]{"CLASS NAME", "REASON"};
        ArrayList<String[]> content = new ArrayList<String[]>();
        if (skippedClassInfo.size() > 0) {
            System.out.println("\n\n******************** SKIPPED TESTS *******************\n");
            for (SkippedClassInfo skippedClassInfo : skippedClassInfo) {
                String[] row = new String[] {
                        skippedClassInfo.getClassName(),
                        skippedClassInfo.getSkippedReason()
                };
                content.add(row);
            }

            ConsoleTablePrinter consoleTablePrinter = new ConsoleTablePrinter(headers, content);
            consoleTablePrinter.printTable();
        }
    }

    /**
     * Invoked after all the test methods belonging to the classes inside the &lt;test&gt; tag have run
     * and all their Configuration methods have been called.
     */
    @Override
    public void onStart(ITestContext context) {
        if (context.getAllTestMethods().length > 0) {
            executionTimeTracker.testStarted(context.getSuite().getName(), context.getName());
            System.out.println("\n******************** MODULE "+ context.getName() +" STARTED *******************\n");
        }
    }

    /**
     * Invoked after all the test methods belonging to the classes inside the &lt;test&gt; tag have run
     * and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) {
        if (context.getAllTestMethods().length > 0) {
            System.out.println("\n******************** MODULE " + context.getName() + " COMPLETED in " + executionTimeTracker.testEnded(context.getSuite().getName(), context.getName()) + "*******************\n");
        }
    }

    public static Integer getExitCode() {
        return failedTestCount == 0 ? 0 : 1;
    }
}
