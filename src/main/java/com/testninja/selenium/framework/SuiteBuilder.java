package com.testninja.selenium.framework;

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
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Class used to construct the TestNg xml file for the given SuiteInfo object.
 *
 */

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
                addListenersToSuite(suiteDocument, suiteElement, suiteInfo.getListeners());
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
                rootElement.setAttribute("verbose", "0");
                rootElement.setAttribute("parallel", suiteInfo.getParallelMode().toString());
                rootElement.setAttribute("thread-count", suiteInfo.getThreadCount().toString());
            } else {
                rootElement.setAttribute("parallel", suiteInfo.getParallelMode().toString());
            }
            return rootElement;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private void addModulesToSuite(Document suiteDocument, Element suiteElement, List<String> modules) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
        for (String module : modules) {
            //String XMLFilepath = getModuleXmlFilePath(module);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getModuleXmlFileAsStream(module));
            doc.getDocumentElement().normalize();
            NodeList testNodes = doc.getElementsByTagName("tests");
            for (int i = 0; i < testNodes.getLength(); i++) {
                for(int j=0;j<testNodes.item(0).getChildNodes().getLength();j++) {
                    suiteElement.appendChild(suiteDocument.importNode(testNodes.item(0).getChildNodes().item(j), true));
                }
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

    private void addListenersToSuite(Document suiteDocument, Element suiteElement, List<String> listenerClasses) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {

        if(listenerClasses.size() == 0) {
            return;
        }

        Element parameterElement = suiteDocument.createElement("listeners");
        for (String listener : listenerClasses) {
            Element listenerElement = suiteDocument.createElement("listener");
            listenerElement.setAttribute("class-name", listener);
            parameterElement.appendChild(listenerElement);
        }
        suiteElement.appendChild(parameterElement);
    }

    private void saveSuiteXmlFile(Document suiteDocument, String filePath) throws TransformerConfigurationException, TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "https://testng.org/testng-1.0.dtd");
        t.transform(new DOMSource(suiteDocument), new StreamResult(filePath));
    }

    private String getModuleXmlFilePath(String moduleName) {
        String fileName = modulesPath + File.separator + moduleName + ".xml";
        return new File(getClass().getClassLoader().getResource(fileName).getFile()).getPath();
    }

    private InputStream getModuleXmlFileAsStream(String moduleName) {
        String fileName =  "/" + modulesPath + "/" + moduleName + ".xml";
        return getClass().getResourceAsStream(fileName);
    }

    private boolean isParallelModeEnabled(XmlSuite.ParallelMode mode) {
        return (!mode.equals(XmlSuite.ParallelMode.NONE));
    }
}
