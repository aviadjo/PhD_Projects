/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Console.Console;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreeNode;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdfviewer.PDFTreeModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureNode;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;

/**
 *
 * @author Aviad
 */
public class PDFBox_SequentialParser2 {

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

            //PDStructureNode a = (PDStructureNode) pdc.getCOSObject();
            //AddPDFStructuralPathsRecursively(pdc.getCOSObject(), "\\");
            //List<COSObject> objects = pdfDocument.getObjects();
            PDFTreeModel ptm = new PDFTreeModel(pdf);
            //AddPDFStructuralPathsRecursively(ptm.getRoot(), "\\");

            //pdf.getDocument().getCatalog().getCOSObject()
            COSDictionary cd = pdc.getCOSDictionary();

            for (COSBase object : cd.getValues()) {
                Console.PrintLine(object.toString(), true, false);
            }

            //PDStructureNode ccc = pdc.ge
            //PDStructureTreeRoot pstr = pdc.getStructureTreeRoot();
            PDStructureTreeRoot pstr = new PDStructureTreeRoot(pdc.getCOSDictionary());

            //PDStructureNode psn = pstr.
            for (Object kid : pstr.getKids()) {
                List<Object> kids2 = ((PDStructureNode) kid).getKids();
            }

            PDStructureNode psn = new PDStructureNode(cd) {
            };
            for (Object child : psn.getKids()) {
                for (Object ob : ((PDStructureNode) child).getKids()) {
                    List<Object> kids2 = ((PDStructureNode) ob).getKids();
                }
            }

            if (pstr != null) {
                List<Object> kids = pstr.getKids();

                for (Object ob : kids) {
                    List<Object> kids2 = ((PDStructureNode) ob).getKids();
                }
            }
            String a = "";
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error parsing PDF file: %s", filePath), true, false);
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
}