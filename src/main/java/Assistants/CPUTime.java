/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assistants;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.Duration;

/**
 *
 * @author Aviadjo
 */
public class CPUTime {

    private static ThreadMXBean m_managementFactory = ManagementFactory.getThreadMXBean();
    private static long m_CPUTime;

    public static void Start() {
        m_managementFactory.setThreadCpuTimeEnabled(true);
    }

    public static void Stop() {
        m_CPUTime =  m_managementFactory.getCurrentThreadCpuTime();
        m_managementFactory.setThreadCpuTimeEnabled(false);
    }

    public static long GetTimeMiliseconds() {
        return m_CPUTime;
    }

}
