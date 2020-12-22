package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.listener.DebugModeListener;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.UUID;

public class DebugModeCommand implements CommandExecutor {
    private final DebugModeListener dml;

    @Inject
    public DebugModeCommand(DebugModeListener dml) {
        this.dml = dml;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.debugmode")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        Player player = (Player) sender;
        if (this.dml.toggleDebugMode(player)) {
            enableDebugMode(player);
            sender.sendMessage("You have enabled debug mode.");
            sender.sendMessage("Remember that in order to properly test your plugin, " +
                    "you still need to disable debug mode in order to ensure that your play testing " +
                    "matches what real players would experience.");
            sender.sendMessage("Debug mode makes testing certain features easier, but it does not " +
                    "substitute for a thorough test.");
            sender.sendMessage("Here are some of the things debug mode does:");
            sender.sendMessage(" - Disables damage to you");
            sender.sendMessage(" - Ensures your hunger doesn't drain");
            sender.sendMessage(" - Prevents hostile mobs from targeting you");
            sender.sendMessage(" - Allows you to fly");
            sender.sendMessage(" - Fixes the world time");
            sender.sendMessage(" - Sets the current world's spawn to your location");
            sender.sendMessage(" - Locks the current world weather");
        } else {
            disableDebugMode(player);
            sender.sendMessage("You have disabled debug mode for yourself.");
        }

        return true;
    }

    private static void enableDebugMode(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);

        Location location = player.getLocation();
        location.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
            if (entity instanceof Creature) {
                Creature c = (Creature) entity;
                LivingEntity currentTarget = c.getTarget();
                if (currentTarget != null && currentTarget.equals(player)) {
                    c.setTarget(null);
                }
            }
        }
    }

    private static void disableDebugMode(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
