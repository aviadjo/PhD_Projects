/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import Data_Structures.MapDB;
import Feature_Selection.AFeatureSelector;
import Math.Entropy;
import Math.MathCalc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;
import org.mapdb.HTreeMap;
import org.mapdb.Pump;
import org.mapdb.Serializer;

/**
 *
 * @author Aviadjo
 */
public final class FeatureSelectorInfoGainRatio extends AFeatureSelector {

    public final Map<String, Double> m_memoInfoGain = new HashMap<>();

    private final int m_Class_A_elements_num;
    private final int m_Class_B_elements_num;
    private final boolean m_GainRatio;
    private final int m_elements_num;
    private final double m_Class_A_elements_percentage;
    private final double m_Class_B_elements_percentage;
    private final double m_Total_entropy;

    /**
     * Initialize Feature Selection - Information Gain
     *
     * @param Class_A_elements_num the amount of elements (files, etc..) from
     * Class A
     * @param Class_B_elements_num the amount of elements (files, etc..) from
     * Class B
     */
    public FeatureSelectorInfoGainRatio(int Class_A_elements_num, int Class_B_elements_num, boolean GainRatio) {
        m_Class_A_elements_num = Class_A_elements_num;
        m_Class_B_elements_num = Class_B_elements_num;
        m_GainRatio = GainRatio;
        m_elements_num = m_Class_A_elements_num + m_Class_B_elements_num;
        m_Class_A_elements_percentage = (double) m_Class_A_elements_num / (double) m_elements_num;
        m_Class_B_elements_percentage = (double) m_Class_B_elements_num / (double) m_elements_num;
        m_Total_entropy = Entropy.Get_Entropy(new ArrayList<>(Arrays.asList(m_Class_A_elements_percentage, m_Class_B_elements_percentage)));
    }

    /**
     * Select X (or X precent) top features from the given features list
     * according to Information Gain scoring
     *
     * @param features_DFs list of features and Document Frequency for each
     * class, A and B
     * @param top_features_amount_to_select the amount of top features to select
     * @param top_features_percent_to_select the percent of top features to
     * select
     * @return ArrayList of features selected by Information Gain Feature
     * Selection algorithm and their DF
     */
    @Override
    public ArrayList<Pair<String, Integer>> Select_Features(HTreeMap<String, int[]> features_DFs, int top_features_amount_to_select, double top_features_percent_to_select) {
        //File_Writer.Write_To_File(Get_Features_Occurrence_In_Malicious_Benign(features_DFs), String.format("D:\\features_occurences_%s.csv", Get_TimeStamp_String()));
        HTreeMap<String, Double> features_InfoGain = Get_Features_InfoGain(features_DFs);

        //Comparator used for iterator
        Comparator features_comperator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((HTreeMap.Entry<String, Double>) o2).getValue().compareTo(
                        ((HTreeMap.Entry<String, Double>) o1).getValue());
            }
        };

        //Initialize an Iterator over the sorted map
        Iterator iterator_sorted = Pump.sort(features_InfoGain.entrySet().iterator(), false, 1000000000, features_comperator, Serializer.BASIC);

        int amount_to_select = ((top_features_amount_to_select < features_InfoGain.size()) ? top_features_amount_to_select : features_InfoGain.size());
        ArrayList<Pair<String, Integer>> features_Top = new ArrayList<>();
        String feature;
        int feature_DF_total;
        int[] feature_DF_of_Classes;
        HTreeMap.Entry<String, Double> entry;
        for (int i = 0; i < amount_to_select; i++) {
            if (iterator_sorted.hasNext()) {
                entry = (HTreeMap.Entry<String, Double>) iterator_sorted.next();
                feature = entry.getKey();
                Console.Console.Print_To_Console(String.format("Feature f%s (%s) Information-Gain-Ratio: %s", i + 1, feature,entry.getValue()), true, false);
                feature_DF_of_Classes = features_DFs.get(feature);
                feature_DF_total = feature_DF_of_Classes[0] + feature_DF_of_Classes[1]; //To be used for TFIDF calculation.
                features_Top.add(new Pair(feature, feature_DF_total));
            }
        }
        return features_Top;
    }

    public String Get_Features_Occurrence_In_Malicious_Benign(HTreeMap<String, int[]> features_DFs) {
        StringBuilder results = new StringBuilder();
        int[] value;
        String seperator = "|";
        for (Map.Entry<String, int[]> entry : features_DFs.entrySet()) {
            value = entry.getValue();
            results.append(entry.getKey()).append(seperator).append(value[0]).append(seperator).append(value[1]).append("\n");
        }
        return results.toString();
    }

    /**
     * Calculates Information Gain (IG) or Information Gain Ratio(IGR) score for
     * the given feature list
     *
     * @param features_DFs list of features and Document Frequency for each
     * class, A and B
     * @return HTreeMap which contains for every feature it's corresponding
     * Information Gain (IG) or Information Gain Ratio(IGR) score
     */
    public HTreeMap<String, Double> Get_Features_InfoGain(HTreeMap<String, int[]> features_DFs) {
        HTreeMap<String, Double> features_InfoGain = MapDB.Get_HTreeMap_String_Double();

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
        m_memoInfoGain.clear();

        for (Map.Entry<String, int[]> entry : features_DFs.entrySet()) {
            feature = entry.getKey();
            values_code = GetClassesValuesCode(entry.getValue());

            if (m_memoInfoGain.containsKey(values_code)) {
                InfoGain = m_memoInfoGain.get(values_code);
            } else {
                //*************************************************
                class_A = (int) entry.getValue()[0];
                class_B = (int) entry.getValue()[1];
                class_A_P = MathCalc.Get_P_with_Laplace_Correction(class_A, class_A + class_B, 2);
                class_B_P = MathCalc.Get_P_with_Laplace_Correction(class_B, class_A + class_B, 2);
                entropy = Entropy.Get_Entropy(new ArrayList<>(Arrays.asList(class_A_P, class_B_P)));
                //*************************************************
                class_Ax = m_Class_A_elements_num - class_A;
                class_Bx = m_Class_B_elements_num - class_B;
                class_Ax_P = MathCalc.Get_P_with_Laplace_Correction(class_Ax, class_Ax + class_Bx, 2);
                class_Bx_P = MathCalc.Get_P_with_Laplace_Correction(class_Bx, class_Ax + class_Bx, 2);
                entropyx = Entropy.Get_Entropy(new ArrayList<>(Arrays.asList(class_Ax_P, class_Bx_P)));
                //*************************************************
                entropy_after_split = (((((double) class_A) + ((double) class_B)) / ((double) m_elements_num)) * entropy)
                                     +(((((double) class_Ax) + ((double) class_Bx)) / ((double) m_elements_num)) * entropyx);
                InfoGain = m_Total_entropy - entropy_after_split;
                if (m_GainRatio) {
                    InfoGain = (InfoGain / entropy);
                }
                m_memoInfoGain.put(values_code, InfoGain);
            }
            features_InfoGain.put(feature, InfoGain);
        }
        return features_InfoGain;
    }

    private String GetClassesValuesCode(int[] values) {
        return values[0] + "," + values[1];
    }

    public String GetAlgorithmName() {
        if (m_GainRatio) {
            return "Information Gain Ratio";
        } else {
            return "Information Gain";
        }
    }

}
