/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Aviad
 * @param <T> An element used by the feature extractor
 */
public abstract class AFeatureExtractor<T> implements IFeatureExtractor<T>, Serializable {

    /**
     * Return list of features (and occurrences) extracted from the given source
     *
     * @param element the type of object represent the element that the features
     * should be extracted from
     * @return list of features (and occurrences) extracted from the given
     * source
     */
    @Override
    public abstract Map<String, Integer> ExtractFeaturesFrequencyFromSingleElement(T element);

    /**
     *
     * @return the features extractor name
     */
    @Override
    public abstract String GetName();
}
