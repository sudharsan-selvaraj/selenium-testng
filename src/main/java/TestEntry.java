import framework.core.*;
import framework.utils.FileUtils;
import org.testng.TestNG;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestEntry {
    public static void main(String[] args) {

        FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
        FrameworkSettings settings = FrameworkSettings.getInstance();

        Launcher launcher = new LaunchBuilder()
                 .setCliArguments(args)
                 .setFrameWorkParameters(frameworkParameters)
                 .setLaunchListener(new LaunchListener())
                 .build();

         launcher.launch();



//
//        Parameters<FrameworkParameters> parameters =  Parameters.getInstance();
//        parameters.initialize(frameworkParameters, args);











        System.out.println(settings.getBrowsers());
        System.out.println(frameworkParameters.getBaseUrl());
        System.out.println(frameworkParameters.getScreenSize());

        try {
            List<String> testFilesList = new ArrayList<>();
            int i = 0;
            for(BrowserType browser: settings.getBrowsers()) {
                if(i>1) {
                    return;
                }
                i++;
                if(browser!=null) {
                    String[] modules = new String[] { "jobs", "users", "datasets" };
                    Document suiteDoc =  DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                    Element rootElement = suiteDoc.createElement("suite");
                    if(settings.getParallelMode() != null) {
                        rootElement.setAttribute("parallel", "tests");
                        rootElement.setAttribute("thread-count", "2");
                    }
                    rootElement.setAttribute("name", browser.name());
                    for(String module: modules) {
                        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(FileUtils.readFileAsStream("config-files/suites/" +module+".xml"));
                        doc.getDocumentElement().normalize();
                        rootElement.appendChild(suiteDoc.importNode(doc.getElementsByTagName("test").item(0),true));
                    }

                    suiteDoc.appendChild(rootElement);
                    Transformer t =  TransformerFactory.newInstance().newTransformer();
                    t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "https://testng.org/testng-1.0.dtd");
                    t.transform(new DOMSource(suiteDoc), new StreamResult(new File("/Users/sudharsan/Documents/git/selenium-testng/"+browser.name()+".xml")));
                    testFilesList.add("/Users/sudharsan/Documents/git/selenium-testng/"+browser.name()+".xml");
                }
            }

            TestNG a = new TestNG();
            a.setTestSuites(testFilesList);
            a.setSuiteThreadPoolSize(1);
            //a.setGroups("jobs,datasets");
            a.run();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
