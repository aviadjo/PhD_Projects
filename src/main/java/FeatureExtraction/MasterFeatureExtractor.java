/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import DataStructures.DataStructures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Integer> ExtractFeaturesDocumentFrequencyFromElements(ArrayList<T> elements, AFeatureExtractor<T> Feature_Extractor) {
        Map<String, Integer> element_features_TF = new HashMap<>();
        Map<String, Integer> elements_features_DF = DataStructures.GetMapStringInteger();

        for (T element : elements) {
            element_features_TF = Feature_Extractor.ExtractFeaturesFrequencyFromSingleElement(element);

            for (String feature : element_features_TF.keySet()) {
                if (!elements_features_DF.containsKey(feature)) {
                    elements_features_DF.put(feature, 1);
                } else {
                    elements_features_DF.put(feature, elements_features_DF.get(feature) + 1);
                }
            }
            //element_features_TF.clear(); //TEST
        }
        return elements_features_DF;
    }

    /**
     * Return Map<String, ArrayList<Integer>> which contain all the feature
     * extracted from all given elements (Class A and Class B) and their
     * Document Frequency (DF) in Class A and Class B
     *
     * @param ClassADF Map<String, Integer> which contain the features and
     * their Document Frequency extracted from elements from ClassA
     * @param ClassBDF Map<String, Integer> which contain the features and
     * their Document Frequency extracted from elements from ClassB
     * @return Map<String, ArrayList<Integer>> which contain all the feature
     * extracted from all given elements (Class A and Class B) and their
     * Document Frequency (DF) in Class A and Class B
     */
    public Map<String, int[]> GatherClassAClassBFeatureFrequency(Map<String, Integer> ClassADF, Map<String, Integer> ClassBDF) {
        Map<String, int[]> ClassesABDF = DataStructures.GetMapStringIntArray();

        String feature;
        Integer DF;
        int[] DFsTest;

        //Loop through Class A Features
        for (Map.Entry<String, Integer> entry : ClassADF.entrySet()) {
            feature = entry.getKey();
            DF = entry.getValue();
            ClassesABDF.put(feature, new int[]{DF /*classA*/, 0/*classB*/}); //Add the feature to the total list.
        }
        ClassADF.clear(); //TEST
        //Loop through Class B Features
        
        for (Map.Entry<String, Integer> entry : ClassBDF.entrySet()) {
            feature = entry.getKey();
            DF = entry.getValue();

            if (ClassesABDF.containsKey(feature)) {
                ClassesABDF.get(feature)[1] = DF; //Set the second value to the feature in index 1 (For class B)
                
                //DFsTest = ClassesABDF.get(feature);
                //ClassesABDF.remove(feature);
                //ClassesABDF.put(feature, new int[]{DFsTest[0] /*classA*/, DF /*classB*/});
                
            } else {
                ClassesABDF.put(feature, new int[]{0 /*classA*/, DF /*classB*/}); //Add the feature to the total list.
            }
        }
        ClassBDF.clear(); //TEST
        return ClassesABDF;
    }
}
