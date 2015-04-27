/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import Console.Console;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
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
            classifier.buildClassifier(data);
            return classifier;
        } catch (Exception ex) {
            Console.PrintLine("Error training classifier!", true, false);
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
        try {
            loader.setSource(new ByteArrayInputStream(csvDataset.getBytes()));
            return loader.getDataSet();
        } catch (IOException ex) {
            return null;
        }
    }
}
