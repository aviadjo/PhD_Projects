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
public class FileWriter {

    /**
     * Write a given string to file
     *
     * @param text some text
     * @param destination_file_path the path of the destination file
     */
    public static void Write_To_File(String text, String destination_file_path) {
        try {
            FileUtils.writeStringToFile(new File(destination_file_path), text);
        } catch (IOException e) {
            Console.Console.Print_To_Console("Error writing strubg to file: " + e.getMessage(), true, false);
        }
    }
}
