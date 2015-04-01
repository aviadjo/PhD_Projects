/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.io.RandomAccess;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author Aviad
 */
public class iTextPDF {

    public static Map ExtractFeaturesFrequencyFromSingleElement(Object element) {
        Map<String, Integer> structuralPaths = new HashMap<>();
        String filePath = (String) element;

        File pdfFile = new File(filePath);
        try {
            File randomAccessFile = new File("D:\\PDFBoxRandomAccessFile.tmp");
            RandomAccess randomAccess = new RandomAccessFile(randomAccessFile, "");
            PDDocument pdf = PDDocument.loadNonSeq(pdfFile, randomAccess);

        } catch (IOException ex) {
            Console.Console.PrintLine(String.format("Error parsing PDF file: %s", filePath), true, false);
        }
        return structuralPaths;
    }
}
