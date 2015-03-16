/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatasetCreation;

import FeatureExtraction.AFeatureExtractor;
import Framework.Framework.Clasification;
import Framework.Framework.FeatureRepresentation;
import IO.FileWriter;
import Math.MathCalc;
import java.util.ArrayList;
import java.util.Map;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Aviad
 */
public class DatasetCSVBuilder<T> {

    /**
     * Return CSV string which represent the dataset
     *
     * @param elements ArrayList of elements to build dataset from
     * @param featureExtractor a Feature Extractor object
     * @param selectedFeatures the top selected features to build the dataset
     * with
     * @param classification the classification of all the records in the
     * dataset
     * @param addElementIDColumn add prefix column identifying the record
     * @param addClassificationColumn add suffix column identifying the class of
     * the record
     * @return CSV string which represent the dataset
     */
    public String BuildDatabaseCSV(ArrayList<T> elements, AFeatureExtractor<T> featureExtractor, ArrayList<Pair<String, Integer>> selectedFeatures, int totalElementsNum, FeatureRepresentation featureRepresentation, Clasification classification, boolean addElementIDColumn, boolean addClassificationColumn) {
        StringBuilder datasetCSV = new StringBuilder();
        String elementFeaturesVectorCSV;

        for (T element : elements) {
            elementFeaturesVectorCSV = GetFeaturesVectorCSV(element, featureExtractor, selectedFeatures, totalElementsNum, featureRepresentation, classification, addElementIDColumn, addClassificationColumn);
            datasetCSV.append(elementFeaturesVectorCSV);
            datasetCSV.append("\n");
        }
        return datasetCSV.toString().substring(0, datasetCSV.lastIndexOf("\n"));
    }

    /**
     * Return CSV string which represent the element features vector
     *
     * @param element the element to extract the features from
     * @param featureExtractor a Feature Extractor object
     * @param selectedFeatures the top selected features to build the dataset
     * with
     * @param classification the classification of given element dataset
     * @param addElementIDColumn add prefix column identifying the record
     * @param addClassificationColumn add suffix column identifying the class of
     * the record
     * @return CSV string which represent the element features vector
     */
    public String GetFeaturesVectorCSV(T element, AFeatureExtractor<T> featureExtractor, ArrayList<Pair<String, Integer>> selectedFeatures, int totalElementsNum, FeatureRepresentation featureRepresentation, Clasification classification, boolean addElementIDColumn, boolean addClassificationColumn) {
        Map<String, Integer> elementFeaturesTF = featureExtractor.ExtractFeaturesFrequencyFromSingleElement(element);
        StringBuilder featuresVectorCSV = new StringBuilder();

        if (addElementIDColumn) {
            featuresVectorCSV.append(element.toString()).append(",");
        }

        //To find the value of the most common feature from the selected features
        String selectedFeature;
        int selectedFeatureValue;
        int numOfOccurrencesOfMostCommonFeature = 0;
        for (Pair<String, Integer> selectedFeaturePair : selectedFeatures) {
            selectedFeature = selectedFeaturePair.getKey();
            if (elementFeaturesTF.containsKey(selectedFeature)) {
                selectedFeatureValue = elementFeaturesTF.get(selectedFeature);
                if (selectedFeatureValue > numOfOccurrencesOfMostCommonFeature) {
                    numOfOccurrencesOfMostCommonFeature = selectedFeatureValue;
                }
            }
        }

        int featureOccurrencesInElement;
        int numOfElementsContainTheFeature;
        double TFIDF;
        String cellValue = "";
        for (Pair<String, Integer> selectedFeaturePair : selectedFeatures) {
            selectedFeature = selectedFeaturePair.getKey();
            if (featureRepresentation == FeatureRepresentation.Binary) {
                if (elementFeaturesTF.containsKey(selectedFeature)) {
                    cellValue = 1 + ",";
                } else {
                    cellValue = 0 + ",";
                }
            } else if (featureRepresentation == FeatureRepresentation.TFIDF) {
                numOfElementsContainTheFeature = selectedFeaturePair.getValue();
                featureOccurrencesInElement = (elementFeaturesTF.containsKey(selectedFeature)) ? elementFeaturesTF.get(selectedFeature) : 0;
                TFIDF = MathCalc.GetTFIDF(featureOccurrencesInElement, numOfOccurrencesOfMostCommonFeature, totalElementsNum, numOfElementsContainTheFeature);
                cellValue = TFIDF + ",";
            }
            featuresVectorCSV.append(cellValue);
        }
        if (addClassificationColumn) {
            featuresVectorCSV.append(classification);
        } else {
            //To remove the last feature ","
            featuresVectorCSV = new StringBuilder(featuresVectorCSV.substring(0, featuresVectorCSV.length() - 1));
        }
        return featuresVectorCSV.toString();
    }

