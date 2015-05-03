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
     * @param filePath the source file path
     * @return string represents the file's content
     */
    public static String ReadFile(String filePath) {
        byte[] encoded = new byte[0];

        try {
            encoded = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException exception) {
            Console.PrintLine(exception.getMessage(), true, false);
        }
        return new String(encoded, m_encoding).trim();
    }

    /**
     * Read a all the text file from the given folder to list of strings
     *
     * @param folderPath path of a folder
     * @return Arraylist of string contains the text of all files within the
     * given folder
     */
    public static ArrayList<String> ReadTextFilesFromFolder(String folderPath) {
        ArrayList<String> list_of_strings = new ArrayList<>();

        File directory = new File(folderPath);
        if (directory.exists()) {
            File[] directory_files = directory.listFiles();
            String file_text = "";
            if (directory_files != null && directory_files.length > 0) {
                for (File child : directory_files) {
                    file_text = FileReader.ReadFile(child.getPath());
                    if (file_text != "") {
                        list_of_strings.add(file_text);
                    }
                }
            } else {
                Console.PrintLine(String.format("Directory %s do not exist!", folderPath), true, false);
            }
        }
        return list_of_strings;
    }

    /**
     * Read a given file to a List of lines
     *
     * @param file_path the source file path
     * @return List of lines represents the file
     */
    public static List<String> ReadFileLines(String file_path) {
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get(file_path), m_encoding);
        } catch (IOException exception) {
            Console.PrintLine(exception.getMessage(), true, false);
        }

        return lines;
    }

    /**
     * Read a given file
     *
     * @param filePath the source file path
     * @return InputStream represents a file located in the given path
     */
    public static InputStream ReadFileToInputStream(String filePath) {
        InputStream is = null;

        try {
            is = new FileInputStream(filePath);
            is.close();
        } catch (FileNotFoundException exception) {
            Console.PrintLine(exception.getMessage(), true, false);
        } catch (IOException exception) {
            Console.PrintLine(exception.getMessage(), true, false);
        }
        return is;
    }

}
