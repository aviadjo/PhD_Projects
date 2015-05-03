/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tester;

import Console.Console;
import FeatureExtraction.AFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import Framework.Framework;
import Framework.Framework.FeatureRepresentation;
import IO.FileReader;
import IO.Serializer;
import Implementations.FeatureExtractorDocxStructuralPaths;
import Implementations.FeatureExtractorPDFStructuralPaths;
import Implementations.FeatureSelectorInfoGainRatio;
import Implementations.FeatureSelectorInfoGainRatio.SelectionMethod;
import Tester.FeatureExtractorPDFStructuralPathsTEST.ParserType;
import Weka.DatasetProperties;
import Weka.TrainedClassifier;
import Weka.Weka;
import Weka.Weka.ClassifierName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class Tester {

    public static void main(String[] args) {
        //TestExtractPDFStructuralFeatures();
        //GeneratePDFDatasets();
        //GenerateDocxDatasets();
        //TestCode();
        //TestSerilizer();
        TestWekaClassification();
    }

    private static void GeneratePDFDatasets() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassA";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\PDF_ClassB";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        AFeatureExtractor<String> featureExtractor = new FeatureExtractorPDFStructuralPaths(FeatureExtractorPDFStructuralPaths.ParserType.Sequential);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        Framework.FeatureRepresentation featureRepresentation = Framework.FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = Framework.GenerateTrainSet(
                benignFolder,
                maliciousFolder,
                destinationFolder,
                featureExtractor,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                printFileFeaturesFrequencies,
                printSelectedFeaturesScore,
                generateTops,
                tops);
    }

    private static void GenerateDocxDatasets() {
        String benignFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassA_100";
        String maliciousFolder = "D:\\Dropbox\\TESTS\\FeatureExtractionData\\DocX_ClassB_20";
        String destinationFolder = "D:\\Dropbox\\DATASETS";
        ArrayList<Integer> tops = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000));

        AFeatureExtractor<String> featureExtractor = new FeatureExtractorDocxStructuralPaths<>();
        //AFeatureExtractor<String> featureExtractor = new FeatureExtractorNgramsString<>(3, 1);
        AFeatureSelector featureSelector = new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain);
        int topFeatures = 2000;
        Framework.FeatureRepresentation featureRepresentation = Framework.FeatureRepresentation.Binary;
        boolean createDatabaseCSV = true;
        boolean addElementIDColumn = false;
        boolean addClassificationColumn = true;
        boolean printFileFeaturesFrequencies = false;
        boolean printSelectedFeaturesScore = true;
        boolean generateTops = true;

        StringBuilder datasetCSV = Framework.GenerateTrainSet(
                benignFolder,
                maliciousFolder,
                destinationFolder,
                featureExtractor,
                featureSelector,
                topFeatures,
                featureRepresentation,
                createDatabaseCSV,
                addElementIDColumn,
                addClassificationColumn,
                printFileFeaturesFrequencies,
                printSelectedFeaturesScore,
                generateTops,
                tops);
    }

    private static void TestExtractPDFStructuralFeatures() {
        FeatureExtractorPDFStructuralPathsTEST featureExtractor = new FeatureExtractorPDFStructuralPathsTEST(ParserType.Sequential);
        Map<String, Integer> structuralFeatures = featureExtractor.ExtractFeaturesFrequencyFromSingleElement("D:\\3.pdf");

        int totalStructuralFeatures = 0;

        for (Integer value : structuralFeatures.values()) {
            totalStructuralFeatures += value;
        }

        Console.PrintLine(String.format("Total features: %s", totalStructuralFeatures), true, false);
        Console.PrintLine(String.format("Unique features: %s", structuralFeatures.size()), true, false);
    }

    private static void TestCode() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000000; i++) {
            list.add(i);
        }

        list.stream().map(x -> x * 2);

        String a = "";
    }

    private static void TestSerilizer() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }

        Serializer.Serialize(list, "D:\\", "serializerTemp");

        ArrayList<Integer> deserializedList = (ArrayList<Integer>) Serializer.Deserialize("D:\\", "serializerTemp", true);
        Console.PrintLine("ArrayListCount: " + deserializedList.size(), true, false);
    }

    private static void TestClassifierSerialization() {

    }

    private static void TestWekaClassification() {
        String csvDataset = FileReader.ReadFile("D:\\String n-gram (String grams=3 skip=1))_FS(Information Gain)_Rep(Binary)_j_Top(100).csv");
        Instances data = Weka.GetInstances(csvDataset);

        DatasetProperties dspr = new DatasetProperties(data,
                new FeatureExtractorDocxStructuralPaths(),
                new FeatureSelectorInfoGainRatio(SelectionMethod.InformationGain),
                FeatureRepresentation.Binary);

        Classifier classifier = Weka.GetClassifier(ClassifierName.J48);
        TrainedClassifier trainedClassifier = new TrainedClassifier(classifier, data, dspr);

        trainedClassifier.GetDatasetProperties().GetClassAttribute()

        ArrayList<Object> classifications = Collections.list(data.classAttribute().enumerateValues());

        String classification;
        double[] classificationDist;
        Instance instance;
        for (int i = 0; i < data.numInstances(); i++) {
            instance = data.instance(i);
            classification = trainedClassifier.GetClassification(instance).toString();
            classificationDist = trainedClassifier.GetDistribution(instance);
            Console.PrintLine(String.format("Instance %s classification: %s", i, classification), true, false);
            Console.PrintLine(String.format("Instance %s distribution: %s , %s", i, classificationDist[0], classificationDist[1]), true, false);
            Console.PrintLine("", true, false);
        }
    }
}
