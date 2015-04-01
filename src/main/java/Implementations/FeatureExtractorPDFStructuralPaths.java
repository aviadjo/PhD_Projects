/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import Console.Console;
import FeatureExtraction.AFeatureExtractor;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorPDFStructuralPaths<T> extends AFeatureExtractor<T> {

    @Override
    public Map ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> structuralFeatures = new HashMap<>();
        String filePath = (String) element;
        File input = new File(filePath);
        PDDocument pdf = new PDDocument();
        try {
            //Using Sequential PDF parser. for non-sequential parser use ".loadNonSeq"
            pdf = PDDocument.load(input);
            pdf.close();
            COSDocument pdfDocument = pdf.getDocument();
            ExtractPDFStructuralPaths(pdfDocument.getTrailer().getCOSObject(), "Trailer", "", structuralFeatures);
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error parsing PDF file: %s", filePath), true, false);
        } finally {
            try {
                pdf.close();
            } catch (IOException ex) {
                Console.PrintLine(String.format("Error closing PDF: %s", filePath), true, false);
            }
        }
        return structuralFeatures;
    }

    public static void ExtractPDFStructuralPaths(COSBase pdfObject, String pdfObjectName, String parentPath, Map<String, Integer> structuralFeatures) {
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
                AddPDFStructuralFeature(objectPath, structuralFeatures);
                break;
            case "COSDocument":
                break;
            case "COSArray":
                AddPDFStructuralFeature(objectPath, structuralFeatures);
                for (int i = 0; i < ((COSArray) pdfObject).size(); i++) {
                    ExtractPDFStructuralPaths(((COSArray) pdfObject).get(i), ".", objectPath, structuralFeatures);
                }
                break;
            case "COSStreamArray":
            case "COSStream":
            case "COSDictionaryLateBinding":
            case "COSDictionary":
                AddPDFStructuralFeature(objectPath, structuralFeatures);
                for (Map.Entry<COSName, COSBase> objectEntry : ((COSDictionary) pdfObject).entrySet()) {
                    if (!(objectEntry.getKey().getName().equals("Parent")
                            || objectEntry.getKey().getName().equals("ParentTree")
                            || objectEntry.getKey().getName().equals("StructTreeRoot"))) {
                        ExtractPDFStructuralPaths(objectEntry.getValue(), objectEntry.getKey().getName(), objectPath, structuralFeatures);
                    }
                }
                break;
            case "COSObject":
                ExtractPDFStructuralPaths(((COSObject) pdfObject).getObject(), pdfObjectName, parentPath, structuralFeatures);
                break;
            default:
                break;
        }
    }

    /**
     * Add structural path to local Map
     *
     * @param feature the key to add to the map
     */
    private static void AddPDFStructuralFeature(String feature, Map<String, Integer> structuralFeatures) {
        if (!structuralFeatures.containsKey(feature)) {
            structuralFeatures.put(feature, 1);
        } else {
            structuralFeatures.put(feature, structuralFeatures.get(feature) + 1);
        }
    }

    @Override
    public String GetName() {
        return "PDF Structural Paths";
    }

}
