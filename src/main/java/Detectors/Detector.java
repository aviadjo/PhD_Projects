/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detectors;

import Console.Console;
import Framework.Framework;
import IO.FileReader;
import Implementations.FeatureExtractorDocxStructuralPaths;
import Implementations.FeatureExtractorDocxStructuralPathsEnhanced;
import Implementations.FeatureSelectorInfoGainRatio;
import Weka.Weka;
import Weka.WekaDatasetProperties;
import Weka.WekaTrainedClassifier;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class Detector {

    public static void ApplyDetectorToTestSet() {
        WekaTrainedClassifier classifer = GetTrainedClassifier();
        Instances testset = Weka.GetInstances(GetTestset());
        ClassifyInstances(classifer, testset);
    }

    private static WekaTrainedClassifier GetTrainedClassifier() {
        String csvDataset = FileReader.ReadFile("D:\\DATASET_2015.05.03_10.23.58_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_j_Top(100).csv");
        Instances data = Weka.GetInstances(csvDataset);

        WekaDatasetProperties datasetProperties = new WekaDatasetProperties(
                data,
                new FeatureExtractorDocxStructuralPathsEnhanced(),
                new FeatureSelectorInfoGainRatio(FeatureSelectorInfoGainRatio.SelectionMethod.InformationGain),
                Framework.FeatureRepresentation.Binary
        );

        Classifier classifier = Weka.GetClassifier(Weka.ClassifierName.J48);
        return new WekaTrainedClassifier(classifier, data, datasetProperties);
    }

    private static String GetTestset() {
        StringBuilder testsetCSV = Framework.GenerateTestSet(
                "D:\\TEST\\DocX_ClassA_20",
                new FeatureExtractorDocxStructuralPaths<String>(),
                "D:\\DATASET_2015.05.03_10.23.58_Files(B16108_M323)_FE(Docx Structural Paths)_FS(Information Gain)_Rep(Binary)_a_FeaturesList.ser",
                100,
                16431,
                Framework.FeatureRepresentation.Binary,
                false,
                true
        );
        return testsetCSV.toString();
    }

    private static void ClassifyInstances(WekaTrainedClassifier trainedClassifier, Instances testset) {
        String classification;
        double[] classificationDist;
        Instance instance;
        for (int i = 0; i < testset.numInstances(); i++) {
            instance = testset.instance(i);
            //Print classification
            classification = trainedClassifier.GetClassification(instance).toString();
            Console.PrintLine(String.format("Instance %s classification: %s", i, classification), true, false);
            //Print classification distribution
            //classificationDist = trainedClassifier.GetDistribution(instance);
            //Console.PrintLine(String.format("Instance %s distribution: %s , %s", i, classificationDist[0], classificationDist[1]), true, false);
            Console.PrintLine("", true, false);
        }
    }
}
