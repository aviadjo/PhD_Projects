/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import FeatureExtraction.AFeatureExtractor;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorDocxStructuralPathsMemory<T> extends AFeatureExtractor<T> {

    @Override
    public Map<String, Integer> ExtractFeaturesFrequencyFromSingleElement(T element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String GetName() {
        return "Docx Structural Paths In Momory";
    }

}
