package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.Config;
import com.gmail.woodyc40.lagger.Lagger;
import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {
    private final Lagger plugin;

    public ConfigModule(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Config.class).toInstance(new Config(this.plugin));
    }
}
