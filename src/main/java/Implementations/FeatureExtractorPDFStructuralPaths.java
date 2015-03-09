/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import FeatureExtraction.AFeatureExtractor;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.*;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorPDFStructuralPaths extends AFeatureExtractor {

    private Map<String, Integer> m_structuralPaths = new HashMap<>();

    @Override
    public Map ExtractFeaturesFrequencyFromSingleElement(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        
        return structuralPaths;
    }
    
    public void AddObjectStructuralPath(String parentObjectPath){
        
    }

    @Override
    public String GetName() {
        return "PDF Structural Paths";
    }

}
