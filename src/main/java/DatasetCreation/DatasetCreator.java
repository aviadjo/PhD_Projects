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
import DatasetCreation.DatasetCSVBuilder.FeatureRepresentation;
import FeatureExtraction.AFeatureExtractor;
import FeatureExtraction.MasterFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import IO.Directories;
import IO.FileWriter;
import Math.Entropy;
import java.util.ArrayList;
import java.util.Map;
import javafx.util.Pair;
import org.mapdb.HTreeMap;

/**
 *
 * @author Aviadjo
 */
public class DatasetCreator {

    /**
     * Return CSV string which represent the dataset
     *
     * @param folder_ClassA folder of elements from class A
     * @param folder_ClassB folder of elements from class B
     * @param featureExtractor The feature extractor to use
     * @param featureSelector The feature selector to use
     * @param topFeatures top features to select
     * @param featureRepresentation feature representation method (for example:
     * Boolea, TFIDF)
     * @param addElementIDColumn whether to add element ID column to the dataset
     * CSV
     * @param addClassificationColumn whether to add classification column to
     * the dataset CSV
     * @param destinationFolderPath destination folder path
     * @param printFeaturesDocumentFrequencies whether to print the features'
     * document frequencies
     * @param createDatabaseCSV whether to create the dataset record
     */
    public static String BuildDataset(String folder_ClassA,
            String folder_ClassB,
            AFeatureExtractor<String> featureExtractor,
            AFeatureSelector featureSelector,
            int topFeatures,
            FeatureRepresentation featureRepresentation,
            boolean createDatabaseCSV,
            boolean addElementIDColumn,
            boolean addClassificationColumn,
            String destinationFolderPath,
            boolean printFeaturesDocumentFrequencies,
            boolean printSelectedFeaturesScore
    ) {
        StopWatch.Start();

        ArrayList<String> ClassAelements = Directories.GetDirectoryFilesPaths(folder_ClassA);
        ArrayList<String> ClassBelements = Directories.GetDirectoryFilesPaths(folder_ClassB);
        int totalElementsNum = ClassAelements.size() + ClassBelements.size();

        Console.Print(String.format("ClassA folder: %s", folder_ClassA), true, false);
        Console.Print(String.format("ClassB folder: %s", folder_ClassB), true, false);
        Console.Print(String.format("ClassA elements: %s", GetStringNumber(ClassAelements.size())), true, false);
        Console.Print(String.format("ClassB elements: %s", GetStringNumber(ClassBelements.size())), true, false);
        Console.Print(String.format("Total elements: %s", GetStringNumber(totalElementsNum)), true, false);

        //FEATURE EXTRACTION
        MasterFeatureExtractor<String> CFE = new MasterFeatureExtractor<>();
        Console.Print(String.format("Feature Extraction: %s", featureExtractor.GetName()), true, false);
        Map<String, Integer> classAFeatures = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassAelements, featureExtractor);
        Console.Print(String.format("ClassA unique features: %s", GetStringNumber(classAFeatures.size())), true, false);
        Map<String, Integer> classBFeatures = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassBelements, featureExtractor);
        Console.Print(String.format("ClassB unique features: %s", GetStringNumber(classBFeatures.size())), true, false);
        Map<String, int[]> classesABFeatures = CFE.GatherClassAClassBFeatureFrequency(classAFeatures, classBFeatures);
        Console.Print(String.format("Total unique features: %s", GetStringNumber(classesABFeatures.size())), true, false);
        //classA_features.clear();
        //classB_features.clear();
        MapDB.m_db_off_heap_FE.commit();

        //PRINT DOCUMENT FREQUENCY
        if (printFeaturesDocumentFrequencies) {
            String filename = String.format(destinationFolderPath + "\\DATASET_%s_DOCX_DF.csv", General.GetTimeStamp());
            Console.Print(String.format("Document frequencies written to: %s", filename), true, false);
            FileWriter.WriteFile(General.GetFeaturesFrequenciesInClassAClassB(classesABFeatures), filename);
        }

        //FEATURE SELECTION
        Console.Print(String.format("Selecting top %s features..", topFeatures), true, false);
        ArrayList<Pair<String, Integer>> selectedFeatures = featureSelector.SelectTopFeatures(classesABFeatures, ClassAelements.size(), ClassBelements.size(), topFeatures, printSelectedFeaturesScore);

        //DATASET CREATION
        String datasetCSV = "";
        if (createDatabaseCSV) {
            Console.Print(String.format("Building dataset..."), true, false);
            Console.Print(String.format("Feature representation: %s", featureRepresentation.toString()), true, false);
            //****************
            DatasetCSVBuilder<String> dataset_builder = new DatasetCSVBuilder<>();
            String datasetHeaderCSV = dataset_builder.GetDatasetHeaderCSV(selectedFeatures.size(), addElementIDColumn, addClassificationColumn);
            String datasetClassACSV = dataset_builder.BuildDatabaseCSV(ClassAelements, featureExtractor, selectedFeatures, totalElementsNum, featureRepresentation, DatasetCSVBuilder.Clasification.Benign, addElementIDColumn, addClassificationColumn);
            String datasetClassBCSV = dataset_builder.BuildDatabaseCSV(ClassBelements, featureExtractor, selectedFeatures, totalElementsNum, featureRepresentation, DatasetCSVBuilder.Clasification.Malicious, addElementIDColumn, addClassificationColumn);
            datasetCSV = datasetHeaderCSV + "\n" + datasetClassBCSV + "\n" + datasetClassACSV;
            StopWatch.Stop();

            //OUTPUTS
            String dataset_path = String.format(destinationFolderPath + "\\DATASET_%s_Files(B%s_M%s)_FE(%s)_FS(%s)_Rep(%s).csv", General.GetTimeStamp(), ClassAelements.size(), ClassBelements.size(), featureExtractor.GetName(), featureSelector.GetName(), featureRepresentation.toString());
            FileWriter.WriteFile(datasetCSV, dataset_path);
            Console.Print(String.format("Dataset saved to: %s", dataset_path), true, false);
            Console.Print(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
            Console.Print(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
            Console.Print(String.format("InfoGain Values: %s", featureSelector.m_memo.size()), true, false);
        }
        return datasetCSV;
    }
}
