/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import IO.Console;
import FeatureExtraction.AFeatureExtractor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorXMLStructuralPaths<T> extends AFeatureExtractor<T> {

    @Override
    public Map<String, Integer> ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String filePath = (String) element;
        AddXMLStructuralPaths(filePath, structuralPaths);
        return structuralPaths;
    }

    /**
     * Add structural paths from the given xml file into the local map
     *
     * @param xmlFilePath the path of a xml file
     * @param structuralPaths the Map to add the extracted features to
     */
    private void AddXMLStructuralPaths(String xmlFilePath, Map<String, Integer> structuralPaths) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xml = db.parse(xmlFilePath);
            NodeList nodeList = xml.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                AddXMLStructuralPathsRecursively(nodeList.item(i), xmlFilePath, structuralPaths);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Console.PrintException(String.format("Error traversing XML file: '%s'", xmlFilePath), ex);
        }
    }

    /**
     * Add structural paths from the given xml file into the local map
     * recursively
     *
     * @param xmlNode xmlNode to look for its childs
     * @param parentNodePath the path of the parent node
     * @param structuralPaths the Map to add the extracted features to
     */
    private void AddXMLStructuralPathsRecursively(Node xmlNode, String parentNodePath, Map<String, Integer> structuralPaths) {
        String currentNodePath = String.format("%s\\%s", parentNodePath, xmlNode.getNodeName());
        AddStructuralPath(currentNodePath, structuralPaths);

        NodeList childNodes = xmlNode.getChildNodes();
        Node childNode;
        for (int i = 0; i < childNodes.getLength(); i++) {
            childNode = childNodes.item(i);
            AddXMLStructuralPathsRecursively(childNode, currentNodePath, structuralPaths);
        }
    }

    /**
     * Add structural path to local Map
     *
     * @param structuralPath the key to add to the map
     * @param structuralPaths the Map to add the feature to
     */
    private void AddStructuralPath(String structuralPath, Map<String, Integer> structuralPaths) {
        if (!structuralPaths.containsKey(structuralPath)) {
            structuralPaths.put(structuralPath, 1);
        } else {
            structuralPaths.put(structuralPath, structuralPaths.get(structuralPath) + 1);
        }
    }

    @Override
    public String GetName() {
        return "XML Structural Paths";
    }
}
