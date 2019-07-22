package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import com.google.inject.AbstractModule;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class SkullCompatModule extends AbstractModule {
    private final Lagger plugin;

    public SkullCompatModule(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        String version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.14")) {
            this.bind(SkullCompat.class).toInstance(new SkullCompat114R01());
            this.plugin.getLogger().info(String.format("Registered skull compat provider for Bukkit %s", version));
        } else if (version.startsWith("1.13")) {
            this.bind(SkullCompat.class).toInstance(new SkullCompat113R01());
            this.plugin.getLogger().info(String.format("Registered skull compat provider for Bukkit %s", version));
        } else if (version.startsWith("1.8")) {
            this.bind(SkullCompat.class).toInstance(new SkullCompat18R01());
            this.plugin.getLogger().info(String.format("Registered skull compat provider for Bukkit %s", version));
        } else {
            this.plugin.getLogger().log(Level.SEVERE, String.format("Unrecognized server version %s", version), new IllegalStateException());
        }
    }
}
