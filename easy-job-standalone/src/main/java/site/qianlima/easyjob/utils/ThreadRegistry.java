package site.qianlima.easyjob.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry to track currently running job threads by job key.
 */
public class ThreadRegistry {
    private static final ConcurrentHashMap<String, Thread> RUNNING_THREADS = new ConcurrentHashMap<>();

    public static void register(String jobKey, Thread thread) {
        if (jobKey == null || thread == null) return;
        RUNNING_THREADS.put(jobKey, thread);
    }

    public static void unregister(String jobKey, Thread thread) {
        if (jobKey == null) return;
        if (thread == null) {
            RUNNING_THREADS.remove(jobKey);
        } else {
            RUNNING_THREADS.remove(jobKey, thread);
        }
    }

    public static Thread get(String jobKey) {
        if (jobKey == null) return null;
        return RUNNING_THREADS.get(jobKey);
    }

    public static boolean interrupt(String jobKey) {
        Thread t = get(jobKey);
        if (t == null) return false;
        try {
            t.interrupt();
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }
}
