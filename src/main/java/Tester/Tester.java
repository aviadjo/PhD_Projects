/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Assistants.General;
import static Assistants.General.Get_String_Number;
import Console.Console;
import Data_Structures.MapDB;
import Dataset_Creation.Dataset_CSV_Builder;
import Dataset_Creation.Dataset_CSV_Builder.Clasification;
import Dataset_Creation.Dataset_CSV_Builder.Feature_Representation;
import Feature_Extraction.Abstract_Feature_Extractor;
import Feature_Extraction.Collective_Feature_Extractor;
import Feature_Extractors.FE_ngram;
import Feature_Selectors.FS_InfoGainRatio;
import IO.File_Reader;
import IO.File_Writer;
import java.util.ArrayList;
import javafx.util.Pair;
import org.mapdb.*;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        Test_Ngram();
    }

    public static void Test_Ngram() {
        String folder_ClassA = "D:\\Dropbox\\TESTS\\FE_ngram\\DocX_ClassA_10";
        String folder_ClassB = "D:\\Dropbox\\TESTS\\FE_ngram\\DocX_ClassB_10";
        ArrayList<String> ClassA_elements = File_Reader.Get_Files_Paths_In_Folder(folder_ClassA);
        ArrayList<String> ClassB_elements = File_Reader.Get_Files_Paths_In_Folder(folder_ClassB);
        //ArrayList<String> list_of_strings_ClassA = new ArrayList<>(Arrays.asList("acbde", "fghij", "klmno"));
        //ArrayList<String> list_of_strings_ClassB = new ArrayList<>(Arrays.asList("pqrst", "uvwxyz"));
        int total_elements_num = ClassA_elements.size() + ClassB_elements.size();

        Console.Print_To_Console(String.format("ClassA folder: %s", folder_ClassA), true, false);
        Console.Print_To_Console(String.format("ClassB folder: %s", folder_ClassB), true, false);
        Console.Print_To_Console(String.format("ClassA elements: %s", Get_String_Number(ClassA_elements.size())), true, false);
        Console.Print_To_Console(String.format("ClassB elements: %s", Get_String_Number(ClassB_elements.size())), true, false);
        Console.Print_To_Console(String.format("Total elements: %s", Get_String_Number(total_elements_num)), true, false);

        //FEATURE EXTRACTION
        int gram = 3;
        int skip = 1;
        Console.Print_To_Console(String.format("Feature Extraction: ngram (grams=%s skip=%s)", gram, skip), true, false);
        Abstract_Feature_Extractor<String> ngram_extractor = new FE_ngram<>(gram, skip);
        Collective_Feature_Extractor<String> CFE = new Collective_Feature_Extractor<>();
        HTreeMap<String, Integer> ngrams_ClassA = CFE.Extract_Features_DF_From_Elements(ClassA_elements, ngram_extractor);
        Console.Print_To_Console(String.format("ClassA unique features: %s", Get_String_Number(ngrams_ClassA.size())), true, false);
        HTreeMap<String, Integer> ngrams_ClassB = CFE.Extract_Features_DF_From_Elements(ClassB_elements, ngram_extractor);
        Console.Print_To_Console(String.format("ClassB unique features: %s", Get_String_Number(ngrams_ClassB.size())), true, false);
        HTreeMap<String, int[]> ngrams_ClassesAB = CFE.Gather_ClassA_ClassB_DF(ngrams_ClassA, ngrams_ClassB);
        Console.Print_To_Console(String.format("Total unique features: %s", Get_String_Number(ngrams_ClassesAB.size())), true, false);
        MapDB.m_db_off_heap_FE.commit();
        
        //FEATURE SELECTION
        int top_features = 2000;
        double top_percent_features = 0.01;
        Console.Print_To_Console(String.format("Selecting features.."), true, false);
        FS_InfoGainRatio fs_IG = new FS_InfoGainRatio(ClassA_elements.size(), ClassB_elements.size(),false);
        ArrayList<Pair<String, Integer>> selected_features = fs_IG.Select_Features(ngrams_ClassesAB, top_features, top_percent_features);
        Console.Print_To_Console(String.format("Selected features: %s",selected_features.size()), true, false);
        
        //DATASET CREATION
        boolean add_preffix_element = false;
        boolean add_suffix_classification = true;
        Feature_Representation feature_representation = Feature_Representation.Binary;
        Console.Print_To_Console(String.format("Building dataset..."), true, false);
        Console.Print_To_Console(String.format("Feature representation: %s",feature_representation.toString()), true, false);
        //****************
        Dataset_CSV_Builder<String> dataset_builder = new Dataset_CSV_Builder<>();
        String dataset_header = dataset_builder.Get_Dataset_Header_CSV(selected_features, add_preffix_element, add_suffix_classification);
        String dataset_classA = dataset_builder.Build_Database_CSV(ClassA_elements, ngram_extractor, selected_features, total_elements_num, feature_representation, Clasification.Benign, add_preffix_element, add_suffix_classification);
        String dataset_classB = dataset_builder.Build_Database_CSV(ClassB_elements, ngram_extractor, selected_features, total_elements_num, feature_representation, Clasification.Malicious, add_preffix_element, add_suffix_classification);
        String dataset = dataset_header + "\n" + dataset_classB + "\n" + dataset_classA;

        //OUTPUTS
        String dataset_path = String.format("D:\\Dropbox\\DATASETS\\DATASET_%s_files(%s)_gram(%s)_Rep(%s).csv", General.Get_TimeStamp_String(),total_elements_num,gram,feature_representation.toString());
        File_Writer.Write_To_File(dataset, dataset_path);
        Console.Print_To_Console(String.format("Dataset saved to: %s",dataset_path), true, false);
    }
}