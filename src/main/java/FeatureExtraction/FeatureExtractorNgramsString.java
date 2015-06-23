/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import FeatureExtraction.AFeatureExtractor;
import IO.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorNgramsString<T> extends AFeatureExtractor<T> {

    private final int m_grams; //the gram size to be used    
    private final int m_skip; //the skip to be used

    public FeatureExtractorNgramsString(int gram_size, int skip) {
        m_grams = gram_size;
        m_skip = skip;
    }

    /**
     * Return list of n-grams (and occurrences) extracted from the given source
     *
     * @param element the type of object represent the element that the features
     * should be extracted from
     * @return list of n-grams (and occurrences) extracted from the given source
     */
    @Override
    public Map<String, Integer> ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> ngrams = new HashMap<>();

        String filePath = (String) element;
        File file = new File(filePath);

        if (file.exists()) {
            String fileString = FileReader.ReadFile(filePath);

            String ngram;
            for (int i = 0; i <= fileString.length() - m_grams; i = i + m_skip) {
                ngram = fileString.substring(i, i + m_grams);
                if (!ngrams.containsKey(ngram)) {
                    ngrams.put(ngram, 1);
                } else {
                    ngrams.put(ngram, ngrams.get(ngram) + 1);
                }
            }
        }
        return ngrams;
    }

    @Override
    public String GetName() {
        return String.format("String n-gram (grams=%s skip=%s)", m_grams, m_skip);
    }

}
