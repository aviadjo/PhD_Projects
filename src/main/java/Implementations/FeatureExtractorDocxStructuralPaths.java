/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import Console.Console;
import Feature_Extraction.AFeatureExtractor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorDocxStructuralPaths extends AFeatureExtractor {

    @Override
    public Map<String, Integer> Extract_Features_TF_From_Single_Element(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String filePath = (String) element;
        String destinationFolder = FileUtils.getTempDirectoryPath() + "\\" + FilenameUtils.getName(filePath);
        
        if (UnzipFileToFolder(filePath,destinationFolder))
        {
        }
        
        return structuralPaths;
    }
    
    /*public Map<String, Integer> GetFolderPaths(String folder){
        
    }*/
    
    /**
     * Unzip the given file to the given folder
     *
     * @param filePath the full path of the file to unzip
     * @param destinationFolder the folder to unzip the file to
     * @return true if the unzipping process done successfully
     */
    public boolean UnzipFileToFolder(String filePath, String destinationFolder) {
        boolean success = false;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(filePath);
            if (!zipFile.isEncrypted()) {
                zipFile.extractAll(destinationFolder);
                success = true;
            }
            else {
                Console.Print_To_Console(String.format("file '%s' is password protected!", filePath), true, false);
            }
        } catch (ZipException ex) {
            Console.Print_To_Console(String.format("Error unzipping file '%s': %s", filePath, ex.getMessage()), true, false);
        } 
        return success;
    }


}
