/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class Directories {

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
                Console.Console.Print_To_Console(String.format("Error creating directory: %s",directoryPath), true, false);
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
            Console.Console.Print_To_Console(String.format("Error deleting directory: %s",directoryPath), true, false);
            return false;
        }
    }
}