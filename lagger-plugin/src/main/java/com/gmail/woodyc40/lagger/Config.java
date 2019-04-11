package com.gmail.woodyc40.lagger;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private final List<String> defaultSnifferFilter;

    public Config(Lagger plugin) {
        FileConfiguration cfg = plugin.getConfig();

        this.defaultSnifferFilter = cfg.getStringList("default-sniffer-filter");
    }

    public List<String> getDefaultSnifferFilter() {
        return this.defaultSnifferFilter;
    }
}
