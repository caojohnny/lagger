package com.gmail.woodyc40.lagger.util;

import com.gmail.woodyc40.lagger.FieldInspector;
import com.google.common.collect.ImmutableSet;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class EventSniffer {
    private static final String BASE_PACKAGE_NAME = "org.bukkit.event.";
    private static final Set<String> EVENT_SUB_PACKAGES =
            ImmutableSet.of("block.",
                    "enchantment.",
                    "entity.",
                    "hanging.",
                    "inventory.",
                    "painting.", // 1.8-specific
                    "player.",
                    "server.",
                    "vehicle.",
                    "weather.",
                    "world.");
    private static final Listener NOOP_LISTENER = new Listener() {
    };

    private final JavaPlugin plugin;
    private final FieldInspector inspector;

    private final Map<Class<?>, ProxyListener> cache = new HashMap<>();
    private final Map<Class<?>, ProxyListener> currentListeners = new HashMap<>();

    private boolean sniffing;
    private boolean dumpEnabled;

    @Inject
    public EventSniffer(JavaPlugin plugin) {
        this.plugin = plugin;
        this.inspector = new FieldInspector(plugin);
    }

    public void sniff() {
        this.sniffing = true;

        Set<String> classNames = new HashSet<>();
        for (Entry<Class<?>, ProxyListener> entry : this.currentListeners.entrySet()) {
            classNames.add(entry.getKey().getSimpleName());
            entry.getValue().enable();
        }

        this.plugin.getLogger().info(String.format("Sniffing for the following events: %s",
                Arrays.toString(classNames.toArray())));
    }

    public void unsniff() {
        this.sniffing = false;

        for (ProxyListener listener : this.currentListeners.values()) {
            listener.disable();
        }
    }

    public void enableDump() {
        this.dumpEnabled = true;
    }

    public void disableDump() {
        this.dumpEnabled = false;
    }

    public void toggleFilter(CommandSender sender, String eventName) {
        Class<?> cls = findClass(eventName);
        if (cls == null) {
            sender.sendMessage(String.format("Could not resolve %s to a Bukkit event, try FQN", eventName));
            return;
        }

        // Toggle off if it is already contained in the filter
        ProxyListener current = this.currentListeners.remove(cls);
        if (current != null) {
            current.disable();
            sender.sendMessage(String.format("Removed %s from the event filter", eventName));
            return;
        }

        // Use the cached listener if available
        ProxyListener cached = this.cache.get(cls);
        if (cached != null) {
            cached.enable();
            this.currentListeners.put(cls, cached);
            sender.sendMessage(String.format("Added %s to the event filter", eventName));

            return;
        }

        HandlerList handlerList;
        try {
            Method getHandlerList = cls.getDeclaredMethod("getHandlerList");

            getHandlerList.setAccessible(true);
            handlerList = (HandlerList) getHandlerList.invoke(null);
        } catch (NoSuchMethodException e) {
            sender.sendMessage(String.format("Event '%s' has no known handler list getter", eventName));
            return;
        } catch (IllegalAccessException | InvocationTargetException e) {
            sender.sendMessage(String.format("Failed to invoke %s#getHandlerList()", eventName));
            return;
        }

        RegisteredListener listener = new RegisteredListener(NOOP_LISTENER,
                (l, e) -> this.inspectEvent(e),
                EventPriority.MONITOR,
                this.plugin,
                false);

        ProxyListener proxyListener = new ProxyListener(handlerList, listener);

        // Automatically refresh actively sniffed events
        if (this.sniffing) {
            proxyListener.enable();
        }

        this.cache.put(cls, proxyListener);
        this.currentListeners.put(cls, proxyListener);

        sender.sendMessage(String.format("Added %s to the event filter", eventName));
    }

    private void inspectEvent(Event event) {
        Logger logger = this.plugin.getLogger();
        logger.info("EVENT FIRED: " + event.getClass().getSimpleName());

        if (this.dumpEnabled) {
            this.inspector.dump(event);
        }
    }

    private static Class<?> findClass(String eventName) {
        // Attempt to find as if eventName is fully qualified
        try {
            return Class.forName(eventName);
        } catch (ClassNotFoundException e) {
            // proceed
        }

        // Attempt to find if it includes a package specifier before the event
        try {
            return Class.forName(BASE_PACKAGE_NAME + eventName);
        } catch (ClassNotFoundException e) {
            // proceed
        }

        // Attempt to find if it is contained in a sub package
        for (String subPackage : EVENT_SUB_PACKAGES) {
            try {
                return Class.forName(BASE_PACKAGE_NAME + subPackage + eventName);
            } catch (ClassNotFoundException e) {
                // proceed
            }
        }

        return null;
    }

    private static class ProxyListener {
        private final HandlerList handlerList;
        private final RegisteredListener listener;

        public ProxyListener(HandlerList handlerList, RegisteredListener listener) {
            this.handlerList = handlerList;
            this.listener = listener;
        }

        public void enable() {
            this.handlerList.register(this.listener);

            // Rebake the HandlerList so Bukkit doesn't need to
            this.handlerList.bake();
        }

        public void disable() {
            this.handlerList.unregister(this.listener);

            // Rebake the HandlerList so Bukkit doesn't need to
            this.handlerList.bake();
        }
    }
}
