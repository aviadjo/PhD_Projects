/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implementations;

import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class FeatureSelectionComperator implements Comparator<Map.Entry<String, Double>> {

    @Override
    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
        int comp = o2.getValue().compareTo(o1.getValue());
        if (comp != 0) {
            return comp;
        } else {
            //When Score is equal, compare by feature name
            return o1.getKey().compareToIgnoreCase(o2.getKey());
        }
        //return o2.getValue().compareTo(o1.getValue());
    }

}
