package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.PacketSniffer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Permits command executors to manipulate the plugin's
 * packet sniffer utility.
 *
 * <p>usage: /psniff <on | off | dump | filter> [arg]</p>
 */
public class PacketSnifferCommand implements CommandExecutor {
    private final PacketSniffer sniffer;

    @Inject
    public PacketSnifferCommand(PacketSniffer sniffer) {
        this.sniffer = sniffer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.psniff")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (args.length == 1 && "on".equalsIgnoreCase(args[0])) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Not a player!");
                return true;
            }

            Player player = (Player) sender;
            this.sniffer.sniff(player);

            sender.sendMessage(String.format("Started packet sniffer for %s, check console", player.getName()));

            return true;
        }

        if (args.length == 1 && "off".equalsIgnoreCase(args[0])) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Not a player!");
                return true;
            }

            Player player = (Player) sender;
            this.sniffer.unsniff(player);

            sender.sendMessage(String.format("Ended packet sniffer for %s", player.getName()));

            return true;
        }

        if (args.length == 2 && "dump".equalsIgnoreCase(args[0])) {
            if ("on".equalsIgnoreCase(args[1])) {
                this.sniffer.enableDump();
                sender.sendMessage("Content dumping has been enabled");
                return true;
            }

            if ("off".equalsIgnoreCase(args[1])) {
                this.sniffer.disableDump();
                sender.sendMessage("Content dumping has been disabled");
                return true;
            }
        }

        if (args.length == 2 && "filter".equalsIgnoreCase(args[0])) {
            String packetName = args[1];
            this.sniffer.filter(packetName);

            sender.sendMessage(String.format("Added %s to the packet filter", packetName));

            return true;
        }

        return false;
    }
}
