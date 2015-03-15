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

    public static Map<String, Integer> GetMapStringInteger() {
        return MapDB.GetMapStringInteger();
        //return GetMap();
    }

    public static Map<String, int[]> GetMapStringIntArray() {
        return MapDB.GetMapStringIntArray();
        //return GetMap();
    }

    public static Map<String, Double> GetMapStringDouble() {
        return MapDB.GetMapStringDouble();
        //return GetMap();
    }
    
    private static Map GetMap(){
        return new HashMap<>();
    }
}
