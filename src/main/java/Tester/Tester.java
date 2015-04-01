/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Console.Console;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        Map<String, Integer> structuralFeatures = PDFBox_SequentialParser.ExtractFeaturesFrequencyFromSingleElement("D:\\4.pdf");

        int totalStructuralFeatures = 0;

        for (Integer value : structuralFeatures.values()) {
            totalStructuralFeatures += value;
        }

        Console.PrintLine(String.format("Total features: %s", totalStructuralFeatures), true, false);
        Console.PrintLine(String.format("Unique features: %s", structuralFeatures.size()), true, false);
    }

}
