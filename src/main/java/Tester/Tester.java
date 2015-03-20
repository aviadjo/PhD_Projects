/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import static Framework.Framework.CreateDataset;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.tree.TreeNode;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdfviewer.PDFTreeModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        CreateDataset();

        //ExtractFeaturesFrequencyFromSingleElement("D:\\2.pdf");
        //SetConfigurationFile();
    }

    private static Map<String, Integer> m_structuralPaths = new HashMap<>();

    public static Map ExtractFeaturesFrequencyFromSingleElement(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String filePath = (String) element;

        File input = new File(filePath);
        try {
            //Using Sequential PDF parser. for non-sequential parser use ".loadNonSeq"
            PDDocument pdf = PDDocument.load(input);
            COSDocument pdfDocument = pdf.getDocument();

            //pdfDocument.
            PDDocumentCatalog pdc = pdf.getDocumentCatalog();

            //List<COSObject> objects = pdfDocument.getObjects();
            PDFTreeModel ptm = new PDFTreeModel(pdf);
            AddPDFStructuralPathsRecursively(ptm.getRoot(), "\\");

            //pdf.getDocument().getCatalog().getCOSObject()
            /*PDDocumentCatalog pdc = pdf.getDocumentCatalog();
             COSBase cb = pdc.getCOSObject();
             PDStructureTreeRoot pstr = pdc.getStructureTreeRoot();
             if (pstr != null) {
             List<Object> kids = pstr.getKids();
             }
             String a = "";*/
        } catch (IOException ex) {
            Console.Console.PrintLine(String.format("Error parsing PDF file: %s", filePath), true, false);
        }
        return structuralPaths;
    }

    /**
     * Add structural paths from the given pdfNode into the local map
     * recursively
     *
     * @param pdfNode pdfNode to look for its childs
     * @param parentNodePath the path of the parent node
     */
    private static void AddPDFStructuralPathsRecursively(Object pdfNode, String parentNodePath) {
        //String currentNodePath = String.format("%s\\%s", parentNodePath, pdfNode.toString());

        COSName key;
        COSBase value;
        TreeNode a = ((TreeNode) pdfNode);

        for (Map.Entry<COSName, COSBase> mapEntry : ((COSDictionary) pdfNode).entrySet()) {
            key = mapEntry.getKey();
            value = mapEntry.getValue();

            AddPDFStructuralPathsRecursively(value.getCOSObject(),/*currentNodePath*/ "");
        }
        /*pdfNode
         String currentNodePath = String.format("%s\\%s", parentNodePath, pdfNode.getNodeName());
         AddPDFStructuralPath(currentNodePath);

         NodeList childNodes = pdfNode.getChildNodes();
         Node childNode;
         for (int i = 0; i < childNodes.getLength(); i++) {
         childNode = childNodes.item(i);
         AddPDFStructuralPathsRecursively(childNode, currentNodePath);
         }*/
    }

    /**
     * Add structural path to local Map
     *
     * @param key the key to add to the map
     */
    private static void AddPDFStructuralPath(String key) {
        if (!m_structuralPaths.containsKey(key)) {
            m_structuralPaths.put(key, 1);
        } else {
            m_structuralPaths.put(key, m_structuralPaths.get(key) + 1);
        }
    }

    /*public static Map ExtractFeaturesFrequencyFromSingleElement2(Object element) {
     Map<String, Integer> structuralPaths = new HashMap<>();
     String filePath = (String) element;

     File input = new File(filePath);
     try {
     // load the PDF file using PDFBox
     PDDocument pdf = PDDocument.load(filePath);
     PDFDomTree parser = new PDFDomTree();
     parser.processDocument(pdf);
     Document dom = parser.getDocument();
     NodeList nodeList = dom.getChildNodes();
     for (int i = 0; i < nodeList.getLength(); i++) {
     AddPDFStructuralPathsRecursively(nodeList.item(i), "");
     }

     } catch (ParserConfigurationException | IOException ex) {
     Console.Console.Print(String.format("Error parsing PDF file: %s", filePath), true, false);
     }
     return structuralPaths;
     }*/
    ///**
    // * Add structural paths from the given pdfNode into the local map
    // * recursively
    // *
    // * @param pdfNode pdfNode to look for its childs
    // * @param parentNodePath the path of the parent node
    // */
    //private static void AddPDFStructuralPathsRecursively(Node pdfNode, String parentNodePath) {
    //    String currentNodePath = String.format("%s\\%s", parentNodePath, pdfNode.getNodeName());
    //    AddPDFStructuralPath(currentNodePath);
//
    //    NodeList childNodes = pdfNode.getChildNodes();
    //    Node childNode;
    //    for (int i = 0; i < childNodes.getLength(); i++) {
    //        childNode = childNodes.item(i);
    //        AddPDFStructuralPathsRecursively(childNode, currentNodePath);
    //    }
    //}
//
    ///**
    // * Add structural path to local Map
    // *
    // * @param key the key to add to the map
    // */
    //private static void AddPDFStructuralPath(String key) {
    //    if (!m_structuralPaths.containsKey(key)) {
    //        m_structuralPaths.put(key, 1);
    //    } else {
    //        m_structuralPaths.put(key, m_structuralPaths.get(key) + 1);
    //    }
    //}
    private static void SetConfigurationFile() {
        Properties prop = new Properties();

        OutputStream output;
        try {
            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("BENIGN_FOLDER", "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20");
            prop.setProperty("MALICIOUS_FOLDER", ":\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100");
            prop.setProperty("SELECT_TOP_FEATURES", "500");

            // save properties to project root folder
            prop.store(output, null);
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }

        String folder_Benign = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        String folder_Malicious = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100";
    }

}
