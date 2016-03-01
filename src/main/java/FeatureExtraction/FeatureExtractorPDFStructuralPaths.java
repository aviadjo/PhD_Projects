/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import IO.Console;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
 * @param <T> An element used by this feature extractor
 */
public class FeatureExtractorPDFStructuralPaths<T> extends AFeatureExtractor<T> {

    private final long serialVersionUID = 1L;
    public ParserType m_parserType;

    public FeatureExtractorPDFStructuralPaths(ParserType parserType) {
        m_parserType = parserType;
    }

    public enum ParserType {

        Sequential,
        NonSequential
    }

    @Override
    public String GetName() {
        return "PDF Structural Paths";
    }

    @Override
    public Map ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        HashSet<COSBase> visitedObjects = new HashSet<>();
        String filePath = (String) element;
        String parentPath = "";
        String pdfObjectName = "Trailer";
        File pdfFile = new File(filePath);
        try {
            switch (m_parserType) {
                case Sequential:
                    try (PDDocument pdf = PDDocument.load(pdfFile); COSDocument pdfDocument = pdf.getDocument()) {
                        ExtractPDFStructuralPathsRecursively(pdfDocument.getTrailer().getCOSObject(), pdfObjectName, parentPath, structuralPaths, visitedObjects, 1);
                    } catch (Exception e) {
                        throw e;
                    }
                    break;
                case NonSequential:
                    File randomAccessFile = new File(filePath + ".ra");
                    RandomAccess randomAccess = new RandomAccessFile(randomAccessFile, "rwd");
                    try (PDDocument pdf = PDDocument.loadNonSeq(pdfFile, randomAccess); COSDocument pdfDocument = pdf.getDocument()) {
                        ExtractPDFStructuralPathsRecursively(pdfDocument.getTrailer().getCOSObject(), pdfObjectName, parentPath, structuralPaths, visitedObjects, 1);
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        randomAccessFile.delete();
                    }
                    break;
            }
        } catch (IOException e) {
            //AddPDFStructuralPath("General Parsing Error", structuralPaths);
            Console.PrintException(String.format("Error parsing PDF file: '%s'", filePath), e);
        }
        return structuralPaths;
    }

    /**
     * Extract the PDF structural paths recursively
     *
     * @param pdfObject the object
     * @param pdfObjectName the name of the current object
     * @param parentPath the path of the parent node
     * @param structuralPaths the Map to add the feature to
     * @param visitedObjects HashSet of visited objects
     * @param recursionIteration recursion iteration
     */
    private void ExtractPDFStructuralPathsRecursively(COSBase pdfObject, String pdfObjectName, String parentPath, Map<String, Integer> structuralPaths, HashSet<COSBase> visitedObjects, int recursionIteration) {
        //To avoid infinite loop
        if (recursionIteration > 100) {
            AddPDFStructuralPath("Recursion Iteration > 100", structuralPaths);
            return;
        }

        if (pdfObject != null) {
            try {
                String objectPath = String.format("%s\\%s", parentPath, pdfObjectName);
                visitedObjects.add(pdfObject);

                switch (pdfObject.getClass().getName().replace("org.apache.pdfbox.cos.", "")) {
                    //An Embedded PDF document
                    case "COSDocument":
                        ExtractPDFStructuralPathsRecursively(((COSDocument) pdfObject).getTrailer().getCOSObject(), "Trailer", "", new HashMap<>(), new HashSet<>(), 1);
                        break;

                    //Data Structure - Array
                    case "COSArray":
                        AddPDFStructuralPath(objectPath, structuralPaths);
                        for (int i = 0; i < ((COSArray) pdfObject).size(); i++) {
                            if (!visitedObjects.contains(((COSArray) pdfObject).get(i))) {
                                ExtractPDFStructuralPathsRecursively(((COSArray) pdfObject).get(i), ".", objectPath, structuralPaths, visitedObjects, recursionIteration + 1);
                            }
                        }
                        break;

                    //Data Structure - Stream & Dictionary
                    case "COSStreamArray":
                    case "COSStream":
                    case "COSDictionaryLateBinding":
                    case "COSDictionary":
                        AddPDFStructuralPath(objectPath, structuralPaths);
                        for (Map.Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject).entrySet()) {
                            if (!visitedObjects.contains(objectEntry.getValue())) {
                                ExtractPDFStructuralPathsRecursively(objectEntry.getValue(), objectEntry.getKey().getName(), objectPath, structuralPaths, visitedObjects, recursionIteration + 1);
                            }
                            //if (!Arrays.asList("Parent", "P", "ParentTree", "StructTreeRoot").contains(objectEntry.getKey().getName())) {}
                        }
                        break;
                    case "COSObject":
                        if (!visitedObjects.contains(((COSObject) pdfObject).getObject())) {
                            ExtractPDFStructuralPathsRecursively(((COSObject) pdfObject).getObject(), pdfObjectName, parentPath, structuralPaths, visitedObjects, recursionIteration + 1);
                        }
                        break;

                    //Primitive Objects
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
                }
            } catch (Exception e) {
                AddPDFStructuralPath("Recursion Parsing Failure", structuralPaths);
            }
        }
    }

    /**
     * Add structural path to the given Map
     *
     * @param structuralPath the key to add to the map
     * @param structuralPath (structural path) to add to the Map
     */
    private void AddPDFStructuralPath(String structuralPath, Map<String, Integer> structuralPaths) {
        if (!structuralPaths.containsKey(structuralPath)) {
            structuralPaths.put(structuralPath, 1);
        } else {
            structuralPaths.put(structuralPath, structuralPaths.get(structuralPath) + 1);
        }
    }

    public boolean IsCompatiblePDF(File pdfFile) {
        try (PDDocument pdf = PDDocument.load(pdfFile)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Return true if the PDF is compatible
     *
     * @param filePath pdf file path
     * @return true if the PDF is compatible
     */
    public boolean IsCompatiblePDF2(String filePath) {
        File pdfFile = new File(filePath);
        Map<String, Integer> structuralPaths = new HashMap<>();
        HashSet<COSBase> visitedObjects = new HashSet<>();
        boolean compatible = true;
        PDDocument pdf = new PDDocument();
        COSDocument pdfDocument = new COSDocument();
        String parentPath = "";
        String pdfObjectName = "Trailer";
        try {
            pdf = PDDocument.load(pdfFile);
            pdfDocument = pdf.getDocument();
            ExtractPDFStructuralPathsRecursively(pdfDocument.getTrailer().getCOSObject(), pdfObjectName, parentPath, structuralPaths, visitedObjects, 1);
        } catch (IOException e) {
            compatible = false;
        } finally {
            try {
                pdf.close();
                pdfDocument.close();
            } catch (IOException e) {
                Console.PrintException(String.format("Error closing PDF file: '%s'", filePath), e);
            }
        }
        return compatible;
    }

    /*private class PDFObject {

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
     }*/
    /**
     * Extract the PDF structural paths
     *
     * @param pdfRootObject the root of the PDF tree
     * @param structuralPaths the Map to add the feature to
     */
    /*public void ExtractPDFStructuralPathsQUEUE(COSBase pdfRootObject, Map<String, Integer> structuralFeatures) {
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
     if (!Arrays.asList("Parent", "P", "ParentTree", "StructTreeRoot", "Reference").contains(objectEntry.getKey().getName())) {
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
     }*/
}
