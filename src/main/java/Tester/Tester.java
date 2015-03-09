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
import Implementations.FeatureSelectorInfoGainRatio;
import Implementations.FeatureExtractorDocxStructuralPaths;

/**
 *
 * @author Aviad
 */
public class Tester {

    

    public static void main(String[] args) {
        BuildDatasetConfiguration();
        //TestGenerateTops();
    }

    public static void BuildDatasetConfiguration() {
        String folder_Benign = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        String folder_Malicious = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100";
        //AFeatureExtractor<String> featureExtractorNgram = new FeatureExtractorNgrams<>(3, 1);
        AFeatureExtractor<String> featureExtractorDocxStructuralPaths = new FeatureExtractorDocxStructuralPaths();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(1000, 100, false);
        int topFeatures = 500;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printDocumentFrequencies = true;
        boolean createDatabaseCSV = false;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        String destinationFolderPath = "D:\\Dropbox\\DATASETS";

        BuildDataset(folder_Benign,
                folder_Malicious,
                featureExtractorDocxStructuralPaths,
                featureSelector,
                topFeatures,
                featureRepresentation,
                addElementIDColumn,
                addClassificationColumn,
                destinationFolderPath,
                printDocumentFrequencies,
                createDatabaseCSV
        );
    }

    private static void TestGenerateTops() {
        String csv = FileReader.ReadFile("D:\\Dropbox\\DATASETS\\DATASET_2015.03.08_11.39.32_DOCX_files(B20_M20)_Rep(Binary).csv");

        DatasetCSVBuilder DCB = new DatasetCSVBuilder();
        String newCSV = DCB.GetTopXDataset(csv, 20);
    }
}
