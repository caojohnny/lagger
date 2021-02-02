package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.*;
import com.gmail.woodyc40.lagger.config.Config;
import com.gmail.woodyc40.lagger.module.*;
import com.gmail.woodyc40.lagger.util.EventSniffer;
import com.gmail.woodyc40.lagger.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.logging.Level;

import static java.lang.String.format;

/**
 * Plugin main class.
 */
public class Lagger extends JavaPlugin {
    /**
     * The injector instance
     */
    private final LaggerComponent cmp;

    /**
     * Entry-point for the plugin configuration
     */
    @Inject
    Config config;

    public Lagger() {
        this.cmp = this.configure();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // Perform field injection
        this.cmp.inject(this);

        // Eager initialize the packet sniffing filter
        for (String packetName : this.cmp.getConfig().getDefaultSnifferFilter()) {
            this.cmp.getPacketSniffer().filter(packetName);
        }

        // Register listeners
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this.cmp.newPacketSnifferListener(), this);
        pm.registerEvents(this.cmp.newDebugModeListener(), this);

        // Register stock commands
        this.registerCommand("pause", new PauseCommand());
        this.registerCommand("psniff", this.cmp.newPSniffCmd());
        this.registerCommand("esniff", this.cmp.newESniffCmd());
        this.registerCommand("chunk", this.cmp.newChunkCmd());
        this.registerCommand("ci", new ClearInventoryCommand());
        this.registerCommand("runas", new RunAsCommand());
        this.registerCommand("copyplugins", this.cmp.newCopyPluginsCmd());
        this.registerCommand("runterm", new RunTermCommand());
        this.registerCommand("hurt", new HurtCommand());
        this.registerCommand("debugmode", this.cmp.newDebugModeCmd());
        this.registerCommand("getitem", new GetItemCommand());

        // Registers extra features if enabled in the configuration
        if (this.config.registerOptionalFeatures()) {
            this.getLogger().info("Optional features has been enabled, performing registration now...");
            this.registerOptionalFeatures();
        } else {
            this.getLogger().info("Optional features has been disabled");
        }
    }

    @Override
    public void onDisable() {
        // Cleanup the packet sniffer
        PacketSniffer ps = this.cmp.getPacketSniffer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ps.unsniff(player);
        }

        // Cleanup the event sniffer
        EventSniffer es = this.cmp.getEventSniffer();
        es.unsniff();
    }

    /**
     * This initializes the Dagger component to faciliate
     * dependency injection for the plugin.
     *
     * @return the project injector component
     */
    private LaggerComponent configure() {
        NmsModule nmsModule;
        AsyncChunkLoader acl = null;

        ServerVersion version = ServerVersion.getVersion();
        if (version == null) {
            throw new UnsupportedOperationException(format("'%s' is not supported",
                    Bukkit.getVersion()));
        }

        switch (version) {
            case V1_8:
                nmsModule = new NmsModule_v1_8_R01();
                break;
            case V1_9:
                nmsModule = new NmsModule_v1_9_R01();
                break;
            case V1_10:
                nmsModule = new NmsModule_v1_10_R01();
                break;
            case V1_11:
                nmsModule = new NmsModule_v1_11_R01();
                break;
            case V1_12:
                nmsModule = new NmsModule_v1_12_R01();
                break;
            case V1_13:
                nmsModule = new NmsModule_v1_13_R01();
                break;
            case V1_14:
                nmsModule = new NmsModule_v1_14_R01();
                if (ServerVersion.isPaper()) {
                    acl = new AsyncChunkLoaderPaper114();
                    this.getLogger().info("Registered AsyncChunkLoader for PaperSpigot 1.14");
                }
                break;
            case V1_15:
                nmsModule = new NmsModule_v1_15_R01();
                if (ServerVersion.isPaper()) {
                    acl = new AsyncChunkLoaderPaper115();
                    this.getLogger().info("Registered AsyncChunkLoader for PaperSpigot 1.15");
                }
                break;
            case V1_16:
                nmsModule = new NmsModule_v1_16_R01();
                if (ServerVersion.isPaper()) {
                    acl = new AsyncChunkLoaderPaper116();
                    this.getLogger().info("Registered AsyncChunkLoader for PaperSpigot 1.16");
                }
                break;
            default:
                throw new UnsupportedOperationException(format("'%s' is not supported",
                        Bukkit.getVersion()));
        }

        this.getLogger().info(format("Registered NMS module for version %s", version));
        return DaggerLaggerComponent.builder()
                .plugin(this)
                .asyncChunkLoader(acl)
                .nmsModule(nmsModule)
                .build();
    }

    /**
     * Registers additional "optional" commands and
     * listeners.
     */
    private void registerOptionalFeatures() {
        this.registerCommand("ohi", this.cmp.newOhiCmd());
        this.registerCommand("setslot", this.cmp.newSetSlotCmd());
        this.registerCommand("lca", this.cmp.newLcaCmd());
        this.registerCommand("sas", this.cmp.newSasCmd());

        PluginManager pm = Bukkit.getPluginManager();
        String version = Bukkit.getBukkitVersion();
        if (version.startsWith("1.14")) {
            pm.registerEvents(new DismountListener(), this);
            this.getLogger().info("Registered DismountListener");
        }

        String serverVersion = Bukkit.getVersion();
        if (serverVersion.toLowerCase().contains("paper")) {
            pm.registerEvents(new EntityRemoveListener(), this);
            this.getLogger().info("Registered EntityRemoveListener for PaperSpigot 1.14");
        }
    }

    /**
     * Procedure used to safely register a command handler
     * with the command string.
     *
     * @param cmd the command string to register
     * @param ce  the handler for that command
     */
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
