/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aviad
 */
public class DataStructures {

    public static Map<String, Integer> GetHTreeMapStringInteger() {
        //return MapDB.GetHTreeMapStringInteger();
        return new HashMap<String,Integer>();
    }

    public static Map<String, int[]> GetHTreeMapStringArrayInt() {
        //return MapDB.GetHTreeMapStringArrayInt();
        return new HashMap<String,int[]>();
    }

    public static Map<String, Double> GetHTreeMapStringDouble() {
        //return MapDB.GetHTreeMapStringDouble();
        return new HashMap<String,Double>();
    }
}
