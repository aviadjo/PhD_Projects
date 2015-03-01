/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import Feature_Extraction.AFeatureExtractor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorDocxStructuralPaths extends AFeatureExtractor {

    @Override
    public Map<String, Integer> Extract_Features_TF_From_Single_Element(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String file_path = (String) element;
        
        return structuralPaths;
    }
    
    public String UnzipFileToTemporaryFolder(String filePath){
        String destinationFolder = "";
        
        return destinationFolder;
    }
    
}
