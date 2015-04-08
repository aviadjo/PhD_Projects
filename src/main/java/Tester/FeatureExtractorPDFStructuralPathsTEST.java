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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.io.RandomAccess;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorPDFStructuralPathsTEST {

    public ParserType m_parserType;

    public FeatureExtractorPDFStructuralPathsTEST(ParserType parserType) {
        m_parserType = parserType;
    }

    public enum ParserType {

        Sequential,
        NonSequential
    }

    public Map ExtractFeaturesFrequencyFromSingleElement(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        HashSet<COSBase> visitedObjects = new HashSet<>();

        String filePath = (String) element;
        File pdfFile = new File(filePath);
        try {
            switch (m_parserType) {
                case Sequential:
                    try (PDDocument pdf = PDDocument.load(pdfFile)) {
                        COSDocument pdfDocument = pdf.getDocument();
                        ExtractPDFStructuralPathsRecursively(pdfDocument.getTrailer().getCOSObject(), "Trailer", "", structuralPaths, visitedObjects);
                        //ExtractPDFStructuralPathsQUEUE(pdfDocument.getTrailer().getCOSObject(), structuralPaths);
                    }
                    break;
                case NonSequential:
                    File randomAccessFile = new File(filePath + ".ra");
                    RandomAccess randomAccess = new RandomAccessFile(randomAccessFile, "rwd");
                    try (PDDocument pdf = PDDocument.loadNonSeq(pdfFile, randomAccess)) {
                        COSDocument pdfDocument = pdf.getDocument();
                        ExtractPDFStructuralPathsRecursively(pdfDocument.getTrailer().getCOSObject(), "Trailer", "", structuralPaths, visitedObjects);
                        //ExtractPDFStructuralPathsQUEUE(pdfDocument.getTrailer().getCOSObject(), structuralPaths);
                    }
                    break;
            }
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error parsing PDF file: %s", filePath), true, false);
        }
        return structuralPaths;
    }

    public void ExtractPDFStructuralPathsRecursively(COSBase pdfObject, String pdfObjectName, String parentPath, Map<String, Integer> structuralPaths, HashSet<COSBase> visitedObjects) {
        String objectPath = String.format("%s\\%s", parentPath, pdfObjectName);
        visitedObjects.add(pdfObject);

        switch (pdfObject.getClass().getName().replace("org.apache.pdfbox.cos.", "")) {
            case "COSNull":
            case "COSUnread":
            case "COSBoolean":
            case "COSInteger":
            case "COSFloat":
            case "COSNumber":
            case "COSString":
            case "COSName":
                AddPDFStructuralPath(objectPath, structuralPaths);
                break;
            case "COSDocument":
                break;
            case "COSArray":
                AddPDFStructuralPath(objectPath, structuralPaths);
                for (int i = 0; i < ((COSArray) pdfObject).size(); i++) {
                    if (!visitedObjects.contains(((COSArray) pdfObject).get(i))) {
                        ExtractPDFStructuralPathsRecursively(((COSArray) pdfObject).get(i), ".", objectPath, structuralPaths, visitedObjects);
                    }
                }
                break;
            case "COSStreamArray":
            case "COSStream":
            case "COSDictionaryLateBinding":
            case "COSDictionary":
                AddPDFStructuralPath(objectPath, structuralPaths);
                for (Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject).entrySet()) {
                    if (!visitedObjects.contains(objectEntry.getValue())) {
                        ExtractPDFStructuralPathsRecursively(objectEntry.getValue(), objectEntry.getKey().getName(), objectPath, structuralPaths, visitedObjects);
                    }
                    /*if (!Arrays.asList("Parent", "P", "ParentTree", "StructTreeRoot").contains(objectEntry.getKey().getName())) {

                     }*/
                }
                break;
            case "COSObject":
                if (!visitedObjects.contains(((COSObject) pdfObject).getObject())) {
                    ExtractPDFStructuralPathsRecursively(((COSObject) pdfObject).getObject(), pdfObjectName, parentPath, structuralPaths, visitedObjects);
                }
                break;
            default:
                break;
        }
    }

    private class PDFObject {

        private final COSBase m_pdfObject;
        private final String m_objectName;
        private final String m_path;

        public COSBase getObject() {
            return m_pdfObject;
        }

        public String getName() {
            return m_objectName;
        }

        public String getPath() {
            return m_path;
        }

        public PDFObject(COSBase pdfObject, String objectName, String parentPath) {
            m_pdfObject = pdfObject;
            m_objectName = objectName;
            m_path = String.format("%s\\%s", parentPath, objectName);
        }
    }

    public void ExtractPDFStructuralPathsQUEUE(COSBase pdfRootObject, Map<String, Integer> structuralFeatures) {
        Queue<PDFObject> queue = new LinkedList<>();

        PDFObject pdfObjectRoot = new PDFObject(pdfRootObject, "Trailer", "");
        queue.add(pdfObjectRoot);

        PDFObject pdfObject;
        while (!queue.isEmpty()) {
            pdfObject = queue.remove();

            switch (pdfObject.getObject().getClass().getName().replace("org.apache.pdfbox.cos.", "")) {
                case "COSNull":
                case "COSUnread":
                case "COSBoolean":
                case "COSInteger":
                case "COSFloat":
                case "COSNumber":
                case "COSString":
                case "COSName":
                    AddPDFStructuralPath(pdfObject.getPath(), structuralFeatures);
                    break;
                case "COSDocument":
                    break;
                case "COSArray":
                    AddPDFStructuralPath(pdfObject.getPath(), structuralFeatures);
                    for (int i = 0; i < ((COSArray) pdfObject.getObject()).size(); i++) {
                        queue.add(new PDFObject(((COSArray) pdfObject.getObject()).get(i), ".", pdfObject.getPath()));
                    }
                    break;
                case "COSStreamArray":
                case "COSStream":
                case "COSDictionaryLateBinding":
                case "COSDictionary":
                    AddPDFStructuralPath(pdfObject.getPath(), structuralFeatures);
                    for (Map.Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject.getObject()).entrySet()) {
                        if (!(objectEntry.getKey().getName().equals("Parent")
                                || objectEntry.getKey().getName().equals("ParentTree")
                                || objectEntry.getKey().getName().equals("StructTreeRoot"))) {
                            queue.add(new PDFObject(objectEntry.getValue(), objectEntry.getKey().getName(), pdfObject.getPath()));
                        }
                    }
                    break;
                case "COSObject":
                    queue.add(new PDFObject(((COSObject) pdfObject.getObject()).getObject(), pdfObject.getName(), pdfObject.getPath()));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Add structural path to the given Map
     *
     * @param feature (structural path) to add to the Map
     */
    private void AddPDFStructuralPath(String feature, Map<String, Integer> structuralPaths) {
        Console.PrintLine(feature, true, false);
        if (!structuralPaths.containsKey(feature)) {
            structuralPaths.put(feature, 1);
        } else {
            structuralPaths.put(feature, structuralPaths.get(feature) + 1);
        }
    }

}
