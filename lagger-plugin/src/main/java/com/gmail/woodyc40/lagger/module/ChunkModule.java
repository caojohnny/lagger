package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import com.google.inject.AbstractModule;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ChunkModule extends AbstractModule {
    private final Lagger plugin;

    public ChunkModule(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        String version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.14")) {
            this.bind(ChunkCompat.class).toInstance(new ChunkCompat114R01());
            this.plugin.getLogger().info(String.format("Registered chunk compat provider for Bukkit %s", version));
        } else if (version.startsWith("1.13")) {
            this.bind(ChunkCompat.class).toInstance(new ChunkCompat113R01());
            this.plugin.getLogger().info(String.format("Registered chunk compat provider for Bukkit %s", version));
        } else if (version.startsWith("1.8")) {
            this.bind(ChunkCompat.class).toInstance(new ChunkCompat114R01());
            this.plugin.getLogger().info(String.format("Registered chunk compat provider for Bukkit %s", version));
        } else {
            this.plugin.getLogger().log(Level.SEVERE, String.format("Unrecognized server version %s", version), new IllegalStateException());
        }
    }
}
