package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.listener.DebugModeListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 * Debugmode toggle command, which enables useeful
 * features and protections to make debugging easier such
 * as fixing the world time and turning off weather
 * changing.
 *
 * <p>usage: /debugmode</p>
 */
public class DebugModeCommand implements CommandExecutor {
    private final DebugModeListener dml;

    @Inject
    public DebugModeCommand(DebugModeListener dml) {
        this.dml = dml;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
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
            sender.sendMessage("--- DEBUG MODE ENABLED ---");
            sender.sendMessage("WARNING: debug mode is NOT a substitute for thorough playtesting");
            sender.sendMessage("- Damage has been disabled");
            sender.sendMessage("- Hunger drain is disabled");
            sender.sendMessage("- Mob targeting is disabled");
            sender.sendMessage("- Fly has been enabled");
            sender.sendMessage("- World time has been locked");
            sender.sendMessage("- Spawn has been relocated here");
            sender.sendMessage("- Current weather has been locked");
        } else {
            disableDebugMode(player);
            sender.sendMessage("You have disabled debug mode for yourself.");
        }

        return true;
    }

    /**
     * Enables the features needed for debugmode for the
     * given player.
     *
     * @param player the player to enable debugmode
     */
    private static void enableDebugMode(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);

        Location location = player.getLocation();
        World world = requireNonNull(location.getWorld());
        world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());

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

    /**
     * Turns off debugmode for the given player.
     *
     * @param player the player to disable debugmode
     */
    private static void disableDebugMode(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
