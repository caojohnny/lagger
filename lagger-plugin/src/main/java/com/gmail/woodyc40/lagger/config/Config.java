package com.gmail.woodyc40.lagger.config;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Access point for the plugin {@code config.yml} file.
 */
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

    /**
     * Obtains the collection of NMS packet class names to
     * filter from the sniffer.
     *
     * @return a list of NMS packet simple class names
     */
    public List<String> getDefaultSnifferFilter() {
        return this.defaultSnifferFilter;
    }

    /**
     * Specifies whether or not the optional features should
     * be enabled.
     *
     * @return {@code true} to enable
     */
    public boolean registerOptionalFeatures() {
        return this.registerOptionalFeatures;
    }
}
