package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.Lagger;
import dagger.Module;
import dagger.Provides;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Singleton;

@Module
public abstract class PlugRemapperModule {
    @Provides
    @Singleton
    public static Plugin providePlugin(Lagger plugin) {
        return plugin;
    }

    @Provides
    @Singleton
    public static JavaPlugin provideJavaPlugin(Lagger plugin) {
        return plugin;
    }
}
