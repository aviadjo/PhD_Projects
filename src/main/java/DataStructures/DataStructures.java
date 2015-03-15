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
        //return new HashMap<>();
    }

    public static Map<String, int[]> GetMapStringIntArray() {
        return MapDB.GetMapStringIntArray();
        //return new HashMap<>();
    }

    public static Map<String, Double> GetMapStringDouble() {
        return MapDB.GetMapStringDouble();
        //return new HashMap<>();
    }
    
    private static Map GetMap(){
        return new HashMap<>();
    }
}
