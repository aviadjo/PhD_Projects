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

/**
 *
 * @author Aviad
 */
public class Serializer {

    private static final String m_serializedFileExtension = "ser";

    public static void Serialize(Object object, String destinationFolder, String destinationFilename) {
        if (Directories.IsDirectory(destinationFolder)) {
            String serializedFilename = String.format("%s\\%s.%s", destinationFolder, destinationFilename, m_serializedFileExtension);

            try (FileOutputStream fos = new FileOutputStream(serializedFilename); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(object);
            } catch (IOException ex) {
                Console.PrintLine(String.format("Error serilizing object '%s': %s", destinationFilename, ex.getMessage()), true, false);
            }
        }
    }

    public static Object Deserialize(String sourceFolder, String objectFilename, boolean deleteSerialized) {
        Object serialized = null;
        if (Directories.IsDirectory(sourceFolder)) {
            String serializedFilename = String.format("%s\\%s.%s", sourceFolder, objectFilename, m_serializedFileExtension);
            if (Files.IsFile(serializedFilename)) {
                try (FileInputStream fin = new FileInputStream(serializedFilename); ObjectInputStream ois = new ObjectInputStream(fin)) {
                    serialized = ois.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    Console.PrintLine(String.format("Error deserilizing object '%s': %s", objectFilename, ex.getMessage()), true, false);
                }
            }
        }
        return serialized;
    }
}
