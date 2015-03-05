/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assistants;

import java.time.Duration;
import java.time.Instant;

/**
 *
 * @author Aviad
 */
public class StopWatch {

    private static Instant m_start_time;
    private static Instant m_end_time;

    public static void Start() {
        m_start_time = Instant.now();
    }

    public static void Stop() {
        m_end_time = Instant.now();
    }

    public static String GetTimeSecondsString() {
        String durationString = "";
        if (m_start_time != null && m_end_time != null) {
            return Duration.between(m_start_time, m_end_time).toString();
        } else {
            return "Error";
        }
    }

    public long GetTimeMilisecondsSeconds() {
        if (m_start_time != null && m_end_time != null) {
            return (Duration.between(m_start_time, m_end_time).toMillis() / 1000);
        } else {
            return -1;
        }
    }

    public long GetTimeSeconds() {
        if (m_start_time != null && m_end_time != null) {
            return GetTimeMilisecondsSeconds()/1000;
        } else {
            return -1;
        }
    }

}
