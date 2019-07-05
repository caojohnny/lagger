package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.DismountListener;
import com.gmail.woodyc40.lagger.EntityRemoveListener;
import com.gmail.woodyc40.lagger.Lagger;
import com.google.inject.AbstractModule;
import org.bukkit.Bukkit;

public class ListenerModule extends AbstractModule {
    private final Lagger plugin;

    public ListenerModule(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        String version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.14")) {
            Bukkit.getPluginManager().registerEvents(new DismountListener(), this.plugin);
            this.plugin.getLogger().info("Registered DismountListener");
        }

        String serverVersion = Bukkit.getVersion();
        if (serverVersion.toLowerCase().contains("paper")) {
            Bukkit.getPluginManager().registerEvents(new EntityRemoveListener(), this.plugin);
            this.plugin.getLogger().info("Registered EntityRemoveListener");
        }
    }
}
