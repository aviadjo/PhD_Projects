/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatasetCreation;

import Console.Console;
import FeatureExtraction.IFeatureExtractor;
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
    public String BuildDatabaseCSV(ArrayList<T> elements, IFeatureExtractor<T> featureExtractor, ArrayList<Pair<String, Integer>> selectedFeatures, int totalElementsNum, FeatureRepresentation featureRepresentation, Clasification classification, boolean addElementIDColumn, boolean addClassificationColumn) {
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
    public String GetFeaturesVectorCSV(T element, IFeatureExtractor<T> featureExtractor, ArrayList<Pair<String, Integer>> selectedFeatures, int totalElementsNum, FeatureRepresentation featureRepresentation, Clasification classification, boolean addElementIDColumn, boolean addClassificationColumn) {
        Map<String, Integer> elementFeaturesFrequencies = featureExtractor.ExtractFeaturesFrequencyFromSingleElement(element);
        StringBuilder featuresVectorCSV = new StringBuilder();

        if (addElementIDColumn) {
            featuresVectorCSV.append(element.toString()).append(",");
        }

        int mostCommonFeatureFrequencyInElement = GetMostCommonSelectedFeatureFrequencyInElement(elementFeaturesFrequencies, selectedFeatures);

        String selectedFeature;
        int featureFrequencyInElement;
        int numOfElementsContainTheFeature;
        double TFIDF;
        String cellValue = "";
        for (Pair<String, Integer> selectedFeaturePair : selectedFeatures) {
            selectedFeature = selectedFeaturePair.getKey();
            switch (featureRepresentation) {
                case Binary:
                    if (elementFeaturesFrequencies.containsKey(selectedFeature)) {
                        cellValue = 1 + "";
                    } else {
                        cellValue = 0 + "";
                    }
                    break;
                case TFIDF:
                    numOfElementsContainTheFeature = selectedFeaturePair.getValue();
                    featureFrequencyInElement = (elementFeaturesFrequencies.containsKey(selectedFeature)) ? elementFeaturesFrequencies.get(selectedFeature) : 0;
                    TFIDF = MathCalc.GetTFIDF(featureFrequencyInElement, mostCommonFeatureFrequencyInElement, totalElementsNum, numOfElementsContainTheFeature);
                    TFIDF = MathCalc.Round(TFIDF, 3);
                    cellValue = TFIDF + "";
                    break;
            }
            featuresVectorCSV.append(cellValue).append(",");
        }
        if (addClassificationColumn) {
            featuresVectorCSV.append(classification.toString());
        } else {
            //To remove the last feature ","
            featuresVectorCSV = new StringBuilder(featuresVectorCSV.substring(0, featuresVectorCSV.length() - 1));
        }
        return featuresVectorCSV.toString();
    }

    /**
     * Return the frequency of the most common (selected) feature in Element
     *
     * @param elementFeaturesFrequencies features frequencies in element
     * @param selectedFeatures the top selected features to build the dataset
     * with
     * @return the frequency of the most common (selected) feature in Element
     */
    private static int GetMostCommonSelectedFeatureFrequencyInElement(Map<String, Integer> elementFeaturesFrequencies, ArrayList<Pair<String, Integer>> selectedFeatures) {
        //To find the value of the most common feature from the selected features
        int numOfOccurrencesOfMostCommonFeature = 0;
        String selectedFeature;
        int selectedFeatureValue;
        for (Pair<String, Integer> selectedFeaturePair : selectedFeatures) {
            selectedFeature = selectedFeaturePair.getKey();
            if (elementFeaturesFrequencies.containsKey(selectedFeature)) {
                selectedFeatureValue = elementFeaturesFrequencies.get(selectedFeature);
                if (selectedFeatureValue > numOfOccurrencesOfMostCommonFeature) {
                    numOfOccurrencesOfMostCommonFeature = selectedFeatureValue;
                }
            }
        }
        return numOfOccurrencesOfMostCommonFeature;
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
            Console.PrintLine(String.format("Dataset Top %s generated!", top), true, false);
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
            Console.PrintLine(String.format("Requested top %s features out of %s!", topX, originalFeaturesCount), true, false);
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

    /**
     * return CSV string contain list of features and their document frequencies
     *
     * @param featuresDocumentFrequencies features document frequencies selected
     * features file to
     * @return StringBuilder
     */
    public static StringBuilder GetFeaturesDocumentFrequenciesCSV(Map<String, int[]> featuresDocumentFrequencies) {
        String seperator = "|";
        StringBuilder sb = new StringBuilder();
        sb.append("Features").append(seperator).append("Benign").append(seperator).append("Malicious").append("\n");
        int[] value;
        for (Map.Entry<String, int[]> entry : featuresDocumentFrequencies.entrySet()) {
            value = entry.getValue();
            sb.append(entry.getKey()).append(seperator).append(value[0]).append(seperator).append(value[1]).append("\n");
        }
        return sb;
    }

    /**
     * Print CSV string contain list of selected features
     *
     * @param selectedFeatures ArrayList of selected features selected features
     * @return StringBuilder
     */
    public static StringBuilder GetSelectedFeaturesCSV(ArrayList<Pair<String, Integer>> selectedFeatures) {
        StringBuilder sb = new StringBuilder();
        sb.append("#,Feature\n");
        for (int i = 0; i < selectedFeatures.size(); i++) {
            sb.append(String.format("f%s,%s", i + 1, selectedFeatures.get(i).getKey())).append("\n");
        }
        return sb;
    }
}
