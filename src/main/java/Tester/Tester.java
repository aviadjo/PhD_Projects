/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Console.Console;
import FeatureExtraction.AFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import Framework.Framework;
import Implementations.FeatureExtractorDocxStructuralPaths;
import Implementations.FeatureExtractorPDFStructuralPaths;
import Implementations.FeatureSelectorInfoGainRatio;
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

        GeneratePDFDataset();
        //GenerateDocxDataset();
    }

    private static void GeneratePDFDataset() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        AFeatureExtractor<String> featureExtractor = new FeatureExtractorPDFStructuralPaths();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        Framework.FeatureRepresentation featureRepresentation = Framework.FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean printFileSelectedFeatures = true;
        boolean generateTops = true;

        Framework.GenerateDatasets(
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
                printFileSelectedFeatures,
                generateTops,
                tops);
    }

    private static void GenerateDocxDataset() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        AFeatureExtractor<String> featureExtractor = new FeatureExtractorDocxStructuralPaths<>();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        Framework.FeatureRepresentation featureRepresentation = Framework.FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean printFileSelectedFeatures = true;
        boolean generateTops = true;

        Framework.GenerateDatasets(
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
                printFileSelectedFeatures,
                generateTops,
                tops);
    }

    private static void TestExtractPDFStructuralFeatures() {
        Map<String, Integer> structuralFeatures = PDFBox_SequentialParser.ExtractFeaturesFrequencyFromSingleElement("D:\\3.pdf");

        int totalStructuralFeatures = 0;

        for (Integer value : structuralFeatures.values()) {
            totalStructuralFeatures += value;
        }

        Console.PrintLine(String.format("Total features: %s", totalStructuralFeatures), true, false);
        Console.PrintLine(String.format("Unique features: %s", structuralFeatures.size()), true, false);
    }

}
