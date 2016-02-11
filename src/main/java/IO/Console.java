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

    private static boolean m_outputToFile = false;

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
        PrintExceptionNormally(errorText, exception);
    }

    public static void PrintExceptionNormally(String errorText, Exception exception) {
        System.err.println("");
        System.err.println(String.format("Failure:   %s", errorText));
        if (exception != null) {
            System.err.println(String.format("Exception: %s:%s",
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            ));
        }
        System.err.println("");
    }

    public static void PrintExceptionSB(String errorText, Exception exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(String.format("Failure:   %s", errorText));
        if (exception != null) {
            sb.append(String.format("Exception: %s:%s",
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            ));
        }
        sb.append("\n");
        System.err.print(sb.toString());

        if (m_outputToFile) {

        }
    }

    public static void PrintDebug(String text) {
        System.err.println(text);
    }
}
