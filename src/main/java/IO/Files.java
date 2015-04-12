/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Aviad FilenameUtiles JavaDoc -
 * http://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/FilenameUtils.html
 */
public class Files {

    /**
     * Returns true if the given file path exists
     *
     * @param filePath file's path
     * @return true if the given file path exists
     */
    public static boolean IsFile(String filePath) {
        File f = new File(filePath);
        return (f.exists() && f.isFile() && !f.isDirectory());
    }

    /**
     * Returns true if the given file is successfully deleted
     *
     * @param filePath file's path to delete
     * @return true if the given file is successfully deleted
     */
    public static boolean DeleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * Return the file extension
     *
     * @param filename a file name without the full path
     * @return the file extension
     */
    public static String GetFileExtansion(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    static Object walk(Path get) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
