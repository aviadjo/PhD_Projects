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
import DatasetCreation.DatasetCSVBuilder.Feature_Representation;
import FeatureExtraction.AFeatureExtractor;
import FeatureExtraction.MasterFeatureExtractor;
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
        //TestNgram();
        TextDocx();
    }

    public static void TestNgram() {
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
        AFeatureExtractor<String> ngram_extractor = new FeatureExtractorNgrams<>(gram, skip);
        Console.Print(String.format("Feature Extraction: %s", ngram_extractor.GetName()), true, false);
        HTreeMap<String, Integer> ngrams_ClassA = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassA_elements, ngram_extractor);
        Console.Print(String.format("ClassA unique features: %s", GetStringNumber(ngrams_ClassA.size())), true, false);
        HTreeMap<String, Integer> ngrams_ClassB = CFE.ExtractFeaturesDocumentFrequencyFromElements(ClassB_elements, ngram_extractor);
        Console.Print(String.format("ClassB unique features: %s", GetStringNumber(ngrams_ClassB.size())), true, false);
        HTreeMap<String, int[]> ngrams_ClassesAB = CFE.GatherClassAClassBFeatureFrequency(ngrams_ClassA, ngrams_ClassB);
        Console.Print(String.format("Total unique features: %s", GetStringNumber(ngrams_ClassesAB.size())), true, false);
        ngrams_ClassA.clear();
        ngrams_ClassB.clear();
        MapDB.m_db_off_heap_FE.commit();

        //FEATURE SELECTION
        int top_features = 2000;
        double top_percent_features = 0.01;
        Console.Print(String.format("Selecting features.."), true, false);
        FeatureSelectorInfoGainRatio fs_IG = new FeatureSelectorInfoGainRatio(ClassA_elements.size(), ClassB_elements.size(), false);
        ArrayList<Pair<String, Integer>> selected_features = fs_IG.SelectTopFeatures(ngrams_ClassesAB, top_features, top_percent_features,false);
        Console.Print(String.format("Selected features: %s", selected_features.size()), true, false);

        //DATASET CREATION
        boolean add_preffix_element = false;
        boolean add_suffix_classification = true;
        Feature_Representation feature_representation = Feature_Representation.Binary;
        Console.Print(String.format("Building dataset..."), true, false);
        Console.Print(String.format("Feature representation: %s", feature_representation.toString()), true, false);
        //****************
        DatasetCSVBuilder<String> dataset_builder = new DatasetCSVBuilder<>();
        String dataset_header = dataset_builder.GetDatasetHeaderCSV(selected_features, add_preffix_element, add_suffix_classification);
        String dataset_classA = dataset_builder.BuildDatabaseCSV(ClassA_elements, ngram_extractor, selected_features, total_elements_num, feature_representation, Clasification.Benign, add_preffix_element, add_suffix_classification);
        String dataset_classB = dataset_builder.BuildDatabaseCSV(ClassB_elements, ngram_extractor, selected_features, total_elements_num, feature_representation, Clasification.Malicious, add_preffix_element, add_suffix_classification);
        String dataset = dataset_header + "\n" + dataset_classB + "\n" + dataset_classA;
        StopWatch.Stop();

        //OUTPUTS
        String dataset_path = String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_files(%s)_gram(%s)_Rep(%s).csv", General.GetTimeStamp(), total_elements_num, gram, feature_representation.toString());
        FileWriter.WriteFile(dataset, dataset_path);
        Console.Print(String.format("Dataset saved to: %s", dataset_path), true, false);
        Console.Print(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
        Console.Print(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
        Console.Print(String.format("InfoGain Values: %s", fs_IG.m_memoInfoGain.size()), true, false);
    }

    private static void TextDocx() {
        StopWatch.Start();

        String folder_ClassA = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
        String folder_ClassB = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100";

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
        FileWriter.WriteFile(General.GetFeaturesFrequenciesInClassAClassB(ClassesAB), String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_DOCX_DF.csv", General.GetTimeStamp()));
        
        //FEATURE SELECTION
        int top_features = 500;
        double top_percent_features = 0.01;
        Console.Print(String.format("Selecting features.."), true, false);
        FeatureSelectorInfoGainRatio fs_IG = new FeatureSelectorInfoGainRatio(ClassA_elements.size(), ClassB_elements.size(), false);
        ArrayList<Pair<String, Integer>> selected_features = fs_IG.SelectTopFeatures(ClassesAB, top_features, top_percent_features,true);
        Console.Print(String.format("Selected features: %s", selected_features.size()), true, false);

        //DATASET CREATION
        boolean add_preffix_element = false;
        boolean add_suffix_classification = true;
        Feature_Representation feature_representation = Feature_Representation.Binary;
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
        String dataset_path = String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_DOCX_files(%s)_Rep(%s).csv", General.GetTimeStamp(), total_elements_num, feature_representation.toString());
        FileWriter.WriteFile(dataset, dataset_path);
        Console.Print(String.format("Dataset saved to: %s", dataset_path), true, false);
        Console.Print(String.format("Running time: %s", StopWatch.GetTimeSecondsString()), true, false);
        Console.Print(String.format("Entropy Values: %s", Entropy.m_memoEntropies.size()), true, false);
        Console.Print(String.format("InfoGain Values: %s", fs_IG.m_memoInfoGain.size()), true, false);
    }

}
