/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import Console.Console;
import FeatureExtraction.AFeatureExtractor;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorOOXMLStructuralPathsEnhanced<T> extends AFeatureExtractor<T> {

    //private String m_OfficeFileTempFolderPath = "";
    @Override
    public Map<String, Integer> ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String filePath = (String) element;
        ExtractStructuralFeaturesInMemory(filePath, structuralPaths);
        return structuralPaths;
    }

    private void ExtractStructuralFeaturesInMemory(String filePath, Map<String, Integer> structuralPaths) {
        String path;
        String directoryPath;
        String fileExtension;
        try {
            ZipFile zipFile = new ZipFile(filePath);

            for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                path = entry.getName();
                directoryPath = FilenameUtils.getFullPath(path);
                fileExtension = FilenameUtils.getExtension(path);

                AddStructuralPath(directoryPath, structuralPaths);
                AddStructuralPath(path, structuralPaths);

                if (fileExtension.equals("rels") || fileExtension.equals("xml")) {
                    AddXMLStructuralPaths(zipFile.getInputStream(entry), path, structuralPaths);
                }
            }
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error extracting structural features from file: %s", filePath), true, false);
        }
    }

    /**
     * Add structural paths from the given xml file into the local map
     *
     * @param xmlFilePath the path of a xml file
     * @param structuralPaths the Map to add the extracted features to
     */
    private void AddXMLStructuralPaths(InputStream xmlFilePath, String path, Map<String, Integer> structuralPaths) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xml = db.parse(xmlFilePath);
            NodeList nodeList = xml.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                AddXMLStructuralPathsRecursively(nodeList.item(i), path, structuralPaths);
            }
        } catch (Exception ex) {
            //Console.Print_To_Console(String.format("Error traversing XML file: '%s'", xmlFilePath), true, false);
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
        structuralPath = structuralPath.replace("/", "\\").replaceAll("[0-9]", "");
        if (!structuralPath.equals("")) {
            if (!structuralPaths.containsKey(structuralPath)) {
                structuralPaths.put(structuralPath, 1);
            } else {
                structuralPaths.put(structuralPath, structuralPaths.get(structuralPath) + 1);
            }
        }
    }

    @Override
    public String GetName() {
        return "OOXML Structural Paths Enhanced";
    }

}
