/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import IO.Directories;
import IO.Serializer;
import java.io.Serializable;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class WekaTrainedClassifier implements Serializable {

    private final long serialVersionUID = 1L;
    private final Classifier m_classifier;
    private final String m_classifierName;
    private final WekaDatasetProperties m_datasetProperties;
    private final String m_ID;

    public WekaTrainedClassifier(Classifier classifier, Instances dataset, WekaDatasetProperties datasetProperties) {
        m_classifier = Weka.TrainClassifier(classifier, dataset);
        m_classifierName = Weka.GetClassifierName(classifier);
        m_datasetProperties = datasetProperties;
        m_ID = SetID();
    }

    public Classifier GetClassifier() {
        return m_classifier;
    }

    public String GetClassifierName() {
        return m_classifierName;
    }

    public WekaDatasetProperties GetDatasetProperties() {
        return m_datasetProperties;
    }

    private String SetID() {
        return String.format("WekaTrainedClassifier(%s)_Files(B%s_M%s)_FE(%s)_FS(%s)_Rep(%s)_Top(%s)",
                GetClassifierName(),
                m_datasetProperties.GetBenignNum(),
                m_datasetProperties.GetMaliciousNum(),
                m_datasetProperties.GetFeatureExtractor().GetName(),
                m_datasetProperties.GetFeatureSelector().GetName(),
                m_datasetProperties.GetFeatureRepresentation().toString(),
                m_datasetProperties.GetTopFeatures());
    }

    public String GetID() {
        return m_ID;
    }

    public String GetClassifierSpecification() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Trained classifier specification").append("\n");
        stringBuilder.append("--------------------------------").append("\n");
        stringBuilder.append(String.format("Classifier: %s", m_classifierName)).append("\n");
        stringBuilder.append(String.format("Benign instances: %s", m_datasetProperties.GetBenignNum())).append("\n");
        stringBuilder.append(String.format("Malicious instances: %s", m_datasetProperties.GetMaliciousNum())).append("\n");
        stringBuilder.append(String.format("Total instances: %s", m_datasetProperties.GetInstancesNum())).append("\n");
        stringBuilder.append(String.format("Feature Extractor: %s", m_datasetProperties.GetFeatureExtractor().GetName())).append("\n");
        stringBuilder.append(String.format("Feature Selector: %s", m_datasetProperties.GetFeatureSelector().GetName())).append("\n");
        stringBuilder.append(String.format("Feature Representation: %s", m_datasetProperties.GetFeatureRepresentation().toString())).append("\n");
        stringBuilder.append(String.format("Top Feature Selection: %s", m_datasetProperties.GetTopFeatures())).append("\n");
        return stringBuilder.toString();
    }

    public String GetClassification(Instance instance) {
        double classIndex = GetClassificationIndex(instance);
        return m_datasetProperties.GetClassValue(classIndex);
    }

    public double GetClassificationIndex(Instance instance) {
        return Weka.GetClassificationIndex(m_classifier, instance);
    }

    public double[] GetDistribution(Instance instance) {
        return Weka.GetDistribution(m_classifier, instance);
    }

    public void SaveToDisk(String destinationFolder) {
        if (Directories.IsDirectory(destinationFolder)) {
            Serializer.Serialize(this, destinationFolder + "\\" + m_ID);
        }
    }

}
