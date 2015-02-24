/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Console;

/**
 *
 * @author Aviad
 */
public class Console {

    public static void Print_To_Console(String text, boolean error, boolean debug) {
        if (debug) {

        }
        if (error) {
            System.err.println(text);   
        } else {
            System.out.println(text);
        }
    }

    public static void Print_To_Debugger_Console(String text) {
        System.err.println(text);
    }
}
