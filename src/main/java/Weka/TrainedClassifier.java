/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import Framework.Framework.Classification;
import IO.Directories;
import IO.Serializer;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class TrainedClassifier {

    private final Classifier m_classifier;
    private final String m_classifierName;
    private final DatasetProperties m_datasetProperties;
    private final String m_serializationFileName;

    public TrainedClassifier(Classifier classifier, Instances dataset, DatasetProperties datasetProperties) {
        m_classifier = Weka.TrainClassifier(classifier, dataset);
        m_classifierName = Weka.GetClassifierName(classifier);
        m_datasetProperties = datasetProperties;
        m_serializationFileName = "";
    }

    public Classifier GetClassifier() {
        return m_classifier;
    }

    public String GetClassifierName() {
        return m_classifierName;
    }

    public DatasetProperties GetDatasetProperties() {
        return m_datasetProperties;
    }

    public String GetSerializationName() {
        return m_serializationFileName;
    }

    public Classification GetClassification(Instance instance) {
        double classification = Weka.GetClassification(m_classifier, instance);
        return Weka.ConvertClassIndexToClassification(classification, m_datasetProperties.GetClassAttribute());
    }

    public double[] GetDistribution(Instance instance) {
        return Weka.GetDistribution(m_classifier, instance);
    }

    public void SaveToDisk(String destinationFolder) {
        if (Directories.IsDirectory(destinationFolder)) {
            Serializer.Serialize(this, destinationFolder, m_serializationFileName);
        }
    }

}
