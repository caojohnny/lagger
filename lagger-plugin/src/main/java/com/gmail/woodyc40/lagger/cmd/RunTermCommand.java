package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static java.lang.String.format;

/**
 * This command allows players to run a command as if they
 * were the terminal or the console.
 *
 * <p>usage: /runterm <command...></p>
 */
public class RunTermCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.runterm")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (args.length < 1) {
            return false;
        }

        StringBuilder cmdBuilder = new StringBuilder();
        for (String arg : args) {
            cmdBuilder.append(arg).append(' ');
        }

        String cmd = cmdBuilder.toString().trim();
        sender.sendMessage(format("Console will run command '%s'", cmd));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);

        return true;
    }
}
