package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.Lagger;
import dagger.Module;
import dagger.Provides;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Singleton;

/**
 * This is a Dagger module that maps instances of the
 * plugin main class to generic types such as
 * {@link Plugin} and {@link JavaPlugin}.
 */
@Module
public class PlugRemapperModule {
    /**
     * Maps the injected plugin instance to an instance of
     * {@link Plugin}.
     *
     * @param plugin the injected plugin
     * @return the plugin casted to an instance of
     * {@link Plugin}
     */
    @Provides
    @Singleton
    public static Plugin providePlugin(Lagger plugin) {
        return plugin;
    }

    /**
     * Maps the injected plugin instance to an instance of
     * {@link JavaPlugin}.
     *
     * @param plugin the injected plugin
     * @return the plugin casted to an instance of
     * {@link JavaPlugin}
     */
    @Provides
    @Singleton
    public static JavaPlugin provideJavaPlugin(Lagger plugin) {
        return plugin;
    }
}
