package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import com.google.inject.AbstractModule;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class PacketSnifferModule extends AbstractModule {
    private final Lagger plugin;

    public PacketSnifferModule(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        String version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.14")) {
            this.bind(PacketSniffer.class).toInstance(new PacketSniffer114R01(this.plugin));
            this.plugin.getLogger().info(String.format("Registered packet sniffer for Bukkit %s", version));
        } else if (version.startsWith("1.13")) {
            this.bind(PacketSniffer.class).toInstance(new PacketSniffer113(this.plugin));
            this.plugin.getLogger().info(String.format("Registered packet sniffer for Bukkit %s", version));
        } else if (version.startsWith("1.8")) {
            this.bind(PacketSniffer.class).toInstance(new PacketSniffer18(this.plugin));
            this.plugin.getLogger().info(String.format("Registered packet sniffer for Bukkit %s", version));
        } else {
            this.plugin.getLogger().log(Level.SEVERE, String.format("Unrecognized server version %s", version), new IllegalStateException());
        }
    }
}
