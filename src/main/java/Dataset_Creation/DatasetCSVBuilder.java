/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataset_Creation;

import Feature_Extraction.AFeatureExtractor;
import Math.MathCalc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Aviad
 */
public class DatasetCSVBuilder<T> {

    public enum Clasification {

        Benign,
        Malicious
    }

    public enum Feature_Representation {

        Binary,
        TFIDF
    }

    /**
     * Return CSV string which represent the dataset
     *
     * @param elements ArrayList of elements to build dataset from
     * @param Feature_Extractor a Feature Extractor object
     * @param selected_features the top selected features to build the dataset
     * with
     * @param classification the classification of all the records in the
     * dataset
     * @param prefix_element add prefix column identifying the record
     * @param suffix_class add suffix column identifying the class of the record
     * @return CSV string which represent the dataset
     */
    public String Build_Database_CSV(ArrayList<T> elements, AFeatureExtractor<T> Feature_Extractor, ArrayList<Pair<String, Integer>> selected_features, int total_elements_num, Feature_Representation representation, Clasification classification, boolean prefix_element, boolean suffix_class) {
        StringBuilder dataset_CSV = new StringBuilder();
        String element_features_vector_CSV;

        for (T element : elements) {
            element_features_vector_CSV = Get_Features_Vector_CSV(element, Feature_Extractor, selected_features, total_elements_num, representation, classification, prefix_element, suffix_class);
            dataset_CSV.append(element_features_vector_CSV);
            dataset_CSV.append("\n");
        }
        return dataset_CSV.toString().substring(0, dataset_CSV.lastIndexOf("\n"));
    }

    /**
     * Return CSV string which represent the element features vector
     *
     * @param element the element to extract the features from
     * @param Feature_Extractor a Feature Extractor object
     * @param selected_features the top selected features to build the dataset
     * with
     * @param classification the classification of given element dataset
     * @param prefix_element add prefix column identifying the record
     * @param suffix_class add suffix column identifying the class of the record
     * @return CSV string which represent the element features vector
     */
    public String Get_Features_Vector_CSV(T element, AFeatureExtractor<T> Feature_Extractor, ArrayList<Pair<String, Integer>> selected_features, int total_elements_num, Feature_Representation representation, Clasification classification, boolean prefix_element, boolean suffix_class) {
        Map<String, Integer> element_features_TF = Feature_Extractor.Extract_Features_TF_From_Single_Element(element);
        StringBuilder features_vector_CSV = new StringBuilder();

        if (prefix_element) {
            String element_string = element.toString();
            features_vector_CSV.append(element_string + ",");
        }

        //To find the value of the most common feature from the selected features
        String selected_feature;
        int selected_feature_value;
        int num_of_occurrences_of_most_common_feature = 0;
        for (Pair<String, Integer> selected_feature_pair : selected_features) {
            selected_feature = selected_feature_pair.getKey();
            if (element_features_TF.containsKey(selected_feature)) {
                selected_feature_value = element_features_TF.get(selected_feature);
                if (selected_feature_value > num_of_occurrences_of_most_common_feature){
                    num_of_occurrences_of_most_common_feature = selected_feature_value;
                }
            }
        }

        int feature_occurrences_in_element;
        int num_of_elements_contain_the_feature;
        double TFIDF;
        String cell_value = "";
        for (Pair<String, Integer> selected_feature_pair : selected_features) {
            selected_feature = selected_feature_pair.getKey();
            if (representation == Feature_Representation.Binary) {
                if (element_features_TF.containsKey(selected_feature)) {
                    cell_value = 1 + ",";
                } else {
                    cell_value = 0 + ",";
                }
            } else if (representation == Feature_Representation.TFIDF) {
                num_of_elements_contain_the_feature = selected_feature_pair.getValue();
                feature_occurrences_in_element = (element_features_TF.containsKey(selected_feature)) ? element_features_TF.get(selected_feature) : 0;
                TFIDF = MathCalc.Get_TFIDF(feature_occurrences_in_element, num_of_occurrences_of_most_common_feature, total_elements_num, num_of_elements_contain_the_feature);
                cell_value = TFIDF + ",";
            }
            features_vector_CSV.append(cell_value);
        }
        if (suffix_class) {
            features_vector_CSV.append(classification);
        } else {
            //To remove the last feature ","
            features_vector_CSV = new StringBuilder(features_vector_CSV.substring(0, features_vector_CSV.length() - 1));
        }
        return features_vector_CSV.toString();
    }

    /**
     * Return CSV string which represent the header row of the dataset
     *
     * @param selected_features the top selected features to build the dataset
     * with
     * @param prefix_element add prefix column identifying the record
     * @param suffix_class add suffix column identifying the class of the record
     * @return CSV string which represent the header row of the dataset
     */
    public String Get_Dataset_Header_CSV(ArrayList<Pair<String, Integer>> selected_features, boolean prefix_element, boolean suffix_class) {
        StringBuilder dataset_header_CSV = new StringBuilder();
        if (prefix_element) {
            dataset_header_CSV.append("Element,");
        }
        for (int i = 1; i <= selected_features.size(); i++) {
            dataset_header_CSV.append(String.format("f%s,", i));
        }
        if (suffix_class) {
            dataset_header_CSV.append("Class");
        } else {
            //To remove the last feature ","
            dataset_header_CSV = new StringBuilder(dataset_header_CSV.substring(0, dataset_header_CSV.length() - 1));
        }
        return dataset_header_CSV.toString();
    }
}
