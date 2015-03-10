/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import DatasetCreation.DatasetCSVBuilder;
import DatasetCreation.DatasetCSVBuilder.FeatureRepresentation;
import static DatasetCreation.DatasetCreator.BuildDataset;
import FeatureExtraction.AFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import IO.FileReader;
import IO.FileWriter;
import Implementations.FeatureSelectorInfoGainRatio;
import Implementations.FeatureExtractorDocxStructuralPaths;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        //BuildDatasetConfiguration();
        //TestGenerateTops();

        ExtractFeaturesFrequencyFromSingleElement("D:\\2.pdf");
    }

    public static void BuildDatasetConfiguration() {
        String folder_Benign = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        String folder_Malicious = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100";
        //AFeatureExtractor<String> featureExtractorNgram = new FeatureExtractorNgrams<>(3, 1);
        AFeatureExtractor<String> featureExtractorDocxStructuralPaths = new FeatureExtractorDocxStructuralPaths();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(false);
        int topFeatures = 500;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFeaturesDocumentFrequencies = false;
        boolean printSelectedFeaturesScore = false;

        String destinationFolderPath = "D:\\Dropbox\\DATASETS";

        String datasetCSV = BuildDataset(folder_Benign,
                folder_Malicious,
                featureExtractorDocxStructuralPaths,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                destinationFolderPath,
                printFeaturesDocumentFrequencies,
                printSelectedFeaturesScore
        );

        if (datasetCSV != "") {
            String datasetTop500 = DatasetCSVBuilder.GetTopXDataset(datasetCSV, 40, addElementIDColumn, addClassificationColumn);
            FileWriter.WriteFile(datasetTop500, "D:\\Dropbox\\DATASETS\\top_40.csv");
        }
    }

    private static void TestGenerateTops() {
        String csv = FileReader.ReadFile("D:\\Dropbox\\DATASETS\\DATASET_TEST_EID_DATA_CLASS.csv");
        String newCSV = DatasetCSVBuilder.GetTopXDataset(csv, 30, true, true);
        FileWriter.WriteFile(newCSV, "D:\\Dropbox\\DATASETS\\_DATASET_TEST_RESULT.csv");
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
            //PDDocumentCatalog pdc = pdf.getDocumentCatalog();
            //COSObject catalog = cd.getCatalog();
            List<COSObject> objects = pdfDocument.getObjects();

            PDDocumentCatalog pdc = pdf.getDocumentCatalog();

            COSBase cb = pdc.getCOSObject();

            PDStructureTreeRoot pstr = pdc.getStructureTreeRoot();
            if (pstr != null) {
                List<Object> kids = pstr.getKids();
            }
            String a = "";

        } catch (IOException ex) {
            Console.Console.Print(String.format("Error parsing PDF file: %s", filePath), true, false);
        }
        return structuralPaths;
    }

    public void AddObjectStructuralPath(String parentObjectPath) {

    }
}
