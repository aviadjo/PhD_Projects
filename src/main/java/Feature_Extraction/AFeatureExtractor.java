/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Feature_Extraction;

import java.util.Map;

/**
 *
 * @author Aviad
 */
public abstract class AFeatureExtractor<T> implements IFeatureExtractor<T> {

    /**
     * Return list of features (and occurrences) extracted from the given source
     *
     * @param element the type of object represent the element that the features
     * should be extracted from
     * @return list of features (and occurrences) extracted from the given
     * source
     */
    @Override
    public abstract Map<String, Integer> Extract_Features_TF_From_Single_Element(T element);

}
