package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.Lagger;
import com.gmail.woodyc40.lagger.util.DelegateCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class CopyPluginsCommand implements CommandExecutor {
    private final Lagger plugin;

    @Inject
    public CopyPluginsCommand(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.copyplugins")) {
            sender.sendMessage("No permission!");
            return true;
        }

        List<String> commandOutput = new ArrayList<>();
        DelegateCommandSender dcs = new DelegateCommandSender(sender) {
            @Override
            public void sendMessage(String s) {
                commandOutput.add(s);
                super.sendMessage(s);
            }
        };

        List<File> filesToDelete = new ArrayList<>();
        if (args.length >= 2) {
            if ("del".equalsIgnoreCase(args[0])) {
                for (int i = 1; i < args.length; i++) {
                    String pluginName = args[i];
                    String fileName = args[i];
                    String[] split = fileName.split(Pattern.quote("/"));
                    if (split.length > 1) {
                        pluginName = split[0];
                        fileName = split[1];
                    }

                    Plugin plugin = null;
                    for (Plugin test : Bukkit.getPluginManager().getPlugins()) {
                        if (test.getName().equalsIgnoreCase(pluginName)) {
                            plugin = test;
                            break;
                        }
                    }
                    if (plugin == null) {
                        dcs.sendMessage(format("Unsure where to get the data dir for plugin '%s'. Abort.", pluginName));
                        return true;
                    }

                    File file = plugin.getDataFolder();
                    if (split.length > 1) {
                        file = new File(file, fileName);
                    }
                    filesToDelete.add(file);
                }
            } else {
                dcs.sendMessage(format("Not sure what to do with '%s'. Abort.", args[0]));
                return true;
            }
        }

        // Sometimes not the root, but usually the case
        File root = Bukkit.getWorldContainer();
        File copyFile = new File(root, "copy.sh");
        String copyFilePath = copyFile.getAbsolutePath();
        if (!copyFile.exists()) {
            dcs.sendMessage(format("No file exists to copy all plugins: %s", copyFilePath));
            return true;
        }

        Process proc;
        try {
            proc = new ProcessBuilder("/bin/sh", copyFilePath)
                    .directory(root)
                    .inheritIO()
                    .start();
        } catch (IOException e) {
            dcs.sendMessage("Failed to run the process");
            this.plugin.getLogger().log(Level.SEVERE, "Failed to initiate /bin/sh to execute copy file", e);
            return true;
        }

        proc.onExit().thenRunAsync(() -> {
            dcs.sendMessage("Copying has completed. Check console in case anything went wrong.");
            dcs.sendMessage("Deleting any requested files...");
            if (filesToDelete.isEmpty()) {
                dcs.sendMessage("None specified. Proceeding...");
            } else {
                for (File file : filesToDelete) {
                    String fileName = file.getPath();
                    if (!file.exists()) {
                        dcs.sendMessage(format("  - File '%s' not found. Ignoring...", fileName));
                    } else {
                        if (this.deleteFile(file)) {
                            dcs.sendMessage(format("  - Deleted the file '%s', as requested.", fileName));
                        } else {
                            dcs.sendMessage(format("Found file '%s', but failed to delete. Abort.", fileName));
                            return;
                        }
                    }
                }
            }

            new Thread(() -> {
                Plugin newlyLoadedPlugin;
                do {
                     newlyLoadedPlugin = Bukkit.getPluginManager().getPlugin(this.plugin.getName());
                     Thread.yield();
                } while (newlyLoadedPlugin == null || newlyLoadedPlugin == this.plugin || !newlyLoadedPlugin.isEnabled());

                Plugin finalNewlyLoadedPlugin = newlyLoadedPlugin;
                Bukkit.getScheduler().runTaskLater(newlyLoadedPlugin, () -> {
                    Logger logger = finalNewlyLoadedPlugin.getLogger();

                    logger.info("--------------------------------------------");
                    logger.info("Echoing output from /copyplugins...");
                    for (String output : commandOutput) {
                        logger.info(output);
                    }
                    logger.info("--------------------------------------------");
                    logger.info("");
                    logger.info("          /COPYPLUGINS COMPLETE           ");
                    logger.info("");
                    logger.info("--------------------------------------------");

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("lagger.copyplugins")) {
                            if (!dcs.matches(onlinePlayer)) {
                                onlinePlayer.sendMessage("--------------------------------------------");
                                onlinePlayer.sendMessage("Echoing output from /copyplugins...");
                                for (String output : commandOutput) {
                                    onlinePlayer.sendMessage(output);
                                }
                            }
                            onlinePlayer.sendMessage("-----------------------------------");
                            onlinePlayer.sendMessage("");
                            onlinePlayer.sendMessage("          /COPYPLUGINS COMPLETE");
                            onlinePlayer.sendMessage("");
                            onlinePlayer.sendMessage("-----------------------------------");
                        }
                    }
                }, 3L);
            }, "Lagger CP Echo Waiter").start();

            dcs.sendMessage("Server will be reloaded. Remember that this command is not meant to be used in production.");
            Bukkit.reload();
        }, command1 -> Bukkit.getScheduler().runTask(this.plugin, command1));

        return true;
    }

    private boolean deleteFile(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File content : files) {
                this.deleteFile(content);
            }
        }

        return file.delete();
    }
}
