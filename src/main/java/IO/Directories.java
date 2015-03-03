/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Console.Console;
import static Tester.Tester.AddKeyToMap;
import static Tester.Tester.GetXMLStructuralPaths;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class Directories {

    /**
     * Returns true if the given directory path exists
     *
     * @param directoryPath directories path
     * @return true if the given directory path exists
     */
    public static boolean IsDirectory(String directoryPath) {
        File f = new File(directoryPath);
        return (f.exists() && f.isDirectory());
    }

    /**
     * Create directory for the given directory path
     *
     * @param directoryPath path of the destination directory to create
     * @return File which represent the created directory
     */
    public static File CreateDirectory(String directoryPath) {
        File destinationDirectory = new File(directoryPath);
        boolean directoryCreated = false;
        //Create destination folder if not exist
        if (!destinationDirectory.exists()) {
            directoryCreated = destinationDirectory.mkdir();
            if (!directoryCreated) {
                Console.Print_To_Console(String.format("Error creating directory: %s", directoryPath), true, false);
            }
        }
        return destinationDirectory;
    }

    /**
     * Delete directory
     *
     * @param directoryPath path of the destination directory to delete
     * @return true if the directory was deleted
     */
    public static boolean DeleteDirectory(String directoryPath) {
        try {
            FileUtils.deleteDirectory(new File(directoryPath));
            return true;
        } catch (IOException e) {
            Console.Print_To_Console(String.format("Error deleting directory: %s", directoryPath), true, false);
            return false;
        }
    }

    public static ArrayList<String> GetAllDirectoryPaths(String directoryPath) {
        ArrayList<String> directoryPaths = new ArrayList<>();
        try {
            java.nio.file.Files.walk(Paths.get(directoryPath)).forEach(filePath -> {
                directoryPaths.add(filePath.toString());
            });
        } catch (IOException e) {
            Console.Print_To_Console(String.format("Error getting directory paths: %s", directoryPath), true, false);
        }

        return directoryPaths;
    }
}
