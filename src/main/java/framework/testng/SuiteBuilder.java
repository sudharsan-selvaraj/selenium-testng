package framework.testng;

import framework.utils.FileUtils;
import org.testng.xml.XmlSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SuiteBuilder {

    private String modulesPath;

    public SuiteBuilder(String modulesPath) {
        this.modulesPath = modulesPath;
    }

    public void generateTestNgSuiteFile(SuiteInfo suiteInfo, String outputPath) {
        try {
            Document suiteDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element suiteElement = constructRootElement(suiteDocument, suiteInfo);
            if (suiteElement != null) {
                addParametersToSuite(suiteDocument, suiteElement, suiteInfo.getParameters());
                addModulesToSuite(suiteDocument, suiteElement, suiteInfo.getModules());
            }
            suiteDocument.appendChild(suiteElement);
            saveSuiteXmlFile(suiteDocument, outputPath);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }


    private Element constructRootElement(Document doc, SuiteInfo suiteInfo) {
        try {
            Element rootElement = doc.createElement("suite");
            rootElement.setAttribute("name", suiteInfo.getName());

            if (isParallelModeEnabled(suiteInfo.getParallelMode())) {
                rootElement.setAttribute("parallel", suiteInfo.getParallelMode().toString());
                rootElement.setAttribute("thread-count", suiteInfo.getThreadCount().toString());
            }
            return rootElement;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private void addModulesToSuite(Document suiteDocument, Element suiteElement, List<Modules> modules) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
        for (Modules module : modules) {
            String XMLFilepath = getModuleXmlFilePath(module);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(FileUtils.readFileAsStream(XMLFilepath));
            doc.getDocumentElement().normalize();
            NodeList testNodes = doc.getElementsByTagName("test");
            for (int i = 0; i < testNodes.getLength(); i++) {
                suiteElement.appendChild(suiteDocument.importNode(testNodes.item(i), true));
            }
        }
    }

    private void addParametersToSuite(Document suiteDocument, Element suiteElement, Map<String, String> parameters) {
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            Element parameterElement = suiteDocument.createElement("parameter");
            parameterElement.setAttribute("name", param.getKey());
            parameterElement.setAttribute("value", param.getValue());
            suiteElement.appendChild(parameterElement);
        }
    }

    private void saveSuiteXmlFile(Document suiteDocument, String filePath) throws TransformerConfigurationException, TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "https://testng.org/testng-1.0.dtd");
        t.transform(new DOMSource(suiteDocument), new StreamResult(filePath));
    }

    private String getModuleXmlFilePath(Modules module) {
        return modulesPath + File.separator + module.name() + ".xml";
    }

    private boolean isParallelModeEnabled(XmlSuite.ParallelMode mode) {
        return (!mode.equals(XmlSuite.ParallelMode.NONE) && !mode.equals(XmlSuite.ParallelMode.FALSE));
    }
}
