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
     * @param featureExtractor a Feature Extractor object
     * @return Map<String, Integer> which contain all the feature extracted from
     * all given elements (using the given Feature Extractor) and their Document
     * Frequency (DF)
     */
    public Map<String, Integer> ExtractFeaturesFrequenciesFromElements(ArrayList<T> elements, IFeatureExtractor<T> featureExtractor) {
        Map<String, Integer> elementFeaturesTF = new HashMap<>();
        Map<String, Integer> elementsFeaturesDF = DataStructures.GetMapStringInteger();

        for (T element : elements) {
            elementFeaturesTF = featureExtractor.ExtractFeaturesFrequencyFromSingleElement(element);

            for (String feature : elementFeaturesTF.keySet()) {
                if (!elementsFeaturesDF.containsKey(feature)) {
                    elementsFeaturesDF.put(feature, 1);
                } else {
                    elementsFeaturesDF.put(feature, elementsFeaturesDF.get(feature) + 1);
                }
            }
            //element_features_TF.clear(); //TEST
        }
        return elementsFeaturesDF;
    }

    /**
     * Return Map<String, ArrayList<Integer>> which contain all the feature
     * extracted from all given elements (Class A and Class B) and their
     * Document Frequency (DF) in Class A and Class B
     *
     * @param classA_DF Map<String, Integer> which contain the features and
     * their Document Frequency extracted from elements from ClassA
     * @param classB_DF Map<String, Integer> which contain the features and
     * their Document Frequency extracted from elements from ClassB
     * @return Map<String, ArrayList<Integer>> which contain all the feature
     * extracted from all given elements (Class A and Class B) and their
     * Document Frequency (DF) in Class A and Class B
     */
    public Map<String, int[]> GatherClassAClassBFeatureFrequency(Map<String, Integer> classA_DF, Map<String, Integer> classB_DF) {
        Map<String, int[]> ClassesABDF = DataStructures.GetMapStringIntArray();

        String feature;
        Integer DF;
        int[] DFsTemp;

        //Loop through Class A Features
        for (Map.Entry<String, Integer> entry : classA_DF.entrySet()) {
            feature = entry.getKey();
            DF = entry.getValue();
            ClassesABDF.put(feature, new int[]{DF /*classA*/, 0/*classB*/}); //Add the feature to the total list.
        }
        classA_DF.clear(); //TEST
        //Loop through Class B Features

        for (Map.Entry<String, Integer> entry : classB_DF.entrySet()) {
            feature = entry.getKey();
            DF = entry.getValue();

            if (!ClassesABDF.containsKey(feature)) {
                ClassesABDF.put(feature, new int[]{0 /*classA*/, DF /*classB*/}); //Add the feature to the total list.
            } else {
                DFsTemp = ClassesABDF.get(feature);
                ClassesABDF.put(feature, new int[]{DFsTemp[0] /*classA*/, DF /*classB*/});
            }
        }
        classB_DF.clear(); //TEST
        return ClassesABDF;
    }
}
