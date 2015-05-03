/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatasetCreation;

import Assistants.General;
import static Assistants.General.GetStringNumber;
import Assistants.StopWatch;
import Console.Console;
import DataStructures.MapDB;
import FeatureExtraction.IFeatureExtractor;
import FeatureExtraction.MasterFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import Framework.Framework.Classification;
import Framework.Framework.FeatureRepresentation;
import IO.Directories;
import IO.FileWriter;
import IO.Serializer;
import java.util.ArrayList;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Aviadjo
 */
public class DatasetCreator {

    public static String m_datasetFilename = "";

    /**
     * Return CSV string which represent the dataset
     *
     * @param ClassAdirectory folder of elements from class A
     * @param ClassBdirectory folder of elements from class B
     * @param featureExtractor The feature extractor to use
     * @param datasetFilenameFormat the format of the destination file
     * @param featureSelector The feature selector to use
     * @param topFeatures top features to select
     * @param featureRepresentation feature representation method (for example:
     * Boolea, TFIDF)
     * @param addElementIDColumn whether to add element ID column to the dataset
     * CSV
     * @param addClassificationColumn whether to add classification column to
     * the dataset CSV
     * @param destinationFolderPath destination folder path
     * @param printFileFeaturesFrequencies whether to print the features'
     * document frequencies
     * @param createDatabaseCSV whether to create the dataset record
     * @param printSelectedFeaturesScore whether to print the score of the
     * selected features
     * @param printFileSelectedFeatures whether to print file with the selected
     * features
     * @return dataset CSV
     */
    public static StringBuilder BuildDataset(
            String ClassAdirectory,
            String ClassBdirectory,
            String destinationFolderPath,
            String datasetFilenameFormat,
            IFeatureExtractor<String> featureExtractor,
            AFeatureSelector featureSelector,
            int topFeatures,
            FeatureRepresentation featureRepresentation,
            boolean createDatabaseCSV,
            boolean addElementIDColumn,
            boolean addClassificationColumn,
            boolean printFileFeaturesFrequencies,
            boolean printSelectedFeaturesScore
    ) {
        StopWatch.Start();

        ArrayList<String> classAelements = Directories.GetDirectoryFilesPaths(ClassAdirectory);
        ArrayList<String> classBelements = Directories.GetDirectoryFilesPaths(ClassBdirectory);
        int totalElementsNum = classAelements.size() + classBelements.size();

        Console.PrintLine(String.format("Benign folder: %s", ClassAdirectory), true, false);
        Console.PrintLine(String.format("Malicious folder: %s", ClassBdirectory), true, false);
        Console.PrintLine(String.format("Benign elements: %s", GetStringNumber(classAelements.size())), true, false);
        Console.PrintLine(String.format("Malicious elements: %s", GetStringNumber(classBelements.size())), true, false);
        Console.PrintLine(String.format("Total elements: %s", GetStringNumber(totalElementsNum)), true, false);

        //FEATURE EXTRACTION
        MasterFeatureExtractor<String> MFE = new MasterFeatureExtractor<>();
        Console.PrintLine(String.format("Feature Extraction: %s", featureExtractor.GetName()), true, false);
        Map<String, Integer> classAfeatures = MFE.ExtractFeaturesFrequenciesFromElements(classAelements, featureExtractor);
        Console.PrintLine(String.format("Benign unique features: %s", GetStringNumber(classAfeatures.size())), true, false);
        Map<String, Integer> classBfeatures = MFE.ExtractFeaturesFrequenciesFromElements(classBelements, featureExtractor);
        Console.PrintLine(String.format("Malicious unique features: %s", GetStringNumber(classBfeatures.size())), true, false);
        Map<String, int[]> classesABfeatures = MFE.GatherClassAClassBFeatureFrequency(classAfeatures, classBfeatures);
        Console.PrintLine(String.format("Total unique features: %s", GetStringNumber(classesABfeatures.size())), true, false);
        MapDB.m_db_off_heap_FE.commit();

        m_datasetFilename = String.format(datasetFilenameFormat, General.GetTimeStamp(), classAelements.size(), classBelements.size(), featureExtractor.GetName(), featureSelector.GetName(), featureRepresentation.toString());

        //PRINT FILE - DOCUMENT FREQUENCY
        if (printFileFeaturesFrequencies) {
            PrintCSVFileFeaturesFrequencies(classesABfeatures, destinationFolderPath);
        }

        //FEATURE SELECTION
        Console.PrintLine(String.format("Selecting top %s features using %s", topFeatures, featureSelector.GetName()), true, false);
        ArrayList<Pair<String, Integer>> selectedFeatures = featureSelector.SelectTopFeatures(classesABfeatures, classAelements.size(), classBelements.size(), topFeatures, printSelectedFeaturesScore);

        //PRINT FILE - SELECTED FEATURES
        PrintCSVFileSelectedFeatures(selectedFeatures, destinationFolderPath);
        SerializeSelectedFeatures(selectedFeatures, destinationFolderPath);

        //DATASET CREATION
        StringBuilder datasetCSV = new StringBuilder();
        if (createDatabaseCSV) {
            Console.PrintLine(String.format("Building dataset..."), true, false);
            Console.PrintLine(String.format("Feature representation: %s", featureRepresentation.toString()), true, false);
            //****************
            DatasetCSVBuilder<String> datasetBuilder = new DatasetCSVBuilder<>();
            StringBuilder datasetHeaderCSV = datasetBuilder.GetDatasetHeaderCSV(selectedFeatures.size(), addElementIDColumn, addClassificationColumn);
            StringBuilder datasetClassACSV = datasetBuilder.BuildDatabaseCSV(classAelements, featureExtractor, selectedFeatures, totalElementsNum, featureRepresentation, Classification.Benign, addElementIDColumn, addClassificationColumn);
            StringBuilder datasetClassBCSV = datasetBuilder.BuildDatabaseCSV(classBelements, featureExtractor, selectedFeatures, totalElementsNum, featureRepresentation, Classification.Malicious, addElementIDColumn, addClassificationColumn);
            datasetCSV.append(datasetHeaderCSV).append("\n").append(datasetClassBCSV).append("\n").append(datasetClassACSV);
            StopWatch.Stop();

            //OUTPUTS
            String datasetPath = destinationFolderPath + "\\" + m_datasetFilename + ".csv";
            FileWriter.WriteFile(datasetCSV.toString(), datasetPath);
            Console.PrintLine(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
            Console.PrintLine(String.format("Dataset saved to: %s", datasetPath), true, false);
            //Console.PrintLine(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
            //Console.PrintLine(String.format("InfoGain Values: %s", featureSelector.m_memo.size()), true, false);
        }
        return datasetCSV;
    }

    /**
     * Print CSV file contain list of features and their document frequencies
     *
     * @param featuresDocumentFrequencies features document frequencies
     * @param destinationFolderPath path of the destination folder to print the
     * selected features file to
     */
    private static void PrintCSVFileFeaturesFrequencies(Map<String, int[]> GetFeaturesDocumentFrequenciesCSV, String destinationFolderPath) {
        String featuresDocumentFrequenciesFilePath = destinationFolderPath + "\\" + m_datasetFilename + "_FeaturesDF" + ".csv";
        StringBuilder sb = DatasetCSVBuilder.GetFeaturesDocumentFrequenciesCSV(GetFeaturesDocumentFrequenciesCSV);
        FileWriter.WriteFile(sb.toString(), featuresDocumentFrequenciesFilePath);
        Console.PrintLine(String.format("Features Document Frequencies saved to: %s", featuresDocumentFrequenciesFilePath), true, false);
    }

    /**
     * Print CSV file contain list of selected features
     *
     * @param selectedFeatures ArrayList of selected features selected features
     * @param destinationFolderPath path of the destination folder to print the
     * selected features file to
     */
    private static void PrintCSVFileSelectedFeatures(ArrayList<Pair<String, Integer>> selectedFeatures, String destinationFolderPath) {
        StringBuilder sb = DatasetCSVBuilder.GetSelectedFeaturesCSV(selectedFeatures);
        String featuresFilePath = destinationFolderPath + "\\" + m_datasetFilename + "_a_FeaturesList" + ".csv";
        FileWriter.WriteFile(sb.toString(), featuresFilePath);
        Console.PrintLine(String.format("Selected Features saved to: %s", featuresFilePath), true, false);
    }

    /**
     * Print SERIALIZED file contain list of selected features
     *
     * @param selectedFeatures ArrayList of selected features selected features
     * @param destinationFolderPath path of the destination folder to print the
     * selected features file to
     */
    private static void SerializeSelectedFeatures(ArrayList<Pair<String, Integer>> selectedFeatures, String destinationFolderPath) {
        Serializer.Serialize(selectedFeatures, destinationFolderPath, m_datasetFilename + "_a_FeaturesList");
    }
}
