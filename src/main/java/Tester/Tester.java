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

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        //BuildDatasetConfiguration();
        TestGenerateTops();
    }

    public static void BuildDatasetConfiguration() {
        String folder_Benign = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        String folder_Malicious = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100";
        //AFeatureExtractor<String> featureExtractorNgram = new FeatureExtractorNgrams<>(3, 1);
        AFeatureExtractor<String> featureExtractorDocxStructuralPaths = new FeatureExtractorDocxStructuralPaths();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(false);
        int topFeatures = 500;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printDocumentFrequencies = true;
        boolean createDatabaseCSV = true;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        String destinationFolderPath = "D:\\Dropbox\\DATASETS";

        String datasetCSV = BuildDataset(folder_Benign,
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

        if (datasetCSV != "") {
            String datasetTop500 = DatasetCSVBuilder.GetTopXDataset(datasetCSV, 40,addElementIDColumn,addClassificationColumn);
            FileWriter.WriteFile(datasetTop500, "D:\\Dropbox\\DATASETS\\top_40.csv");
        }
    }

    private static void TestGenerateTops() {
        String csv = FileReader.ReadFile("D:\\Dropbox\\DATASETS\\DATASET_TEST_EID_DATA_CLASS.csv");
        String newCSV = DatasetCSVBuilder.GetTopXDataset(csv, 30,true,true);
        FileWriter.WriteFile(newCSV, "D:\\Dropbox\\DATASETS\\_DATASET_TEST_RESULT.csv");
    }
}
