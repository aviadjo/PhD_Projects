/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Console.Console;
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
                Console.PrintLine(String.format("Error creating directory: %s", directoryPath), true, false);
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
            Console.PrintLine(String.format("Error deleting directory: %s", directoryPath), true, false);
            return false;
        }
    }

    /**
     * Extract all the paths of the files and sub-folders of the given directory
     *
     * @param directoryPath path of the destination directory to delete
     * @return true if the directory was deleted
     */
    public static ArrayList<String> GetDirectoryFilesPaths(String directoryPath) {
        ArrayList<String> directoryPaths = new ArrayList<>();
        try {
            java.nio.file.Files.walk(Paths.get(directoryPath)).forEach(filePath -> {
                directoryPaths.add(filePath.toString());
            });
        } catch (IOException e) {
            Console.PrintLine(String.format("Error getting directory paths: %s", directoryPath), true, false);
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
    private static ArrayList<String> GetFolderFilesPaths(String folder_path) {
        ArrayList<String> files_paths_in_folder = new ArrayList<>();

        File directory = new File(folder_path);
        if (directory.exists()) {
            File[] directory_files = directory.listFiles();
            for (File file : directory_files) {
                files_paths_in_folder.add(file.getPath());
            }
        }

        return files_paths_in_folder;
    }

    /**
     * Return ArrayList<File> filed with File objects represent the files in the
     * given folder
     *
     * @param folder_path path of a folder
     * @return ArrayList<File> filed with File objects represent the files in
     * the given folder
     */
    public static ArrayList<File> GetFolderFiles(String folder_path) {
        ArrayList<File> files_in_folder = new ArrayList<>();

        File directory = new File(folder_path);
        if (directory.exists()) {
            File[] directory_files = directory.listFiles();
            files_in_folder.addAll(Arrays.asList(directory_files));
        }

        return files_in_folder;
    }

}
