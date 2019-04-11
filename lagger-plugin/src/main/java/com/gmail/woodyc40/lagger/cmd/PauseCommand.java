package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PauseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.pause")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(command.getUsage());
            return true;
        }

        try {
            long pauseMillis = 1000L * Long.parseLong(args[0]);
            sender.sendMessage("Beginning pause; time = " + pauseMillis + "ms");

            fakeBusyWait(pauseMillis);

            sender.sendMessage("Pause has ended.");
        } catch (NumberFormatException e) {
            sender.sendMessage("Provided time was not a whole number");
        }

        return true;
    }

    private static void fakeBusyWait(long durationMillis) {
        long beginTime = System.currentTimeMillis();
        while (true) {
            long elapsedTimeMillis = System.currentTimeMillis() - beginTime;
            if (elapsedTimeMillis >= durationMillis) {
                break;
            }

            Thread.yield();
        }
    }
}
