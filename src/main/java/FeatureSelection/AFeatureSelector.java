/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Aviad
 */
public abstract class AFeatureSelector implements IFeatureSelector {

    public final Map<String, Double> m_memo = new HashMap<>();

    /**
     * Return ArrayList of features selected from the given features list using
     * some Feature Selection method
     *
     * @param featuresDFs list of features and Document Frequency for each
     * class, A and B
     * @param topFeatures_amount the amount of top feature to select
     * @param top_features_percent the percent of top features to select
     * @return ArrayList of features selected from the given features list using
     * some Feature Selection method and their DF
     */
    public abstract ArrayList<Pair<String, Integer>> SelectTopFeatures(Map<String, int[]> featuresDFs, int classBelementsNum, int topFeaturesToSelect, int classAelementsNum, boolean printScores);

    public abstract String GetName();
}
