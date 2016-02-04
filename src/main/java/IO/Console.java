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
        //
        //Using Log4j - http://stackoverflow.com/questions/9362574/how-to-write-error-log-or-exception-into-file-in-java
        //Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);

        //StringBuilder sb = new StringBuilder();
        //sb.append("\n");
        //sb.append(String.format("Failure:   %s", errorText));
        //if (exception != null) {
        //    sb.append(String.format("Exception: %s:%s",
        //            exception.getClass().getSimpleName(),
        //            exception.getMessage()
        //    ));
        //}
        //sb.append("\n");
        //System.err.print(sb.toString());
        //
        //if (m_outputToFile){
        //
        //}
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

    public static void PrintDebug(String text) {
        System.err.println(text);
    }
}
