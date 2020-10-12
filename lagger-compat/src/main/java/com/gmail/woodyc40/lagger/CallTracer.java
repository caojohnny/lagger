package com.gmail.woodyc40.lagger;

import java.util.logging.Logger;

/**
 * Utility class providing stack tracing capability for
 * a method call.
 */
public final class CallTracer {
    private CallTracer() { // Suppress instantiation
    }

    /**
     * Prints the stacktrace for the current thread to the
     * given logger.
     *
     * @param logger the logger to use for printing the
     *               stracktrace
     */
    public static void trace(Logger logger) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : trace) {
            logger.info(String.format("  @ %s#%s ln %d", element.getClassName(), element.getMethodName(), element.getLineNumber()));
        }
    }
}
