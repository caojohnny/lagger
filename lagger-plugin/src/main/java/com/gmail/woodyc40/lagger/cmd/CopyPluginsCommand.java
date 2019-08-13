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
        sender = new DelegateCommandSender(sender) {
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
                        sender.sendMessage(format("Unsure where to get the data dir for plugin '%s'. Abort.", pluginName));
                        return true;
                    }

                    File file = plugin.getDataFolder();
                    if (split.length > 1) {
                        file = new File(file, fileName);
                    }
                    filesToDelete.add(file);
                }
            } else {
                sender.sendMessage(format("Not sure what to do with '%s'. Abort.", args[0]));
                return true;
            }
        }

        // Sometimes not the root, but usually the case
        File root = Bukkit.getWorldContainer();
        File copyFile = new File(root, "copy.sh");
        String copyFilePath = copyFile.getAbsolutePath();
        if (!copyFile.exists()) {
            sender.sendMessage(format("No file exists to copy all plugins: %s", copyFilePath));
            return true;
        }

        Process proc;
        try {
            proc = new ProcessBuilder("/bin/sh", copyFilePath)
                    .directory(root)
                    .inheritIO()
                    .start();
        } catch (IOException e) {
            sender.sendMessage("Failed to run the process");
            this.plugin.getLogger().log(Level.SEVERE, "Failed to initiate /bin/sh to execute copy file", e);
            return true;
        }

        CommandSender finalSender = sender;
        proc.onExit().thenRunAsync(() -> {
            finalSender.sendMessage("Copying has completed. Check console in case anything went wrong.");
            finalSender.sendMessage("Deleting any requested data folders...");
            if (filesToDelete.isEmpty()) {
                finalSender.sendMessage("None specified. Proceeding...");
            } else {
                for (File file : filesToDelete) {
                    String fileName = file.getPath();
                    if (!file.exists()) {
                        finalSender.sendMessage(format("Data folder for '%s' not found. Ignoring...", fileName));
                    } else {
                        File[] files = file.listFiles();
                        if (files != null) {
                            finalSender.sendMessage(format("There are still files in '%s'. Will delete those as well.", fileName));
                            for (File fileToDelete : files) {
                                if (!fileToDelete.delete()) {
                                    finalSender.sendMessage(format("Failed to remove '%s' from queued folder. Abort.", fileToDelete.getPath()));
                                    return;
                                }
                            }
                        }

                        boolean result = file.delete();
                        if (!result) {
                            finalSender.sendMessage(format("Found folder for '%s', but failed to delete. Proceeding...", fileName));
                        } else {
                            finalSender.sendMessage(format("Deleted the file '%s', as requested.", fileName));
                        }
                    }
                }
            }

            new Thread(() -> {
                Plugin newlyLoadedPlugin;
                do {
                     newlyLoadedPlugin = Bukkit.getPluginManager().getPlugin(this.plugin.getName());
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
                    logger.info("---------- /COPYPLUGINS COMPLETE -----------");

                    Player newSender = Bukkit.getPlayer(finalSender.getName());
                    if (newSender != null) {
                        newSender.sendMessage("---------- /COPYPLUGINS COMPLETE -----------");
                    }
                }, 3L);
            }, "Lagger CP Echo Waiter").start();

            finalSender.sendMessage("Server will be reloaded. Remember that this command is not meant to be used in production.");
            Bukkit.reload();
        }, command1 -> Bukkit.getScheduler().runTask(this.plugin, command1));

        return true;
    }
}
