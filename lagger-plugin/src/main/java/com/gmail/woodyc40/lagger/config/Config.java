package com.gmail.woodyc40.lagger.config;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import java.util.List;

public class Config {
    private final List<String> defaultSnifferFilter;

    @Inject
    public Config(Lagger plugin) {
        FileConfiguration cfg = plugin.getConfig();

        this.defaultSnifferFilter = cfg.getStringList("default-sniffer-filter");
    }

    public List<String> getDefaultSnifferFilter() {
        return this.defaultSnifferFilter;
    }
}
