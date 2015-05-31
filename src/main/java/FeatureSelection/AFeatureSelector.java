/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureSelection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Aviad
 */
public abstract class AFeatureSelector implements IFeatureSelector, Serializable {

    public final Map<String, Double> m_memo = new HashMap<>();

    /**
     * Return ArrayList of features selected from the given features list using
     * some Feature Selection method
     *
     * @param featuresDFs list of features and Document Frequency for each
     * class, A and B
     * @param classBelementsNum number of element in class B
     * @param classAelementsNum number of element in class A
     * @param topFeaturesToSelect the amount of top features to select
     * @param printScores
     * @return ArrayList of features selected from the given features list using
     * some Feature Selection method and their DF
     */
    @Override
    public abstract ArrayList<Pair<String, Integer>> SelectTopFeatures(Map<String, int[]> featuresDFs, int classAelementsNum, int classBelementsNum, int topFeaturesToSelect, boolean printScores);

    @Override
    public abstract String GetName();
}
