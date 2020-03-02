package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.*;
import com.gmail.woodyc40.lagger.module.*;
import com.gmail.woodyc40.lagger.util.EventSniffer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static java.lang.String.format;

public class Lagger extends JavaPlugin {
    private final LaggerComponent cmp;

    public Lagger() {
        this.cmp = this.configure();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        for (String packetName : this.cmp.getConfig().getDefaultSnifferFilter()) {
            this.cmp.getPacketSniffer().filter(packetName);
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this.cmp.newPacketSnifferListener(), this);
        pm.registerEvents(this.cmp.newDebugModeListener(), this);

        this.registerCommand("pause", new PauseCommand());
        this.registerCommand("ohi", this.cmp.newOhiCmd());
        this.registerCommand("psniff", this.cmp.newPSniffCmd());
        this.registerCommand("esniff", this.cmp.newESniffCmd());
        this.registerCommand("chunk", this.cmp.newChunkCmd());
        this.registerCommand("ci", new ClearInventoryCommand());
        this.registerCommand("setslot", this.cmp.newSetSlotCmd());
        this.registerCommand("runas", new RunAsCommand());
        this.registerCommand("copyplugins", this.cmp.newCopyPluginsCmd());
        this.registerCommand("runterm", new RunTermCommand());
        this.registerCommand("hurt", new HurtCommand());
        this.registerCommand("debugmode", this.cmp.newDebugModeCmd());
        this.registerCommand("lca", this.cmp.newLcaCmd());
    }

    @Override
    public void onDisable() {
        PacketSniffer ps = this.cmp.getPacketSniffer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ps.unsniff(player);
        }

        EventSniffer es = this.cmp.getEventSniffer();
        es.unsniff();
    }

    private LaggerComponent configure() {
        NmsModule nmsModule;
        AsyncChunkLoader acl = null;

        String version = Bukkit.getBukkitVersion();
        String serverVersion = Bukkit.getVersion();
        if (version.startsWith("1.15")) {
            nmsModule = new NmsModule_v1_15_R01();

            if (serverVersion.toLowerCase().contains("paper")) {
                // pm.registerEvents(new EntityRemoveListener(), this);
                // this.getLogger().info("Registered EntityRemoveListener for PaperSpigot 1.14");

                acl = new AsyncChunkLoaderPaper115();
                this.getLogger().info("Registered AsyncChunkLoader for PaperSpigot 1.15");
            }
        } else if (version.startsWith("1.14")) {
            nmsModule = new NmsModule_v1_14_R01();

            /* PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(new DismountListener(), this);
            this.getLogger().info("Registered DismountListener"); */

            if (serverVersion.toLowerCase().contains("paper")) {
                // pm.registerEvents(new EntityRemoveListener(), this);
                // this.getLogger().info("Registered EntityRemoveListener for PaperSpigot 1.14");

                acl = new AsyncChunkLoaderPaper114();
                this.getLogger().info("Registered AsyncChunkLoader for PaperSpigot 1.14");
            }
        } else if (version.startsWith("1.13")) {
            nmsModule = new NmsModule_v1_13_R01();
        } else if (version.startsWith("1.8")) {
            nmsModule = new NmsModule_v1_8_R01();
        } else {
            throw new UnsupportedOperationException(format("Bukkit '%s' is not supported", version));
        }

        this.getLogger().info(format("Registered NMS module for version %s", version));
        return DaggerLaggerComponent
                .builder()
                .plugin(this)
                .asyncChunkLoader(acl)
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
