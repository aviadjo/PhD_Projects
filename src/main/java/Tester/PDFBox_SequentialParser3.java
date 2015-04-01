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
import java.util.Map;
import java.util.Map.Entry;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;

/**
 *
 * @author Aviad
 */
public class PDFBox_SequentialParser3 {

    private static Map<String, Integer> structuralPaths = new HashMap<>();

    public static Map ExtractFeaturesFrequencyFromSingleElement(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String filePath = (String) element;

        File input = new File(filePath);
        try {
            //Using Sequential PDF parser. for non-sequential parser use ".loadNonSeq"
            PDDocument pdf = PDDocument.load(input);
            COSDocument pdfDocument = pdf.getDocument();
            PDDocumentCatalog pdc = pdf.getDocumentCatalog();

            //PDStructureTreeRoot pstr = pdc.getStructureTreeRoot();
            //PrintPDFObjects(pdfDocument.getCatalog());
            //PDStructureNode psn = (PDStructureNode) pdc.getCOSDictionary();
            PrintPDFStructuralPaths(pdfDocument.getTrailer().getCOSObject(), "", "");
            //PrintPDFObjects(pdfDocument.getTrailer()/*pdfDocument.getCatalog()*/);
            //PDStructureTreeRoot pstr = new PDStructureTreeRoot(pdfDocument.getTrailer());
            //Map<String, Object> c = pstr.
            //pntn = pstr.getParentTree();
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error parsing PDF file: %s", filePath), true, false);
        }
        return structuralPaths;
    }

    public static void ExtractPDFStructuralPaths(COSBase pdfObject, String pdfObjectName, String parentPath, Map<String, Integer> structuralPaths) {
        String objectPath = String.format("%s\\%s", parentPath, pdfObjectName);

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
                    ExtractPDFStructuralPaths(((COSArray) pdfObject).get(i), ".", objectPath, structuralPaths);
                }
                break;
            case "COSStreamArray":
            case "COSStream":
            case "COSDictionaryLateBinding":
            case "COSDictionary":
                AddPDFStructuralPath(objectPath, structuralPaths);
                for (Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject).entrySet()) {
                    if (!(objectEntry.getKey().getName().equals("Parent")
                            || objectEntry.getKey().getName().equals("ParentTree")
                            || objectEntry.getKey().getName().equals("StructTreeRoot"))) {
                        ExtractPDFStructuralPaths(objectEntry.getValue(), objectEntry.getKey().getName(), objectPath, structuralPaths);
                    }
                }
                break;
            case "COSObject":
                ExtractPDFStructuralPaths(((COSObject) pdfObject).getObject(), pdfObjectName, parentPath, structuralPaths);
                break;
            default:
                break;
        }
    }

    /**
     * Add structural path to local Map
     *
     * @param key the key to add to the map
     */
    private static void AddPDFStructuralPath(String key, Map<String, Integer> structuralPaths) {
        if (!structuralPaths.containsKey(key)) {
            structuralPaths.put(key, 1);
        } else {
            structuralPaths.put(key, structuralPaths.get(key) + 1);
        }
    }

    public static void PrintPDFStructuralPaths(COSBase pdfObject, String pdfObjectName, String parentPath) {
        String objectPath = String.format("%s\\%s", parentPath, pdfObjectName);

        switch (pdfObject.getClass().getName().replace("org.apache.pdfbox.cos.", "")) {
            case "COSNull":
            case "COSUnread":
            case "COSBoolean":
            case "COSInteger":
            case "COSFloat":
            case "COSNumber":
            case "COSString":
            case "COSName":
                Console.PrintLine(objectPath, true, false);
                break;
            case "COSDocument":
                break;
            case "COSArray":
                Console.PrintLine(objectPath, true, false);
                for (int i = 0; i < ((COSArray) pdfObject).size(); i++) {
                    PrintPDFStructuralPaths(((COSArray) pdfObject).get(i), ".", objectPath);
                }
                break;
            case "COSStreamArray":
            case "COSStream":
            case "COSDictionaryLateBinding":
            case "COSDictionary":
                Console.PrintLine(objectPath, true, false);
                for (Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject).entrySet()) {
                    if (!(objectEntry.getKey().getName().equals("Parent")
                            || objectEntry.getKey().getName().equals("ParentTree")
                            || objectEntry.getKey().getName().equals("StructTreeRoot"))) {
                        PrintPDFStructuralPaths(objectEntry.getValue(), objectEntry.getKey().getName(), objectPath);
                    }
                }
                break;
            case "COSObject":
                PrintPDFStructuralPaths(((COSObject) pdfObject).getObject(), pdfObjectName, parentPath);
                break;
            default:
                break;
        }
    }

    public static void PrintPDFObjectsOLD(COSBase pdfObject, String pdfObjectName, String parentPath) {
        String objectPath = String.format("%s\\%s", parentPath, pdfObjectName);

        //TestSwitch(pdfObject);
        if (pdfObject instanceof COSInteger) {
            //Console.PrintLine(objectEntry.getValue().toString() + "~" + objectEntry.getKey().getName(), true, false);
            Console.PrintLine(objectPath, true, false);
        } else if (pdfObject instanceof COSFloat) {
            Console.PrintLine(objectPath, true, false);
        } else if (pdfObject instanceof COSString) {
            Console.PrintLine(objectPath, true, false);
        } else if (pdfObject instanceof COSName) {
            Console.PrintLine(objectPath, true, false);
        } else if (pdfObject instanceof COSArray) {
            Console.PrintLine(objectPath, true, false);
            for (int i = 0; i < ((COSArray) pdfObject).size(); i++) {
                PrintPDFObjectsOLD(((COSArray) pdfObject).get(i), ".", objectPath);
            }
        } else if (pdfObject instanceof COSDictionary) {
            Console.PrintLine(objectPath, true, false);
            for (Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject).entrySet()) {
                if (!(objectEntry.getKey().getName().equals("Parent")
                        || objectEntry.getKey().getName().equals("ParentTree")
                        || objectEntry.getKey().getName().equals("StructTreeRoot"))) {
                    PrintPDFObjectsOLD(objectEntry.getValue(), objectEntry.getKey().getName(), objectPath);
                }
            }
        } else if (pdfObject instanceof COSObject) {
            PrintPDFObjectsOLD(((COSObject) pdfObject).getObject(), pdfObjectName, parentPath);
        } else {
            String a = "";
            //TODO - check this unchecked condition
        }
    }

}
