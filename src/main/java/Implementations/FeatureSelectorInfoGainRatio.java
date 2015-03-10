/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import DataStructures.DataStructures;
import DataStructures.MapDB;
import FeatureSelection.AFeatureSelector;
import Math.Entropy;
import Math.MathCalc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;
import org.mapdb.Pump;
import org.mapdb.Serializer;

/**
 *
 * @author Aviadjo
 */
public final class FeatureSelectorInfoGainRatio extends AFeatureSelector {

    private int m_Class_A_elements_num;
    private int m_Class_B_elements_num;
    private boolean m_GainRatio;
    private int m_elements_num;
    private double m_Class_A_elements_percentage;
    private double m_Class_B_elements_percentage;
    private double m_Total_entropy;

    public FeatureSelectorInfoGainRatio(boolean GainRatio) {
        m_GainRatio = GainRatio;
    }

    /**
     * Initialize Feature Selection - Information Gain
     *
     * @param Class_A_elements_num the amount of elements (files, etc..) from
     * Class A
     * @param Class_B_elements_num the amount of elements (files, etc..) from
     * Class B
     */
    public void SetTotalInfo(int Class_A_elements_num, int Class_B_elements_num) {
        m_Class_A_elements_num = Class_A_elements_num;
        m_Class_B_elements_num = Class_B_elements_num;

        m_elements_num = m_Class_A_elements_num + m_Class_B_elements_num;
        m_Class_A_elements_percentage = (double) m_Class_A_elements_num / (double) m_elements_num;
        m_Class_B_elements_percentage = (double) m_Class_B_elements_num / (double) m_elements_num;
        m_Total_entropy = Entropy.GetEntropy(new ArrayList<>(Arrays.asList(m_Class_A_elements_percentage, m_Class_B_elements_percentage)));
    }

    /**
     * Select X (or X precent) top features from the given features list
     * according to Information Gain scoring
     *
     * @param featuresDFs list of features and Document Frequency for each
     * class, A and B
     * @param topFeaturesToSelect the amount of top features to select
     * @param top_features_percent_to_select the percent of top features to
     * select
     * @return ArrayList of features selected by Information Gain Feature
     * Selection algorithm and their DF
     */
    @Override
    public ArrayList<Pair<String, Integer>> SelectTopFeatures(Map<String, int[]> featuresDFs, int classAelementsNum, int classBelementsNum, int topFeaturesToSelect, boolean printScores) {
        SetTotalInfo(classAelementsNum, classBelementsNum);
        Map<String, Double> featuresInfoGain = GetFeaturesInfoGain(featuresDFs);

        //Comparator used for iterator
        Comparator featuresComperator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Double>) o2).getValue().compareTo(
                        ((Map.Entry<String, Double>) o1).getValue());
            }
        };

        //Initialize an Iterator over the sorted map
        Iterator iteratorSorted = Pump.sort(featuresInfoGain.entrySet().iterator(), false, 1000000000, featuresComperator, Serializer.BASIC);

        int amount_to_select = ((topFeaturesToSelect < featuresInfoGain.size()) ? topFeaturesToSelect : featuresInfoGain.size());
        ArrayList<Pair<String, Integer>> topFeatures = new ArrayList<>();
        String feature;
        int featureDFtotal;
        int[] featureDFofClasses;
        Map.Entry<String, Double> entry;
        for (int i = 0; i < amount_to_select; i++) {
            if (iteratorSorted.hasNext()) {
                entry = (Map.Entry<String, Double>) iteratorSorted.next();
                feature = entry.getKey();
                if (printScores) {
                    Console.Console.Print(String.format("Feature f%s: %s: %s -> %s", i + 1, GetName(), entry.getValue(), feature), true, false);
                }
                featureDFofClasses = featuresDFs.get(feature);
                featureDFtotal = featureDFofClasses[0] + featureDFofClasses[1]; //To be used for TFIDF calculation.
                topFeatures.add(new Pair(feature, featureDFtotal));
            }
        }
        return topFeatures;
    }

    /**
     * Calculates Information Gain (IG) or Information Gain Ratio(IGR) score for
     * the given feature list
     *
     * @param features_DFs list of features and Document Frequency for each
     * class, A and B
     * @return Map which contains for every feature it's corresponding
     * Information Gain (IG) or Information Gain Ratio(IGR) score
     */
    public Map<String, Double> GetFeaturesInfoGain(Map<String, int[]> features_DFs) {
        Map<String, Double> features_InfoGain = DataStructures.GetMapStringDouble();

        //TODO - convert all the variables to double
        int class_A;
        int class_B;
        double class_A_P;
        double class_B_P;
        double entropy;
        //************
        int class_Ax;
        int class_Bx;
        double class_Ax_P;
        double class_Bx_P;
        double entropyx;
        //************
        double entropy_after_split;
        double InfoGain = 0;

        String feature;
        String values_code;
        m_memo.clear();

        for (Map.Entry<String, int[]> entry : features_DFs.entrySet()) {
            feature = entry.getKey();
            values_code = GetClassesValuesCode(entry.getValue());

            if (m_memo.containsKey(values_code)) {
                InfoGain = m_memo.get(values_code);
            } else {
                //*************************************************
                class_A = (int) entry.getValue()[0];
                class_B = (int) entry.getValue()[1];
                class_A_P = MathCalc.GetPwithLaplaceCorrection(class_A, class_A + class_B, 2);
                class_B_P = MathCalc.GetPwithLaplaceCorrection(class_B, class_A + class_B, 2);
                entropy = Entropy.GetEntropy(new ArrayList<>(Arrays.asList(class_A_P, class_B_P)));
                //*************************************************
                class_Ax = m_Class_A_elements_num - class_A;
                class_Bx = m_Class_B_elements_num - class_B;
                class_Ax_P = MathCalc.GetPwithLaplaceCorrection(class_Ax, class_Ax + class_Bx, 2);
                class_Bx_P = MathCalc.GetPwithLaplaceCorrection(class_Bx, class_Ax + class_Bx, 2);
                entropyx = Entropy.GetEntropy(new ArrayList<>(Arrays.asList(class_Ax_P, class_Bx_P)));
                //*************************************************
                entropy_after_split = (((((double) class_A) + ((double) class_B)) / ((double) m_elements_num)) * entropy)
                        + (((((double) class_Ax) + ((double) class_Bx)) / ((double) m_elements_num)) * entropyx);
                InfoGain = m_Total_entropy - entropy_after_split;
                if (m_GainRatio) {
                    InfoGain = (InfoGain / entropy);
                }
                m_memo.put(values_code, InfoGain);
            }
            features_InfoGain.put(feature, InfoGain);
        }
        return features_InfoGain;
    }

    private String GetClassesValuesCode(int[] values) {
        return values[0] + "," + values[1];
    }

    public String GetName() {
        if (m_GainRatio) {
            return "Information Gain Ratio";
        } else {
            return "Information Gain";
        }
    }

}
