/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Console.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviad
 */
public class serializer {

    private static final String m_systemTempDirectory = FileUtils.getTempDirectoryPath();
    private static final String m_serializedFileExtension = "ser";

    public static void Serialize(Object object, String destinationFilename) {
        String serializedFilename = String.format("%s\\%s.%s", m_systemTempDirectory, destinationFilename, m_serializedFileExtension);

        try (FileOutputStream fos = new FileOutputStream(serializedFilename)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error serilizing object '%s': %s", destinationFilename, ex.getMessage()), true, false);
        }
    }

    public static Object Deserialize(String objectFilename, boolean deleteSerialized) {
        String serializedFilename = String.format("%s\\%s.%s", m_systemTempDirectory, objectFilename, m_serializedFileExtension);
        Object serialized = null;

        if (Files.IsFile(serializedFilename)) {
            try (FileInputStream fin = new FileInputStream(serializedFilename)) {
                ObjectInputStream ois = new ObjectInputStream(fin);
                serialized = ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException ex) {
                Console.PrintLine(String.format("Error deserilizing object '%s': %s", objectFilename, ex.getMessage()), true, false);
            }
        }

        return serialized;
    }
}
