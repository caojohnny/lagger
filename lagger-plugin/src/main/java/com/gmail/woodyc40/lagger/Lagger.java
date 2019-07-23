package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.ClearInventoryCommand;
import com.gmail.woodyc40.lagger.cmd.PauseCommand;
import com.gmail.woodyc40.lagger.module.NmsModule;
import com.gmail.woodyc40.lagger.module.NmsModule_v1_13_R01;
import com.gmail.woodyc40.lagger.module.NmsModule_v1_14_R01;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static java.lang.String.format;

public class Lagger extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        LaggerComponent component = this.configure();
        for (String packetName : component.getConfig().getDefaultSnifferFilter()) {
            component.getPacketSniffer().filter(packetName);
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(component.newPacketSnifferListener(), this);

        this.registerCommand("pause", new PauseCommand());
        this.registerCommand("ohi", component.newOhiCmd());
        this.registerCommand("psniff", component.newPSniffCmd());
        this.registerCommand("esniff", component.newESniffCmd());
        this.registerCommand("chunk", component.newChunkCmd());
        this.registerCommand("ci", new ClearInventoryCommand());
    }

    private LaggerComponent configure() {
        NmsModule nmsModule;

        String version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.14")) {
            nmsModule = new NmsModule_v1_14_R01();

            /* PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(new DismountListener(), this);
            this.getLogger().info("Registered DismountListener");

            String serverVersion = Bukkit.getVersion();
            if (serverVersion.toLowerCase().contains("paper")) {
                pm.registerEvents(new EntityRemoveListener(), this);
                this.getLogger().info("Registered EntityRemoveListener for PaperSpigot 1.14");
            } */
        } else if (version.startsWith("1.13")) {
            nmsModule = new NmsModule_v1_13_R01();
        } else if (version.startsWith("1.8")) {
            nmsModule = new NmsModule_v1_14_R01();
        } else {
            throw new UnsupportedOperationException(format("Bukkit '%s' is not supported", version));
        }

        this.getLogger().info(format("Registered NMS module for version %s", version));
        return DaggerLaggerComponent
                .builder()
                .plugin(this)
                .nmsModule(nmsModule)
                .build();
    }

    private void registerCommand(String cmd, CommandExecutor ce) {
        PluginCommand pc = this.getCommand(cmd);
        if (pc == null) {
            this.getLogger().log(Level.SEVERE, format("%s is not in the plugin.yml", cmd), new IllegalStateException());
            return;
        }

        pc.setExecutor(ce);
        this.getLogger().info(format("Registered command '%s' to CommandExecutor %s", cmd, ce.getClass().getSimpleName()));
    }
}
