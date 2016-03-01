/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Assistants.General;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 *
 * @author Aviad
 */
public class Console {

    private static boolean m_outputToFile = true;
    private static final String m_outputLogFileFormat = "Logs\\DatasetBuilderLog_%s.txt";
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
     * Set output to file (true - output, false - do not output)
     *
     * @param bool true to output console to file
     */
    public static void SetOutputToFile(boolean bool) {
        m_outputToFile = bool;
    }

    /**
     * Redirect the console output to file
     *
     */
    public static void RedirectOutputToFile() {
        try {
            System.setOut(new PrintStream(new FileOutputStream(m_outputLogFilePath)));
            System.setErr(new PrintStream(new FileOutputStream(m_outputLogFilePath)));
        } catch (FileNotFoundException ex) {
            Console.PrintException(String.format("Error redirecting output to file: %s", m_outputLogFilePath), ex);
        }
        Console.PrintLine(String.format("Log file is written to: %s", m_outputLogFilePath));
    }

    /**
     * return line separator string
     *
     * @return string which represent line separation
     */
    public static String GetLineSeparator() {
        //return "\r\n";
        return System.getProperty("line.separator");
    }

    /**
     * Print text to console
     *
     * @param text text to print
     */
    public static void PrintLine(String text) {
        System.err.println(text);

        if (m_outputToFile) {
            FileWriter.AppendFile(text + GetLineSeparator(), m_outputLogFilePath);
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
        StringBuilder sb = new StringBuilder();
        sb.append(GetLineSeparator());
        sb.append(String.format("Failure:   %s", errorText));
        if (exception != null) {
            sb.append(GetLineSeparator());
            sb.append(String.format("Exception: %s:%s",
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            ));
            sb.append(GetLineSeparator());
        }
        sb.append(GetLineSeparator());
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
                PrintException(errorText, exception);
            }
        }.start();
    }
}
