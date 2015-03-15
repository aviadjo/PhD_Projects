/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Console;

import Tester.Tester;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aviad
 */
public class Console {

    /**
     * Print text to console
     *
     * @param text text to print
     * @param error is error message
     * @param debug is debug mode
     */
    public static void Print(String text, boolean error, boolean debug) {
        if (debug) {

        }
        if (error) {
            System.err.println(text);
            //Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } else {
            System.out.println(text);
        }
    }

    public static void PrintDebug(String text) {
        System.err.println(text);
    }
}
