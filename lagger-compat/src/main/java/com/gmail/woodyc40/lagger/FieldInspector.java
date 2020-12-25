package com.gmail.woodyc40.lagger;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Utility class that provides debugging support for the
 * field values of object instances.
 */
public class FieldInspector {
    /**
     * The instance of the plugin to use for logging.
     */
    private final JavaPlugin plugin;
    /**
     * A collection of field inspectors that print the
     * contents of the given field type.
     */
    private final List<Consumer<Object>> fieldInfoListeners =
            new ArrayList<>();

    /**
     * Creates the field inspector using the logger for the
     * given plugin.
     *
     * @param plugin the plugin whose logger will be used
     *               to debug field values
     */
    public FieldInspector(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Adds a handler to for a field instance that should
     * be handled uniquely.
     *
     * @param listener the listener to inject into the
     *                 {@link #dump(Object)} method
     */
    public void addFieldListener(Consumer<Object> listener) {
        this.fieldInfoListeners.add(listener);
    }

    /**
     * Obtains the plugin whose logger is being used to
     * debug field information.
     *
     * @return the plugin instance
     */
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * Debugs the fields of the given object.
     *
     * @param object the object whose fields to print to
     *               the plugin logger
     */
    public void dump(Object object) {
        Logger logger = this.plugin.getLogger();
        logger.info("{");
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldType = field.getType().getSimpleName();
            String fieldName = field.getName();
            try {
                Object fieldValue = field.get(object);
                logger.info(String.format("  %s %s = %s", fieldType, fieldName, fieldValue));

                for (Consumer<Object> listener : this.fieldInfoListeners) {
                    listener.accept(fieldValue);
                }
            } catch (Exception e) {
                logger.warning(String.format("  %s %s = ? (%s)", fieldType, fieldName, e.getMessage()));
            }
        }

        logger.info("");
        CallTracer.trace(logger);
        logger.info("}");
    }
}
