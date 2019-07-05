package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.*;
import com.gmail.woodyc40.lagger.listener.PacketSnifferListener;
import com.gmail.woodyc40.lagger.module.*;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Lagger extends JavaPlugin {
    @Inject
    private Config config;
    @Inject
    private PacketSniffer sniffer;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        Injector injector = Guice.createInjector(
                new PluginModule(this),
                new ConfigModule(this),
                new CompatModule(this),
                new PacketSnifferModule(this),
                new ListenerModule(this));
        injector.injectMembers(this);

        for (String packetName : this.config.getDefaultSnifferFilter()) {
            this.sniffer.filter(packetName);
        }

        Bukkit.getPluginManager().registerEvents(injector.getInstance(PacketSnifferListener.class), this);

        this.registerCommand("pause", new PauseCommand());
        this.registerCommand("ohi", injector.getInstance(OpenHeadInventoryCommand.class));
        this.registerCommand("psniff", injector.getInstance(PacketSnifferCommand.class));
        this.registerCommand("esniff", injector.getInstance(EventSnifferCommand.class));
    }

    private void registerCommand(String cmd, CommandExecutor ce) {
        PluginCommand pc = this.getCommand(cmd);
        if (pc == null) {
            this.getLogger().log(Level.SEVERE, String.format("%s is not in the plugin.yml", cmd), new IllegalStateException());
            return;
        }

        pc.setExecutor(ce);
        this.getLogger().info(String.format("Registered command '%s' to CommandExecutor %s", cmd, ce.getClass().getSimpleName()));
    }
}
