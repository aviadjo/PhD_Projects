/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assistants;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.mapdb.HTreeMap;

/**
 *
 * @author Aviad
 */
public class General {

    /**
     * Throws UnsupportedOperationException
     *
     */
    public static void NotImplementedException() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Current TimeStamp string
     *
     * @return Current TimeStamp string
     */
    public static String GetTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());
    }

    /**
     * return integer number in a string with ',' seperation on thousands
     *
     * @return string which represent the given integer number with ','
     * seperation on thousands
     */
    public static String GetStringNumber(int number) {
        return NumberFormat.getIntegerInstance(/*Locale.US*/).format(number);
    }

    /**
     * return ArrayList of elements generated from the given Array
     *
     * @param <T>
     * @param array an array
     * @return ArrayList of elements generated from the given Array
     */
    public static <T> ArrayList GetArrayList(T[] array) {
        ArrayList<T> arraylist = new ArrayList<>();
        arraylist.addAll(Arrays.asList(array));
        return arraylist;
    }

    /**
     * Returns string contain list of the given features and their Frequencies
     * in classA and classB
     *
     * @param features_DFs list of features and Document Frequency for each
     * class, A and B
     * @return string which contains for every feature it's Frequencies in
     * classA and classB
     */
    public static String GetFeaturesFrequenciesInClassAClassB(HTreeMap<String, int[]> features_DFs) {
        StringBuilder results = new StringBuilder();
        int[] value;
        String seperator = "|";
        for (Map.Entry<String, int[]> entry : features_DFs.entrySet()) {
            value = entry.getValue();
            results.append(entry.getKey()).append(seperator).append(value[0]).append(seperator).append(value[1]).append("\n");
        }
        return results.toString();
    }
}
