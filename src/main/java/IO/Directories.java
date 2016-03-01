/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
                Console.PrintException(String.format("Error creating directory: %s", directoryPath), null);
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
        boolean deleted = false;
        try {
            if (Directories.IsDirectory(directoryPath)) {
                FileUtils.deleteDirectory(new File(directoryPath));
                deleted = true;
            }
        } catch (IOException e) {
            Console.PrintException(String.format("Error deleting directory: %s", directoryPath), e);
        }
        return deleted;
    }

    /**
     * Extract all the paths of the files in given directory and its
     * sub-directories
     *
     * @param directoryPath path of the destination directory to delete
     * @return true if the directory was deleted
     */
    public static ArrayList<String> GetDirectoryFilesPaths(String directoryPath) {
        ArrayList<String> directoryPaths = new ArrayList<>();
        try {
            java.nio.file.Files
                    .walk(Paths.get(directoryPath))
                    .filter(filePath -> Files.IsFile(filePath.toString()))
                    .forEach(filePath -> {
                        directoryPaths.add(filePath.toString());
                    });
        } catch (IOException e) {
            Console.PrintException(String.format("Error getting files' paths from directory: %s", directoryPath), e);
        }
        directoryPaths.remove(directoryPath);
        return directoryPaths;
    }

    /**
     * Return ArrayList<String> filed with the paths of the files in the given
     * folder
     *
     * @param folder_path path of a folder
     * @return ArrayList<String> filed with the paths of the files in the given
     * folder
     */
    private static ArrayList<String> GetFolderFilesPaths(String folderPath) {
        ArrayList<String> filesPaths = new ArrayList<>();
        File directory = new File(folderPath);
        if (directory.exists()) {
            File[] directoryFiles = directory.listFiles();
            for (File file : directoryFiles) {
                filesPaths.add(file.getPath());
            }
        }
        return filesPaths;
    }

    /**
     * Return ArrayList<File> filed with File objects represent the files in the
     * given folder
     *
     * @param folder_path path of a folder
     * @return ArrayList<File> filed with File objects represent the files in
     * the given folder
     */
    public static ArrayList<File> GetFolderFiles(String folderPath) {
        ArrayList<File> filesPaths = new ArrayList<>();
        File directory = new File(folderPath);
        if (directory.exists()) {
            File[] directoryFiles = directory.listFiles();
            filesPaths.addAll(Arrays.asList(directoryFiles));
        }
        return filesPaths;
    }
}
