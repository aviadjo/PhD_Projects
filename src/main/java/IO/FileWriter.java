/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class FileWriter {

    /**
     * Write a given InputStream to file
     *
     * @param is InputStream represent a file
     * @param file_path the destination file path
     * @return true if the file was successfully written to the disk
     */
    public static boolean SaveInputStreamToFile(InputStream is, String file_path) {
        try {
            File f = new File(file_path);
            try (FileOutputStream fos = new FileOutputStream(f)) {
                byte[] buf = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buf)) != -1) {
                    fos.write(buf, 0, bytesRead);
                }
            }
            return true;
        } catch (IOException exception) {
            Console.PrintException("Error save InputStream to file!", exception);
        }
        return false;
    }

    /**
     * Write a given string to file
     *
     * @param text some text
     * @param destination_file_path the path of the destination file
     */
    public static void WriteFile(String text, String destination_file_path) {
        try {
            FileUtils.writeStringToFile(new File(destination_file_path), text);
        } catch (IOException e) {
            Console.PrintException("Error writing string to file!", e);
        }
    }
}
