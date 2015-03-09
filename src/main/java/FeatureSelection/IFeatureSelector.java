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
     * @param featuresDFs list of features and Document Frequency for each
     * class, A and B
     * @param topFeaturesToSelect the amount of top feature to select
     * @param top_features_percent the percent of top features to select
     * @return ArrayList of features selected from the given features list using
     * some Feature Selection method and their DF
     */
    public ArrayList<Pair<String, Integer>> SelectTopFeatures(Map<String, int[]> featuresDFs, int classAelementsNum, int classBelementsNum,int topFeaturesToSelect, boolean printScores);
    
    public String GetName();
}
