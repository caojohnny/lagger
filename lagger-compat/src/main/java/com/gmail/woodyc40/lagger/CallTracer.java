package com.gmail.woodyc40.lagger;

import java.util.logging.Logger;

public final class CallTracer {
    private CallTracer() { // Suppress instantiation
    }

    public static void trace(Logger logger) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : trace) {
            logger.info(String.format("  @ %s#%s ln %d", element.getClassName(), element.getMethodName(), element.getLineNumber()));
        }
    }
}
