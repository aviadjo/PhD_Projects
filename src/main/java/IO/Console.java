/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Assistants.General;

/**
 *
 * @author Aviad
 */
public class Console {

    private static final boolean m_outputToFile = true;
    private static final String m_outputLogFileFormat = "DatasetBuilderLog_%s.txt";
    private static final String m_outputLogFilePath = GetOutputLogFilePath();

    /**
     * return the output log file path
     *
     * @return string which represent the output log file path
     */
    private static String GetOutputLogFilePath() {
        return String.format(m_outputLogFileFormat, General.GetTimeStamp());
    }

    /**
     * Print text to console
     *
     * @param text text to print
     */
    public static void PrintLine(String text) {
        System.err.println(text);

        if (m_outputToFile) {
            FileWriter.AppendFile("\n" + text, m_outputLogFilePath);
        }
    }

    /**
     * Print error to console
     *
     * @param text text to print
     */
    public static void PrintDebug(String text) {
        System.err.println(text);
    }

    /**
     * Print text to console
     *
     * @param errorText error message as text
     * @param exception an exception object
     */
    public static void PrintException(String errorText, Exception exception) {
        PrintExceptionSB(errorText, exception);
    }

    /**
     * Print text to console
     *
     * @param errorText error message as text
     * @param exception an exception object
     */
    public static void PrintExceptionRegularly(String errorText, Exception exception) {
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

    /**
     * Print text to console
     *
     * @param errorText error message as text
     * @param exception an exception object
     */
    public static void PrintExceptionSB(String errorText, Exception exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(String.format("Failure:   %s", errorText));
        if (exception != null) {
            sb.append("\n");
            sb.append(String.format("Exception: %s:%s",
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            ));
        }
        sb.append("\n\n");
        System.err.print(sb.toString());

        if (m_outputToFile) {
            FileWriter.AppendFile(sb.toString(), m_outputLogFilePath);
        }
    }

    /**
     * Print exception to console (by new thread
     *
     * @param errorText The text of the error
     * @param exception Exception object
     */
    private static void PrintExceptionInThread(String errorText, Exception exception) {
        new Thread() {
            @Override
            public void run() {
                PrintExceptionRegularly(errorText, exception);
            }
        }.start();
    }
}
