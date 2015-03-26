/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import FeatureExtraction.AFeatureExtractor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorPDFStructuralPaths<T> extends AFeatureExtractor<T> {

    private Map<String, Integer> m_structuralPaths = new HashMap<>();

    @Override
    public Map ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> structuralPaths = new HashMap<>();

        return structuralPaths;
    }

    public void AddObjectStructuralPath(String parentObjectPath) {

    }

    @Override
    public String GetName() {
        return "PDF Structural Paths";
    }

}
