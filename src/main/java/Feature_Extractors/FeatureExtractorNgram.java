/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Feature_Extractors;

import Data_Structures.MapDB;
import Feature_Extraction.AFeatureExtractor;
import IO.FileReader;
import java.io.File;
import org.mapdb.HTreeMap;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorNgram<T> extends AFeatureExtractor<T> {

    private final int m_grams; //the gram size to be used
    private final int m_skip; //the skip to be used

    public FeatureExtractorNgram(int gram_size, int skip) {
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
    public HTreeMap<String, Integer> Extract_Features_TF_From_Single_Element(T element) {
        HTreeMap<String, Integer> ngrams = MapDB.Get_HTreeMap_String_Integer();
        
        String file_path = (String)element;
        File file = new File(file_path);

        if (file.exists()) {
            String file_string = FileReader.Read_File_To_String(file_path);
            
            String ngram;
            for (int i = 0; i <= file_string.length() - m_grams; i = i + m_skip) {
                ngram = file_string.substring(i, i + m_grams);
                if (!ngrams.containsKey(ngram)) {
                    ngrams.put(ngram, 1);
                } else {
                    ngrams.put(ngram, ngrams.get(ngram) + 1);
                }
            }
        }

        return ngrams;
    }

}
