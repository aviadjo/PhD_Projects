/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.Map;

/**
 *
 * @author Aviad
 */
public interface IDataStructure {

    public Map<String, Integer> GetMapStringInteger();

    public Map<String, int[]> GetMapStringArrayInt();

    public Map<String, Double> GetMapStringDouble();
}
