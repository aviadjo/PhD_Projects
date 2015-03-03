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
     * @param file_path file's path
     * @return true if the given file path exists
     */
    public static boolean IsFile(String file_path) {
        File f = new File(file_path);
        return (f.exists() && f.isFile() && !f.isDirectory());
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
