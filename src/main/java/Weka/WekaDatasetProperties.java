/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import FeatureExtraction.AFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import Framework.Framework.Classification;
import Framework.Framework.FeatureRepresentation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class WekaDatasetProperties {

    private int m_benignNum;
    private int m_maliciousNum;
    private final int m_topFeatures;
    private final Attribute m_classAttribute;
    private final String m_featureExtractorName;
    private final String m_featureSelectorName;
    private final String m_featureRepresentationName;

    public WekaDatasetProperties(Instances dataset, AFeatureExtractor featureExtractor, AFeatureSelector featureSelector, FeatureRepresentation featureRepresentation) {
        m_topFeatures = dataset.numAttributes();
        m_classAttribute = dataset.classAttribute();
        m_featureExtractorName = featureExtractor.GetName();
        m_featureSelectorName = featureSelector.GetName();
        m_featureRepresentationName = featureRepresentation.toString();
        SetBenignMaliciousInstancesNum(dataset);
    }

    private void SetBenignMaliciousInstancesNum(Instances dataset) {
        m_benignNum = 0;
        m_maliciousNum = 0;
        double classificationValue;
        Classification classification;
        for (Instance instance : dataset) {
            classificationValue = instance.classValue();
            classification = Weka.ConvertClassIndexToClassification(classificationValue, m_classAttribute);
            switch (classification) {
                case Benign:
                    m_benignNum++;
                    break;
                case Malicious:
                    m_maliciousNum++;
                    break;
            }
        }
    }

    public int GetBenignNum() {
        return m_benignNum;
    }

    public int GetMaliciousNum() {
        return m_maliciousNum;
    }

    public int GetTopFeatures() {
        return m_topFeatures;
    }

    public Attribute GetClassAttribute() {
        return m_classAttribute;
    }

    public String GetFeatureExtractorName() {
        return m_featureExtractorName;
    }

    public String GetFeatureSelectorName() {
        return m_featureSelectorName;
    }

    public String GetFeatureRepresentationName() {
        return m_featureRepresentationName;
    }

}
