package com.gmail.woodyc40.lagger.config;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Config {
    private final List<String> defaultSnifferFilter;
    private final boolean registerOptionalFeatures;

    @Inject
    public Config(Lagger plugin) {
        FileConfiguration cfg = plugin.getConfig();

        this.defaultSnifferFilter = cfg.getStringList("default-sniffer-filter");
        this.registerOptionalFeatures = cfg.getBoolean("register-optional-features");
    }

    public List<String> getDefaultSnifferFilter() {
        return this.defaultSnifferFilter;
    }

    public boolean registerOptionalFeatures() {
        return this.registerOptionalFeatures;
    }
}
