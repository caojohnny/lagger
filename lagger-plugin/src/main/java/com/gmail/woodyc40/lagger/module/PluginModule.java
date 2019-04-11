package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.Lagger;
import com.google.inject.AbstractModule;

public class PluginModule extends AbstractModule {
    private final Lagger plugin;

    public PluginModule(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Lagger.class).toInstance(this.plugin);
    }
}
