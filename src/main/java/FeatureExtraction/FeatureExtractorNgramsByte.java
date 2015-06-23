/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import IO.Console;
import FeatureExtraction.AFeatureExtractor;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorNgramsByte<T> extends AFeatureExtractor<T> {

    private final int m_grams; //the gram size to be used    
    private final int m_skip; //the skip to be used

    public FeatureExtractorNgramsByte(int gram_size, int skip) {
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
            try {
                byte[] fileBytes = FileUtils.readFileToByteArray(file);
                String ngram = "";
                for (int i = 0; i <= fileBytes.length - m_grams; i = i + m_skip) {
                    for (int j = 0; j < m_grams; j++) {
                        ngram += (char) fileBytes[i + j];
                    }
                    if (!ngrams.containsKey(ngram)) {
                        ngrams.put(ngram, 1);
                    } else {
                        ngrams.put(ngram, ngrams.get(ngram) + 1);
                    }
                    ngram = "";
                }
            } catch (IOException e) {
                Console.PrintException(String.format("Error extracting Byte n-grams features from file: %s", filePath), e);
            }
        }
        return ngrams;
    }

    @Override
    public String GetName() {
        return String.format("Byte n-gram (grams=%s skip=%s)", m_grams, m_skip);
    }

}
