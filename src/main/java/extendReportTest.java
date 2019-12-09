import framework.core.BrowserType;

public class extendReportTest {

    public static void main(String[] args) {
//        ExtentReports report = new ExtentReports();
//        ExtentHtmlReporter html = new ExtentHtmlReporter(System.getProperty("user.dir")+"/ExtentReportResults.html");
//        report.attachReporter(html);
//
//        ExtentTest browser = report.createTest("chrome");
//        ExtentTest module = browser.createNode("module");
//        ExtentTest test = module.createNode("test 1");
//        test.log(Status.PASS, "First Test step");
//        test.log(Status.FAIL, "Second Test step");
//        test.log(Status.PASS, "First Test step");
//
//
//        ExtentTest test1 = test.createNode("test 1");
//        ExtentTest test2 = test1.createNode("test 2");
//        test2.log(Status.PASS, "First Test step");
//        test2.log(Status.PASS, "Second Test step");
//        test2.log(Status.PASS, "First Test step");
//
//        report.flush();

        BrowserType b = BrowserType.chrome;
        System.out.println(BrowserType.valueOf("chrome1"));
    }

}
