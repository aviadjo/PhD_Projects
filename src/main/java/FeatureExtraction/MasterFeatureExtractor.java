/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import DataStructures.MapDB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.mapdb.HTreeMap;

/**
 *
 * @author Aviad
 */
public class MasterFeatureExtractor<T> {

    /**
     * Return Map<String, Integer> which contain all the feature extracted from
     * all given elements (using the given Feature Extractor) and their Document
     * Frequency (DF)
     *
     * @param elements ArrayList of elements to calculate document frequency for
     * @param Feature_Extractor a Feature Extractor object
     * @return Map<String, Integer> which contain all the feature extracted from
     * all given elements (using the given Feature Extractor) and their Document
     * Frequency (DF)
     */
    public HTreeMap<String, Integer> ExtractFeaturesDocumentFrequencyFromElements(ArrayList<T> elements, AFeatureExtractor<T> Feature_Extractor) {
        Map<String, Integer> element_features_TF = new HashMap<>();
        HTreeMap<String, Integer> elements_features_DF = MapDB.GetHTreeMapStringInteger();

        for (T element : elements) {
            element_features_TF = Feature_Extractor.ExtractFeaturesFrequencyFromSingleElement(element);

            for (String feature : element_features_TF.keySet()) {
                if (!elements_features_DF.containsKey(feature)) {
                    elements_features_DF.put(feature, 1);
                } else {
                    elements_features_DF.put(feature, elements_features_DF.get(feature) + 1);
                }
            }
        }
        return elements_features_DF;
    }

    /**
     * Return Map<String, ArrayList<Integer>> which contain all the feature
     * extracted from all given elements (Class A and Class B) and their
     * Document Frequency (DF) in Class A and Class B
     *
     * @param DF_ClassA Map<String, Integer> which contain the features and
     * their Document Frequency extracted from elements from ClassA
     * @param DF_ClassB Map<String, Integer> which contain the features and
     * their Document Frequency extracted from elements from ClassB
     * @return Map<String, ArrayList<Integer>> which contain all the feature
     * extracted from all given elements (Class A and Class B) and their
     * Document Frequency (DF) in Class A and Class B
     */
    public HTreeMap<String, int[]> GatherClassAClassBFeatureFrequency(HTreeMap<String, Integer> DF_ClassA, HTreeMap<String, Integer> DF_ClassB) {
        HTreeMap<String, int[]> DF_classA_classB = MapDB.GetHTreeMapStringArrayInt();

        String feature = "";
        Integer DF = 0;

        //Loop through Class A Features
        for (Map.Entry<String, Integer> entry : DF_ClassA.entrySet()) {
            feature = entry.getKey();
            DF = entry.getValue();

            DF_classA_classB.put(feature, new int[2]); //Add the feature to the total list.
            DF_classA_classB.get(feature)[0] = DF; //Add the first value to the feature
            DF_classA_classB.get(feature)[1] = 0;
        }
        DF_ClassA.clear(); //TEST
        //Loop through Class B Features
        for (Map.Entry<String, Integer> entry : DF_ClassB.entrySet()) {
            feature = entry.getKey();
            DF = entry.getValue();

            if (DF_classA_classB.containsKey(feature)) {
                DF_classA_classB.get(feature)[1] = DF; //Set the second value to the feature in index 1 (For class B)
            } else {
                DF_classA_classB.put(feature, new int[2]); //Add the feature to the total list.
                DF_classA_classB.get(feature)[0] = 0; //Add the first value (For class A) to the feature
                DF_classA_classB.get(feature)[1] = DF; //Add the second value (For class B) to the feature
            }
        }
        DF_ClassB.clear(); //TEST
        return DF_classA_classB;
    }
}