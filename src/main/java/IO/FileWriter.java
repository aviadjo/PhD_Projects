/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.io.File;
import java.io.FileNotFoundException;
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
        boolean success = false;
        try {
            File f = new File(file_path);
            is.reset();
            try (FileOutputStream fos = new FileOutputStream(f)) {
                byte[] buf = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buf)) != -1) {
                    fos.write(buf, 0, bytesRead);
                }
            }
            success = true;
            is.reset();
        } catch (FileNotFoundException exception) {
            //Cope with filenames in exotic languages like chinees
            String fileName = Files.GetFileNameWithoutExtension(file_path);
            file_path = file_path.replace(fileName, fileName.hashCode() + "");
            success = SaveInputStreamToFile(is, file_path);
        } catch (IOException exception) {
            Console.PrintException("Error save InputStream to file", exception);
        }
        return success;
    }

    /**
     * Write a given string to file
     *
     * @param text some text
     * @param destinationFilePath the path of the destination file
     */
    public static void WriteFile(String text, String destinationFilePath) {
        try {
            FileUtils.writeStringToFile(new File(destinationFilePath), text);
        } catch (IOException e) {
            Console.PrintException("Error writing string to file", e);
        }
    }

    /**
     * Append a given string to file
     *
     * @param text some text
     * @param destinationFilePath the path of the destination file
     */
    public static void AppendFile(String text, String destinationFilePath) {
        try (java.io.FileWriter fw = new java.io.FileWriter(destinationFilePath, true)) {
            fw.write(text);
        } catch (IOException ex) {
            Console.PrintException(String.format("Error appending text to file: %s", destinationFilePath), ex);
        }
    }

}
