/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import FeatureExtraction.AFeatureExtractor;
import FeatureSelection.AFeatureSelector;
import Framework.Framework.FeatureRepresentation;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.util.Pair;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Aviad
 */
public class WekaDatasetProperties implements Serializable {

    private int m_benignNum;
    private int m_maliciousNum;
    private final int m_instancesNum;
    private final int m_topFeatures;
    //private Map<Double, String> m_classes;
    private String[] m_classes;
    private final ArrayList<Pair<String, Integer>> m_selectedFeatures;
    private final boolean m_hasElementIDAttribute;
    private final boolean m_hasClassificationAttribute;
    private final AFeatureExtractor m_featureExtractor;
    private final AFeatureSelector m_featureSelector;
    private final FeatureRepresentation m_featureRepresentation;

    public WekaDatasetProperties(Instances dataset, ArrayList<Pair<String, Integer>> selectedFeatures, AFeatureExtractor featureExtractor, AFeatureSelector featureSelector, FeatureRepresentation featureRepresentation) {
        SetClasses(dataset);
        SetBenignMaliciousInstancesNum(dataset);
        m_instancesNum = dataset.numInstances();
        m_topFeatures = (dataset.classIndex() > 0) ? dataset.numAttributes() - 1 : dataset.numAttributes();
        m_selectedFeatures = selectedFeatures;
        m_hasElementIDAttribute = !(dataset.attribute(0).name().equals("f1"));
        m_hasClassificationAttribute = (dataset.attribute(dataset.numAttributes() - 1).name().equals("Class"));
        m_featureExtractor = featureExtractor;
        m_featureSelector = featureSelector;
        m_featureRepresentation = featureRepresentation;
    }

    private void SetClasses(Instances dataset) {
        /*m_classes = new HashMap<>();
         for (int i = 0; i < dataset.classAttribute().numValues(); i++) {
         m_classes.put((double) i, dataset.classAttribute().value(i));
         }*/
        m_classes = new String[dataset.classAttribute().numValues()];
        for (int i = 0; i < dataset.classAttribute().numValues(); i++) {
            m_classes[i] = dataset.classAttribute().value(i);
        }
    }

    private void SetBenignMaliciousInstancesNum(Instances dataset) {
        m_benignNum = 0;
        m_maliciousNum = 0;
        double classificationIndex;
        String classification;
        for (Instance instance : dataset) {
            classificationIndex = instance.classValue();
            classification = GetClassValue(classificationIndex);
            switch (classification) {
                case "Benign":
                    m_benignNum++;
                    break;
                case "Malicious":
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

    public int GetInstancesNum() {
        return m_instancesNum;
    }

    public int GetTopFeatures() {
        return m_topFeatures;
    }

    public ArrayList<Pair<String, Integer>> GetSelectedFeatures() {
        return m_selectedFeatures;
    }

    public boolean HasElementIDAttribute() {
        return m_hasElementIDAttribute;
    }

    public boolean HasClassificationAttribute() {
        return m_hasClassificationAttribute;
    }

    public String GetClassValue(double classIndex) {
        return m_classes[(int) classIndex];
        //return m_classes.get(classIndex);
    }

    public AFeatureExtractor GetFeatureExtractor() {
        return m_featureExtractor;
    }

    public AFeatureSelector GetFeatureSelector() {
        return m_featureSelector;
    }

    public FeatureRepresentation GetFeatureRepresentation() {
        return m_featureRepresentation;
    }

}
