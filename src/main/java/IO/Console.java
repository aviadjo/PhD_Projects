/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

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
    public static void PrintLine(String text) {
        System.err.println(text);
        //System.out.println(text);
    }

    public static void PrintException(String errorText, Exception exception) {
        System.err.println("");
        System.err.println(String.format("Failure:   %s", errorText));
        if (exception != null) {
            System.err.println(String.format("Exception: %s:%s",
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            ));
        }
        System.err.println("");
        //Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
    }

    public static void PrintDebug(String text) {
        System.err.println(text);
    }
}
