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
public class WekaTrainedClassifier {

    private final Classifier m_classifier;
    private final String m_classifierName;
    private final WekaDatasetProperties m_datasetProperties;
    private final String m_serializationFileName;

    public WekaTrainedClassifier(Classifier classifier, Instances dataset, WekaDatasetProperties datasetProperties) {
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

    public WekaDatasetProperties GetDatasetProperties() {
        return m_datasetProperties;
    }

    public String GetSerializationName() {
        return m_serializationFileName;
    }

    public String GetClassifierSpecification() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Trained classifier specification").append("\n");
        stringBuilder.append("--------------------------------").append("\n");
        stringBuilder.append(String.format("Classifier: %s", m_classifierName)).append("\n");
        stringBuilder.append(String.format("training benign: %s", m_datasetProperties.GetBenignNum())).append("\n");
        stringBuilder.append(String.format("training malicious: %s", m_datasetProperties.GetMaliciousNum())).append("\n");
        stringBuilder.append(String.format("Feature extractor: %s", m_datasetProperties.GetFeatureExtractorName())).append("\n");
        stringBuilder.append(String.format("Feature selector: %s", m_datasetProperties.GetFeatureSelectorName())).append("\n");
        stringBuilder.append(String.format("Feature representation: %s", m_datasetProperties.GetFeatureRepresentationName())).append("\n");
        stringBuilder.append(String.format("Top feature selection: %s", m_datasetProperties.GetTopFeatures())).append("\n");
        return stringBuilder.toString();
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
