/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Framework;

import Console.Console;
import DatasetCreation.DatasetCSVBuilder;
import DatasetCreation.DatasetCreator;
import static DatasetCreation.DatasetCreator.BuildDataset;
import FeatureExtraction.AFeatureExtractor;
import FeatureExtraction.IFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import IO.Directories;
import Implementations.FeatureExtractorDocxStructuralPaths;
import Implementations.FeatureSelectorInfoGainRatio;
import Implementations.FeatureSelectorInfoGainRatio.SelectionMethod;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class Framework {

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

    public static enum FeatureRepresentation {

        Binary,
        TFIDF
    }

    public static final String m_systemTempDirectory = FileUtils.getTempDirectoryPath();
    public static final String m_benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
    public static final String m_maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
    public static final String m_testFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
    public static final String m_destinationFolder = "D:\\Dropbox\\DATASETS";
    public static final String m_datasetFilenameFormat = "DATASET_%s_Files(B%s_M%s)_FE(%s)_FS(%s)_Rep(%s)";
    public static final ArrayList<Integer> m_tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

    private static void CreateTrainSetDefaultSettings() {
        //AFeatureExtractor<String> featureExtractor = new FeatureExtractorNgrams<>(3, 1);
        //AFeatureExtractor<String> featureExtractor = new FeatureExtractorDocStreamPaths();
        AFeatureExtractor<String> featureExtractor = new FeatureExtractorDocxStructuralPaths();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(SelectionMethod.InformationGain);
        int topFeatures = 2000;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = false;

        StringBuilder datasetCSV = BuildDataset(
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
            Console.PrintLine("Generating tops:", true, false);
            DatasetCSVBuilder.GenerateTopDatasets(datasetCSV, m_tops, m_destinationFolder, DatasetCreator.m_datasetFilename, addElementIDColumn, addClassificationColumn);
        }
    }

    public static void GenerateTrainSet(
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
        StringBuilder datasetCSV = BuildDataset(
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
            Console.PrintLine("Generating tops:", true, false);
            DatasetCSVBuilder.GenerateTopDatasets(datasetCSV, tops, destinationFolder, DatasetCreator.m_datasetFilename, addElementIDColumn, addClassificationColumn);
        }
    }

    public static void GenerateTestSet(
            String testFolder,
            String destinationFolder,
            IFeatureExtractor<String> featureExtractor,
            String selectedFeaturesSerializedFilePath,
            int numOfElementsInTrainset,
            FeatureRepresentation featureRepresentation,
            boolean addElementIDColumn,
            boolean addClassificationColumn,
            boolean generateTopsDatasets,
            ArrayList<Integer> tops) {

        DatasetCSVBuilder<String> datasetBuilder = new DatasetCSVBuilder<>();
        ArrayList<String> testElements = Directories.GetDirectoryFilesPaths(testFolder);
        ArrayList<Pair<String, Integer>> selectedFeatures = new ArrayList<>();
        StringBuilder datasetCSV = datasetBuilder.BuildDatabaseCSV(
                testElements,
                featureExtractor,
                selectedFeatures,
                numOfElementsInTrainset,
                featureRepresentation,
                Classification.Unknown,
                addElementIDColumn,
                addClassificationColumn);

        if (generateTopsDatasets) {
            Console.PrintLine("Generating tops:", true, false);
            DatasetCSVBuilder.GenerateTopDatasets(datasetCSV, tops, destinationFolder, DatasetCreator.m_datasetFilename, addElementIDColumn, addClassificationColumn);
        }
    }

    public static void main(String[] args) {
        CreateTrainSetDefaultSettings();
    }
}
