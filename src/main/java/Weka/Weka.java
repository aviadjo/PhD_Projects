/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import Console.Console;
import Framework.Framework.Classification;
import Math.MathCalc;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Aviad
 */
public class Weka {

    public enum ClassifierName {

        J48,
        RandomForest,
        NaiveBayes,
        BayesNet,
        Logistic,
        LogitBoost
    }

    /**
     * return Classifier
     *
     * @param classifierName the name of the wanted classifier
     * @return Classifier
     */
    public static Classifier GetClassifier(ClassifierName classifierName) {
        Classifier classifier = null;
        switch (classifierName) {
            case J48:
                classifier = new J48();
                break;
            case RandomForest:
                classifier = new RandomForest();
                break;
            case NaiveBayes:
                classifier = new NaiveBayes();
                break;
            case BayesNet:
                classifier = new BayesNet();
                break;
            case Logistic:
                classifier = new Logistic();
                break;
            case LogitBoost:
                classifier = new LogitBoost();
                break;
        }
        return classifier;
    }

    /**
     * Train the given classifier with the given data
     *
     * @param classifier classifier
     * @param data dataset
     * @return trained classifier
     */
    public static Classifier TrainClassifier(Classifier classifier, Instances data) {
        try {
            Console.PrintLine(String.format("Training classifier %s...", classifier.getClass().getName()), true, false);
            classifier.buildClassifier(data);
            return classifier;
        } catch (Exception ex) {
            Console.PrintLine(String.format("Error training classifier: %s", ex.getMessage()), true, false);
            return null;
        }
    }

    /**
     * return Instances created from the given CSV string
     *
     * @param csvDataset CSV dataset
     * @return Instances created from the given CSV string
     */
    public static Instances GetInstances(String csvDataset) {
        CSVLoader loader = new CSVLoader();
        Instances data = null;
        try {
            loader.setSource(new ByteArrayInputStream(csvDataset.getBytes()));
            data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
        } catch (IOException ex) {
            Console.PrintLine(String.format("Error loading csv to Instances: %s", ex.getMessage()), true, false);
        }
        return data;
    }

    /**
     * return classification for the given instance
     *
     * @param classifier classifier
     * @param instance given test instance
     * @return classification for the given instance
     */
    public static double GetClassification(Classifier classifier, Instance instance) {
        double classification = -1;
        try {
            classification = classifier.classifyInstance(instance);
        } catch (Exception ex) {
            Console.PrintLine("Error classifying instance!", true, false);
        }
        return classification;
    }

    public static String GetClassifierName(Classifier classifier) {
        return classifier.getClass().getSimpleName();
    }

    /**
     * return classification distribution for the given instance
     *
     * @param classifier classifier
     * @param instance given test instance
     * @return classification distribution for the given instance
     */
    public static double[] GetDistribution(Classifier classifier, Instance instance) {
        double[] classification = {0, 0};
        try {
            classification = classifier.distributionForInstance(instance);
            for (int i = 0; i < classification.length; i++) {
                classification[i] = MathCalc.Round(classification[i], 3);
            }
        } catch (Exception ex) {
            Console.PrintLine("Error classifying instance!", true, false);
        }
        return classification;
    }

    /**
     * return string classification
     *
     * @param classificationIndex class index returned by ClassifyInstance()
     * @param trainsetClassAttribute class attribute of the training set
     * @return string classification
     */
    public static Classification ConvertClassIndexToClassification(double classificationIndex, Attribute trainsetClassAttribute) {
        String classificationString = trainsetClassAttribute.value((int) classificationIndex);
        Classification classification = Classification.Unknown;
        if (classificationString.equals(Classification.Benign.toString())) {
            classification = Classification.Benign;
        } else if (classificationString.equals(Classification.Malicious.toString())) {
            classification = Classification.Malicious;
        } else if (classificationString.equals(Classification.Unknown.toString())) {
            classification = Classification.Unknown;
        }
        return classification;
    }

    /**
     * return trained classifier
     *
     * @param trainedClassifierFilePath trained classifier file location on disk
     * @return trained classifier
     */
    private static TrainedClassifier LoadTrainedClassifier(String trainedClassifierFilePath) {
        TrainedClassifier classifier = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(trainedClassifierFilePath);
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                classifier = (TrainedClassifier) objectInputStream.readObject();
            } catch (ClassNotFoundException ex) {
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        return classifier;
    }

    private static void EvaluateClassifier(Classifier classifier, Instances data) {
        Evaluation eval;
        try {
            eval = new Evaluation(data);
            eval.crossValidateModel(classifier, data, 10, new Random(1));
        } catch (Exception ex) {

        }
    }
}
