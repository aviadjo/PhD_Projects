/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureSelection;

import java.util.ArrayList;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Aviad
 */
public interface IFeatureSelector {

    /**
     * Return ArrayList of features selected from the given features list using
     * some Feature Selection method
     *
     * @param features_DFs list of features and Document Frequency for each
     * class, A and B
     * @param top_features_amount the amount of top feature to select
     * @param top_features_percent the percent of top features to select
     * @return ArrayList of features selected from the given features list using
     * some Feature Selection method and their DF
     */
    public ArrayList<Pair<String, Integer>> SelectTopFeatures(Map<String, int[]> features_DFs, int top_features_amount, double top_features_percent, boolean printScores);
    
    public String GetName();
}
