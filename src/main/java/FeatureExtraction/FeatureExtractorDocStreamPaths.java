/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import IO.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author Aviad
 */
public class FeatureExtractorDocStreamPaths<T> extends AFeatureExtractor<T> {

    private final long serialVersionUID = 1L;

    @Override
    public Map ExtractFeaturesFrequencyFromSingleElement(T element) {
        Map<String, Integer> streamPaths = new HashMap<>();
        String filePath = (String) element;

        try {
            InputStream inputStream = new FileInputStream(filePath);
            POIFSFileSystem poiFileSystem = new POIFSFileSystem(inputStream);
            DirectoryNode directoryNode = poiFileSystem.getRoot();
            //HWPFDocument document = new HWPFDocument(directoryNode);
            GetStreamsPaths(directoryNode, "", streamPaths);

        } catch (FileNotFoundException ex) {
            Console.PrintException(String.format("Error extracting DOC features from file: %s", filePath), ex);
        } catch (IOException ex) {
            Console.PrintException(String.format("Error extracting DOC features from file: %s", filePath), ex);
        }

        return streamPaths;
    }

    private static void GetStreamsPaths(DirectoryNode dir, String parentPath, Map<String, Integer> streamPaths) {
        // run over all directory chidlrens
        for (Iterator<Entry> entryIter = dir.getEntries(); entryIter.hasNext();) {
            Entry entry = entryIter.next();
            String entryName = entry.getName();

            // Some entry names starts with binary value that are not printable - remove it
            if (entryName.charAt(0) < 10) {
                entryName = entryName.substring(1);
            }

            // Recursively search for directory (storage) children
            if (entry instanceof DirectoryNode) {
                GetStreamsPaths((DirectoryNode) entry, parentPath + "\\" + entryName, streamPaths);
            } else {
                // Add stream path to set of paths
                String filePath = parentPath + "\\" + entryName;
                AddStreamPath(filePath, streamPaths);
            }
        }
    }

    /**
     * Add path to local Map
     *
     * @param key the key to add to the map
     */
    private static void AddStreamPath(String key, Map<String, Integer> structuralPaths) {
        if (!structuralPaths.containsKey(key)) {
            structuralPaths.put(key, 1);
        } else {
            structuralPaths.put(key, structuralPaths.get(key) + 1);
        }
    }

    @Override
    public String GetName() {
        return "Doc Streams";
    }

}
