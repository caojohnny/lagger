package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.AsyncChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static java.lang.String.format;

/**
 * This command permits command senders to load a given
 * chunk asynchronously, if supported.
 *
 * <p>usage: /lca [world] &lt;x&gt; &lt;z&gt;</p>
 */
public class LoadChunkAsyncCommand implements CommandExecutor {
    private final AsyncChunkLoader acl;

    @Inject
    public LoadChunkAsyncCommand(@Nullable AsyncChunkLoader acl) {
        this.acl = acl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("lagger.lca")) {
            sender.sendMessage("You have no permission!");
            return true;
        }

        if (this.acl == null) {
            sender.sendMessage("You are not running Paper; therefore you cannot load chunks asynchronously");
            return true;
        }

        World world;
        int x;
        int z;
        try {
            if (args.length != 2 && args.length != 3) {
                return false;
            } else if (args.length == 2) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You must provide a world if you are not a player!");
                    return true;
                }

                world = ((Player) sender).getWorld();
                x = Integer.parseInt(args[0]);
                z = Integer.parseInt(args[1]);
            } else {
                world = Bukkit.getWorld(args[0]);
                if (world == null) {
                    sender.sendMessage(format("World '%s' cannot be found", args[0]));
                    return true;
                }

                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("The command you entered was malformed");
            return true;
        }

        boolean alreadyLoaded = world.isChunkLoaded(x, z);
        boolean alreadyGenerated = world.isChunkGenerated(x, z);
        sender.sendMessage(format("Loaded @ (%d, %d) = %s | generated = %s",
                x, z, alreadyLoaded, alreadyGenerated));
        this.acl.loadChunkAsync(world, x, z, chunk -> {
            sender.sendMessage(format("Chunk at @ (%d, %d) has now been loaded", x, z));
            sender.sendMessage(format("Callback called by '%s'", Thread.currentThread().getName()));
        });

        return true;
    }
}
