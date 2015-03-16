/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import Console.Console;
import static Math.MathCalc.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author Aviad
 */
public class Entropy {
    public static Map<String, Double> m_memoEntropies = new HashMap<>();

    /**
     * Calculates the entropy of the given features values
     *
     * @param values values represent the occurrences number of each class
     * @return the entropy of the given features values
     */
    public static double GetEntropy(ArrayList<Double> values) {
        double entropy = 0.0;
        String values_code = GetEntropyValuesCode(values);

        if (m_memoEntropies.containsKey(values_code)) {
            entropy = m_memoEntropies.get(values_code);
        } else {
            //AreEntropyValuesSumOne(values); //TODO
            for (Double value : values) {
                entropy -= value * Log(value, 2);
            }
            m_memoEntropies.put(values_code, entropy);
        }
        return entropy;
    }

    private static String GetEntropyValuesCode(ArrayList<Double> values) {
        Double a = values.get(0);
        Double b = values.get(1);
        return (a >= b) ? a.toString() + "," + b.toString() + "" : b.toString() + "," + a.toString() + "";
    }

    /**
     * Check if the values for the entropy calculations are summed to 1
     *
     * @param values values represent the occurrences number of each class
     * @return true if the the entropy calculations are summed to 1
     */
    private static boolean AreEntropyValuesSumOne(ArrayList<Double> values) {
        double values_sum = 0;
        for (Double value : values) {
            values_sum += value;
        }

        if (FastMath.round(values_sum) == 1) {
            return true;
        } else {
            Console.PrintLine("Error: Get_Entropy() was provided with value that do not sum to 1!", true, false);
            return false;
        }
    }

}
