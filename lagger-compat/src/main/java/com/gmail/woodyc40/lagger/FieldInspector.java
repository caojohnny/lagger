package com.gmail.woodyc40.lagger;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class FieldInspector {
    private final JavaPlugin plugin;
    private final List<Consumer<Object>> fieldInfoListeners =
            new ArrayList<>();

    public FieldInspector(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addFieldListener(Consumer<Object> listener) {
        this.fieldInfoListeners.add(listener);
    }

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
