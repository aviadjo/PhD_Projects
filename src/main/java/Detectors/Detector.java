/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detectors;

import FeatureExtraction.AFeatureExtractor;
import FeatureRepresentation.FeatureRepresentor.FeatureRepresentation;
import FeatureSelection.AFeatureSelector;
import Framework.DBFramework;
import IO.Console;
import IO.FileReader;
import IO.Serializer;
import Weka.Weka;
import static Weka.Weka.GetInstancesFromCSVWithFormat;
import Weka.Weka.WekaClassifier;
import Weka.WekaDatasetProperties;
import Weka.WekaTrainedClassifier;
import java.util.ArrayList;
import javafx.util.Pair;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class Detector {

    public static void CreateAndSaveDetector(
            String traningsetCSVFilePath,
            String selectedFeaturesSerializedFilePath,
            AFeatureExtractor<String> featureExtractor,
            AFeatureSelector featureSelector,
            FeatureRepresentation featureRepresentation,
            WekaClassifier wekaClassifier,
            String description,
            double classificationThreshold,
            String saveToDestinationPath
    ) {
        WekaTrainedClassifier trainedClassifier = GetTrainedClassifier(wekaClassifier, traningsetCSVFilePath, selectedFeaturesSerializedFilePath, featureExtractor, featureSelector, featureRepresentation, description, classificationThreshold);
        trainedClassifier.SaveToDisk(saveToDestinationPath);
    }

    private static WekaTrainedClassifier GetTrainedClassifier(
            WekaClassifier wekaClassifier,
            String traningsetCSVFilePath,
            String selectedFeaturesSerializedFilePath,
            AFeatureExtractor<String> featureExtractor,
            AFeatureSelector featureSelector,
            FeatureRepresentation featureRepresentation,
            String description,
            double classificationThreshold
    ) {
        String csvDataset = FileReader.ReadFile(traningsetCSVFilePath);
        Instances trainset = Weka.GetInstancesFromCSV(csvDataset);
        ArrayList<Pair<String, Integer>> selectedFeatures = (ArrayList<Pair<String, Integer>>) Serializer.Deserialize(selectedFeaturesSerializedFilePath);

        WekaDatasetProperties datasetProperties = new WekaDatasetProperties(
                trainset,
                selectedFeatures,
                featureExtractor,
                featureSelector,
                featureRepresentation
        );

        Classifier classifier = Weka.GetClassifier(wekaClassifier);
        return new WekaTrainedClassifier(classifier, trainset, datasetProperties, description, classificationThreshold);
    }

    public static void ApplyDetectorToTestFolder(String wekaTrainedClassifierFilePath, String testFolder) {
        WekaTrainedClassifier trainedClassifier = (WekaTrainedClassifier) Serializer.Deserialize(wekaTrainedClassifierFilePath);

        //Generate Testset
        int topFeatures = trainedClassifier.GetDatasetProperties().GetTopFeatures();
        int numOfElementsInTrainset = trainedClassifier.GetDatasetProperties().GetInstancesNum();
        AFeatureExtractor featureExtractor = trainedClassifier.GetDatasetProperties().GetFeatureExtractor();
        FeatureRepresentation featureRepresentation = trainedClassifier.GetDatasetProperties().GetFeatureRepresentation();
        ArrayList<Pair<String, Integer>> selectedFeatures = trainedClassifier.GetDatasetProperties().GetSelectedFeatures();
        boolean hasElementIDAttribute = trainedClassifier.GetDatasetProperties().HasElementIDAttribute();
        boolean hasClassificationAttribute = trainedClassifier.GetDatasetProperties().HasClassificationAttribute();
        StringBuilder testsetCSV = DBFramework.GenerateTestSet(testFolder, featureExtractor, selectedFeatures, topFeatures, numOfElementsInTrainset, featureRepresentation, hasElementIDAttribute, hasClassificationAttribute);
        Instances testset = GetInstancesFromCSVWithFormat(testsetCSV.toString());

        //Classify Instances
        ClassifyInstances(trainedClassifier, testset);
    }

    private static void ClassifyInstances(WekaTrainedClassifier trainedClassifier, Instances testset) {
        String classification;
        double classificationIndex;
        double[] classificationDist;
        Instance instance;
        for (int i = 0; i < testset.numInstances(); i++) {
            instance = testset.instance(i);
            //Print classification
            classification = trainedClassifier.GetClassification(instance);
            classificationIndex = trainedClassifier.GetClassificationIndex(instance);
            Console.PrintLine(String.format("Instance %s classification: %s", i + 1, classification));
            //Print classification distribution
            classificationDist = trainedClassifier.GetDistribution(instance);
            Console.PrintLine(String.format("Instance %s distribution: %s , %s", i + 1, classificationDist[0], classificationDist[1]));
            Console.PrintLine("");
        }
    }
}
