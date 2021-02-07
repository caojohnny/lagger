package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import static java.lang.String.format;

/**
 * This is a sudo command that permits command executors
 * to run a command as if they were another player.
 *
 * <p>usage: /runas &lt;player&gt <command...></p>
 */
public class RunAsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
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
