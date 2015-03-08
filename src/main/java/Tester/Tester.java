/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Assistants.General;
import static Assistants.General.GetStringNumber;
import Assistants.StopWatch;
import Console.Console;
import DataStructures.MapDB;
import DatasetCreation.DatasetCSVBuilder;
import DatasetCreation.DatasetCSVBuilder.Clasification;
import DatasetCreation.DatasetCSVBuilder.FeatureRepresentation;
import FeatureExtraction.AFeatureExtractor;
import FeatureExtraction.MasterFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import IO.Directories;
import Implementations.FeatureExtractorNgrams;
import Implementations.FeatureSelectorInfoGainRatio;
import IO.FileWriter;
import Implementations.FeatureExtractorDocxStructuralPaths;
import Math.Entropy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import org.mapdb.*;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static Map<String, Integer> m_structuralPaths = new HashMap<>();
    public static String m_PathOfficeFileTempFolder = "";
    public static ArrayList<String> m_ArrayList = new ArrayList<String>();

    public static void main(String[] args) {
        BuildDatasetConfiguration();
    }

    public static void BuildDatasetConfiguration() {
        String folder_Benign = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassA";
        String folder_Malicious = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassB";
        AFeatureExtractor<String> featureExtractorNgram = new FeatureExtractorNgrams<>(3, 1);
        AFeatureExtractor<String> featureExtractorDocxStructuralPaths = new FeatureExtractorDocxStructuralPaths();
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(20, 1000, true);
        int topFeatures = 500;
        FeatureRepresentation featureRepresentation = FeatureRepresentation.Binary;
        boolean preffixElement = false;
        boolean suffixClassification = true;
        String destinationFolderPath = "D:\\Dropbox\\Dataset";

        BuildDataset(folder_Benign,
                folder_Malicious,
                featureExtractorDocxStructuralPaths,
                featureSelector,
                topFeatures,
                featureRepresentation,
                preffixElement,
                suffixClassification,
                destinationFolderPath
        );
    }

    public static void BuildDataset(String folder_ClassA, String folder_ClassB, AFeatureExtractor<String> featureExtractor, AFeatureSelector featureSelector, int topFeatures, FeatureRepresentation featureRepresentation, boolean preffixElement, boolean suffixClassification, String destinationFolderPath) {
        StopWatch.Start();

        ArrayList<String> ClassA_elements = Directories.GetDirectoryFilesPaths(folder_ClassA);
        ArrayList<String> ClassB_elements = Directories.GetDirectoryFilesPaths(folder_ClassB);
        int total_elements_num = ClassA_elements.size() + ClassB_elements.size();

        Console.Print(String.format("ClassA folder: %s", folder_ClassA), true, false);
        Console.Print(String.format("ClassB folder: %s", folder_ClassB), true, false);
        Console.Print(String.format("ClassA elements: %s", GetStringNumber(ClassA_elements.size())), true, false);
        Console.Print(String.format("ClassB elements: %s", GetStringNumber(ClassB_elements.size())), true, false);
        Console.Print(String.format("Total elements: %s", GetStringNumber(total_elements_num)), true, false);

        //FEATURE EXTRACTION
        MasterFeatureExtractor<String> CFE = new MasterFeatureExtractor<>();
        Console.Print(String.format("Feature Extraction: %s", featureExtractor.GetName()), true, false);
        HTreeMap<String, Integer> classAFeatures = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassA_elements, featureExtractor);
        Console.Print(String.format("ClassA unique features: %s", GetStringNumber(classAFeatures.size())), true, false);
        HTreeMap<String, Integer> classBFeatures = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassB_elements, featureExtractor);
        Console.Print(String.format("ClassB unique features: %s", GetStringNumber(classBFeatures.size())), true, false);
        HTreeMap<String, int[]> classesABFeatures = CFE.GatherClassAClassBFeatureFrequency(classAFeatures, classBFeatures);
        Console.Print(String.format("Total unique features: %s", GetStringNumber(classesABFeatures.size())), true, false);
        //classA_features.clear();
        //classB_features.clear();
        MapDB.m_db_off_heap_FE.commit();

        //PRINT DOCUMENT FREQUENCY
        //String filename = String.format(destinationFolderPath + "\\DATASET_%s_DOCX_DF.csv", General.GetTimeStamp());
        //Console.Print(String.format("Document frequencies written to: %s", filename), true, false);
        //FileWriter.WriteFile(General.GetFeaturesFrequenciesInClassAClassB(classesABFeatures), filename);

        //FEATURE SELECTION
        Console.Print(String.format("Selecting top %s features..", topFeatures), true, false);
        ArrayList<Pair<String, Integer>> selected_features = featureSelector.SelectTopFeatures(classesABFeatures, topFeatures, 0.01, false);

        //DATASET CREATION
        Console.Print(String.format("Building dataset..."), true, false);
        Console.Print(String.format("Feature representation: %s", featureRepresentation.toString()), true, false);
        //****************
        DatasetCSVBuilder<String> dataset_builder = new DatasetCSVBuilder<>();
        String dataset_header = dataset_builder.GetDatasetHeaderCSV(selected_features, preffixElement, suffixClassification);
        String dataset_classA = dataset_builder.BuildDatabaseCSV(ClassA_elements, featureExtractor, selected_features, total_elements_num, featureRepresentation, Clasification.Benign, preffixElement, suffixClassification);
        String dataset_classB = dataset_builder.BuildDatabaseCSV(ClassB_elements, featureExtractor, selected_features, total_elements_num, featureRepresentation, Clasification.Malicious, preffixElement, suffixClassification);
        String dataset = dataset_header + "\n" + dataset_classB + "\n" + dataset_classA;
        StopWatch.Stop();

        //OUTPUTS
        String dataset_path = String.format(destinationFolderPath + "\\DATASET_%s_files(B%s_M%s)_FE(%s)_FS(%s)_Rep(%s).csv", General.GetTimeStamp(), ClassA_elements.size(), ClassB_elements.size(), featureExtractor.GetName(), featureSelector.GetName(), featureRepresentation.toString());
        FileWriter.WriteFile(dataset, dataset_path);
        Console.Print(String.format("Dataset saved to: %s", dataset_path), true, false);
        Console.Print(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
        //Console.Print(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
        //Console.Print(String.format("InfoGain Values: %s", featureSelector.m_memo.size()), true, false);
    }

    /*public static void TestNgram() {
        StopWatch.Start();

        //String folder_ClassA = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        //String folder_ClassB = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
        String folder_ClassA = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassA";
        String folder_ClassB = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassB";
        //String folder_ClassA = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\TXT_ClassA";
        //String folder_ClassB = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\TXT_ClassB";
        ArrayList<String> ClassA_elements = Directories.GetDirectoryFilesPaths(folder_ClassA);
        ArrayList<String> ClassB_elements = Directories.GetDirectoryFilesPaths(folder_ClassB);
        //ArrayList<String> ClassA_elements = new ArrayList<>(Arrays.asList("a1b1c1d1", "d1e1f1g1h1", "h1i1j1k1l1","l1m1n1o1p1"));
        //ArrayList<String> ClassB_elements = new ArrayList<>(Arrays.asList("p1q1r1s1t1", "t1u1v1w1x1","x1y1z1"));
        int total_elements_num = ClassA_elements.size() + ClassB_elements.size();

        Console.Print(String.format("ClassA folder: %s", folder_ClassA), true, false);
        Console.Print(String.format("ClassB folder: %s", folder_ClassB), true, false);
        Console.Print(String.format("ClassA elements: %s", GetStringNumber(ClassA_elements.size())), true, false);
        Console.Print(String.format("ClassB elements: %s", GetStringNumber(ClassB_elements.size())), true, false);
        Console.Print(String.format("Total elements: %s", GetStringNumber(total_elements_num)), true, false);

        //FEATURE EXTRACTION
        int gram = 3;
        int skip = 1;
        MasterFeatureExtractor<String> CFE = new MasterFeatureExtractor<>();
        AFeatureExtractor<String> featureExtractorNgram = new FeatureExtractorNgrams<>(gram, skip);
        Console.Print(String.format("Feature Extraction: %s", featureExtractorNgram.GetName()), true, false);
        HTreeMap<String, Integer> classA_features = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassA_elements, featureExtractorNgram);
        Console.Print(String.format("ClassA unique features: %s", GetStringNumber(classA_features.size())), true, false);
        HTreeMap<String, Integer> classB_features = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassB_elements, featureExtractorNgram);
        Console.Print(String.format("ClassB unique features: %s", GetStringNumber(classB_features.size())), true, false);
        HTreeMap<String, int[]> classesAB_features = CFE.GatherClassAClassBFeatureFrequency(classA_features, classB_features);
        Console.Print(String.format("Total unique features: %s", GetStringNumber(classesAB_features.size())), true, false);
        classA_features.clear();
        classB_features.clear();
        MapDB.m_db_off_heap_FE.commit();

        //FEATURE SELECTION
        int top_features = 2000;
        double top_percent_features = 0.01;
        Console.Print(String.format("Selecting features.."), true, false);
        AFeatureSelector fs_IG = new FeatureSelectorInfoGainRatio(ClassA_elements.size(), ClassB_elements.size(), false);
        ArrayList<Pair<String, Integer>> selected_features = fs_IG.SelectTopFeatures(classesAB_features, top_features, top_percent_features, false);
        Console.Print(String.format("Selected features: %s", selected_features.size()), true, false);

        //DATASET CREATION
        boolean add_preffix_element = false;
        boolean add_suffix_classification = true;
        FeatureRepresentation feature_representation = FeatureRepresentation.Binary;
        Console.Print(String.format("Building dataset..."), true, false);
        Console.Print(String.format("Feature representation: %s", feature_representation.toString()), true, false);
        //****************
        DatasetCSVBuilder<String> dataset_builder = new DatasetCSVBuilder<>();
        String dataset_header = dataset_builder.GetDatasetHeaderCSV(selected_features, add_preffix_element, add_suffix_classification);
        String dataset_classA = dataset_builder.BuildDatabaseCSV(ClassA_elements, featureExtractorNgram, selected_features, total_elements_num, feature_representation, Clasification.Benign, add_preffix_element, add_suffix_classification);
        String dataset_classB = dataset_builder.BuildDatabaseCSV(ClassB_elements, featureExtractorNgram, selected_features, total_elements_num, feature_representation, Clasification.Malicious, add_preffix_element, add_suffix_classification);
        String dataset = dataset_header + "\n" + dataset_classB + "\n" + dataset_classA;
        StopWatch.Stop();

        //OUTPUTS
        String dataset_path = String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_files(B%s_M%s)_gram(%s)_Rep(%s).csv", General.GetTimeStamp(), ClassA_elements.size(), ClassB_elements.size(), gram, feature_representation.toString());
        FileWriter.WriteFile(dataset, dataset_path);
        Console.Print(String.format("Dataset saved to: %s", dataset_path), true, false);
        Console.Print(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
        Console.Print(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
        Console.Print(String.format("InfoGain Values: %s", fs_IG.m_memo.size()), true, false);
    }*/

    /*private static void TextDocx() {
        StopWatch.Start();

        String folder_ClassA = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        String folder_ClassB = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";

        ArrayList<String> ClassA_elements = Directories.GetDirectoryFilesPaths(folder_ClassA);
        ArrayList<String> ClassB_elements = Directories.GetDirectoryFilesPaths(folder_ClassB);

        int total_elements_num = ClassA_elements.size() + ClassB_elements.size();

        Console.Print(String.format("ClassA folder: %s", folder_ClassA), true, false);
        Console.Print(String.format("ClassB folder: %s", folder_ClassB), true, false);
        Console.Print(String.format("ClassA elements: %s", GetStringNumber(ClassA_elements.size())), true, false);
        Console.Print(String.format("ClassB elements: %s", GetStringNumber(ClassB_elements.size())), true, false);
        Console.Print(String.format("Total elements: %s", GetStringNumber(total_elements_num)), true, false);

        //FEATURE EXTRACTION
        MasterFeatureExtractor<String> CFE = new MasterFeatureExtractor<>();
        AFeatureExtractor<String> structuralFeaturesExtractorDOCX = new FeatureExtractorDocxStructuralPaths();
        Console.Print(String.format("Feature Extraction: %s", structuralFeaturesExtractorDOCX.GetName()), true, false);
        HTreeMap<String, Integer> ClassA = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassA_elements, structuralFeaturesExtractorDOCX);
        Console.Print(String.format("ClassA unique features: %s", GetStringNumber(ClassA.size())), true, false);
        HTreeMap<String, Integer> ClassB = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassB_elements, structuralFeaturesExtractorDOCX);
        Console.Print(String.format("ClassB unique features: %s", GetStringNumber(ClassB.size())), true, false);
        HTreeMap<String, int[]> ClassesAB = CFE.GatherClassAClassBFeatureFrequency(ClassA, ClassB);
        Console.Print(String.format("Total unique features: %s", GetStringNumber(ClassesAB.size())), true, false);
        //ClassA.clear();
        //ClassB.clear();
        MapDB.m_db_off_heap_FE.commit();

        //Write
        String filename = String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_DOCX_DF.csv", General.GetTimeStamp());
        Console.Print(String.format("Document frequencies written to: %s", filename), true, false);
        FileWriter.WriteFile(General.GetFeaturesFrequenciesInClassAClassB(ClassesAB), filename);

        //FEATURE SELECTION
        int top_features = 500;
        double top_percent_features = 0.01;
        Console.Print(String.format("Selecting features.."), true, false);
        FeatureSelectorInfoGainRatio fs_IG = new FeatureSelectorInfoGainRatio(ClassA_elements.size(), ClassB_elements.size(), false);
        ArrayList<Pair<String, Integer>> selected_features = fs_IG.SelectTopFeatures(ClassesAB, top_features, top_percent_features, true);
        Console.Print(String.format("Selected features: %s", selected_features.size()), true, false);

        //DATASET CREATION
        boolean add_preffix_element = false;
        boolean add_suffix_classification = true;
        FeatureRepresentation feature_representation = FeatureRepresentation.Binary;
        Console.Print(String.format("Building dataset..."), true, false);
        Console.Print(String.format("Feature representation: %s", feature_representation.toString()), true, false);
        //****************
        DatasetCSVBuilder<String> dataset_builder = new DatasetCSVBuilder<>();
        String dataset_header = dataset_builder.GetDatasetHeaderCSV(selected_features, add_preffix_element, add_suffix_classification);
        String dataset_classA = dataset_builder.BuildDatabaseCSV(ClassA_elements, structuralFeaturesExtractorDOCX, selected_features, total_elements_num, feature_representation, Clasification.Benign, add_preffix_element, add_suffix_classification);
        String dataset_classB = dataset_builder.BuildDatabaseCSV(ClassB_elements, structuralFeaturesExtractorDOCX, selected_features, total_elements_num, feature_representation, Clasification.Malicious, add_preffix_element, add_suffix_classification);
        String dataset = dataset_header + "\n" + dataset_classB + "\n" + dataset_classA;
        StopWatch.Stop();

        //OUTPUTS
        String dataset_path = String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_DOCX_files(B%s_M%s)_Rep(%s).csv", General.GetTimeStamp(), ClassA_elements.size(), ClassB_elements.size(), feature_representation.toString());
        FileWriter.WriteFile(dataset, dataset_path);
        Console.Print(String.format("Dataset saved to: %s", dataset_path), true, false);
        Console.Print(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
        Console.Print(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
        Console.Print(String.format("InfoGain Values: %s", fs_IG.m_memo.size()), true, false);
    }*/

}
