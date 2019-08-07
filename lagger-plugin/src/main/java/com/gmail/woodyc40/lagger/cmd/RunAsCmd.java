package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.String.format;

public class RunAsCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.runas")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (args.length < 2) {
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(format("Player %s is not currently online", args[0]));
            return false;
        }

        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            cmdBuilder.append(args[i]).append(' ');
        }

        String cmd = cmdBuilder.toString().trim();
        sender.sendMessage(format("Player %s will run command '%s'", args[0], cmd));

        player.performCommand(cmd);

        return true;
    }
}