    /**
     * Return CSV string which represent the header row of the dataset
     *
     * @param selectedFeaturesNum the top selected features number to build the
     * dataset with
     * @param addElementIDColumn add prefix column identifying the record
     * @param addClassificationColumn add suffix column identifying the class of
     * the record
     * @return CSV string which represent the header row of the dataset
     */
    public static String GetDatasetHeaderCSV(int selectedFeaturesNum, boolean addElementIDColumn, boolean addClassificationColumn) {
        StringBuilder datasetHeaderCSV = new StringBuilder();
        if (addElementIDColumn) {
            datasetHeaderCSV.append("Element,");
        }
        for (int i = 1; i <= selectedFeaturesNum; i++) {
            datasetHeaderCSV.append(String.format("f%s,", i));
        }
        if (addClassificationColumn) {
            datasetHeaderCSV.append("Class");
        } else {
            //To remove the last feature ","
            datasetHeaderCSV = new StringBuilder(datasetHeaderCSV.substring(0, datasetHeaderCSV.length() - 1));
        }
        return datasetHeaderCSV.toString();
    }

    /**
     * Generate Top datasets from the given original CSV dataset
     *
     * @param originalCSVDataset the original dataset to extract top X features
     * from
     * @param destinationFolder the destination folder to write the datasets
     * @param tops top X datasets to build
     * @param datasetFilename the dataset filename
     * @param elementIDColumnExist is column identifying the record exists
     * @param classificationColumnExist is column identifying the class of the
     * record exists
     */
    public static void GenerateTopDatasets(String originalCSVDataset, ArrayList<Integer> tops, String destinationFolder, String datasetFilename, boolean elementIDColumnExist, boolean classificationColumnExist) {
        String topDataset;
        String destinationFile;
        char letter = 'a';
        for (Integer top : tops) {
            Console.Console.PrintLine(top + "", true, false);
            destinationFile = String.format("%s_%s_Top(%s).csv", datasetFilename, letter, top);
            topDataset = GetTopXDataset(originalCSVDataset, top, elementIDColumnExist, classificationColumnExist);
            FileWriter.WriteFile(topDataset, destinationFolder + "\\" + destinationFile);
            letter = (char) (((int) letter) + 1);
        }
    }

    /**
     * Return CSV string of the top X features from the given dataset
     *
     * @param originalCSVDataset the original dataset to extract top X features
     * from
     * @param topX top X features to extract
     * @return CSV string of the top X features from the given dataset
     */
    public static String GetTopXDataset(String originalCSVDataset, int topX, boolean elementIDColumnExist, boolean classificationColumnExist) {
        StringBuilder newCSVDatabase = new StringBuilder();

        String[] lines = originalCSVDataset.split("\n");
        int originalFeaturesCount = lines[0].split(",").length + ((elementIDColumnExist) ? -1 : 0) + ((classificationColumnExist) ? -1 : 0);

        if (originalFeaturesCount >= topX) {
            String newLine = "";
            for (String line : lines) {
                if (!line.equals("")) {
                    newLine = GetTopXCSVLine(line, topX, elementIDColumnExist, classificationColumnExist);
                    newCSVDatabase.append(newLine).append("\n");
                }
            }
        } else {
            Console.Console.PrintLine(String.format("Requested top %s features out of %s!", topX, originalFeaturesCount), true, false);
        }

        return newCSVDatabase.toString();
    }

    /**
     * Return CSV line of the top X features from the given dataset line
     *
     * @param csvLine the original dataset CSV line to extract top X features
     * from
     * @param topX top X features to extract
     * @return CSV line of the top X features from the given dataset line
     */
    private static String GetTopXCSVLine(String csvLine, int topX, boolean elementIDColumnExist, boolean classificationColumnExist) {
        int indexOfLastTop = StringUtils.ordinalIndexOf(csvLine, ",", topX + ((elementIDColumnExist) ? 1 : 0));
        String topFeatures = csvLine.substring(0, indexOfLastTop + 1);

        String classColumn = "";
        if (classificationColumnExist) {
            int indexOdfirstClassColumn = csvLine.lastIndexOf(",") + 1;
            classColumn = csvLine.substring(indexOdfirstClassColumn, csvLine.length());
        }
        return topFeatures + classColumn;
    }
}
