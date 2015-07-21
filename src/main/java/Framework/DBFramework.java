/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Framework;

import DatasetCreation.DatasetCSVBuilder;
import DatasetCreation.DatasetCreator;
import static DatasetCreation.DatasetCreator.BuildDatasetCSV;
import FeatureExtraction.AFeatureExtractor;
import FeatureExtraction.FeatureExtractorOOXMLStructuralPaths;
import FeatureExtraction.IFeatureExtractor;
import FeatureRepresentation.FeatureRepresentor.FeatureRepresentation;
import FeatureSelection.AFeatureSelector;
import FeatureSelection.FeatureSelectorInfoGainRatio;
import FeatureSelection.FeatureSelectorInfoGainRatio.SelectionMethod;
import IO.Console;
import IO.Directories;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class DBFramework {

    public static final String m_systemTempDirectory = FileUtils.getTempDirectoryPath();
    public static final String m_benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
    public static final String m_maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
    public static final String m_testFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
    public static final String m_destinationFolder = "D:\\Dropbox\\DATASETS";
    public static final String m_datasetFilenameFormat = "Dataset_%s_Files(B%s_M%s)_Type(%s)_FE(%s)_FS(%s)_Rep(%s)";
    public static final ArrayList<Integer> m_tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

    public static final String m_classificationModelsFolder = "";

    public static enum Classification {

        Benign("Benign"),
        Malicious("Malicious"),
        Unknown("?");

        private final String toString;

        private Classification(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    public Classification GetClassicitaion(String classificationString) {
        Classification classification = Classification.Unknown;
        switch (classificationString) {
            case "Benign":
                classification = Classification.Benign;
            case "Malicious":
                classification = Classification.Malicious;
        }
        return classification;
    }

    private static StringBuilder CreateTrainSetDefaultSettings() {
        //AFeatureExtractor<String> featureExtractor = new FeatureExtractorNgrams<>(3, 1);
        //AFeatureExtractor<String> featureExtractor = new FeatureExtractorDocStreamPaths();
        String fileType = "";
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorOOXMLStructuralPaths(false);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(SelectionMethod.InformationGain);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = false;

        StringBuilder datasetCSV = BuildDatasetCSV(
                fileType,
                m_benignFolder,
                m_maliciousFolder,
                m_destinationFolder,
                m_datasetFilenameFormat,
                featureExtractor,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                printFileFeaturesFrequencies,
                printSelectedFeaturesScore
        );

        if (generateTops) {
            Console.PrintLine("Generating tops:");
            DatasetCSVBuilder.GenerateTopDatasets(datasetCSV, m_tops, m_destinationFolder, DatasetCreator.m_datasetFilename, addElementIDColumn, addClassificationColumn);
        }

        return datasetCSV;
    }

    /**
     * Return CSV string which represent the dataset
     *
     * @param benignFolder the folder which contain the training benign files
     * @param maliciousFolder the folder which contain the training malicious
     * files
     * @param destinationFolder the destination folder to write the dataset
     * @param featureExtractor a Feature Extractor object
     * @param featureSelector a Feature Selector object
     * @param topFeatures the number of top features to extract
     * @param featureRepresentation featureRepresentation
     * @param createDatabaseCSV indicated whether to write CSV training set to
     * disk or not
     * @param addElementIDColumn add prefix column identifying the record
     * @param addClassificationColumn add suffix column identifying the class of
     * the record
     * @param printFileFeaturesFrequencies indicated whether to write Features
     * Frequencies file to disk or not
     * @param printSelectedFeaturesScore indicated whether to print to the
     * console the selected features' score
     * @param generateTopsDatasets indicated whether to generate Top datasets
     * @return CSV string which represent the dataset
     */
    public static StringBuilder GenerateTrainSet(
            String fileType,
            String benignFolder,
            String maliciousFolder,
            String destinationFolder,
            IFeatureExtractor<String> featureExtractor,
            AFeatureSelector featureSelector,
            int topFeatures,
            FeatureRepresentation featureRepresentation,
            boolean createDatabaseCSV,
            boolean addElementIDColumn,
            boolean addClassificationColumn,
            boolean printFileFeaturesFrequencies,
            boolean printSelectedFeaturesScore,
            boolean generateTopsDatasets,
            ArrayList<Integer> tops) {
        StringBuilder datasetCSV = BuildDatasetCSV(
                fileType,
                benignFolder,
                maliciousFolder,
                destinationFolder,
                m_datasetFilenameFormat,
                featureExtractor,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                printFileFeaturesFrequencies,
                printSelectedFeaturesScore
        );

        if (generateTopsDatasets) {
            Console.PrintLine("Generating tops:");
            DatasetCSVBuilder.GenerateTopDatasets(datasetCSV, tops, destinationFolder, DatasetCreator.m_datasetFilename, addElementIDColumn, addClassificationColumn);
        }

        return datasetCSV;
    }

    /**
     * Return CSV string which represent the dataset
     *
     * @param testFolder the folder in which the files for test are located
     * @param featureExtractor a Feature Extractor object
     * @param selectedFeatures the top selected features to build the dataset
     * with
     * @param topFeatures the number of top features to extract
     * @param numOfElementsInTrainset number of elements in the training from
     * which the classifier were trained
     * @param featureRepresentation featureRepresentation
     * @param addElementIDColumn add prefix column identifying the record
     * @param addClassificationColumn add suffix column identifying the class of
     * the record
     * @return CSV string which represent the dataset
     */
    public static StringBuilder GenerateTestSet(
            String testFolder,
            AFeatureExtractor<String> featureExtractor,
            ArrayList<Pair<String, Integer>> selectedFeatures,
            int topFeatures,
            int numOfElementsInTrainset,
            FeatureRepresentation featureRepresentation,
            boolean addElementIDColumn,
            boolean addClassificationColumn
    ) {
        DatasetCSVBuilder<String> datasetBuilder = new DatasetCSVBuilder<>();
        ArrayList<String> testElements = Directories.GetDirectoryFilesPaths(testFolder);
        selectedFeatures = new ArrayList<>(selectedFeatures.subList(0, topFeatures));
        StringBuilder datasetCSVHeader = DatasetCSVBuilder.GetDatasetHeaderCSV(topFeatures, addElementIDColumn, addClassificationColumn);
        StringBuilder datasetCSV = datasetBuilder.BuildDatabaseCSV(
                testElements,
                featureExtractor,
                selectedFeatures,
                numOfElementsInTrainset,
                featureRepresentation,
                Classification.Unknown,
                addElementIDColumn,
                addClassificationColumn
        );
        return datasetCSVHeader.append("\n").append(datasetCSV);
    }

    public static void main(String[] args) {
        CreateTrainSetDefaultSettings();
    }
}
