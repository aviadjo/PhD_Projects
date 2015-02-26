/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Console.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aviad
 */
public class FileReader {

    private static final Charset m_encoding = Charset.defaultCharset();

    /**
     * Read a given file
     *
     * @param file_path the source file path
     * @return string represents the file's content
     */
    public static String Read_File_To_String(String file_path) {
        byte[] encoded = new byte[0];

        try {
            encoded = Files.readAllBytes(Paths.get(file_path));
        } catch (IOException exception) {
            Console.Print_To_Console(exception.getMessage(), true, false);
        }
        return new String(encoded, m_encoding).trim();
    }

    /**
     * Read a all the text file from the given folder to list of strings
     *
     * @param folder_path path of a folder
     * @return Arraylist of string contains the text of all files within the
     * given folder
     */
    public static ArrayList<String> Read_Text_Files_From_Folder(String folder_path) {
        ArrayList<String> list_of_strings = new ArrayList<>();

        File directory = new File(folder_path);
        if (directory.exists()) {
            File[] directory_files = directory.listFiles();
            String file_text = "";
            if (directory_files != null && directory_files.length > 0) {
                for (File child : directory_files) {
                    file_text = FileReader.Read_File_To_String(child.getPath());
                    if (file_text != "") {
                        list_of_strings.add(file_text);
                    }
                }
            } else {
                Console.Print_To_Console(String.format("Directory %s do not exist!", folder_path), true, false);
            }
        }
        return list_of_strings;
    }

    /**
     * Return ArrayList<File> filed with File objects represent the files in the
     * given folder
     *
     * @param folder_path path of a folder
     * @return ArrayList<File> filed with File objects represent the files in
     * the given folder
     */
    public static ArrayList<File> Get_Files_In_Folder(String folder_path) {
        ArrayList<File> files_in_folder = new ArrayList<>();

        File directory = new File(folder_path);
        if (directory.exists()) {
            File[] directory_files = directory.listFiles();
            files_in_folder.addAll(Arrays.asList(directory_files));
        }

        return files_in_folder;
    }

    /**
     * Return ArrayList<String> filed with the paths of the files in the
     * given folder
     *
     * @param folder_path path of a folder
     * @return ArrayList<String> filed with the paths of the files in the
     * given folder
     */
    public static ArrayList<String> Get_Files_Paths_In_Folder(String folder_path) {
        ArrayList<String> files_paths_in_folder = new ArrayList<>();
        
        File directory = new File(folder_path);
        if (directory.exists()) {
            File[] directory_files = directory.listFiles();
            for (File file : directory_files)
            {
                files_paths_in_folder.add(file.getPath());
            }
        }
        
        return files_paths_in_folder;
    }

    /**
     * Read a given file to a List of lines
     *
     * @param file_path the source file path
     * @return List of lines represents the file
     */
    public static List<String> Read_File_To_List_of_Lines(String file_path) {
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get(file_path), m_encoding);
        } catch (IOException exception) {
            Console.Print_To_Console(exception.getMessage(), true, false);
        }

        return lines;
    }

    /**
     * Read a given file
     *
     * @param file_path the source file path
     * @return InputStream represents a file located in the given path
     */
    public static InputStream Read_File_To_InputStream(String file_path) {
        InputStream is = null;

        try {
            is = new FileInputStream(file_path);
            is.close();
        } catch (FileNotFoundException exception) {
            Console.Print_To_Console(exception.getMessage(), true, false);
        } catch (IOException exception) {
            Console.Print_To_Console(exception.getMessage(), true, false);
        }
        return is;
    }

}
