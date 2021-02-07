package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.util.EventSniffer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Permits control over the event sniffer, which allows
 * listening to certain selected events dynamically at
 * runtime.
 *
 * <p>usage: /esniff [on | off | dump | filter] [arg]</p>
 */
public class EventSnifferCommand implements CommandExecutor {
    private final EventSniffer sniffer;

    @Inject
    public EventSnifferCommand(EventSniffer sniffer) {
        this.sniffer = sniffer;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
        if (!sender.hasPermission("lagger.esniff")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (args.length == 1 && "on".equalsIgnoreCase(args[0])) {
            this.sniffer.sniff();
            sender.sendMessage("Began event sniffer");

            return true;
        }

        if (args.length == 1 && "off".equalsIgnoreCase(args[0])) {
            this.sniffer.unsniff();
            sender.sendMessage("Ended event sniffer");

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
            String eventName = args[1];
            this.sniffer.toggleFilter(sender, eventName);

            return true;
        }

        return false;
    }
}
