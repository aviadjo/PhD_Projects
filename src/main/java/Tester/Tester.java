/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Detectors.Detector;
import FeatureExtraction.AFeatureExtractor;
import FeatureRepresentation.FeatureRepresentor.FeatureRepresentation;
import FeatureSelection.AFeatureSelector;
import Framework.DBFramework;
import IO.FileWriter;
import Implementations.FeatureExtractorOOXMLStructuralPaths;
import Implementations.FeatureExtractorOOXMLStructuralPathsDisk;
import Implementations.FeatureExtractorPDFStructuralPaths;
import Implementations.FeatureSelectorInfoGainRatio;
import Weka.Weka.WekaClassifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        //TestExtractPDFStructuralFeatures();
        //GeneratePDFDatasets();
        //GenerateDocxDatasets();
        //TestCode();
        //TestSerilizer();
        //CreateDetectorDOCX();
        //TestDetectionDOCX();
        //CreateDetectorPDF();
        //TestDetectionPDF();
        //TestUnzipFileInMemory();
        TestDetectionPDF();
    }

    private static void GeneratePDFDatasets() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassA";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassB";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        AFeatureExtractor<String> featureExtractor = new FeatureExtractorPDFStructuralPaths(FeatureExtractorPDFStructuralPaths.ParserType.Sequential);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = DBFramework.GenerateTrainSet(
                benignFolder,
                maliciousFolder,
                destinationFolder,
                featureExtractor,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                printFileFeaturesFrequencies,
                printSelectedFeaturesScore,
                generateTops,
                tops);
    }

    private static void GenerateDocxDatasets() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths<>(false);
        //AFeatureExtractor<String> featureExtractor = new FeatureExtractorNgramsString<>(3, 1);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = DBFramework.GenerateTrainSet(
                benignFolder,
                maliciousFolder,
                destinationFolder,
                featureExtractor,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                printFileFeaturesFrequencies,
                printSelectedFeaturesScore,
                generateTops,
                tops);
    }

    private static void CreateDetectorPDF() {
        String traningsetCSVFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.04.16_14.40.42_Files(B8928_M36307)_FE(PDF Structural Paths)_FS(Information Gain)_Rep(Binary)_j_Top(100).csv";
        String selectedFeaturesSerializedFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.04.16_14.40.42_Files(B8928_M36307)_FE(PDF Structural Paths)_FS(Information Gain)_Rep(Binary)_FeaturesList.ser";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorPDFStructuralPaths(FeatureExtractorPDFStructuralPaths.ParserType.Sequential);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        WekaClassifier wekaClassifier = WekaClassifier.J48;
        String saveToDestinationPath = "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers";
        Detector.GenerateAndSaveDetector(
                traningsetCSVFilePath,
                selectedFeaturesSerializedFilePath,
                featureExtractor,
                featureSelector,
                featureRepresentation,
                wekaClassifier,
                saveToDestinationPath
        );
    }

    private static void CreateDetectorDOCX() {
        String traningsetCSVFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.05.04_12.35.12_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_j_Top(100).csv";
        String selectedFeaturesSerializedFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.05.04_12.35.12_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_a_FeaturesList.ser";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPathsDisk<>(false);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        WekaClassifier wekaClassifier = WekaClassifier.RandomForest;
        String saveToDestinationPath = "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers";
        Detector.GenerateAndSaveDetector(
                traningsetCSVFilePath,
                selectedFeaturesSerializedFilePath,
                featureExtractor,
                featureSelector,
                featureRepresentation,
                wekaClassifier,
                saveToDestinationPath
        );
    }

    private static void TestDetectionDOCX() {
        Detector.ApplyDetectorToTestFolder(
                "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers\\WekaTrainedClassifier(J48)_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_Top(100).ser",
                "D:\\TEST\\DocX_Malicious"
        );
    }

    private static void TestDetectionPDF() {
        Detector.ApplyDetectorToTestFolder(
                "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers\\WekaTrainedClassifier(J48)_Files(B8928_M36307)_FE(PDF Structural Paths)_FS(Information Gain)_Rep(Binary)_Top(100).ser",
                "D:\\TEST\\PDF_Benign"
        );
    }

    private static void PrintDicToFile(Map<String, Integer> dic, String destFilePath) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : dic.entrySet()) {
            sb.append(entry.getKey()).append(",").append("\n");
        }
        FileWriter.WriteFile(sb.toString(), destFilePath);
    }

}
