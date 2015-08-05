/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assistants;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 *
 * @author Aviadjo
 */
public class CPUTime {

    private static ThreadMXBean m_managementFactory = ManagementFactory.getThreadMXBean();
    private static long m_CPUTime;

    /**
     * Start measuring CPU Time
     *
     */
    public static void Start() {
        m_managementFactory.setThreadCpuTimeEnabled(true);
    }

    /**
     * Stop measuring CPU Time
     *
     */
    public static void Stop() {
        m_CPUTime = m_managementFactory.getCurrentThreadCpuTime();
        m_managementFactory.setThreadCpuTimeEnabled(false);
    }

    /**
     * return the measured CPU time in miliseconds (long)
     *
     * @return the measured CPU time in miliseconds (long)
     */
    public static long GetTimeMiliseconds() {
        return m_CPUTime;
    }

}
