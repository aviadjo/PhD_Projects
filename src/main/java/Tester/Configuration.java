/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Aviad
 */
public class Configuration {

    private static void SetConfigurationFile() {
        Properties prop = new Properties();

        OutputStream output;
        try {
            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("BENIGN_FOLDER", "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20");
            prop.setProperty("MALICIOUS_FOLDER", ":\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100");
            prop.setProperty("SELECT_TOP_FEATURES", "500");

            // save properties to project root folder
            prop.store(output, null);
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }

        String folder_Benign = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_20";
        String folder_Malicious = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_100";
    }
}
