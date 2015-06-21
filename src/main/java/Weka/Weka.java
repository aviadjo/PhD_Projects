/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import Framework.Framework.Classification;
import IO.Console;
import Math.MathCalc;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Arrays;
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
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Aviad
 */
public class Weka {

    public enum WekaClassifier {

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
    public static Classifier GetClassifier(WekaClassifier classifierName) {
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
            Console.PrintLine(String.format("Training classifier %s...", classifier.getClass().getSimpleName()));
            classifier.buildClassifier(data);
            return classifier;
        } catch (Exception ex) {
            Console.PrintException(String.format("Error training classifier: %s", classifier.getClass().getSimpleName()), ex);
            return null;
        }
    }

    /**
     * return Instances created from the given CSV string
     *
     * @param csvDataset CSV dataset
     * @return Instances created from the given CSV string
     */
    public static Instances GetInstancesFromCSV(String csvDataset) {
        CSVLoader loader = new CSVLoader();
        Instances instances = null;
        try {
            loader.setSource(new ByteArrayInputStream(csvDataset.getBytes()));
            instances = loader.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException ex) {
            Console.PrintException(String.format("Error loading csv to Instances"), ex);
        }
        return instances;
    }

    /**
     * return Instances that contain only the dataset structure (without data)
     *
     * @param numOfFeatures number of features in the dataset
     * @return Instances that contain only the dataset structure (without data)
     */
    public static Instances GetDatasetFormat(int numOfFeatures) {
        FastVector attributes = new FastVector();
        //Add Attributes
        for (int i = 1; i <= numOfFeatures; i++) {
            attributes.addElement(new Attribute("f" + i));
        }
        //Add Class Attribute
        attributes.addElement(new Attribute("Class", Arrays.asList("Malicious", "Benign")));

        Instances dataset = new Instances("Dataset", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        return dataset;
    }

    /**
     * return Instances that contain data from the given CSV and with the
     * correct format of attributes and class
     *
     * @param csvDataset CSV dataset
     * @return Instances that contain data from the given CSV and with the
     * correct format of attributes and class
     */
    public static Instances GetInstancesFromCSVWithFormat(String csvDataset) {
        Instances data = GetInstancesFromCSV(csvDataset);
        Instances format = GetDatasetFormat(data.numAttributes() - 1);
        if (data != null && format != null) {
            format.addAll(data);
        }
        return format;
    }

    /**
     * return Instances created from the given CSV string
     *
     * @param csvDataset CSV dataset
     * @return Instances created from the given CSV string
     */
    public static Instances GetInstancesFromARFF(String arffDataset) {
        Instances instances = null;
        try {
            ArffReader arffReader = new ArffReader(new InputStreamReader(new ByteArrayInputStream(arffDataset.getBytes())));
            instances = arffReader.getData();
        } catch (IOException ex) {
            Console.PrintException(String.format("Error loading arff to Instances"), ex);
        }
        return instances;
    }

    /**
     * return classification for the given instance
     *
     * @param classifier classifier
     * @param instance given test instance
     * @return classification for the given instance
     */
    public static double GetClassificationIndex(Classifier classifier, Instance instance) {
        double classification = -1;
        try {
            classification = classifier.classifyInstance(instance);
        } catch (Exception ex) {
            Console.PrintException("Error providing classification index", ex);
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
        double[] classification = {};
        try {
            classification = classifier.distributionForInstance(instance);
            for (int i = 0; i < classification.length; i++) {
                classification[i] = MathCalc.Round(classification[i], 3);
            }
        } catch (Exception ex) {
            Console.PrintException("Error providing distribution for instance", ex);
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
    private static WekaTrainedClassifier LoadTrainedClassifier(String trainedClassifierFilePath) {
        WekaTrainedClassifier classifier = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(trainedClassifierFilePath);
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                classifier = (WekaTrainedClassifier) objectInputStream.readObject();
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
