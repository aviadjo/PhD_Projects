/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import DatasetCreation.DatasetCSVBuilder;
import DetectorCreation.DetectorCreator;
import FeatureExtraction.AFeatureExtractor;
import FeatureExtraction.FeatureExtractorOOXMLStructuralPaths;
import FeatureExtraction.FeatureExtractorPDFStructuralPaths;
import FeatureRepresentation.FeatureRepresentor.FeatureRepresentation;
import FeatureSelection.AFeatureSelector;
import FeatureSelection.FeatureSelectorInfoGainRatio;
import Framework.DBFramework;
import static Framework.DBFramework.m_tops;
import IO.Console;
import IO.Directories;
import IO.FileReader;
import IO.FileWriter;
import IO.Files;
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
        //GenerateTops();
        //boolean nnSFExtraction = false;
        //GenerateDocxDatasets1(nnSFExtraction);
        //GenerateDocxDatasets2(nnSFExtraction);
        //GenerateDocxDatasets3(nnSFExtraction);
        //GenerateDocxDatasets4(nnSFExtraction);
        //nnSFExtraction = true;
        //GenerateDocxDatasets1(nnSFExtraction);
        //GenerateDocxDatasets2(nnSFExtraction);
        //GenerateDocxDatasets3(nnSFExtraction);
        //GenerateDocxDatasets4(nnSFExtraction);
        //TestCode();
        //TestSerilizer();
        //TestDetectionDOCX();
        ////CreateDetectorPDF();
        ////CreateDetectorDOCX();
        //TestDetectionPDF();
        //TestUnzipFileInMemory();
        //TestDetectionPDF();
        //PDFCompatibilityCheck();
    }

    private static void GeneratePDFDatasets() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassA";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassB";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        String fileType = "pdf";
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
                fileType,
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

    private static void GenerateDocxDatasets1(boolean nnSFExtraction) {
        String benignFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Benign_16108";
        String maliciousFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Malicious_831";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000, 2000));

        String fileType = "docx";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths<>(nnSFExtraction);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.TFIDF;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = DBFramework.GenerateTrainSet(
                fileType,
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

    private static void GenerateDocxDatasets2(boolean nnSFExtraction) {
        String benignFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Benign_16108";
        String maliciousFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Malicious_831";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        String fileType = "docx";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths<>(nnSFExtraction);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.TFIDF;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = DBFramework.GenerateTrainSet(
                fileType,
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

    private static void GenerateDocxDatasets3(boolean nnSFExtraction) {
        String benignFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Benign_16108";
        String maliciousFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Malicious_831";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        String fileType = "docx";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths<>(nnSFExtraction);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGainRatio);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = DBFramework.GenerateTrainSet(
                fileType,
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

    private static void GenerateDocxDatasets4(boolean nnSFExtraction) {
        String benignFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Benign_16108";
        String maliciousFolder = "D:\\Nir_Aviad_DT_Shared\\DATASETS\\Office\\_OFFICE_DATASET_MERGED_SHA256\\DOCX_Malicious_831";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        String fileType = "docx";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths<>(nnSFExtraction);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGainRatio);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.TFIDF;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = DBFramework.GenerateTrainSet(
                fileType,
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
        String trainingsetCSVFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.04.16_14.40.42_Files(B8928_M36307)_FE(PDF Structural Paths)_FS(Information Gain)_Rep(Binary)_j_Top(100).csv";
        String trainingsetCSVFileType = "pdf";
        String selectedFeaturesSerializedFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.04.16_14.40.42_Files(B8928_M36307)_FE(PDF Structural Paths)_FS(Information Gain)_Rep(Binary)_FeaturesList.ser";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorPDFStructuralPaths(FeatureExtractorPDFStructuralPaths.ParserType.Sequential);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        WekaClassifier wekaClassifier = WekaClassifier.J48;
        String description = "Detection of Malicious PDF files";
        double classificationTreshold = 0.5;
        String saveToDestinationPath = "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers";
        DetectorCreator.CreateAndSaveDetector(
                trainingsetCSVFilePath,
                trainingsetCSVFileType,
                selectedFeaturesSerializedFilePath,
                featureExtractor,
                featureSelector,
                featureRepresentation,
                wekaClassifier,
                description,
                classificationTreshold,
                saveToDestinationPath
        );
    }

    private static void CreateDetectorDOCX() {
        String trainingsetCSVFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.05.04_12.35.12_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_j_Top(100).csv";
        String trainingsetCSVFileType = "docx";
        String selectedFeaturesSerializedFilePath = "D:\\Dropbox\\TESTS\\DATASET_2015.05.04_12.35.12_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_a_FeaturesList.ser";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths<>(false);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        WekaClassifier wekaClassifier = WekaClassifier.J48;
        String description = "Detection of Malicious DOCX files";
        double classificationTreshold = 0.5;
        String saveToDestinationPath = "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers";
        DetectorCreator.CreateAndSaveDetector(
                trainingsetCSVFilePath,
                trainingsetCSVFileType,
                selectedFeaturesSerializedFilePath,
                featureExtractor,
                featureSelector,
                featureRepresentation,
                wekaClassifier,
                description,
                classificationTreshold,
                saveToDestinationPath
        );
    }

    private static void TestDetectionDOCX() {
        DetectorCreator.ApplyDetectorToTestFolder(
                "D:\\Dropbox\\DATASETS\\WekaTrainedClassifiers\\WekaTrainedClassifier(J48)_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_Top(100).ser",
                "D:\\TEST\\DocX_Malicious"
        );
    }

    private static void TestDetectionPDF() {
        DetectorCreator.ApplyDetectorToTestFolder(
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

    private static void GenerateTops() {
        String dataCSVfilePath = "C:\\Users\\Aviadjo\\Desktop\\A\\Dataset_2015.08.09_12.10.35_Type(docx)_Files(B16108_M830)_FE(OOXML Structural Paths NN)_FS(Fisher Score)_Rep(TFIDF).csv";
        StringBuilder datasetCSV = new StringBuilder(FileReader.ReadFile(dataCSVfilePath));
        DatasetCSVBuilder.GenerateTopDatasets(datasetCSV, m_tops, "C:\\Users\\Aviadjo\\Desktop\\A\\", "Dataset_2015.08.09_12.10.35_Type(docx)_Files(B16108_M830)_FE(OOXML Structural Paths NN)_FS(Fisher Score)_Rep(TFIDF)", false, true);
    }

    private static void PDFCompatibilityCheck() {
        String sourceFolderPath = "K:\\Collections\\CiteseerX Scanning\\Malicious Only";
        //String sourceFolderPath = "D:\\TEST\\TEST";
        ArrayList<String> files = Directories.GetDirectoryFilesPaths(sourceFolderPath);
        FeatureExtractorPDFStructuralPaths<String> featureExtractor = new FeatureExtractorPDFStructuralPaths(FeatureExtractorPDFStructuralPaths.ParserType.Sequential);

        int loopCounter = 0;
        int compatibleCounter = 0;
        int incompatibleCounter = 0;
        String fileName;
        boolean compatible;
        for (String filePath : files) {
            loopCounter++;
            fileName = Files.GetFileNameWithExtension(filePath);
            compatible = featureExtractor.IsCompatiblePDF2(filePath);
            if (compatible) {
                compatibleCounter++;
            } else {
                incompatibleCounter++;
                //compatible = featureExtractor.IsCompatiblePDF(filePath);
            }
            Console.PrintLine(String.format("%s. %s - %s", loopCounter, fileName, compatible));
        }

        Console.PrintLine(String.format("Total files: %s", files.size()));
        Console.PrintLine(String.format("Compatible file: %s", compatibleCounter));
    }

}
