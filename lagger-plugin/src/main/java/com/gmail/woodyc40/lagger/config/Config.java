package com.gmail.woodyc40.lagger.config;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * This is a wrapper class over the plugin's default
 * configuration file.
 */
@Singleton
public class Config {
    private final List<String> defaultSnifferFilter;
    private final boolean registerOptionalFeatures;

    /**
     * Constructs the configuration wrapper and populates
     * the internal fields with configuration values.
     *
     * @param plugin the plugin whose config is being read
     */
    @Inject
    public Config(Lagger plugin) {
        FileConfiguration cfg = plugin.getConfig();

        this.defaultSnifferFilter = cfg.getStringList("default-sniffer-filter");
        this.registerOptionalFeatures = cfg.getBoolean("register-optional-features");
    }

    /**
     * Obtains the list of packet names that will be
     * filtered by default.
     *
     * @return a list of packet simple class names
     */
    public List<String> getDefaultSnifferFilter() {
        return this.defaultSnifferFilter;
    }

    /**
     * Determines whether to register optional features or
     * not.
     *
     * @return {@code true} if the additional features are
     * desired
     */
    public boolean registerOptionalFeatures() {
        return this.registerOptionalFeatures;
    }
}
