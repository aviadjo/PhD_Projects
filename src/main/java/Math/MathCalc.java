/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/**
 *
 * @author Aviad
 */
public class MathCalc {

    /**
     * Return Logarithm of the given number in the given base
     *
     * @param number the number to calculate to Logarithm on
     * @param base base of the Logarithm
     * @return Logarithm of the given number in the given base
     */
    public static double Log(double number, double base) {
        return FastMath.log(base, number);
    }

    /**
     * Return Probability P with Laplace Correction
     *
     * @param part part value
     * @param total total values
     * @param parts_num number of parts
     * @return Probability P with Laplace Correction
     */
    public static double GetPwithLaplaceCorrection(int part, int total, int parts_num) {
        return ((double) part + (double) 1.0) / ((double) total + (double) parts_num);
    }

    /**
     * Return TD*IDF calculation
     *
     * @param numOfElements total number of elements in dataset (Malicious +
     * Benign)
     * @param mostCommonFeatureFrequencyInElement add prefix column identifying
     * the record
     * @param featureFrequencyInElement count the number of times a specific
     * feature appear in element
     * @param numOfElementsContainTheFeature add suffix column identifying the
     * class of the record
     * @return TD*IDF calculation
     */
    public static double GetTFIDF(int featureFrequencyInElement, int mostCommonFeatureFrequencyInElement, int numOfElements, int numOfElementsContainTheFeature) {
        double TF = ((double) featureFrequencyInElement) / ((double) mostCommonFeatureFrequencyInElement);
        double IDF = MathCalc.Log((((double) numOfElements) / ((double) numOfElementsContainTheFeature)), 2);
        return TF * IDF;
    }

    /**
     * Return rounded double number
     *
     * @param num the number to round
     * @param decimalPlaces decimal places to cut the number
     * @return rounded double number
     */
    public static double Round(double num, int decimalPlaces) {
        return Precision.round(num, decimalPlaces);
    }
}
